package cz.ivosahlik.library.service;

import cz.ivosahlik.library.dao.BookRepository;
import cz.ivosahlik.library.dao.CheckoutRepository;
import cz.ivosahlik.library.dao.HistoryRepository;
import cz.ivosahlik.library.entity.Book;
import cz.ivosahlik.library.entity.Checkout;
import cz.ivosahlik.library.entity.History;
import cz.ivosahlik.library.responsemodels.ShelfCurrentLoansResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final CheckoutRepository checkoutRepository;
    private final HistoryRepository historyRepository;

    public Book checkoutBook (String userEmail, Long bookId) throws Exception {
        log.debug("BookService: Attempting checkout for book ID {} by user {}", bookId, userEmail);

        try {
            if (userEmail == null || userEmail.trim().isEmpty()) {
                log.error("BookService: User email is null or empty");
                throw new Exception("User email is required");
            }

            if (bookId == null) {
                log.error("BookService: Book ID is null");
                throw new Exception("Book ID is required");
            }

            Optional<Book> book = bookRepository.findById(bookId);
            log.debug("BookService: Book found: {}", book.isPresent() ? "Yes" : "No");

            if (book.isEmpty()) {
                log.error("BookService: Book with ID {} not found", bookId);
                throw new Exception("Book doesn't exist with ID: " + bookId);
            }

            Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
            log.debug("BookService: Book already checked out by user: {}", validateCheckout != null ? "Yes" : "No");

            if (validateCheckout != null) {
                log.error("BookService: Book already checked out by user");
                throw new Exception("Book already checked out by user");
            }

            if (book.get().getCopiesAvailable() <= 0) {
                log.error("BookService: No copies available for book ID {}", bookId);
                throw new Exception("No copies available for checkout");
            }

            // Proceed with checkout
            log.debug("BookService: Decreasing available copies for book ID {}", bookId);
            book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
            Book savedBook = bookRepository.save(book.get());

            log.debug("BookService: Creating checkout record");
            Checkout checkout = new Checkout(
                    userEmail,
                    LocalDate.now().toString(),
                    LocalDate.now().plusDays(7).toString(),
                    book.get().getId()
            );

            Checkout savedCheckout = checkoutRepository.save(checkout);
            log.debug("BookService: Checkout successfully recorded with ID: {}", savedCheckout.getId());

            return savedBook;
        } catch (Exception e) {
            log.error("BookService: Error during checkout: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Boolean checkoutBookByUser(String userEmail, Long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        return validateCheckout != null;
    }

    public int currentLoansCount(String userEmail) {
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {

        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses = new ArrayList<>();

        List<Checkout> checkoutList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList = checkoutList.stream().map(Checkout::getBookId).collect(Collectors.toList());

        List<Book> books = bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book : books) {
            Optional<Checkout> checkout = checkoutList.stream()
                    .filter(x -> x.getBookId().equals(book.getId())).findFirst();

            if (checkout.isPresent()) {

                Date d1 = sdf.parse(checkout.get().getReturnDate());
                Date d2 = sdf.parse(LocalDate.now().toString());

                TimeUnit time = TimeUnit.DAYS;

                long differenceInTime = time.convert(d1.getTime() - d2.getTime(),
                        TimeUnit.MILLISECONDS);

                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book, (int) differenceInTime));
            }
        }
        return shelfCurrentLoansResponses;
    }

    public void returnBook (String userEmail, Long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (book.isEmpty() || validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);

        bookRepository.save(book.get());
        checkoutRepository.deleteById(validateCheckout.getId());

        History history = new History(
                userEmail,
                validateCheckout.getCheckoutDate(),
                LocalDate.now().toString(),
                book.get().getTitle(),
                book.get().getAuthor(),
                book.get().getDescription(),
                book.get().getImg()
        );

        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId) throws Exception {

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);

        if (validateCheckout == null) {
            throw new Exception("Book does not exist or not checked out by user");
        }

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date d1 = sdFormat.parse(validateCheckout.getReturnDate());
        Date d2 = sdFormat.parse(LocalDate.now().toString());

        if (d1.compareTo(d2) <= 0 && d1.compareTo(d2) != 0) {
            return;
        }
        validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
        checkoutRepository.save(validateCheckout);
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

}















