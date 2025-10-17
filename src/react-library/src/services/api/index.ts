/**
 * API Services Index
 *
 * Exports all API services for easy importing throughout the application
 */

import { ApiService } from './ApiService';
import { BookService } from './BookService';
import { ReviewService } from './ReviewService';

// Create instances of each service
export const bookService = new BookService();
export const reviewService = new ReviewService();

// Export service classes
export { ApiService, BookService, ReviewService };

// Export types
export type { RequestOptions } from './ApiService';
