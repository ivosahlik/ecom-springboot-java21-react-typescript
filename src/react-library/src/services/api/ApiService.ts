/**
 * Base API Service
 *
 * Provides common functionality for making API requests with proper error handling
 * and authentication token management.
 */

import { getApiBaseUrl } from '../../config/env-config';

export interface RequestOptions extends RequestInit {
  requiresAuth?: boolean;
  accessToken?: string;
}

export class ApiService {

  /**
   * Make an API request with proper error handling and authentication
   */
  protected async request<T>(
    endpoint: string,
    options: RequestOptions = {}
  ): Promise<T> {
    const { requiresAuth = false, accessToken, ...fetchOptions } = options;
    const url = `${getApiBaseUrl()}${endpoint}`;

    // Set up headers
    const headers = new Headers(fetchOptions.headers);

    // Set default content type if not provided
    if (!headers.has('Content-Type')) {
      headers.set('Content-Type', 'application/json');
    }

    // Add auth token if required
    if (requiresAuth && accessToken) {
      headers.set('Authorization', `Bearer ${accessToken}`);
    }

    // Prepare final request options
    const requestOptions: RequestInit = {
      ...fetchOptions,
      headers
    };

    try {
      const response = await fetch(url, requestOptions);

      if (!response.ok) {
        throw new Error(`API request failed with status ${response.status}: ${response.statusText}`);
      }

      // Parse JSON response
      const data = await response.json();
      return data as T;
    } catch (error) {
      console.error(`Error making request to ${url}:`, error);
      throw error;
    }
  }

  /**
   * Make a GET request
   */
  protected async get<T>(endpoint: string, options: RequestOptions = {}): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'GET',
      ...options
    });
  }

  /**
   * Make a POST request
   */
  protected async post<T>(endpoint: string, body: any, options: RequestOptions = {}): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'POST',
      body: JSON.stringify(body),
      ...options
    });
  }

  /**
   * Make a PUT request
   */
  protected async put<T>(endpoint: string, body?: any, options: RequestOptions = {}): Promise<T> {
    const requestOptions: RequestOptions = {
      method: 'PUT',
      ...options
    };

    if (body) {
      requestOptions.body = JSON.stringify(body);
    }

    return this.request<T>(endpoint, requestOptions);
  }

  /**
   * Make a DELETE request
   */
  protected async delete<T>(endpoint: string, options: RequestOptions = {}): Promise<T> {
    return this.request<T>(endpoint, {
      method: 'DELETE',
      ...options
    });
  }
}
