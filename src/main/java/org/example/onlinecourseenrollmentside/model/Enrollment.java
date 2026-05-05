package org.example.onlinecourseenrollmentside.model;

public class Enrollment {
    private final int studentId;
    private final int courseId;
    private final String courseTitle;
    private final double fee;
    private boolean paid;

    public Enrollment(int studentId, int courseId, String courseTitle, double fee, boolean paid) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.fee = fee;
        this.paid = paid;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public double getFee() {
        return fee;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        String status = paid ? "PAID" : "PENDING";
        return courseId + " - " + courseTitle + " ($" + String.format("%.2f", fee) + ") [" + status + "]";
    }
}

