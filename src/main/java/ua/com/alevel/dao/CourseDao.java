package ua.com.alevel.dao;

import ua.com.alevel.dto.Course;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface CourseDao {
    boolean addCourse(Course course);

    List<Course> selectAll();

    List<Course> selectAll(boolean useSort);

    Course selectById(int id) throws NoDataInDBException;

    List<Course> selectAllNotStarted();

    List<Course> selectAllStarted();

    List<Course> selectAllEnded();

    Course selectCourseByTeacherId(int teacherId) throws NoDataInDBException;

    List<Course> selectAll(String login, boolean useSort);

    List<Course> selectAllNotStarted(String login);

    List<Course> selectAllStarted(String login);

    List<Course> selectAllEnded(String login);

    boolean updateCourse(Course course, int id) throws NoDataInDBException;

    boolean deleteCourse(int id) throws NoDataInDBException;

    List<Course> selectAllByTheme(String theme) throws NoDataInDBException;
}
