package service;

import model.Course;
import repository.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseService {

    private final List<Course> courses;
    private final FileManager  fileManager;

    public CourseService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.courses     = new ArrayList<>(fileManager.loadCourses());
    }

    public Course addCourse(int courseId, String courseName, int credits) {
        if (findById(courseId).isPresent()) {
            System.out.println("[CourseService] Course ID " + courseId + " already exists.");
            return null;
        }
        Course course = new Course(courseId, courseName, credits);
        courses.add(course);
        fileManager.saveCourses(courses);
        System.out.println("[CourseService] Course added: " + courseName);
        return course;
    }


    public Optional<Course> findById(int courseId) {
        return courses.stream()
                .filter(c -> c.getCourseId() == courseId)
                .findFirst();
    }

 
    public Optional<Course> findByName(String name) {
        return courses.stream()
                .filter(c -> c.getCourseName().equalsIgnoreCase(name))
                .findFirst();
    }


    public List<Course> getAllCourses() {
        return List.copyOf(courses);
    }

    public void listAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("  No courses available yet.");
            return;
        }
        System.out.println("\n┌──────────────────────────────────────────┐");
        System.out.println("│               ALL COURSES                │");
        System.out.println("├───────────┬────────────────────┬──────────┤");
        System.out.printf( "│ %-9s │ %-18s │ %-8s │%n", "Course ID", "Course Name", "Credits");
        System.out.println("├───────────┼────────────────────┼──────────┤");
        for (Course c : courses) {
            System.out.printf("│ %-9d │ %-18s │ %-8d │%n",
                    c.getCourseId(), c.getCourseName(), c.getCredits());
        }
        System.out.println("└───────────┴────────────────────┴──────────┘");
    }

    public boolean removeCourse(int courseId) {
        Optional<Course> opt = findById(courseId);
        if (opt.isEmpty()) {
            System.out.println("[CourseService] Course ID " + courseId + " not found.");
            return false;
        }
        courses.remove(opt.get());
        fileManager.saveCourses(courses);
        System.out.println("[CourseService] Course removed: " + opt.get().getCourseName());
        return true;
    }

    public int getCourseCount() {
        return courses.size();
    }
}