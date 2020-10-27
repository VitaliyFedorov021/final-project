package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.builders.CourseBuilder;
import ua.com.alevel.builders.StudentBuilder;
import ua.com.alevel.builders.impl.CourseBuilderImpl;
import ua.com.alevel.builders.impl.StudentBuilderImpl;
import ua.com.alevel.dao.StudentDao;
import ua.com.alevel.dto.Course;
import ua.com.alevel.dto.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private static final Logger log = Logger.getLogger(StudentDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private static final String SQL_FOR_CREATE = "INSERT INTO students(login, name, surname, courseId, state)" +
            " values (?, ?, ?, ?, ?)";
    private static final String SQL_FOR_ALL = "SELECT * FROM students";
    private static final String SQL_FOR_LOGIN = "SELECT * FROM base_for_course.students WHERE login = ?";

    @Override
    public void createStudent(Student student) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_CREATE)) {
            connection.setAutoCommit(false);
            pStatement.setString(1, student.getLogin());
            pStatement.setString(2, student.getName());
            pStatement.setString(3, student.getSurname());
            try {
                pStatement.setInt(4, student.getCourseId());
            } catch (SQLException e) {
                log.error("There is no course with this id " + e);
            }
            pStatement.setString(5, student.getState());
            pStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
    }

    @Override
    public List<Student> selectAll() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALL)) {
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    students.add(mapStudent(rSet));
                } catch (SQLException e) {
                    log.error("Can't map student from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return students;
    }

    @Override
    public Student findStudentByLogin(String login) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_LOGIN)) {
            pStatement.setString(1, login);
            ResultSet rSet = pStatement.executeQuery();
            try {
                return mapStudent(rSet);
            } catch (SQLException e) {
                log.error("Can't map student from result set " + e);
            }
        } catch (SQLException e) {
//            log.error("Can't create connection " + e);
            e.printStackTrace();
        }
        return new Student(52, "fes", "few", "sfgwr", "fesg");
    }

    @Override
    public void changeState() {

    }

    private Student mapStudent(ResultSet rSet) throws SQLException {
        StudentBuilder builder = new StudentBuilderImpl();
        builder.setLogin(rSet.getString("login"));
        builder.setName(rSet.getString("name"));
        builder.setCourseId(rSet.getInt("courseId"));
        builder.setSurname(rSet.getString("surname"));
        builder.setState(rSet.getString("state"));
        return builder.build();
    }
}
