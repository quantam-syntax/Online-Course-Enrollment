package org.example.onlinecourseenrollmentside.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.onlinecourseenrollmentside.App;
import org.example.onlinecourseenrollmentside.service.AuthService;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final AuthService authService = new AuthService(App.studentService);

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        String role = authService.login(username, password);
        if (role == null) {
            messageLabel.setText("Invalid credentials.");
            return;
        }

        App.currentRole = role;

        try {
            if ("ADMIN".equals(role)) {
                App.currentStudent = null;
                App.showScene("admin_dashboard.fxml", "Admin Dashboard");
                return;
            }

            int studentId = Integer.parseInt(username);
            App.currentStudent = App.studentService.findById(studentId).orElse(null);
            App.showScene("student_dashboard.fxml", "Student Dashboard");
        } catch (IOException e) {
            messageLabel.setText("Unable to open dashboard.");
        }
    }
}

