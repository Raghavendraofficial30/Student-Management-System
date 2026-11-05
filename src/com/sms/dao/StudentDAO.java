package com.sms.dao;

import com.sms.model.Student;
import com.sms.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) class for Student operations.
 * Separates data persistence logic from business logic.
 * Uses PreparedStatement to prevent SQL Injection attacks.
 */
public class StudentDAO {

    /**
     * Checks if a student with the given ID already exists in the database.
     * 
     * @param id Student ID
     * @return true if exists, false otherwise
     */
    public boolean isIdExists(int id) {
        String sql = "SELECT id FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[Database Error] Error checking student existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adds a new student record to the database.
     * 
     * @param student Student model containing details
     * @return true if insertion was successful, false otherwise
     */
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (id, name, age, course, email, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Parameter mapping ensures values are escaped, preventing SQL injection
            ps.setInt(1, student.getId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getCourse());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhone());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[Database Error] Failed to add student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing student's details by their ID.
     * 
     * @param student Student model containing updated details
     * @return true if update was successful, false otherwise
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, age = ?, course = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getName());
            ps.setInt(2, student.getAge());
            ps.setString(3, student.getCourse());
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPhone());
            ps.setInt(6, student.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[Database Error] Failed to update student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a student record by ID.
     * 
     * @param id Student ID to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("[Database Error] Failed to delete student: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a student by their unique ID.
     * 
     * @param id Student ID
     * @return Student object if found, null otherwise
     */
    public Student getStudentById(int id) {
        String sql = "SELECT id, name, age, course, email, phone FROM students WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToStudent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("[Database Error] Failed to retrieve student by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Searches students by matching a substring in their name (case-insensitive).
     * 
     * @param name Name or partial name
     * @return List of matching students
     */
    public List<Student> searchStudentsByName(String name) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, name, age, course, email, phone FROM students WHERE LOWER(name) LIKE LOWER(?) ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(mapRowToStudent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("[Database Error] Failed to search students by name: " + e.getMessage());
        }
        return students;
    }

    /**
     * Retrieves all student records from the database.
     * 
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT id, name, age, course, email, phone FROM students ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }

        } catch (SQLException e) {
            System.err.println("[Database Error] Failed to fetch all students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Helper method to map the current row of a ResultSet to a Student object.
     * 
     * @param rs ResultSet positioned at a valid row
     * @return Populated Student object
     * @throws SQLException if a column reading error occurs
     */
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        return new Student(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getString("course"),
            rs.getString("email"),
            rs.getString("phone")
        );
    }
}

