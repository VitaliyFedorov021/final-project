package ua.com.alevel.services.impl;

import ua.com.alevel.util.Checker;
import ua.com.alevel.dao.TeacherDao;
import ua.com.alevel.dao.impl.TeacherDaoImpl;
import ua.com.alevel.dto.Teacher;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;
import ua.com.alevel.services.TeacherService;

import java.util.List;

public class TeacherServiceImpl implements TeacherService {
    TeacherDao teacherDao = TeacherDaoImpl.getInstance();

    @Override
    public boolean createTeacher(Teacher teacher) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectName(teacher.getName())) {
            teacherDao.createTeacher(teacher);
            return true;
        }
        throw new IncorrectDataException("Incorrect teacher name");
    }

    @Override
    public boolean deleteTeacher(int id) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectNumber(id)) {
            teacherDao.deleteTeacher(id);
            return true;
        }
        throw new IncorrectDataException("Incorrect teacher id");
    }

    @Override
    public List<Teacher> selectAll() {
        return teacherDao.selectAll();
    }

    @Override
    public Teacher findTeacherById(int id) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectNumber(id)) {
            return teacherDao.findTeacherById(id);
        }
        throw new IncorrectDataException("Incorrect teacher id");
    }

    @Override
    public boolean updateTeacher(Teacher teacher) throws NoDataInDBException, IncorrectDataException {
        if (Checker.isCorrectName(teacher.getName())) {
            teacherDao.updateTeacher(teacher);
            return true;
        }
        throw new IncorrectDataException("Incorrect teacher name");
    }
}
