import model.Course;
import model.Student;
import repository.FileManager;
import service.CourseService;
import service.EnrollmentService;
import service.StudentService;

import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        StudentService studentService = new StudentService(fileManager);
        CourseService courseService = new CourseService(fileManager);
        EnrollmentService enrollmentService = new EnrollmentService(fileManager);

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n====== Online Course Enrollment ======");
            System.out.println("1. Add Student");
            System.out.println("2. Add Course");
            System.out.println("3. Enroll Student in Course");
            System.out.println("4. Drop Course");
            System.out.println("5. List All Students");
            System.out.println("6. List All Courses");
            System.out.println("7. List All Enrollments");
            System.out.println("8. View Student's Courses");
            System.out.println("9. View Course's Students");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            choice = Integer.parseInt(sc.nextLine().trim());

            switch (choice) {
                case 1 -> {
                    System.out.print("Student ID: "); int sid = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Name: ");       String sname = sc.nextLine().trim();
                    System.out.print("Department: "); String dept = sc.nextLine().trim();
                    studentService.addStudent(sid, sname, dept);
                }
                case 2 -> {
                    System.out.print("Course ID: ");   int cid = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Course Name: "); String cname = sc.nextLine().trim();
                    System.out.print("Credits: ");     int credits = Integer.parseInt(sc.nextLine().trim());
                    courseService.addCourse(cid, cname, credits);
                }
                case 3 -> {
                    System.out.print("Student ID: "); int sid = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Course ID: ");  int cid = Integer.parseInt(sc.nextLine().trim());
                    Optional<Student> s = studentService.findByStudentId(sid);
                    Optional<Course>  c = courseService.findById(cid);
                    if (s.isPresent() && c.isPresent()) enrollmentService.enrollStudent(s.get(), c.get());
                    else System.out.println("Student or Course not found.");
                }
                case 4 -> {
                    System.out.print("Student ID: "); int sid = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Course ID: ");  int cid = Integer.parseInt(sc.nextLine().trim());
                    Optional<Student> s = studentService.findByStudentId(sid);
                    Optional<Course>  c = courseService.findById(cid);
                    if (s.isPresent() && c.isPresent()) enrollmentService.dropCourse(s.get(), c.get());
                    else System.out.println("Student or Course not found.");
                }
                case 5 -> studentService.listAllStudents();
                case 6 -> courseService.listAllCourses();
                case 7 -> enrollmentService.listAllEnrollments();
                case 8 -> {
                    System.out.print("Student ID: "); int sid = Integer.parseInt(sc.nextLine().trim());
                    studentService.findByStudentId(sid)
                        .ifPresentOrElse(
                            s -> enrollmentService.listCoursesForStudent(s, courseService),
                            () -> System.out.println("Student not found.")
                        );
                }
                case 9 -> {
                    System.out.print("Course ID: "); int cid = Integer.parseInt(sc.nextLine().trim());
                    courseService.findById(cid)
                        .ifPresentOrElse(
                            c -> enrollmentService.listStudentsForCourse(c, studentService),
                            () -> System.out.println("Course not found.")
                        );
                }
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid option.");
            }
        } while (choice != 0);

        sc.close();
    }
}