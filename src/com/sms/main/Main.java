package com.sms.main;

import com.sms.dao.StudentDAO;
import com.sms.model.Student;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Main application class providing a console-based, menu-driven user interface.
 * Implements input validation, exception handling, and calls StudentDAO.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();

    // Regular expression for validating standard email formats
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Regular expression for validating phone numbers (10 to 15 digits, optional leading +)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("       WELCOME TO STUDENT MANAGEMENT SYSTEM       ");
        System.out.println("==================================================");

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = readIntegerInput("Enter your choice (1-6): ");

            switch (choice) {
                case 1:
                    addNewStudent();
                    break;
                case 2:
                    updateStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    searchStudents();
                    break;
                case 5:
                    viewAllStudents();
                    break;
                case 6:
                    System.out.println("\nThank you for using Student Management System. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n[Error] Invalid choice! Please select an option between 1 and 6.");
            }
        }

        scanner.close();
    }

    /**
     * Displays the primary menu options.
     */
    private static void displayMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("                   MAIN MENU                      ");
        System.out.println("--------------------------------------------------");
        System.out.println("1. Add New Student");
        System.out.println("2. Update Student Details by ID");
        System.out.println("3. Delete Student by ID");
        System.out.println("4. Search Students by ID or Name");
        System.out.println("5. View All Students");
        System.out.println("6. Exit");
        System.out.println("--------------------------------------------------");
    }

    /**
     * Feature 1: Adds a new student after thorough validation.
     */
    private static void addNewStudent() {
        System.out.println("\n--- Add New Student ---");

        // 1. Validate ID (Must be positive and unique)
        int id;
        while (true) {
            id = readIntegerInput("Enter Student ID: ");
            if (id <= 0) {
                System.out.println("[Validation Error] Student ID must be a positive integer.");
                continue;
            }
            if (studentDAO.isIdExists(id)) {
                System.out.println("[Validation Error] A student with ID " + id + " already exists. Please use a unique ID.");
                continue;
            }
            break;
        }

        // 2. Validate Name
        String name = readNonEmptyString("Enter Student Name: ");

        // 3. Validate Age
        int age;
        while (true) {
            age = readIntegerInput("Enter Student Age (5 - 120): ");
            if (age < 5 || age > 120) {
                System.out.println("[Validation Error] Please enter a realistic age between 5 and 120.");
                continue;
            }
            break;
        }

        // 4. Validate Course
        String course = readNonEmptyString("Enter Course: ");

        // 5. Validate Email Format
        String email;
        while (true) {
            email = readNonEmptyString("Enter Email: ");
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                System.out.println("[Validation Error] Invalid email format! Example: user@domain.com");
                continue;
            }
            break;
        }

        // 6. Validate Phone Number
        String phone;
        while (true) {
            phone = readNonEmptyString("Enter Phone Number (10-15 digits): ");
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                System.out.println("[Validation Error] Invalid phone number! Must be 10-15 digits (optional leading +).");
                continue;
            }
            break;
        }

        // Persist via DAO
        Student student = new Student(id, name, age, course, email, phone);
        boolean success = studentDAO.addStudent(student);

        if (success) {
            System.out.println("\n[Success] Student added successfully!");
        } else {
            System.out.println("\n[Error] Could not add student. Check database logs for details.");
        }
    }

    /**
     * Feature 2: Updates an existing student's details.
     */
    private static void updateStudent() {
        System.out.println("\n--- Update Student Details ---");
        int id = readIntegerInput("Enter Student ID to update: ");

        Student student = studentDAO.getStudentById(id);
        if (student == null) {
            System.out.println("[Not Found] No student found with ID: " + id);
            return;
        }

        System.out.println("\nCurrent Details:");
        printStudentTable(List.of(student));
        System.out.println("(Press Enter directly without typing to keep the current value)");

        // Update Name
        System.out.print("Enter New Name [" + student.getName() + "]: ");
        String nameInput = scanner.nextLine().trim();
        if (!nameInput.isEmpty()) {
            student.setName(nameInput);
        }

        // Update Age
        while (true) {
            System.out.print("Enter New Age [" + student.getAge() + "]: ");
            String ageInput = scanner.nextLine().trim();
            if (ageInput.isEmpty()) {
                break;
            }
            try {
                int age = Integer.parseInt(ageInput);
                if (age < 5 || age > 120) {
                    System.out.println("[Validation Error] Age must be between 5 and 120.");
                    continue;
                }
                student.setAge(age);
                break;
            } catch (NumberFormatException e) {
                System.out.println("[Validation Error] Invalid number format. Please enter a valid integer.");
            }
        }

        // Update Course
        System.out.print("Enter New Course [" + student.getCourse() + "]: ");
        String courseInput = scanner.nextLine().trim();
        if (!courseInput.isEmpty()) {
            student.setCourse(courseInput);
        }

        // Update Email
        while (true) {
            System.out.print("Enter New Email [" + student.getEmail() + "]: ");
            String emailInput = scanner.nextLine().trim();
            if (emailInput.isEmpty()) {
                break;
            }
            if (!EMAIL_PATTERN.matcher(emailInput).matches()) {
                System.out.println("[Validation Error] Invalid email format! Example: user@domain.com");
                continue;
            }
            student.setEmail(emailInput);
            break;
        }

        // Update Phone
        while (true) {
            System.out.print("Enter New Phone [" + student.getPhone() + "]: ");
            String phoneInput = scanner.nextLine().trim();
            if (phoneInput.isEmpty()) {
                break;
            }
            if (!PHONE_PATTERN.matcher(phoneInput).matches()) {
                System.out.println("[Validation Error] Invalid phone number! Must be 10-15 digits.");
                continue;
            }
            student.setPhone(phoneInput);
            break;
        }

        // Persist update via DAO
        boolean success = studentDAO.updateStudent(student);
        if (success) {
            System.out.println("\n[Success] Student details updated successfully!");
        } else {
            System.out.println("\n[Error] Failed to update student details.");
        }
    }

    /**
     * Feature 3: Deletes a student by their ID with confirmation.
     */
    private static void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        int id = readIntegerInput("Enter Student ID to delete: ");

        Student student = studentDAO.getStudentById(id);
        if (student == null) {
            System.out.println("[Not Found] No student found with ID: " + id);
            return;
        }

        printStudentTable(List.of(student));
        System.out.print("Are you sure you want to delete this student record? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            boolean success = studentDAO.deleteStudent(id);
            if (success) {
                System.out.println("\n[Success] Student record deleted successfully!");
            } else {
                System.out.println("\n[Error] Failed to delete student record.");
            }
        } else {
            System.out.println("\n[Cancelled] Deletion cancelled by user.");
        }
    }

    /**
     * Feature 4: Searches students by ID or Name.
     */
    private static void searchStudents() {
        System.out.println("\n--- Search Students ---");
        System.out.println("1. Search by Student ID");
        System.out.println("2. Search by Student Name (or substring)");
        int choice = readIntegerInput("Enter search option (1-2): ");

        if (choice == 1) {
            int id = readIntegerInput("Enter Student ID: ");
            Student student = studentDAO.getStudentById(id);
            if (student != null) {
                printStudentTable(List.of(student));
            } else {
                System.out.println("\n[Not Found] No student found with ID: " + id);
            }
        } else if (choice == 2) {
            String name = readNonEmptyString("Enter student name to search: ");
            List<Student> results = studentDAO.searchStudentsByName(name);
            if (!results.isEmpty()) {
                printStudentTable(results);
            } else {
                System.out.println("\n[Not Found] No students found matching name: \"" + name + "\"");
            }
        } else {
            System.out.println("[Error] Invalid search option.");
        }
    }

    /**
     * Feature 5: Views all students in tabular format.
     */
    private static void viewAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentDAO.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("[Info] No student records found in database.");
        } else {
            printStudentTable(students);
        }
    }

    /**
     * Prints a formatted ASCII table of students.
     * 
     * @param students List of students to display
     */
    private static void printStudentTable(List<Student> students) {
        String line = "+-------+----------------------+-----+-----------------------+--------------------------------+-----------------+";
        System.out.println(line);
        System.out.printf("| %-5s | %-20s | %-3s | %-21s | %-30s | %-15s |\n", 
                          "ID", "NAME", "AGE", "COURSE", "EMAIL", "PHONE");
        System.out.println(line);

        for (Student s : students) {
            System.out.printf("| %-5d | %-20s | %-3d | %-21s | %-30s | %-15s |\n",
                    s.getId(),
                    truncate(s.getName(), 20),
                    s.getAge(),
                    truncate(s.getCourse(), 21),
                    truncate(s.getEmail(), 30),
                    truncate(s.getPhone(), 15));
        }
        System.out.println(line);
        System.out.println("Total records: " + students.size());
    }

    /**
     * Helper to safely read an integer without Scanner newline skipping issues.
     */
    private static int readIntegerInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[Validation Error] Invalid input! Please enter a valid number.");
            }
        }
    }

    /**
     * Helper to read a non-empty string input.
     */
    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[Validation Error] This field cannot be empty. Please enter a value.");
        }
    }

    /**
     * Truncates long strings so table columns do not wrap awkwardly.
     */
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}

