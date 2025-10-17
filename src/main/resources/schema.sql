-- This file is optional as Hibernate will create tables automatically
-- Use this file if you want more control over table creation

-- Create users table if it doesn't exist
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- Note: Spring Boot will execute this schema.sql file
-- before Hibernate starts if spring.jpa.hibernate.ddl-auto=create-drop
-- For ddl-auto=update, this script runs but won't modify existing tables
