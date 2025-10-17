/**
 * Environment configuration for API endpoints
 *
 * This file contains configuration for different environments (development, production, etc.)
 * and provides a centralized place to manage API URLs.
 */

// Environments
export enum Environment {
  Development = 'development',
  Production = 'production',
  Staging = 'staging',
  Test = 'test'
}

// Get current environment from process.env or default to development
export const getCurrentEnvironment = (): Environment => {
  const env = process.env.REACT_APP_ENV || 'development';
  return env as Environment;
};

// API URL configuration for different environments
const API_CONFIG = {
  [Environment.Development]: {
    apiUrl: 'http://localhost:8080',
  },
  [Environment.Production]: {
    apiUrl: 'https://api.library-app.com', // Replace with actual production URL
  },
  [Environment.Staging]: {
    apiUrl: 'https://staging-api.library-app.com', // Replace with actual staging URL
  },
  [Environment.Test]: {
    apiUrl: 'http://localhost:8080',
  }
};

// Get the base API URL for the current environment
export const getApiBaseUrl = (): string => {
  const currentEnv = getCurrentEnvironment();
  return API_CONFIG[currentEnv].apiUrl;
};

// Export API paths for different endpoints
export const API_PATHS = {
  BOOKS: '/api/books',
  REVIEWS: '/api/reviews',
  SECURE_BOOKS: '/api/books/secure',
  SECURE_REVIEWS: '/api/reviews/secure',
  SEARCH_REVIEWS_BY_BOOK_ID: (bookId: string) => `/api/reviews/search/findByBookId?bookId=${bookId}`,
  SECURE_USER_BOOK_REVIEW: (bookId: string) => `/api/reviews/secure/user/book/?bookId=${bookId}`,
  SECURE_CURRENT_LOANS_COUNT: '/api/books/secure/currentloans/count',
  SECURE_IS_CHECKED_OUT_BY_USER: (bookId: string) => `/api/books/secure/ischeckedout/byuser/?bookId=${bookId}`,
  SECURE_CHECKOUT_BOOK: (bookId: number) => `/api/books/secure/checkout/?bookId=${bookId}`
};
