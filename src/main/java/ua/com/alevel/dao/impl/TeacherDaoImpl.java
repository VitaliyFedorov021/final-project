package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.builders.TeacherBuilder;
import ua.com.alevel.builders.impl.TeacherBuilderImpl;
import ua.com.alevel.dao.TeacherDao;
import ua.com.alevel.dto.Teacher;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherDaoImpl implements TeacherDao {
    private static TeacherDaoImpl instance;
    private final Logger log = Logger.getLogger(TeacherDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private static final String SQL_FOR_INSERT = "INSERT INTO teachers (name, courseId) values (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM teachers WHERE id = ?";
    private static final String SQL_FOR_ALL = "SELECT * FROM teachers";
    private static final String SQL_FOR_UPDATE = "UPDATE teachers SET name = ?, courseId = ? WHERE id = ?";
    private static final String SQL_FOR_ID = "SELECT * FROM teachers WHERE id = ?";

    private TeacherDaoImpl() {
    }

    public static TeacherDaoImpl getInstance() {
        if (instance == null) {
            instance = new TeacherDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean createTeacher(Teacher teacher) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_INSERT)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setString(1, teacher.getName());
            pStatement.setInt(2, teacher.getCourseId());
            try {
                pStatement.executeUpdate();
            }
            catch(SQLException e){
                connection.rollback();
                throw new NoDataInDBException("There is no course with this id", e);
            }
            log.info("Inserting into DB teachers");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return false;
    }

    @Override
    public boolean deleteTeacher(int id) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_DELETE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setInt(1, id);
            int a = pStatement.executeUpdate();
            if (a == 0) {
                throw new NoDataInDBException("Can't delete teacher with this id");
            }
            log.info("Deleting from DB teachers");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection");
        }
        return false;
    }

    @Override
    public List<Teacher> selectAll() {
        List<Teacher> teachers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALL)) {
            log.info("Created connection and statement");
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    teachers.add(mapTeacher(rSet));
                } catch (SQLException e) {
                    log.error("Can't map teacher from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return teachers;
    }

    @Override
    public Teacher findTeacherById(int id) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ID)) {
            log.info("Created connection and statement");
            pStatement.setInt(1, id);
            ResultSet rSet = null;
            rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    return mapTeacher(rSet);
                } catch (SQLException e) {
                    log.error("Can't map teacher from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no teacher with this id");
    }

    @Override
    public boolean updateTeacher(Teacher teacher) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_UPDATE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setString(1, teacher.getName());
            pStatement.setInt(2, teacher.getCourseId());
            try {
                pStatement.executeUpdate();
            }
            catch(SQLException e){
                connection.rollback();
                throw new NoDataInDBException("There is no course with this id", e);
            }
            log.info("Updating in DB teachers");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection");
        }
        return false;
    }

    private Teacher mapTeacher(ResultSet rSet) throws SQLException {
        TeacherBuilder builder = new TeacherBuilderImpl();
        builder.setId(rSet.getInt("id"));
        builder.setName(rSet.getString("name"));
        builder.setCourseId(rSet.getInt("courseId"));
        return builder.build();
    }

}
