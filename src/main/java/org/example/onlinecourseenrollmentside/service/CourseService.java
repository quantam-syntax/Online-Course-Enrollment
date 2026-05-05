package org.example.onlinecourseenrollmentside.service;

import org.example.onlinecourseenrollmentside.model.Course;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseService {
    private final Path filePath;
    private final List<Course> courses = new ArrayList<>();

    public CourseService() {
        this(Path.of("data", "courses.csv"));
    }

    CourseService(Path filePath) {
        this.filePath = filePath;
        load();
    }

    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }

    public Optional<Course> findById(int id) {
        return courses.stream().filter(c -> c.getCourseId() == id).findFirst();
    }

    public boolean addCourse(Course course) {
        if (findById(course.getCourseId()).isPresent()) {
            return false;
        }
        courses.add(course);
        save();
        return true;
    }

    private void load() {
        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                return;
            }
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", 3);
                courses.add(new Course(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2])));
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load courses", e);
        }
    }

    private void save() {
        List<String> lines = courses.stream()
                .map(c -> c.getCourseId() + "," + c.getTitle() + "," + c.getFee())
                .toList();
        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save courses", e);
        }
    }
}

