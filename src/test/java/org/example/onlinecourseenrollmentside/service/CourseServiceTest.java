package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Instructor;
import org.example.onlinecourseenrollmentside.model.Schedule;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CourseServiceTest {

    @Test
    void shouldPersistCourseInstructorAndSchedule() throws Exception {
        Path tempDir = Files.createTempDirectory("course-test");
        CourseService courseService = new CourseService(tempDir.resolve("courses.csv"));

        Course course = new Course(77, "Software Architecture", 250.0, new Instructor(10, "Dr. Lin"), new Schedule("14:00", "Friday"));

        assertTrue(courseService.addCourse(course));
        assertFalse(courseService.addCourse(course));
        assertEquals("Software Architecture", courseService.findById(77).orElseThrow().getTitle());
        assertEquals("Friday at 14:00", courseService.findById(77).orElseThrow().getSchedule().viewSchedule());

        CourseService reloaded = new CourseService(tempDir.resolve("courses.csv"));
        assertEquals("Dr. Lin", reloaded.findById(77).orElseThrow().getInstructor().getName());
        assertEquals("Friday at 14:00", reloaded.findById(77).orElseThrow().getSchedule().viewSchedule());
    }

    @Test
    void shouldPersistCourseWithoutInstructor() throws Exception {
        Path tempDir = Files.createTempDirectory("course-test-no-instructor");
        CourseService courseService = new CourseService(tempDir.resolve("courses.csv"));

        Course course = new Course(88, "Database Systems", 180.0, null, new Schedule("10:30", "Tuesday"));

        assertTrue(courseService.addCourse(course));

        CourseService reloaded = new CourseService(tempDir.resolve("courses.csv"));
        assertEquals("Database Systems", reloaded.findById(88).orElseThrow().getTitle());
        assertEquals("Tuesday at 10:30", reloaded.findById(88).orElseThrow().getSchedule().viewSchedule());
        assertNull(reloaded.findById(88).orElseThrow().getInstructor());
    }
}

