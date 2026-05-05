package org.example.onlinecourseenrollmentside.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.example.onlinecourseenrollmentside.App;
import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Enrollment;
import org.example.onlinecourseenrollmentside.model.Department;

import java.io.IOException;
import java.util.List;

public class StudentController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private ComboBox<Course> coursesCombo;

    @FXML
    private ComboBox<Department> departmentsCombo;

    @FXML
    private ListView<Enrollment> enrollmentsList;

    @FXML
    private Label feeLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        if (App.currentStudent == null) {
            statusLabel.setText("No student session.");
            return;
        }
        welcomeLabel.setText("Welcome, " + App.currentStudent.getName() + " (ID: " + App.currentStudent.getStudentId() + ")");
        coursesCombo.setItems(FXCollections.observableArrayList(App.courseService.getCourses()));
        departmentsCombo.setItems(FXCollections.observableArrayList(App.departmentService.getDepartments()));
        refreshEnrollmentView();
    }

    @FXML
    private void handleEnroll() {
        Course selected = coursesCombo.getValue();
        if (selected == null || App.currentStudent == null) {
            statusLabel.setText("Select a course first.");
            return;
        }

        boolean enrolled = App.enrollmentService.selectCourse(App.currentStudent.getStudentId(), selected.getCourseId());
        statusLabel.setText(enrolled ? "Enrollment successful." : "Enrollment failed or already exists.");
        refreshEnrollmentView();
    }

    @FXML
    private void handlePayFees() {
        if (App.currentStudent == null) {
            statusLabel.setText("No student session.");
            return;
        }

        double paid = App.feesService.payFees(App.currentStudent.getStudentId());
        if (paid == 0.0) {
            statusLabel.setText("No pending fees.");
        } else {
            statusLabel.setText("Paid $" + String.format("%.2f", paid));
        }
        refreshEnrollmentView();
    }

    @FXML
    private void handleRefresh() {
        refreshEnrollmentView();
    }

    @FXML
    private void handleEnrollDepartment() {
        Department selected = departmentsCombo.getValue();
        if (selected == null || App.currentStudent == null) {
            statusLabel.setText("Select a department first.");
            return;
        }

        int added = App.enrollmentService.selectDepartment(App.currentStudent.getStudentId(), selected.getName());
        statusLabel.setText(added > 0
                ? "Department enrollment successful. Added " + added + " course(s)."
                : "No new courses were added from that department.");
        refreshEnrollmentView();
    }

    @FXML
    private void handleLogout() {
        App.currentRole = null;
        App.currentStudent = null;
        try {
            App.showScene("login.fxml", "Course Enrollment Login");
        } catch (IOException e) {
            statusLabel.setText("Unable to return to login.");
        }
    }

    private void refreshEnrollmentView() {
        if (App.currentStudent == null) {
            return;
        }

        int studentId = App.currentStudent.getStudentId();
        List<Enrollment> enrollments = App.enrollmentService.viewEnrollments(studentId);
        enrollmentsList.setItems(FXCollections.observableArrayList(enrollments));

        double pending = App.enrollmentService.pendingFees(studentId);
        feeLabel.setText("Pending Fees: $" + String.format("%.2f", pending));
    }
}

