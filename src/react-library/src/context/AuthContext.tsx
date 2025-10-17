import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';

export interface AuthState {
  isAuthenticated: boolean;
  token: string | null;
  username: string | null;
  role: string | null;
}

interface AuthContextType {
  authState: AuthState;
  login: (token: string, username: string, role: string) => void;
  logout: () => void;
}

const defaultAuthState: AuthState = {
  isAuthenticated: false,
  token: null,
  username: null,
  role: null
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [authState, setAuthState] = useState<AuthState>(() => {
    // Try to get auth state from localStorage
    const storedAuth = localStorage.getItem('authState');
    return storedAuth ? JSON.parse(storedAuth) : defaultAuthState;
  });

  // Update localStorage when auth state changes
  useEffect(() => {
    localStorage.setItem('authState', JSON.stringify(authState));
  }, [authState]);

  const login = (token: string, username: string, role: string) => {
    setAuthState({
      isAuthenticated: true,
      token,
      username,
      role
    });
  };

  const logout = () => {
    localStorage.removeItem('authState');
    setAuthState(defaultAuthState);
  };

  const value = {
    authState,
    login,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}
