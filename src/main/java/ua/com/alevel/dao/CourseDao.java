package ua.com.alevel.dao;

import ua.com.alevel.dto.Course;

import java.util.List;

public interface CourseDao {
    void addCourse(Course course);

    List<Course> selectAll();

    Course selectById(int id);

    List<Course> selectAllNotStarted();

    List<Course> selectAllStarted();

    List<Course> selectAllEnded();

    void updateCourse(Course course);

    void deleteCourse(int id);
}
