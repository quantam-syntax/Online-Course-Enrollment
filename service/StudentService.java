package service;

import model.Student;
import repository.FileManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class StudentService {

    private final List<Student> students;
    private final FileManager   fileManager;
    private int nextPersonId; 

    public StudentService(FileManager fileManager) {
        this.fileManager = fileManager;
        this.students    = new ArrayList<>(fileManager.loadStudents());
        this.nextPersonId = students.stream()
                .mapToInt(Student::getId)
                .max()
                .orElse(0) + 1;
    }

    public Student addStudent(int studentId, String name, String department) {
        if (findByStudentId(studentId).isPresent()) {
            System.out.println("[StudentService] Student ID " + studentId + " already exists.");
            return null;
        }
        Student student = new Student(studentId, nextPersonId++, name, department);
        students.add(student);
        fileManager.saveStudents(students);
        System.out.println("[StudentService] Student registered: " + name);
        return student;
    }

    public Optional<Student> findByStudentId(int studentId) {
        return students.stream()
                .filter(s -> s.getStudentId() == studentId)
                .findFirst();
    }


    public Optional<Student> findByName(String name) {
        return students.stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();
    }


    public List<Student> getAllStudents() {
        return List.copyOf(students);
    }

    public void listAllStudents() {
        if (students.isEmpty()) {
            System.out.println("  No students registered yet.");
            return;
        }
        System.out.println("\n┌─────────────────────────────────────────────────────┐");
        System.out.println("│                    ALL STUDENTS                     │");
        System.out.println("├──────────┬────────────────────────┬──────────────────┤");
        System.out.printf( "│ %-8s │ %-22s │ %-16s │%n", "Stu. ID", "Name", "Department");
        System.out.println("├──────────┼────────────────────────┼──────────────────┤");
        for (Student s : students) {
            System.out.printf("│ %-8d │ %-22s │ %-16s │%n",
                    s.getStudentId(), s.getName(), s.getDepartment());
        }
        System.out.println("└──────────┴────────────────────────┴──────────────────┘");
    }


    public boolean removeStudent(int studentId) {
        Optional<Student> opt = findByStudentId(studentId);
        if (opt.isEmpty()) {
            System.out.println("[StudentService] Student ID " + studentId + " not found.");
            return false;
        }
        students.remove(opt.get());
        fileManager.saveStudents(students);
        System.out.println("[StudentService] Student removed: " + opt.get().getName());
        return true;
    }


    public int getStudentCount() {
        return students.size();
    }
}