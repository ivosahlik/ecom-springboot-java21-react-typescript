import { AuthResponse } from '../models/AuthResponse';

export class AuthService {
  private static baseUrl = 'http://localhost:8080/api/auth';

  public static async login(username: string, password: string): Promise<AuthResponse> {
    const url = `${this.baseUrl}/login`;
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      throw new Error('Login failed');
    }

    return await response.json();
  }
}
