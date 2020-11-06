package ua.com.alevel.dao;

import ua.com.alevel.dto.Student;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface StudentDao {
    boolean createStudent(Student student) throws NoDataInDBException, IncorrectDataException;

    List<Student> selectAll();

    Student findStudentByLogin(String login) throws NoDataInDBException;

    void changeState(String login) throws NoDataInDBException;

    public void selectAllDistinct();

    int findId(String login, int courseId) throws NoDataInDBException;

    public void selectAllDistinctByCourseId(int courseId) throws NoDataInDBException;

    boolean updateStudent(Student student) throws NoDataInDBException;

    boolean deleteStudent(String login) throws NoDataInDBException;

    void registerToCourse(Student student, int newCourseId) throws IncorrectDataException;
}
