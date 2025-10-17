package cz.ivosahlik.library.controller;

import cz.ivosahlik.library.annotation.CurrentUser;
import cz.ivosahlik.library.entity.Review;
import cz.ivosahlik.library.requestmodels.ReviewRequest;
import cz.ivosahlik.library.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/search/findByBookId")
    public Page<Review> findById(@RequestParam Long bookId,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "9") int size) {
        return reviewService.findAllReviewByBookId(bookId, PageRequest.of(page, size));
    }

    @GetMapping("/secure/user/book")
    public Boolean reviewBookByUser(@CurrentUser String userEmail,
                                    @RequestParam Long bookId) throws Exception {
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        return reviewService.userReviewListed(userEmail, bookId);
    }

    @PostMapping("/secure")
    public void postReview(@CurrentUser String userEmail,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {
        if (userEmail == null) {
            throw new Exception("User email is missing");
        }
        reviewService.postReview(userEmail, reviewRequest);
    }
}
