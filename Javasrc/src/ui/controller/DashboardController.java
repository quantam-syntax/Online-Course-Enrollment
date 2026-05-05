package ui.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import model.Course;
import model.Enrollment;
import model.Student;
import service.CourseService;
import service.EnrollmentService;
import service.StudentService;
import ui.model.EnrollmentRow;

public class DashboardController {

    @FXML
    private TextField studentNameField;
    @FXML
    private TextField studentDepartmentField;
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private TableColumn<Student, Integer> studentIdColumn;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private TableColumn<Student, String> studentDepartmentColumn;

    @FXML
    private TextField courseNameField;
    @FXML
    private TextField courseCreditsField;
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private TableColumn<Course, Integer> courseIdColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, Integer> courseCreditsColumn;

    @FXML
    private ComboBox<Student> enrollmentStudentCombo;
    @FXML
    private ComboBox<Course> enrollmentCourseCombo;
    @FXML
    private TableView<EnrollmentRow> enrollmentsTable;
    @FXML
    private TableColumn<EnrollmentRow, Integer> enrollmentIdColumn;
    @FXML
    private TableColumn<EnrollmentRow, Integer> enrollmentStudentIdColumn;
    @FXML
    private TableColumn<EnrollmentRow, String> enrollmentStudentNameColumn;
    @FXML
    private TableColumn<EnrollmentRow, Integer> enrollmentCourseIdColumn;
    @FXML
    private TableColumn<EnrollmentRow, String> enrollmentCourseNameColumn;
    @FXML
    private TableColumn<EnrollmentRow, String> enrollmentDateColumn;

    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;

    @FXML
    public void initialize() {
        studentService = new StudentService();
        courseService = new CourseService();
        enrollmentService = new EnrollmentService(courseService);

        initializeTables();
        initializeComboBoxes();
        refreshAll();
    }

    private void initializeTables() {
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentDepartmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseCreditsColumn.setCellValueFactory(new PropertyValueFactory<>("credits"));

        enrollmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));
        enrollmentStudentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        enrollmentStudentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        enrollmentCourseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        enrollmentCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        enrollmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentDate"));
    }

    private void initializeComboBoxes() {
        enrollmentStudentCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Student student) {
                if (student == null) {
                    return "";
                }
                return student.getStudentId() + " - " + student.getName();
            }

            @Override
            public Student fromString(String s) {
                return null;
            }
        });

        enrollmentCourseCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Course course) {
                if (course == null) {
                    return "";
                }
                return course.getCourseId() + " - " + course.getCourseName();
            }

            @Override
            public Course fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    public void onRegisterStudent() {
        String name = studentNameField.getText().trim();
        String department = studentDepartmentField.getText().trim();

        if (name.isEmpty() || department.isEmpty()) {
            showError("Both student name and department are required.");
            return;
        }

        studentService.registerStudent(name, department);
        studentNameField.clear();
        studentDepartmentField.clear();
        refreshStudents();
        refreshEnrollmentSelectors();
    }

    @FXML
    public void onAddCourse() {
        String name = courseNameField.getText().trim();
        String creditsText = courseCreditsField.getText().trim();

        if (name.isEmpty() || creditsText.isEmpty()) {
            showError("Course name and credits are required.");
            return;
        }

        int credits;
        try {
            credits = Integer.parseInt(creditsText);
        } catch (NumberFormatException ex) {
            showError("Credits must be a valid number.");
            return;
        }

        if (credits <= 0) {
            showError("Credits must be greater than 0.");
            return;
        }

        courseService.addCourse(name, credits);
        courseNameField.clear();
        courseCreditsField.clear();
        refreshCourses();
        refreshEnrollmentSelectors();
    }

    @FXML
    public void onEnrollStudent() {
        Student selectedStudent = enrollmentStudentCombo.getValue();
        Course selectedCourse = enrollmentCourseCombo.getValue();

        if (selectedStudent == null || selectedCourse == null) {
            showError("Please select both a student and a course.");
            return;
        }

        enrollmentService.selectCourse(selectedStudent.getStudentId(), selectedCourse.getCourseId());
        refreshEnrollments();
    }

    @FXML
    public void onDropEnrollment() {
        EnrollmentRow selected = enrollmentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select an enrollment row to drop.");
            return;
        }

        enrollmentService.dropCourse(selected.getEnrollmentId());
        refreshEnrollments();
    }

    @FXML
    public void onRefreshData() {
        refreshAll();
    }

    private void refreshAll() {
        refreshStudents();
        refreshCourses();
        refreshEnrollmentSelectors();
        refreshEnrollments();
    }

    private void refreshStudents() {
        studentsTable.setItems(FXCollections.observableArrayList(studentService.getStudents()));
    }

    private void refreshCourses() {
        coursesTable.setItems(FXCollections.observableArrayList(courseService.getCourses()));
    }

    private void refreshEnrollmentSelectors() {
        enrollmentStudentCombo.setItems(FXCollections.observableArrayList(studentService.getStudents()));
        enrollmentCourseCombo.setItems(FXCollections.observableArrayList(courseService.getCourses()));
    }

    private void refreshEnrollments() {
        ObservableList<EnrollmentRow> rows = FXCollections.observableArrayList();
        for (Enrollment enrollment : enrollmentService.getEnrollments()) {
            Student student = studentService.getStudentById(enrollment.getStudentId());
            Course course = courseService.getCourseById(enrollment.getCourseId());

            String studentName = student != null ? student.getName() : "Unknown";
            String courseName = course != null ? course.getCourseName() : "Unknown";

            rows.add(new EnrollmentRow(
                    enrollment.getEnrollmentId(),
                    enrollment.getStudentId(),
                    studentName,
                    enrollment.getCourseId(),
                    courseName,
                    enrollment.getEnrollmentDate()
            ));
        }

        enrollmentsTable.setItems(rows);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Cannot complete action");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

