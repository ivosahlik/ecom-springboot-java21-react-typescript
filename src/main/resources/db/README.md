# Authentication Database Schema

This document describes the database tables used for authentication in the Library Application.

## Users Table

The main table for authentication is `users`, which stores all user credentials and roles.

| Column    | Type         | Description                               |
|-----------|--------------|-------------------------------------------|
| id        | BIGINT       | Primary key, auto-increment               |
| username  | VARCHAR(50)  | Unique username for authentication        |
| password  | VARCHAR(255) | Encrypted password (BCrypt)               |
| email     | VARCHAR(100) | User's email address                      |
| role      | VARCHAR(20)  | User role (user, admin)                   |

## Authentication Flow

1. Users register or login with username/password
2. Credentials are validated against the users table
3. On successful validation, JWT token is generated
4. JWT token is used for subsequent authenticated requests

## Database Initialization

The database tables are created in one of two ways:

1. **Automatically by Hibernate**: Using JPA annotations in the User entity class with `spring.jpa.hibernate.ddl-auto=update`
2. **Manually with SQL script**: Optional schema.sql script is provided for more controlled table creation

## Initial Users

The DataInitializer component creates two initial users:

1. **Admin User**:
   - Username: admin
   - Password: admin123
   - Role: admin

2. **Regular User**:
   - Username: user  
   - Password: user123
   - Role: user
