package ua.com.alevel.services;

import ua.com.alevel.dto.Teacher;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.util.List;

public interface TeacherService {
    boolean createTeacher(Teacher teacher) throws NoDataInDBException, IncorrectDataException;

    boolean deleteTeacher(int id) throws NoDataInDBException, IncorrectDataException;

    List<Teacher> selectAll();

    Teacher findTeacherById(int id) throws NoDataInDBException, IncorrectDataException;

    boolean updateTeacher(Teacher teacher) throws NoDataInDBException, IncorrectDataException;
}
