import React from 'react';
import { Route, Redirect, RouteProps } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

interface SecureRouteProps extends RouteProps {
  path: string;
  children: React.ReactNode;
}

export const SecureRoute = ({ children, ...rest }: SecureRouteProps) => {
  const { authState } = useAuth();

  return (
    <Route
      {...rest}
      render={({ location }) =>
        authState.isAuthenticated ? (
          children
        ) : (
          <Redirect
            to={{
              pathname: '/login',
              state: { from: location }
            }}
          />
        )
      }
    />
  );
};
