package org.example.onlinecourseenrollmentside.model;

public class Course {
    private final int courseId;
    private final String title;
    private final double fee;
    private final Instructor instructor;
    private final Schedule schedule;

    public Course(int courseId, String title, double fee) {
        this(courseId, title, fee, null, new Schedule());
    }

    public Course(int courseId, String title, double fee, Instructor instructor) {
        this(courseId, title, fee, instructor, new Schedule());
    }

    public Course(int courseId, String title, double fee, Instructor instructor, Schedule schedule) {
        this.courseId = courseId;
        this.title = title;
        this.fee = fee;
        this.instructor = instructor;
        this.schedule = schedule == null ? new Schedule() : schedule;
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

    public Instructor getInstructor() {
        return instructor;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        String base = courseId + " - " + title + " ($" + String.format("%.2f", fee) + ")";
        StringBuilder details = new StringBuilder(base);
        if (instructor != null) {
            details.append(" | Instructor: ").append(instructor.getName());
        }
        if (schedule != null && schedule.assignSchedule()) {
            details.append(" | Schedule: ").append(schedule.viewSchedule());
        }
        return details.toString();
    }
}

