package ua.com.alevel.dao;

import ua.com.alevel.dto.Teacher;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface TeacherDao {
    boolean createTeacher(Teacher teacher) throws NoDataInDBException;

    boolean deleteTeacher(int id) throws NoDataInDBException;

    List<Teacher> selectAll();

    Teacher findTeacherById(int id) throws NoDataInDBException;

    boolean updateTeacher(Teacher teacher) throws NoDataInDBException;

}
