package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Instructor;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstructorServiceTest {

    @Test
    void shouldPersistInstructors() throws Exception {
        Path tempDir = Files.createTempDirectory("instructor-test");
        InstructorService instructorService = new InstructorService(tempDir.resolve("instructors.csv"));

        assertTrue(instructorService.addInstructor(new Instructor(501, "Dr. Maya")));
        assertFalse(instructorService.addInstructor(new Instructor(501, "Duplicate")));
        assertEquals(1, instructorService.getInstructors().size());
        assertEquals("Dr. Maya", instructorService.findById(501).orElseThrow().getName());

        InstructorService reloaded = new InstructorService(tempDir.resolve("instructors.csv"));
        assertEquals("Dr. Maya", reloaded.findById(501).orElseThrow().getName());
    }
}

