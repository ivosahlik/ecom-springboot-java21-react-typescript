/**
 * Book Service
 *
 * Handles all API calls related to books
 */

import { ApiService } from './ApiService';
import { API_PATHS } from '../../config/env-config';
import BookModel from '../../models/BookModel';

export class BookService extends ApiService {

  /**
   * Fetch a book by its ID
   */
  async getBook(bookId: string): Promise<BookModel> {
    const endpoint = `${API_PATHS.BOOKS}/${bookId}`;
    const responseJson = await this.get<any>(endpoint);

    // Transform response to BookModel
    return {
      id: responseJson.id,
      title: responseJson.title,
      author: responseJson.author,
      description: responseJson.description,
      copies: responseJson.copies,
      copiesAvailable: responseJson.copiesAvailable,
      category: responseJson.category,
      img: responseJson.img,
    };
  }

  /**
   * Check if a book is checked out by the current user
   */
  async isCheckedOutByUser(bookId: string, accessToken: string): Promise<boolean> {
    const endpoint = API_PATHS.SECURE_IS_CHECKED_OUT_BY_USER(bookId);

    return this.get<boolean>(endpoint, {
      requiresAuth: true,
      accessToken
    });
  }

  /**
   * Get the count of books currently checked out by the user
   */
  async getCurrentLoansCount(accessToken: string): Promise<number> {
    const endpoint = API_PATHS.SECURE_CURRENT_LOANS_COUNT;

    return this.get<number>(endpoint, {
      requiresAuth: true,
      accessToken
    });
  }

  /**
   * Check out a book for the current user
   */
  async checkoutBook(bookId: number, accessToken: string): Promise<void> {
    const endpoint = API_PATHS.SECURE_CHECKOUT_BOOK(bookId);

    await this.put<void>(endpoint, undefined, {
      requiresAuth: true,
      accessToken
    });
  }
}
