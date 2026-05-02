package repository;

import model.Course;
import model.Enrollment;
import model.Student;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String STUDENTS_FILE    = "students.csv";
    private static final String COURSES_FILE     = "courses.csv";
    private static final String ENROLLMENTS_FILE = "enrollments.csv";

    public void saveStudents(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student s : students) {
                writer.println(s.getStudentId() + "," +
                               s.getId()        + "," +
                               s.getName()      + "," +
                               s.getDepartment());
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving students: " + e.getMessage());
        }
    }

    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        File file = new File(STUDENTS_FILE);
        if (!file.exists()) return students;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    int    studentId = Integer.parseInt(parts[0].trim());
                    int    personId  = Integer.parseInt(parts[1].trim());
                    String name      = parts[2].trim();
                    String dept      = parts[3].trim();
                    students.add(new Student(studentId, personId, name, dept));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("[FileManager] Error loading students: " + e.getMessage());
        }
        return students;
    }


    public void saveCourses(List<Course> courses) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSES_FILE))) {
            for (Course c : courses) {
                writer.println(c.getCourseId()   + "," +
                               c.getCourseName() + "," +
                               c.getCredits());
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving courses: " + e.getMessage());
        }
    }

   
    public List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        File file = new File(COURSES_FILE);
        if (!file.exists()) return courses;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    int    courseId = Integer.parseInt(parts[0].trim());
                    String name     = parts[1].trim();
                    int    credits  = Integer.parseInt(parts[2].trim());
                    courses.add(new Course(courseId, name, credits));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("[FileManager] Error loading courses: " + e.getMessage());
        }
        return courses;
    }

    public void saveEnrollments(List<Enrollment> enrollments) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ENROLLMENTS_FILE))) {
            for (Enrollment e : enrollments) {
                writer.println(e.toString());
            }
        } catch (IOException e) {
            System.out.println("[FileManager] Error saving enrollments: " + e.getMessage());
        }
    }


    public List<Enrollment> loadEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        File file = new File(ENROLLMENTS_FILE);
        if (!file.exists()) return enrollments;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    int    enrollId  = Integer.parseInt(parts[0].trim());
                    String date      = parts[1].trim();
                    int    studentId = Integer.parseInt(parts[2].trim());
                    int    courseId  = Integer.parseInt(parts[3].trim());
                    enrollments.add(new Enrollment(enrollId, date, studentId, courseId));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("[FileManager] Error loading enrollments: " + e.getMessage());
        }
        return enrollments;
    }
}