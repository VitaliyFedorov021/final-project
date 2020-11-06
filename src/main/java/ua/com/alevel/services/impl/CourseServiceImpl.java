package ua.com.alevel.services.impl;

import ua.com.alevel.util.Checker;
import ua.com.alevel.dao.CourseDao;
import ua.com.alevel.dao.impl.CourseDaoImpl;
import ua.com.alevel.dto.Course;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.services.CourseService;


import java.lang.annotation.Target;
import java.util.List;


public class CourseServiceImpl implements CourseService {
    CourseDao courseDao = CourseDaoImpl.getInstance();

    @Override
    public boolean addCourse(Course course) throws IncorrectDataException {
        if (Checker.isCorrectName(course.getName()) &&
                Checker.isCorrectDate(course.getStartDate(), course.getEndDate())) {
            courseDao.addCourse(course);
            return true;
        }
        throw new IncorrectDataException("Incorrect data");
    }

    @Override
    public List<Course> selectAll() {
        return courseDao.selectAll();
    }

    @Override
    public List<Course> selectAll(boolean useSort) {
        return courseDao.selectAll(useSort);
    }

    @Override
    public Course selectById(int id) throws IncorrectDataException, NoDataInDBException {
        if (Checker.isCorrectNumber(id)) {
            return courseDao.selectById(id);
        }
        throw new IncorrectDataException("Incorrect id");
    }

    @Override
    public List<Course> selectAllNotStarted() {
        return courseDao.selectAllNotStarted();
    }

    @Override
    public List<Course> selectAllStarted() {
        return courseDao.selectAllStarted();
    }

    @Override
    public List<Course> selectAllEnded() {
        return courseDao.selectAllEnded();
    }

    @Override
    public List<Course> selectAll(String login, boolean useSort) {
        return courseDao.selectAll(login, useSort);
    }

    @Override
    public Course selectCourseByTeacherId(int teacherId) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectNumber(teacherId)) {
            return courseDao.selectCourseByTeacherId(teacherId);
        }
        throw new IncorrectDataException("Incorrect teacher id");
    }

    @Override
    public List<Course> selectAllNotStarted(String login) throws IncorrectDataException {
        if (Checker.isCorrectName(login)) {
            return courseDao.selectAllNotStarted(login);
        }
        throw new IncorrectDataException("Incorrect student login");
    }

    @Override
    public List<Course> selectAllStarted(String login) throws IncorrectDataException {
        if (Checker.isCorrectName(login)) {
            return courseDao.selectAllStarted(login);
        }
        throw new IncorrectDataException("Incorrect student login");
    }

    @Override
    public List<Course> selectAllEnded(String login) throws IncorrectDataException {
        if (Checker.isCorrectName(login)) {
            return courseDao.selectAllEnded(login);
        }
        throw new IncorrectDataException("Incorrect student login");
    }

    @Override
    public boolean updateCourse(Course course, int id) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectNumber(course.getId()) &&
                Checker.isCorrectName(course.getName()) &&
                Checker.isCorrectDate(course.getStartDate(), course.getEndDate())) {
            courseDao.updateCourse(course, id);
            return true;
        }
        throw new IncorrectDataException("Incorrect data");
    }

    @Override
    public boolean deleteCourse(int id) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectNumber(id)) {
            courseDao.deleteCourse(id);
            return true;
        }
        throw new IncorrectDataException("Incorrect id");
    }

    @Override
    public List<Course> selectAllByTheme(String theme) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectName(theme)) {
            return courseDao.selectAllByTheme(theme);
        }
        throw new IncorrectDataException("Incorrect theme");
    }

}
