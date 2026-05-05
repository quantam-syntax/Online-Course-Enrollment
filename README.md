# Online Course Enrollment Side

JavaFX role-based student management system.

## Features
- Admin login (`admin` / `admin123`)
- Student login (`studentId` / `studentId`)
- Admin: add students and courses
- Student: enroll in courses, view enrollments, pay pending fees
- File-based persistence in `data/*.csv`

## Run
```bash
mvn clean test
mvn javafx:run
```

## Notes
- Add a student from admin dashboard first, then use that student id to log in as student.
- Data persists between runs under the project `data/` folder.

