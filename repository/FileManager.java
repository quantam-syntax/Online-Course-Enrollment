package repository;

import model.Course;
import model.Enrollment;
import model.Student;
import java.io.*;
import java.util.ArrayList;

public class FileManager {

    private static final String STUDENTS_FILE = "students.txt";
    private static final String COURSES_FILE = "courses.txt";
    private static final String ENROLLMENTS_FILE = "enrollments.txt";

    public void saveStudent(Student student) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENTS_FILE, true))) {
            bw.write(student.getStudentId() + "," +student.getName() + "," +student.getDepartment());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving student: " + e.getMessage());
        }
    }
    public ArrayList<String[]> loadStudents() {
        ArrayList<String[]> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    public void saveCourse(Course course) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(COURSES_FILE, true))) {
            bw.write(course.getCourseId() + "," +course.getCourseName() + "," +course.getCredits());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving course: " + e.getMessage());
        }
    }
    public ArrayList<String[]> loadCourses() {
        ArrayList<String[]> courses = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                courses.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading courses: " + e.getMessage());
        }
        return courses;
    }

    public void saveEnrollment(Enrollment enrollment) {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(ENROLLMENTS_FILE, true))) {
            bw.write(enrollment.toString());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error saving enrollment: " + e.getMessage());
        }
    }

    public ArrayList<String[]> loadEnrollments() {
        ArrayList<String[]> enrollments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new FileReader(ENROLLMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                enrollments.add(line.split(","));
            }
        } catch (IOException e) {
            System.out.println("Error loading enrollments: " + e.getMessage());
        }
        return enrollments;
    }

    public void deleteEnrollment(int enrollmentId) {
        ArrayList<String> remaining = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ENROLLMENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (Integer.parseInt(parts[0]) != enrollmentId) {
                    remaining.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrollments: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ENROLLMENTS_FILE, false))) {
            for (String line : remaining) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating enrollments: " + e.getMessage());
        }
    }
}