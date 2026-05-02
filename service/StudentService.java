package service;
import model.Student;
import repository.FileManager;
import java.util.ArrayList;

public class StudentService {
    private ArrayList<Student>students;
    private FileManager fileManager;
    private int studentCounter = 1;
    public StudentService(){
        this.students = new ArrayList<>();
        this.fileManager = new FileManager();
        loadFromFile();
    }
    public void loadFromFile(){
        ArrayList<String[]> data = fileManager.loadStudents();
        for(String[]parts : data ){
            if (parts.length == 3){
                students.add(new Student(Integer.parseInt(parts[0]),Integer.parseInt(parts[0]),parts[1],parts[2]));
                studentCounter = Integer.parseInt(parts[0]+1);
            }
        }
    }
    public Student registerStudent(String name, String department){
        Student student = new Student(studentCounter , studentCounter , name , department);
        studentCounter++;
        students.add(student);
        fileManager.saveStudent(student);
        System.out.println("Student registered ID : "+ student.getStudentId() );
        return student;
    }
    public Student getStudentById(int studentId){
        for (Student s : students){
            if (s.getStudentId() == studentId){
                return s;
            }
        }
        return null;
    }
    public void displayAllStudents(){
        if (students.isEmpty()){
            System.out.println("No students registered ");
            return;
        }
        for (Student s : students ){
            s.displayInfo();
        }
    }
        public ArrayList<Student> getStudents(){
            return students;
        }


        
    }
