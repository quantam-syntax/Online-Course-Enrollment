package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Student;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnrollmentServiceTest {

    @Test
    void shouldPersistEnrollmentAndFees() throws Exception {
        Path tempDir = Files.createTempDirectory("enrollment-test");

        StudentService studentService = new StudentService(tempDir.resolve("students.csv"));
        CourseService courseService = new CourseService(tempDir.resolve("courses.csv"));
        EnrollmentService enrollmentService = new EnrollmentService(courseService, tempDir.resolve("enrollments.csv"));

        studentService.addStudent(new Student(200, "Riya"));
        courseService.addCourse(new Course(11, "Java Basics", 49.99));

        assertTrue(enrollmentService.selectCourse(200, 11));
        assertEquals(1, enrollmentService.viewEnrollments(200).size());
        assertEquals(49.99, enrollmentService.pendingFees(200), 0.001);

        double paid = enrollmentService.payAllPendingFees(200);
        assertEquals(49.99, paid, 0.001);
        assertEquals(0.0, enrollmentService.pendingFees(200), 0.001);
    }
}

