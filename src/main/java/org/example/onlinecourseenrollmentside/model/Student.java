package org.example.onlinecourseenrollmentside.model;

public class Student {
    private final int studentId;
    private final String name;

    public Student(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return studentId + " - " + name;
    }
}

