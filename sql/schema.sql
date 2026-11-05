-- ===================================================
-- Student Management System Database Setup Script
-- ===================================================

-- Create the database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS student_management_db;

-- Switch to the student_management_db database
USE student_management_db;

-- Drop table if it exists (useful for clean reinstalls)
-- DROP TABLE IF EXISTS students;

-- Create students table
CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    course VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample records for initial testing
INSERT INTO students (id, name, age, course, email, phone) VALUES
(101, 'Alice Johnson', 20, 'Computer Science', 'alice.johnson@example.com', '9876543210'),
(102, 'Bob Smith', 22, 'Information Technology', 'bob.smith@example.com', '9876543211'),
(103, 'Charlie Brown', 21, 'Mechanical Engineering', 'charlie.brown@example.com', '9876543212')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Verify table data
SELECT * FROM students;

