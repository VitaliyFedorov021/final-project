package ua.com.alevel.dao;

import ua.com.alevel.dto.Teacher;

import java.util.List;

public interface TeacherDao {
    void createTeacher(Teacher teacher);

    void deleteTeacher(int id);

    List<Teacher> selectAll();

    void updateTeacher(int id);
}
