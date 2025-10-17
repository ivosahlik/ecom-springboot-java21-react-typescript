import React from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import './App.css';
import { BookCheckoutPage } from './layouts/BookCheckoutPage/BookCheckoutPage';
import { HomePage } from './layouts/HomePage/HomePage';
import { Footer } from './layouts/NavbarAndFooter/Footer';
import { Navbar } from './layouts/NavbarAndFooter/Navbar';
import { SearchBooksPage } from './layouts/SearchBooksPage/SearchBooksPage';
import { ReviewListPage } from './layouts/BookCheckoutPage/ReviewListPage/ReviewListPage';
import { ShelfPage } from './layouts/ShelfPage/ShelfPage';
import { MessagesPage } from './layouts/MessagesPage/MessagesPage';
import { ManageLibraryPage } from './layouts/ManageLibraryPage/ManageLibraryPage';
import { SecureRoute } from './Auth/SecureRoute';
import { LoginPage } from './Auth/LoginPage';
import { AuthProvider } from './context/AuthContext';

export const App = () => {
  return (
    <AuthProvider>
      <div className='d-flex flex-column min-vh-100'>
        <Navbar />
        <div className='flex-grow-1'>
          <Switch>
            <Route path='/' exact>
              <Redirect to='/home' />
            </Route>
            <Route path='/home'>
              <HomePage />
            </Route>
            <Route path='/search'>
              <SearchBooksPage />
            </Route>
            <Route path='/reviewlist/:bookId'>
              <ReviewListPage/>
            </Route>
            <Route path='/checkout/:bookId'>
              <BookCheckoutPage/>
            </Route>
            <Route path='/login'>
              <LoginPage />
            </Route>
            <SecureRoute path='/shelf'>
              <ShelfPage/>
            </SecureRoute>
            <SecureRoute path='/messages'>
              <MessagesPage/>
            </SecureRoute>
            <SecureRoute path='/admin'>
              <ManageLibraryPage/>
            </SecureRoute>
          </Switch>
        </div>
        <Footer />
      </div>
    </AuthProvider>
  );
}
