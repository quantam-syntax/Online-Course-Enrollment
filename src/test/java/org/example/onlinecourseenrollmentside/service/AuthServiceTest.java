package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Student;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthServiceTest {

    @Test
    void shouldLoginAdminAndStudent() throws Exception {
        Path tempDir = Files.createTempDirectory("auth-test");
        StudentService studentService = new StudentService(tempDir.resolve("students.csv"));
        studentService.addStudent(new Student(101, "Ari"));

        AuthService authService = new AuthService(studentService);

        assertEquals("ADMIN", authService.login("admin", "admin123"));
        assertEquals("STUDENT", authService.login("101", "101"));
        assertNull(authService.login("101", "wrong"));
    }
}

