package org.example.onlinecourseenrollmentside.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.onlinecourseenrollmentside.App;
import org.example.onlinecourseenrollmentside.model.Course;
import org.example.onlinecourseenrollmentside.model.Department;
import org.example.onlinecourseenrollmentside.model.Instructor;
import org.example.onlinecourseenrollmentside.model.Schedule;
import org.example.onlinecourseenrollmentside.model.Student;

import java.io.IOException;

public class AdminController {
    @FXML
    private TextField studentIdField;

    @FXML
    private TextField studentNameField;

    @FXML
    private TextField instructorIdField;

    @FXML
    private TextField instructorNameField;

    @FXML
    private TextField departmentNameField;

    @FXML
    private TextField courseIdField;

    @FXML
    private TextField courseTitleField;

    @FXML
    private TextField courseFeeField;

    @FXML
    private TextField courseDayField;

    @FXML
    private TextField courseTimeField;

    @FXML
    private ComboBox<Department> instructorDepartmentCombo;

    @FXML
    private ComboBox<Course> instructorCourseCombo;

    @FXML
    private ComboBox<Department> courseDepartmentCombo;

    @FXML
    private ComboBox<Instructor> courseInstructorCombo;

    @FXML
    private ListView<Student> studentsList;

    @FXML
    private ListView<Instructor> instructorsList;

    @FXML
    private ListView<Department> departmentsList;

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
    private void handleAddInstructor() {
        try {
            int id = Integer.parseInt(instructorIdField.getText().trim());
            String name = instructorNameField.getText().trim();
            Department department = instructorDepartmentCombo.getValue();
            Course course = instructorCourseCombo.getValue();
            if (name.isBlank()) {
                statusLabel.setText("Instructor name is required.");
                return;
            }

            if (department == null || course == null) {
                statusLabel.setText("Select a department and a course for the instructor.");
                return;
            }

            Instructor newInstructor = new Instructor(id, name);
            boolean added = App.instructorService.addInstructor(newInstructor);
            if (added) {
                boolean courseAssigned = App.courseService.assignInstructorToCourse(course.getCourseId(), newInstructor);
                App.departmentService.addInstructorToDepartment(department.getName(), id);
                App.departmentService.addCourseToDepartment(department.getName(), course.getCourseId());
                if (!courseAssigned) {
                    statusLabel.setText("Instructor added, but the course could not be updated.");
                    refreshLists();
                    return;
                }
            }
            statusLabel.setText(added ? "Instructor added." : "Instructor ID already exists.");
            refreshLists();
        } catch (NumberFormatException e) {
            statusLabel.setText("Instructor ID must be a number.");
        }
    }

    @FXML
    private void handleAddDepartment() {
        String name = departmentNameField.getText().trim();
        if (name.isBlank()) {
            statusLabel.setText("Department name is required.");
            return;
        }

        boolean added = App.departmentService.addDepartment(name);
        statusLabel.setText(added ? "Department added." : "Department already exists.");
        refreshLists();
    }

    @FXML
    private void handleAddCourse() {
        try {
            int id = Integer.parseInt(courseIdField.getText().trim());
            String title = courseTitleField.getText().trim();
            double fee = Double.parseDouble(courseFeeField.getText().trim());
            String day = courseDayField.getText().trim();
            String time = courseTimeField.getText().trim();
            Department department = courseDepartmentCombo.getValue();
            Instructor instructor = courseInstructorCombo.getValue();

            if (title.isBlank()) {
                statusLabel.setText("Course title is required.");
                return;
            }

            if (day.isBlank() || time.isBlank()) {
                statusLabel.setText("Course day and time are required.");
                return;
            }

            if (department == null) {
                statusLabel.setText("Select a department for the course.");
                return;
            }

            Schedule schedule = new Schedule(time, day);
            if (!schedule.assignSchedule()) {
                statusLabel.setText("Course day and time are required.");
                return;
            }

            boolean added = App.courseService.addCourse(new Course(id, title, fee, instructor, schedule));
            if (added) {
                App.departmentService.addCourseToDepartment(department.getName(), id);
                if (instructor != null) {
                    App.departmentService.addInstructorToDepartment(department.getName(), instructor.getInstructorId());
                }
            }
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

    @FXML
    private void handleDeleteStudent() {
        Student selected = studentsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a student to delete.");
            return;
        }
        boolean removed = App.studentService.deleteStudent(selected.getStudentId());
        int removedEnrollments = App.enrollmentService.removeEnrollmentsByStudent(selected.getStudentId());
        statusLabel.setText(removed ? "Student deleted. Enrollments removed: " + removedEnrollments : "Failed to delete student.");
        refreshLists();
    }

    @FXML
    private void handleDeleteInstructor() {
        Instructor selected = instructorsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an instructor to delete.");
            return;
        }
        boolean removed = App.instructorService.deleteInstructor(selected.getInstructorId());
        boolean unassigned = App.courseService.unassignInstructorFromCourses(selected.getInstructorId());
        boolean deptUpdated = App.departmentService.removeInstructorFromDepartments(selected.getInstructorId());
        statusLabel.setText(removed ? "Instructor deleted." : "Failed to delete instructor.");
        if (unassigned) {
            statusLabel.setText(statusLabel.getText() + " Instructor unassigned from some courses.");
        }
        if (deptUpdated) {
            statusLabel.setText(statusLabel.getText() + " Departments updated.");
        }
        refreshLists();
    }

    @FXML
    private void handleDeleteDepartment() {
        Department selected = departmentsList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a department to delete.");
            return;
        }
        boolean removed = App.departmentService.deleteDepartment(selected.getName());
        statusLabel.setText(removed ? "Department deleted." : "Failed to delete department.");
        refreshLists();
    }

    @FXML
    private void handleDeleteCourse() {
        Course selected = coursesList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a course to delete.");
            return;
        }
        boolean removed = App.courseService.deleteCourse(selected.getCourseId());
        int removedEnrollments = App.enrollmentService.removeEnrollmentsByCourse(selected.getCourseId());
        boolean deptUpdated = App.departmentService.removeCourseFromDepartments(selected.getCourseId());
        statusLabel.setText(removed ? "Course deleted. Enrollments removed: " + removedEnrollments : "Failed to delete course.");
        if (deptUpdated) {
            statusLabel.setText(statusLabel.getText() + " Departments updated.");
        }
        refreshLists();
    }

    private void refreshLists() {
        studentsList.setItems(FXCollections.observableArrayList(App.studentService.getStudents()));
        instructorsList.setItems(FXCollections.observableArrayList(App.instructorService.getInstructors()));
        instructorDepartmentCombo.setItems(FXCollections.observableArrayList(App.departmentService.getDepartments()));
        instructorCourseCombo.setItems(FXCollections.observableArrayList(App.courseService.getCourses()));
        courseDepartmentCombo.setItems(FXCollections.observableArrayList(App.departmentService.getDepartments()));
        courseInstructorCombo.setItems(FXCollections.observableArrayList(App.instructorService.getInstructors()));
        departmentsList.setItems(FXCollections.observableArrayList(App.departmentService.getDepartments()));
        coursesList.setItems(FXCollections.observableArrayList(App.courseService.getCourses()));
    }
}

