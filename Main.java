
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StudentService studentService = new StudentService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = 
            new EnrollmentService(courseService);

        while (true) {
            System.out.println("\n=== Online Course Enrollment System ===");
            System.out.println("1. Register Student");
            System.out.println("2. View Available Courses");
            System.out.println("3. Add Course");
            System.out.println("4. Enroll in Course");
            System.out.println("5. Drop Course");
            System.out.println("6. View My Enrollments");
            System.out.println("7. View All Students");
            System.out.println("8. Exit");
            System.out.print("Choice: ");

            try {
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter department: ");
                        String dept = sc.nextLine();
                        studentService.registerStudent(name, dept);
                        break;
                    case 2:
                        courseService.viewCourses();
                        break;
                    case 3:
                        System.out.print("Enter course name: ");
                        String cName = sc.nextLine();
                        System.out.print("Enter credits: ");
                        int credits = sc.nextInt();
                        courseService.addCourse(cName, credits);
                        break;
                    case 4:
                        System.out.print("Enter your student ID: ");
                        int sId = sc.nextInt();
                        System.out.print("Enter course ID: ");
                        int cId = sc.nextInt();
                        enrollmentService.selectCourse(sId, cId);
                        break;
                    case 5:
                        System.out.print("Enter enrollment ID to drop: ");
                        int eId = sc.nextInt();
                        enrollmentService.dropCourse(eId);
                        break;
                    case 6:
                        System.out.print("Enter your student ID: ");
                        int sid = sc.nextInt();
                        enrollmentService.viewEnrollments(sid);
                        break;
                    case 7:
                        studentService.displayAllStudents();
                        break;
                    case 8:
                        System.out.println("Goodbye!");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input: " + e.getMessage());
                sc.nextLine();
            }
        }
    }
}