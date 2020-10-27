package ua.com.alevel.dao;

import ua.com.alevel.dto.Student;

import java.util.List;

public interface StudentDao {
    void createStudent(Student student);

    List<Student> selectAll();

    Student findStudentByLogin(String login);

    void changeState();
}
