package service;

import model.Course;
import model.Enrollment;
import model.Student;
import repository.FileManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class EnrollmentService {

    private final List<Enrollment> enrollments;
    private final FileManager      fileManager;
    private int nextEnrollmentId;

    public EnrollmentService(FileManager fileManager) {
        this.fileManager  = fileManager;
        this.enrollments  = new ArrayList<>(fileManager.loadEnrollments());
        
        this.nextEnrollmentId = enrollments.stream()
                .mapToInt(Enrollment::getEnrollmentId)
                .max()
                .orElse(0) + 1;
    }

    public boolean enrollStudent(Student student, Course course) {
        
        if (isEnrolled(student.getStudentId(), course.getCourseId())) {
            System.out.println("[EnrollmentService] " + student.getName()
                    + " is already enrolled in '" + course.getCourseName() + "'.");
            return false;
        }

        String     today      = LocalDate.now().toString();
        Enrollment enrollment = new Enrollment(nextEnrollmentId++, today,
                student.getStudentId(), course.getCourseId());

        enrollments.add(enrollment);
        student.addCourseToList(course.getCourseId());
        student.enrollCourse();   

        fileManager.saveEnrollments(enrollments);
        System.out.println("[EnrollmentService] Enrollment successful! ID: "
                + enrollment.getEnrollmentId() + " | Date: " + today);
        return true;
    }

    public boolean dropCourse(Student student, Course course) {
        Optional<Enrollment> opt = findEnrollment(student.getStudentId(), course.getCourseId());
        if (opt.isEmpty()) {
            System.out.println("[EnrollmentService] " + student.getName()
                    + " is not enrolled in '" + course.getCourseName() + "'.");
            return false;
        }

        enrollments.remove(opt.get());
        student.dropCourse(); 

        fileManager.saveEnrollments(enrollments);
        System.out.println("[EnrollmentService] Drop successful for course: " + course.getCourseName());
        return true;
    }

    
    public boolean isEnrolled(int studentId, int courseId) {
        return enrollments.stream()
                .anyMatch(e -> e.getStudentId() == studentId && e.getCourseId() == courseId);
    }

    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

  
    public List<Enrollment> getEnrollmentsByCourse(int courseId) {
        return enrollments.stream()
                .filter(e -> e.getCourseId() == courseId)
                .collect(Collectors.toList());
    }

    public void listCoursesForStudent(Student student, CourseService courseService) {
        List<Enrollment> list = getEnrollmentsByStudent(student.getStudentId());
        if (list.isEmpty()) {
            System.out.println("  " + student.getName() + " is not enrolled in any courses.");
            return;
        }
        System.out.println("\n┌───────────────────────────────────────────────────────────┐");
        System.out.println("│  Enrollments for: " + padRight(student.getName(), 41) + "│");
        System.out.println("├──────────────┬──────────────────────┬────────────────────┤");
        System.out.printf( "│ %-12s │ %-20s │ %-18s │%n", "Enroll. ID", "Course Name", "Date");
        System.out.println("├──────────────┼──────────────────────┼────────────────────┤");
        for (Enrollment e : list) {
            String courseName = courseService.findById(e.getCourseId())
                    .map(Course::getCourseName)
                    .orElse("Unknown (" + e.getCourseId() + ")");
            System.out.printf("│ %-12d │ %-20s │ %-18s │%n",
                    e.getEnrollmentId(), courseName, e.getEnrollmentDate());
        }
        System.out.println("└──────────────┴──────────────────────┴────────────────────┘");
    }
    public void listStudentsForCourse(Course course, StudentService studentService) {
        List<Enrollment> list = getEnrollmentsByCourse(course.getCourseId());
        if (list.isEmpty()) {
            System.out.println("  No students enrolled in '" + course.getCourseName() + "' yet.");
            return;
        }
        System.out.println("\n┌──────────────────────────────────────────────────────────┐");
        System.out.println("│  Students in: " + padRight(course.getCourseName(), 44) + "│");
        System.out.println("├──────────────┬────────────────────────┬──────────────────┤");
        System.out.printf( "│ %-12s │ %-22s │ %-16s │%n", "Student ID", "Name", "Department");
        System.out.println("├──────────────┼────────────────────────┼──────────────────┤");
        for (Enrollment e : list) {
            studentService.findByStudentId(e.getStudentId()).ifPresent(s ->
                System.out.printf("│ %-12d │ %-22s │ %-16s │%n",
                        s.getStudentId(), s.getName(), s.getDepartment())
            );
        }
        System.out.println("└──────────────┴────────────────────────┴──────────────────┘");
    }

 
    public void listAllEnrollments() {
        if (enrollments.isEmpty()) {
            System.out.println("  No enrollments recorded.");
            return;
        }
        System.out.println("\n┌──────────────────────────────────────────────────────────┐");
        System.out.println("│                    ALL ENROLLMENTS                      │");
        System.out.println("├──────────────┬────────────┬────────────┬────────────────┤");
        System.out.printf( "│ %-12s │ %-10s │ %-10s │ %-14s │%n",
                "Enroll. ID", "Student ID", "Course ID", "Date");
        System.out.println("├──────────────┼────────────┼────────────┼────────────────┤");
        for (Enrollment e : enrollments) {
            System.out.printf("│ %-12d │ %-10d │ %-10d │ %-14s │%n",
                    e.getEnrollmentId(), e.getStudentId(),
                    e.getCourseId(), e.getEnrollmentDate());
        }
        System.out.println("└──────────────┴────────────┴────────────┴────────────────┘");
    }

   
    public int getEnrollmentCount() {
        return enrollments.size();
    }

  
    private Optional<Enrollment> findEnrollment(int studentId, int courseId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId() == studentId && e.getCourseId() == courseId)
                .findFirst();
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}
