package org.example.onlinecourseenrollmentside.model;

public class Schedule {
    private String time;
    private String day;

    public Schedule() {
        this("", "");
    }

    public Schedule(String time, String day) {
        this.time = time == null ? "" : time.trim();
        this.day = day == null ? "" : day.trim();
    }

    public String getTime() {
        return time;
    }

    public String getDay() {
        return day;
    }

    public boolean assignSchedule() {
        return !time.isBlank() && !day.isBlank();
    }

    public String viewSchedule() {
        if (!assignSchedule()) {
            return "Schedule not assigned";
        }
        return day + " at " + time;
    }
}


