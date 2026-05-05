package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Instructor;
import org.example.onlinecourseenrollmentside.model.Student;
import org.example.onlinecourseenrollmentside.model.Schedule;
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
        InstructorService instructorService = new InstructorService(tempDir.resolve("instructors.csv"));
        DepartmentService departmentService = new DepartmentService(courseService, instructorService, tempDir.resolve("departments.csv"));
        EnrollmentService enrollmentService = new EnrollmentService(courseService, departmentService, tempDir.resolve("enrollments.csv"));

        studentService.addStudent(new Student(200, "Riya"));
        courseService.addCourse(new Course(11, "Java Basics", 49.99, new Instructor(501, "Dr. Maya"), new Schedule("10:00", "Tuesday")));
        courseService.addCourse(new Course(12, "Advanced Java", 59.99, new Instructor(501, "Dr. Maya"), new Schedule("12:00", "Thursday")));

        departmentService.addDepartment("Software Engineering");
        departmentService.addCourseToDepartment("Software Engineering", 11);
        departmentService.addCourseToDepartment("Software Engineering", 12);

        assertEquals("Dr. Maya", courseService.findById(11).orElseThrow().getInstructor().getName());

        assertTrue(enrollmentService.selectCourse(200, 11));
        assertEquals(1, enrollmentService.viewEnrollments(200).size());
        assertEquals(49.99, enrollmentService.pendingFees(200), 0.001);

        int added = enrollmentService.selectDepartment(200, "Software Engineering");
        assertEquals(1, added);
        assertEquals(2, enrollmentService.viewEnrollments(200).size());
        assertEquals(109.98, enrollmentService.pendingFees(200), 0.001);

        double paid = enrollmentService.payAllPendingFees(200);
        assertEquals(109.98, paid, 0.001);
        assertEquals(0.0, enrollmentService.pendingFees(200), 0.001);
    }
}

