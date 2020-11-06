package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.builders.StudentBuilder;
import ua.com.alevel.builders.impl.StudentBuilderImpl;
import ua.com.alevel.dao.StudentDao;
import ua.com.alevel.dto.Student;
import ua.com.alevel.exceptions.IncorrectDataException;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    private static StudentDaoImpl instance;
    private static final Logger log = Logger.getLogger(StudentDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private static final String SQL_FOR_CREATE = "INSERT INTO students(login, name, surname, courseId, state)" +
            " values (?, ?, ?, ?, ?)";
    private static final String SQL_FOR_ALL = "SELECT * FROM students";
    private static final String SQL_FOR_LOGIN = "SELECT * FROM project_base.students WHERE login = ?";
    private static final String SQL_FOR_UPDATESTATE = "UPDATE students SET state = ? WHERE login = ?";
    private static final String SQL_DELETE = "DELETE FROM students WHERE login = ?";
    private static final String SQL_FOR_UPDATE = "UPDATE students SET name = ?, surname = ?, courseId = ?, state = ? WHERE login = ?";
    private static final String SQL_REGISTER = "INSERT INTO students(login, name, surname, courseId, state) " +
            "values (?, ?, ?, ?, ?)";
    private static final String SQL_LIST_INTS = "SELECT courseId FROM students WHERE login = ?";
    private static final String SQL_LOGINS = "SELECT login FROM students";
    private static final String SQL_DISTINCT = "SELECT DISTINCT login, state FROM students s";
    private static final String SQL_DISTINCTID = "SELECT DISTINCT login, name FROM students s" +
            " WHERE s.courseId = ?";
    private static final String SQL_FIND_ID = "SELECT id FROM students WHERE login = ? AND courseId = ?";

    private StudentDaoImpl() {
    }

    public static StudentDaoImpl getInstance() {
        if (instance == null) {
            instance = new StudentDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean createStudent(Student student) throws NoDataInDBException, IncorrectDataException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_CREATE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            for (String currentLogin : selectLogins()) {
                if (student.getLogin().equalsIgnoreCase(currentLogin)) {
                    throw new IncorrectDataException("This login is reserved");
                }
            }
            pStatement.setString(1, student.getLogin());
            pStatement.setString(2, student.getName());
            pStatement.setString(3, student.getSurname());
            pStatement.setInt(4, student.getCourseId());
            pStatement.setString(5, "unblocked");
            try {
                pStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new NoDataInDBException("There is no course with this id or this login is using, " + e);
            }
            log.info("Inserting into DB students");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return false;
    }

    @Override
    public List<Student> selectAll() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALL)) {
            log.info("Created connection and statement");
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
    public int findId(String login, int courseId) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FIND_ID)) {
            log.info("Created connection and statement");
            pStatement.setString(1, login);
            pStatement.setInt(2, courseId);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    int studentId = rSet.getInt("id");
                    return studentId;

                } catch (SQLException e) {
                    log.error("Can't map student from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no student with this login ");
    }


    @Override
    public Student findStudentByLogin(String login) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_LOGIN)) {
            log.info("Created connection and statement");
            pStatement.setString(1, login);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    Student student = mapStudent((rSet));
                    return student;

                } catch (SQLException e) {
                    log.error("Can't map student from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no student with this login ");
    }

    @Override
    public void changeState(String login) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_UPDATESTATE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            if (findStudentByLogin(login).getState().equalsIgnoreCase("unblocked")) {
                pStatement.setString(1, "blocked");
                pStatement.setString(2, login);
            } else {
                pStatement.setString(1, "unblocked");
                pStatement.setString(2, login);
            }
            int a = pStatement.executeUpdate();
            if (a == 0) {
                throw new NoDataInDBException("Can't update student with this login");
            }
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
    }

    @Override
    public void selectAllDistinct() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_DISTINCT)) {
            log.info("Created connection and statement");
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                parseForDistinctState(rSet);
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
    }

    @Override
    public void selectAllDistinctByCourseId(int courseId) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_DISTINCTID)) {
            log.info("Created connection and statement");
            pStatement.setInt(1, courseId);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                parseForDistinctName(rSet);
            }
            return;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no course with this id, or no students in this course");
    }

    @Override
    public boolean updateStudent(Student student) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_UPDATE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setString(1, student.getName());
            pStatement.setString(2, student.getSurname());
            pStatement.setInt(3, student.getCourseId());
            pStatement.setString(4, student.getState());
            pStatement.setString(5, student.getLogin());
            int a;
            try {
                a = pStatement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback();
                throw new NoDataInDBException("There is no course with this id ", e);
            }
            if (a == 0) {
                throw new NoDataInDBException("Can't update student with this login");
            }
            log.info("Updating in DB students");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection");
        }
        return false;
    }

    @Override
    public boolean deleteStudent(String login) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_DELETE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setString(1, login);
            int a = pStatement.executeUpdate();
            if (a == 0) {
                throw new NoDataInDBException("There is no student with this login");
            }
            log.info("Deleting from DB students");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection");
        }
        return false;
    }

    @Override
    public void registerToCourse(Student student, int newCourseId) throws IncorrectDataException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_REGISTER)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, student.getLogin());
            preparedStatement.setString(2, student.getName());
            preparedStatement.setString(3, student.getSurname());
            for (Integer currentId :
                    selectCourseId(student.getLogin())) {
                if (newCourseId == currentId) {
                    throw new IncorrectDataException("Student registred for this course");
                }
            }
            preparedStatement.setInt(4, newCourseId);
            preparedStatement.setString(5, "unblocked");
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error("can't create connection");
        }
    }

    private List<Integer> selectCourseId(String login) {
        List<Integer> integers = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_LIST_INTS)) {
            pStatement.setString(1, login);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                integers.add(rSet.getInt("courseId"));
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return integers;
    }

    private List<String> selectLogins() {
        List<String> strings = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_LOGINS)) {
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                strings.add(rSet.getString("login"));
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return strings;
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

    private void parseForDistinctState(ResultSet rSet) {
        try {
            System.out.println("login: " + rSet.getString("login") + " " + "state: " + rSet.getString("state"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void parseForDistinctName(ResultSet rSet) {
        try {
            System.out.println("login: " + rSet.getString("login") + " " + "name: " + rSet.getString("name"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
