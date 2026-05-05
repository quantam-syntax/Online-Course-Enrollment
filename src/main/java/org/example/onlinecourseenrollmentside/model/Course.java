package org.example.onlinecourseenrollmentside.model;

public class Course {
    private final int courseId;
    private final String title;
    private final double fee;

    public Course(int courseId, String title, double fee) {
        this.courseId = courseId;
        this.title = title;
        this.fee = fee;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getTitle() {
        return title;
    }

    public double getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return courseId + " - " + title + " ($" + String.format("%.2f", fee) + ")";
    }
}

