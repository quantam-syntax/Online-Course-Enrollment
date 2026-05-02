package service;
import model.Course;
import model.Enrollment;
import repository.FileManager;
import java.util.ArrayList;
import java.time.LocalDate;

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
    private ArrayList<Enrollment> enrollments;
    private FileManager fileManager;
    private CourseService courseService;
    private int enrollmentCounter = 1;

    public EnrollmentService(CourseService courseService){
        this.enrollments = new ArrayList<>();
        this.fileManager = new FileManager();
        this.courseService = courseService;
        loadFromFile();
    }
    private void loadFromFile(){
        ArrayList<String[]>data = fileManager.loadEnrollments();
        for (String[]parts : data){
            if (parts.length == 4){
                enrollments.add(new Enrollment(Integer.parseInt(parts[0]), parts[3] ,Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                enrollmentCounter = Integer.parseInt(parts[0] + 1);
            }
        }

    }
    public void selectCourse(int studentId,int courseId){
        boolean available = courseService.checkAvailability(courseId);
        if (available){
            Enrollment enrollment = new Enrollment(enrollmentCounter++,LocalDate.now().toString(),studentId,courseId);
            enrollments.add(enrollment);
            fileManager.saveEnrollment(enrollment);
            System.out.println("Enrollment confirmed ID : " + enrollment.getEnrollmentId());
        }else{
            System.out.println("Error : course not available");
        }

    
    }
    public boolean dropCourse(int enrollmentId){
        for (Enrollment e : enrollments){
            if (e.getEnrollmentId() == enrollmentId){
                enrollments.remove(e);
                fileManager.deleteEnrollment(enrollmentId);
                System.out.println("Course dropped ");
                return true;
            }
        }
        System.out.println("Enrollment not found");
        return false;
    }
    public void viewEnrollments(int studentId){
        boolean found = false;
        for (Enrollment e : enrollments ){
            if (e.getStudentId() == studentId){
                Course course = courseService.getCourseById(e.getCourseId());
                System.out.println("Enrollment ID : " + e.getEnrollmentId() + " Course : " + (course != null ? course.getCourseName() : e.getCourseId()) + "Date : " + e.getEnrollmentDate());
                found = true;
            }
        }
        if (!found){
            System.out.println("Enrollment not found ");

        }
        }
        public ArrayList<Enrollment>getEnrollments(){
            return enrollments;

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
