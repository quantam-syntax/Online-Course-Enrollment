package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Student;

public class AuthService {
    private final StudentService studentService;
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";

    public AuthService(StudentService studentService) {
        this.studentService = studentService;
    }

    public String login(String username, String password) {
        if (adminUsername.equals(username) && adminPassword.equals(password)) {
            return "ADMIN";
        }

        for (Student student : studentService.getStudents()) {
            String studentId = String.valueOf(student.getStudentId());
            if (studentId.equals(username) && studentId.equals(password)) {
                return "STUDENT";
            }
        }

        return null;
    }
}

