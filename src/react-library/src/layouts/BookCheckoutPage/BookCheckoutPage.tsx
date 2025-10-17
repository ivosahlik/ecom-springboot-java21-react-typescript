import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import ReviewModel from "../../models/ReviewModel";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { StarsReview } from "../Utils/StarsReview";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";
import { LatestReviews } from "./LatestReviews";
import { useAuth } from "../../context/AuthContext";
import { bookService, reviewService } from "../../services/api";

export const BookCheckoutPage = () => {

    const { authState } = useAuth();

    const [book, setBook] = useState<BookModel>();
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);

    // Review State
    const [reviews, setReviews] = useState<ReviewModel[]>([])
    const [totalStars, setTotalStars] = useState(0);
    const [isLoadingReview, setIsLoadingReview] = useState(true);

    const [isReviewLeft, setIsReviewLeft] = useState(false);
    const [isLoadingUserReview, setIsLoadingUserReview] = useState(true);

    // Loans Count State
    const [currentLoansCount, setCurrentLoansCount] = useState(0);
    const [isLoadingCurrentLoansCount, setIsLoadingCurrentLoansCount] = useState(true);

    // Is Book Check Out?
    const [isCheckedOut, setIsCheckedOut] = useState(false);
    const [isLoadingBookCheckedOut, setIsLoadingBookCheckedOut] = useState(true);

    const bookId = (window.location.pathname).split('/')[2];

    useEffect(() => {
        const fetchBook = async () => {
            try {
                const loadedBook = await bookService.getBook(bookId);
                setBook(loadedBook);
                setIsLoading(false);
            } catch (error: any) {
                setIsLoading(false);
                setHttpError(error.message);
            }
        };
        fetchBook();
        // setTimeout(() => {
        //     fetchBook().catch((error: any) => {
        //         setIsLoading(false);
        //         setHttpError(error.message);
        //     })
        // }, 10000);
    }, [isCheckedOut]);

    useEffect(() => {
        const fetchBookReviews = async () => {
            try {
                const loadedReviews = await reviewService.getBookReviews(bookId);

                // Calculate average star rating
                const averageStars = reviewService.calculateAverageStarRating(loadedReviews);
                setTotalStars(averageStars);

                setReviews(loadedReviews);
                setIsLoadingReview(false);
            } catch (error: any) {
                setIsLoadingReview(false);
                setHttpError(error.message);
            }
        };

        fetchBookReviews();
    }, [isReviewLeft]);

    useEffect(() => {
        const fetchUserReviewBook = async () => {
            try {
                if (authState && authState.isAuthenticated) {
                    const hasReviewed = await reviewService.hasUserReviewedBook(
                        bookId,
                        authState.token || ''
                    );
                    setIsReviewLeft(hasReviewed);
                }
                setIsLoadingUserReview(false);
            } catch (error: any) {
                setIsLoadingUserReview(false);
                setHttpError(error.message);
            }
        }
        // fetchUserReviewBook();
    }, [authState]);

    useEffect(() => {
        const fetchUserCurrentLoansCount = async () => {
            try {
                if (authState && authState.isAuthenticated) {
                    const loansCount = await bookService.getCurrentLoansCount(
                        authState.token || ''
                    );
                    setCurrentLoansCount(loansCount);
                }
                setIsLoadingCurrentLoansCount(false);
            } catch (error: any) {
                setIsLoadingCurrentLoansCount(false);
                setHttpError(error.message);
            }
        }
        // fetchUserCurrentLoansCount();
    }, [authState, isCheckedOut]);

    useEffect(() => {
        const fetchUserCheckedOutBook = async () => {
            try {
                if (authState && authState.isAuthenticated) {
                    const isCheckedOutByUser = await bookService.isCheckedOutByUser(
                        bookId,
                        authState.token || ''
                    );
                    setIsCheckedOut(isCheckedOutByUser);
                }
                setIsLoadingBookCheckedOut(false);
            } catch (error: any) {
                setIsLoadingBookCheckedOut(false);
                setHttpError(error.message);
            }
        }
        // fetchUserCheckedOutBook();
    }, [authState]);

    if (
      isLoading
      // || isLoadingReview
      // || isLoadingCurrentLoansCount
      // || isLoadingBookCheckedOut
      // || isLoadingUserReview
    ) {
        return (
            <SpinnerLoading />
        )
    }

    if (httpError) {
        return (
            <div className='container m-5'>
                <p>{httpError}</p>
            </div>
        )
    }

    async function checkoutBook() {
        try {
            if (book?.id && authState?.token) {
                await bookService.checkoutBook(
                    book.id,
                    authState.token
                );
                setIsCheckedOut(true);
            }
        } catch (error: any) {
            setHttpError(error.message);
        }
    }

    async function submitReview(starInput: number, reviewDescription: string) {
        try {
            if (book?.id && authState?.token) {
                await reviewService.submitReview(
                    starInput,
                    book.id,
                    reviewDescription,
                    authState.token
                );
                setIsReviewLeft(true);
            }
        } catch (error: any) {
            setHttpError(error.message);
        }
    }

    return (
        <div>
            <div className='container d-none d-lg-block'>
                <div className='row mt-5'>
                    <div className='col-sm-2 col-md-2'>
                        {book?.img ?
                            <img src={book?.img} width='226' height='349' alt='Book' />
                            :
                            <img src={require('./../../Images/BooksImages/book-1000.png')} width='226'
                                height='349' alt='Book' />
                        }
                    </div>
                    <div className='col-4 col-md-4 container'>
                        <div className='ml-2'>
                            <h2>{book?.title}</h2>
                            <h5 className='text-primary'>{book?.author}</h5>
                            <p className='lead'>{book?.description}</p>
                            <StarsReview rating={totalStars} size={32} />
                        </div>
                    </div>
                    <CheckoutAndReviewBox book={book} mobile={false} currentLoansCount={currentLoansCount}
                        isAuthenticated={authState?.isAuthenticated} isCheckedOut={isCheckedOut}
                        checkoutBook={checkoutBook} isReviewLeft={isReviewLeft} submitReview={submitReview}/>
                </div>
                <hr />
                <LatestReviews reviews={reviews} bookId={book?.id} mobile={false} />
            </div>
            <div className='container d-lg-none mt-5'>
                <div className='d-flex justify-content-center alighn-items-center'>
                    {book?.img ?
                        <img src={book?.img} width='226' height='349' alt='Book' />
                        :
                        <img src={require('./../../Images/BooksImages/book-1000.png')} width='226'
                            height='349' alt='Book' />
                    }
                </div>
                <div className='mt-4'>
                    <div className='ml-2'>
                        <h2>{book?.title}</h2>
                        <h5 className='text-primary'>{book?.author}</h5>
                        <p className='lead'>{book?.description}</p>
                        <StarsReview rating={totalStars} size={32} />
                    </div>
                </div>
                <CheckoutAndReviewBox book={book} mobile={true} currentLoansCount={currentLoansCount}
                    isAuthenticated={authState?.isAuthenticated} isCheckedOut={isCheckedOut}
                    checkoutBook={checkoutBook} isReviewLeft={isReviewLeft} submitReview={submitReview}/>
                <hr />
                <LatestReviews reviews={reviews} bookId={book?.id} mobile={true} />
            </div>
        </div>
    );
}
