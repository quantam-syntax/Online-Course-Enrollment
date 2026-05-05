package org.example.onlinecourseenrollmentside.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.onlinecourseenrollmentside.App;
import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Student;

import java.io.IOException;

public class AdminController {
    @FXML
    private TextField studentIdField;

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField courseIdField;

    @FXML
    private TextField courseTitleField;

    @FXML
    private TextField courseFeeField;

    @FXML
    private ListView<Student> studentsList;

    @FXML
    private ListView<Course> coursesList;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        refreshLists();
    }

    @FXML
    private void handleAddStudent() {
        try {
            int id = Integer.parseInt(studentIdField.getText().trim());
            String name = studentNameField.getText().trim();
            if (name.isBlank()) {
                statusLabel.setText("Student name is required.");
                return;
            }

            boolean added = App.studentService.addStudent(new Student(id, name));
            statusLabel.setText(added ? "Student added." : "Student ID already exists.");
            refreshLists();
        } catch (NumberFormatException e) {
            statusLabel.setText("Student ID must be a number.");
        }
    }

    @FXML
    private void handleAddCourse() {
        try {
            int id = Integer.parseInt(courseIdField.getText().trim());
            String title = courseTitleField.getText().trim();
            double fee = Double.parseDouble(courseFeeField.getText().trim());

            if (title.isBlank()) {
                statusLabel.setText("Course title is required.");
                return;
            }

            boolean added = App.courseService.addCourse(new Course(id, title, fee));
            statusLabel.setText(added ? "Course added." : "Course ID already exists.");
            refreshLists();
        } catch (NumberFormatException e) {
            statusLabel.setText("Course ID and fee must be numbers.");
        }
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

    private void refreshLists() {
        studentsList.setItems(FXCollections.observableArrayList(App.studentService.getStudents()));
        coursesList.setItems(FXCollections.observableArrayList(App.courseService.getCourses()));
    }
}

