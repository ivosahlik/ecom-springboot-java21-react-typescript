/**
 * Review Service
 *
 * Handles all API calls related to reviews
 */

import { ApiService, RequestOptions } from './ApiService';
import { API_PATHS } from '../../config/env-config';
import ReviewModel from '../../models/ReviewModel';
import ReviewRequestModel from '../../models/ReviewRequestModel';

export class ReviewService extends ApiService {

  /**
   * Fetch reviews for a specific book
   */
  async getBookReviews(bookId: string): Promise<ReviewModel[]> {
    const endpoint = API_PATHS.SEARCH_REVIEWS_BY_BOOK_ID(bookId);
    const responseJson = await this.get<any>(endpoint);

    // Extract reviews from response
    const responseData = responseJson.content;
    const loadedReviews: ReviewModel[] = [];

    for (const key in responseData) {
      loadedReviews.push({
        id: responseData[key].id,
        userEmail: responseData[key].userEmail,
        date: responseData[key].date,
        rating: responseData[key].rating,
        book_id: responseData[key].bookId,
        reviewDescription: responseData[key].reviewDescription,
      });
    }

    return loadedReviews;
  }

  /**
   * Calculate the average star rating from a list of reviews
   */
  calculateAverageStarRating(reviews: ReviewModel[]): number {
    if (!reviews || reviews.length === 0) {
      return 0;
    }

    let weightedStarReviews = 0;
    for (const review of reviews) {
      weightedStarReviews += review.rating;
    }

    const round = (Math.round((weightedStarReviews / reviews.length) * 2) / 2).toFixed(1);
    return Number(round);
  }

  /**
   * Check if the current user has left a review for a specific book
   */
  async hasUserReviewedBook(bookId: string, accessToken: string): Promise<boolean> {
    const endpoint = API_PATHS.SECURE_USER_BOOK_REVIEW(bookId);

    return this.get<boolean>(endpoint, {
      requiresAuth: true,
      accessToken
    });
  }

  /**
   * Submit a new review
   */
  async submitReview(
    rating: number,
    bookId: number,
    reviewDescription: string,
    accessToken: string
  ): Promise<void> {
    const endpoint = API_PATHS.SECURE_REVIEWS;
    const reviewRequestModel = new ReviewRequestModel(rating, bookId, reviewDescription);

    await this.post<void>(endpoint, reviewRequestModel, {
      requiresAuth: true,
      accessToken
    });
  }
}
