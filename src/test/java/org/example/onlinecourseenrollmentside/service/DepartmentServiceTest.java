package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Instructor;
import org.example.onlinecourseenrollmentside.model.Schedule;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DepartmentServiceTest {

    @Test
    void shouldPersistDepartmentMembership() throws Exception {
        Path tempDir = Files.createTempDirectory("department-test");
        InstructorService instructorService = new InstructorService(tempDir.resolve("instructors.csv"));
        CourseService courseService = new CourseService(tempDir.resolve("courses.csv"));
        DepartmentService departmentService = new DepartmentService(courseService, instructorService, tempDir.resolve("departments.csv"));

        instructorService.addInstructor(new Instructor(900, "Dr. Noor"));
        courseService.addCourse(new Course(401, "Databases", 1200.0, instructorService.findById(900).orElseThrow(), new Schedule("09:00", "Monday")));
        courseService.addCourse(new Course(402, "Distributed Systems", 1300.0, instructorService.findById(900).orElseThrow(), new Schedule("11:00", "Wednesday")));

        assertTrue(departmentService.addDepartment("Computer Science"));
        assertFalse(departmentService.addDepartment("Computer Science"));
        assertTrue(departmentService.addCourseToDepartment("Computer Science", 401));
        assertTrue(departmentService.addInstructorToDepartment("Computer Science", 900));

        assertEquals(2, departmentService.getCoursesForDepartment("Computer Science").size());

        DepartmentService reloaded = new DepartmentService(courseService, instructorService, tempDir.resolve("departments.csv"));
        assertEquals(1, reloaded.findByName("Computer Science").orElseThrow().getCourseIds().size());
        assertEquals(1, reloaded.findByName("Computer Science").orElseThrow().getInstructorIds().size());
    }
}

