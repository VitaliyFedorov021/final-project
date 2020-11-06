package ua.com.alevel.services.impl;

import ua.com.alevel.util.Checker;
import ua.com.alevel.dao.StudentDao;
import ua.com.alevel.dao.impl.StudentDaoImpl;
import ua.com.alevel.dto.Student;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.services.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    StudentDao studentDao = StudentDaoImpl.getInstance();

    @Override
    public boolean createStudent(Student student) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectName(student.getName()) &&
                Checker.isCorrectName(student.getSurname()) &&
                Checker.isCorrectName(student.getLogin())) {
            if (student.getState().equalsIgnoreCase("blocked") ||
                    student.getState().equalsIgnoreCase("unblocked")) {
                studentDao.createStudent(student);
                return true;
            } else {
                throw new IncorrectDataException("Incorrect student state");
            }
        }
        throw new IncorrectDataException("Incorrect data");
    }

    @Override
    public List<Student> selectAll() {
        return studentDao.selectAll();
    }

    @Override
    public Student findStudentByLogin(String login) throws IncorrectDataException, NoDataInDBException {
        if (Checker.isCorrectName(login)) {
            return studentDao.findStudentByLogin(login);
        }
        throw new IncorrectDataException("Incorrect login");
    }

    @Override
    public void changeState(String login) throws NoDataInDBException {
        studentDao.changeState(login);
    }

    @Override
    public void selectAllDistinct() {
        studentDao.selectAllDistinct();
    }

    @Override
    public int findId(String login, int courseId) throws IncorrectDataException, NoDataInDBException {
        if (Checker.isCorrectNumber(courseId) && Checker.isCorrectName(login)) {
            return studentDao.findId(login, courseId);
        }
        throw new IncorrectDataException("Incorrect input");
    }

    @Override
    public void selectAllDistinctByCourseId(int courseId) throws IncorrectDataException, NoDataInDBException {
        if (Checker.isCorrectNumber(courseId)) {
            studentDao.selectAllDistinctByCourseId(courseId);
            return;
        }
        throw new IncorrectDataException("Incorrect course id");
    }


    @Override
    public boolean updateStudent(Student student) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectName(student.getLogin()) &&
                Checker.isCorrectName(student.getSurname()) &&
                Checker.isCorrectName(student.getName())) {
            if (student.getState().equalsIgnoreCase("blocked") ||
                    student.getState().equalsIgnoreCase("unblocked")) {
                studentDao.updateStudent(student);
                return true;
            } else {
                throw new IncorrectDataException("Incorrect state");
            }
        }
        throw new IncorrectDataException("Incorrect data");
    }

    @Override
    public boolean deleteStudent(String login) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectName(login)) {
            studentDao.deleteStudent(login);
            return true;
        }
        throw new IncorrectDataException("Incorrect login");
    }

    @Override
    public void registerToCourse(Student student, int newCourseId) throws IncorrectDataException {
        if (Checker.isCorrectName(student.getLogin())) {
            studentDao.registerToCourse(student, newCourseId);
            return;
        }
        throw new IncorrectDataException("Incorrect login");
    }
}
