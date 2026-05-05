package org.example.onlinecourseenrollmentside.service;

public class FeesService {
    private final EnrollmentService enrollmentService;

    public FeesService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    public double payFees(int studentId) {
        return enrollmentService.payAllPendingFees(studentId);
    }
}

