package org.example.onlinecourseenrollmentside.model;

public class Instructor {
    private final int instructorId;
    private final String name;

    public Instructor(int instructorId, String name) {
        this.instructorId = instructorId;
        this.name = name;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return instructorId + " - " + name;
    }
}

