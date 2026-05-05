# 🚀 AGENT.md — JavaFX Role-Based Student Management System

---

# 🧠 SYSTEM PURPOSE

Build a JavaFX desktop application that allows:
- Admin users to manage students and courses
- Student users to enroll in courses and pay fees

Constraints:
- Use existing backend classes
- Only ONE new class: AuthService
- File-based persistence

---

# ⚠️ HARD CONSTRAINTS

- No new model classes
- No major structural changes
- Only AuthService allowed

---

# 🧱 ARCHITECTURE

MVC:
Model: Student, Course, Enrollment, etc.
Service: StudentService, CourseService, EnrollmentService
View: JavaFX (FXML)
Controller: UI logic
New: AuthService

---

# 🔐 AUTHENTICATION

Admin:
username: admin
password: admin123

Student:
username = studentId
password = studentId

---

# 🧠 AUTH SERVICE

public class AuthService {
    private StudentService studentService;
    private final String ADMIN_USERNAME = "admin";
    private final String ADMIN_PASSWORD = "admin123";

    public AuthService(StudentService studentService) {
        this.studentService = studentService;
    }

    public String login(String username, String password) {
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            return "ADMIN";
        }
        for (Student s : studentService.getStudents()) {
            if (String.valueOf(s.getStudentId()).equals(username) &&
                String.valueOf(s.getStudentId()).equals(password)) {
                return "STUDENT";
            }
        }
        return null;
    }
}

---

# 🌍 GLOBAL STATE

public static String currentRole;
public static Student currentStudent;

---

# 🖥️ SCREENS

1. login.fxml
2. admin_dashboard.fxml
3. student_dashboard.fxml

---

# 🎮 CONTROLLERS

LoginController:
- handles login
- redirects based on role

AdminController:
- add student
- add course

StudentController:
- enroll
- view enrollments
- pay fees

---

# 🔗 CORE ACTIONS

enrollmentService.selectCourse(studentId, courseId);
enrollmentService.viewEnrollments(studentId);
fees.payFees();

---

# 🔄 FLOW

Login → Role Check → Dashboard

---

# 🧪 TESTING

- Admin login works
- Student login works
- Enrollment works
- Fees works
- Data persists

---

# 🏁 FINAL

Role-Based JavaFX Student Management System
