package com.sms.model;

/**
 * Model class representing a Student entity.
 * Encapsulates all student attributes following OOP principles (Encapsulation).
 */
public class Student {
    private int id;
    private String name;
    private int age;
    private String course;
    private String email;
    private String phone;

    // Default constructor
    public Student() {
    }

    // Parameterized constructor
    public Student(int id, String name, int age, String course, String email, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Student [ID=" + id + ", Name=" + name + ", Age=" + age + 
               ", Course=" + course + ", Email=" + email + ", Phone=" + phone + "]";
    }
}

