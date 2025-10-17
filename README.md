# Library Management System

A full-stack library management application built with Spring Boot and React, allowing users to browse, checkout, and manage books while administrators can respond to user queries and manage the library inventory.

## Project Overview

This Library Management System provides a digital platform for users to interact with a library's resources. It enables users to browse books, checkout, return, and renew loans, write reviews, and communicate with library administrators. The system also includes admin functionalities for managing books and responding to user messages.

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 21
- **Database**: MySQL 8
- **ORM**: Hibernate/JPA
- **Authentication**: Okta OAuth2
- **Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Build Tool**: Maven

### Frontend
- **Framework**: React 18.2.0
- **Language**: TypeScript 4.7.4
- **Routing**: React Router DOM 5.3.3
- **Authentication**: Okta React & Okta Sign-in Widget
- **Build Tool**: npm/yarn

## Project Structure

### Backend Structure
```
src/main/java/cz/ivosahlik/library/
├── config/         # Application configuration
├── controller/     # REST API endpoints
├── dao/            # Data Access Objects (repositories)
├── entity/         # JPA entity models
├── requestmodels/  # Request DTOs
├── responsemodels/ # Response DTOs
├── service/        # Business logic
└── utils/          # Utility classes
```

### Frontend Structure
```
src/react-library/
├── public/         # Static files
└── src/
    ├── layouts/    # Page layouts and components
    │   ├── BookCheckoutPage/
    │   ├── HomePage/
    │   ├── MessagesPage/
    │   ├── NavbarAndFooter/
    │   ├── SearchBooksPage/
    │   └── ShelfPage/
    ├── lib/        # Library utilities
    ├── models/     # TypeScript interfaces
    └── services/   # API services
```

## Features and Functionalities

### User Features
- **Book Browsing**: View all books with pagination
- **Book Search**: Search for books by title, author, or category
- **Book Details**: View comprehensive details about each book
- **Book Checkout**: Borrow books from the library
- **Book Return**: Return borrowed books
- **Loan Renewal**: Extend the borrowing period for a book
- **User Shelf**: View currently borrowed books and checkout history
- **Reviews**: Read and write reviews for books
- **Messaging**: Send questions or messages to library administrators

### Admin Features
- **Book Management**: Add, edit, or remove books from the library inventory
- **Message Handling**: Respond to user questions and messages
- **User Management**: View and manage user accounts and activities

## Setup and Installation

### Prerequisites
- Java 21 or higher
- Node.js and npm/yarn
- MySQL 8
- Okta Developer Account

### Backend Setup
1. Clone the repository
   ```bash
   git clone https://github.com/your-username/spring-boot-library.git
   cd spring-boot-library
   ```

2. Configure MySQL Database
   - Create a database named `reactlibrarydatabase`
   - Update credentials in `src/main/resources/application.properties` if needed

3. Configure Okta
   - Create an Okta developer account
   - Set up an OpenID Connect application
   - Update Okta settings in `application.properties`

4. Run the Spring Boot application
   ```bash
   mvn spring-boot:run
   ```
   The backend will be available at `http://localhost:8080`

### Frontend Setup
1. Navigate to the React app directory
   ```bash
   cd src/react-library
   ```

2. Install dependencies
   ```bash
   npm install
   ```

3. Start the development server
   ```bash
   npm start
   ```
   The frontend will be available at `http://localhost:3000`

## API Documentation

API documentation is available using Swagger UI at `http://localhost:8080/swagger-ui.html` after starting the backend server.

Key API endpoints:
- `/api/books` - Book related operations
- `/api/reviews` - Review related operations
- `/api/messages` - Messaging related operations

## Database Schema

The application uses the following main entities:
- **Book**: Stores book information including title, author, description, and availability
- **Checkout**: Records book checkouts by users
- **Review**: Stores user reviews for books
- **Message**: Manages communication between users and administrators
- **History**: Tracks checkout history

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Author

- **Ivo Vošahlík** - Initial work and maintenance

## License

This project is licensed under the MIT License - see the LICENSE file for details.
