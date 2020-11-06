package ua.com.alevel.services;

import ua.com.alevel.dto.Course;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface CourseService {
    boolean addCourse(Course course) throws IncorrectDataException;

    List<Course> selectAll();

    List<Course> selectAll(boolean useSort);

    Course selectById(int id) throws IncorrectDataException, NoDataInDBException;

    List<Course> selectAllNotStarted();

    List<Course> selectAllStarted();

    List<Course> selectAllEnded();

    List<Course> selectAll(String login, boolean useSort);

    Course selectCourseByTeacherId(int teacherId) throws NoDataInDBException, IncorrectDataException;

    List<Course> selectAllNotStarted(String login) throws IncorrectDataException;

    List<Course> selectAllStarted(String login) throws IncorrectDataException;

    List<Course> selectAllEnded(String login) throws IncorrectDataException;

    boolean updateCourse(Course course, int id) throws NoDataInDBException, IncorrectDataException;

    boolean deleteCourse(int id) throws NoDataInDBException, IncorrectDataException;

    List<Course> selectAllByTheme(String theme) throws NoDataInDBException, IncorrectDataException;
}
