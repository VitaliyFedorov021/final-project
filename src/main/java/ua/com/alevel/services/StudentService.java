package ua.com.alevel.services;

import ua.com.alevel.dto.Student;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface StudentService {
    boolean createStudent(Student student) throws NoDataInDBException, IncorrectDataException;

    List<Student> selectAll();

    Student findStudentByLogin(String login) throws IncorrectDataException, NoDataInDBException;

    void changeState(String login) throws NoDataInDBException;

    public void selectAllDistinct();

    int findId(String login, int courseId) throws IncorrectDataException, NoDataInDBException;

    public void selectAllDistinctByCourseId(int courseId) throws IncorrectDataException, NoDataInDBException;

    boolean updateStudent(Student student) throws NoDataInDBException, IncorrectDataException;

    boolean deleteStudent(String login) throws NoDataInDBException, IncorrectDataException;

    void registerToCourse(Student student, int newCourseId) throws IncorrectDataException;
}
