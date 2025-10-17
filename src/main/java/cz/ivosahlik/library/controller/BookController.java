package cz.ivosahlik.library.controller;

import cz.ivosahlik.library.annotation.CurrentUser;
import cz.ivosahlik.library.entity.Book;
import cz.ivosahlik.library.responsemodels.ShelfCurrentLoansResponse;
import cz.ivosahlik.library.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public Page<Book> books(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "9") int size) {
        return bookService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/{bookId}")
    public Book findById(@PathVariable Long bookId) {
        return bookService.findById(bookId);
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@CurrentUser String userEmail)
        throws Exception
    {
        try {
            log.debug("Current loans request received");
            if (userEmail == null) {
                log.error("Current loans error: User email not found");
                throw new Exception("User email not found");
            }
            log.debug("Getting current loans for user: {}", userEmail);
            return bookService.currentLoans(userEmail);
        } catch (Exception e) {
            log.error("Error getting current loans: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoansCount(@CurrentUser String userEmail) {
        try {
            log.debug("Current loans count request received");
            if (userEmail == null) {
                log.error("Current loans count error: User email not found");
                return 0; // Return 0 instead of throwing exception for this endpoint
            }
            log.debug("Getting current loans count for user: {}", userEmail);
            return bookService.currentLoansCount(userEmail);
        } catch (Exception e) {
            log.error("Error getting current loans count: {}", e.getMessage(), e);
            return 0; // Return 0 on error
        }
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public Boolean checkoutBookByUser(@CurrentUser String userEmail,
                                      @RequestParam Long bookId) {
        try {
            log.debug("Is book checked out request received for bookId: {}", bookId);
            if (userEmail == null) {
                log.error("Is book checked out error: User email not found");
                return false; // Return false instead of throwing exception
            }
            log.debug("Checking if book {} is checked out by user: {}", bookId, userEmail);
            return bookService.checkoutBookByUser(userEmail, bookId);
        } catch (Exception e) {
            log.error("Error checking if book is checked out: {}", e.getMessage(), e);
            return false; // Return false on error
        }
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook (@CurrentUser String userEmail,
                              @RequestParam Long bookId) throws Exception {
        try {
            log.debug("Checkout request received for bookId: {}", bookId);

            if (userEmail == null) {
                log.error("Checkout error: User email not found");
                throw new Exception("User email not found");
            }

            log.debug("Attempting to checkout book {} for user {}", bookId, userEmail);
            Book book = bookService.checkoutBook(userEmail, bookId);
            log.debug("Book checkout successful for bookId: {}", bookId);

            return book;
        } catch (Exception e) {
            log.error("Error during book checkout: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/secure/return")
    public void returnBook(@CurrentUser String userEmail,
                           @RequestParam Long bookId) throws Exception {
        try {
            log.debug("Return book request received for bookId: {}", bookId);

            if (userEmail == null) {
                log.error("Return book error: User email not found");
                throw new Exception("User email not found");
            }

            log.debug("Attempting to return book {} for user {}", bookId, userEmail);
            bookService.returnBook(userEmail, bookId);
            log.debug("Book return successful for bookId: {}", bookId);
        } catch (Exception e) {
            log.error("Error during book return: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PutMapping("/secure/renew/loan")
    public void renewLoan(@CurrentUser String userEmail,
                          @RequestParam Long bookId) throws Exception {
        try {
            log.debug("Renew loan request received for bookId: {}", bookId);

            if (userEmail == null) {
                log.error("Renew loan error: User email not found");
                throw new Exception("User email not found");
            }

            log.debug("Attempting to renew loan for book {} for user {}", bookId, userEmail);
            bookService.renewLoan(userEmail, bookId);
            log.debug("Loan renewal successful for bookId: {}", bookId);
        } catch (Exception e) {
            log.error("Error during loan renewal: {}", e.getMessage(), e);
            throw e;
        }
    }

}












