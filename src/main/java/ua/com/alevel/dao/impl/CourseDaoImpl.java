package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.builders.CourseBuilder;
import ua.com.alevel.builders.impl.CourseBuilderImpl;
import ua.com.alevel.builders.impl.ThemeBuilderImpl;
import ua.com.alevel.dao.CourseDao;
import ua.com.alevel.dao.ThemeDao;
import ua.com.alevel.dto.Course;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class CourseDaoImpl implements CourseDao {
    private static CourseDaoImpl instance;
    private static final Logger log = Logger.getLogger(CourseDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private final Date currentDate = new Date();
    private final ThemeDao themeDao = ThemeDaoImpl.getInstance();
    private static final String SQL_FOR_INSERT = "INSERT INTO courses (name, theme, startDate, endDate) " +
            "values (?, ?, ?, ?)";
    private static final String SQL_FOR_ALL = "SELECT * FROM courses";
    private static final String SQL_BEFORE_START = "SELECT * FROM courses WHERE startDate > ?";
    private static final String SQL_AFTER_START = "SELECT * FROM courses WHERE startDate <= ?";
    private static final String SQL_BEFORE_STARTID = "SELECT * FROM courses c JOIN students s " +
            "WHERE s.login = ? AND s.courseId = c.id AND startDate > ?";
    private static final String SQL_AFTER_STARTID = "SELECT * FROM courses c JOIN students s " +
            "WHERE s.login = ? AND s.courseId = c.id AND startDate <= ?";
    private static final String SQL_FOR_ID = "SELECT * FROM courses WHERE id = ?";
    private static final String SQL_FOR_TID = "SELECT t.courseId FROM teachers t WHERE t.id = ?";
    private static final String SQL_ENDED = "SELECT * FROM courses WHERE endDate <= ?";
    private static final String SQL_ENDEDID = "SELECT * FROM courses c JOIN students s " +
            "WHERE s.login = ? AND s.courseId = c.id AND endDate <= ?";
    private static final String SQL_UPDATE = "UPDATE courses SET name = ?, theme = ?, startDate = ?, endDate = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM courses WHERE id = ?";
    private static final String SQL_FOR_ALLSORT = "SELECT * FROM courses ORDER BY name";
    private static final String SQL_FOR_ALLSORTLOGIN = "SELECT * FROM courses c JOIN students s " +
            "WHERE s.login = ? AND s.courseId = c.id ORDER BY c.name";
    private static final String SQL_FOR_THEMES = "SELECT * FROM courses c WHERE theme = ?";


    private CourseDaoImpl() {
    }

    public static CourseDaoImpl getInstance() {
        if (instance == null) {
            instance = new CourseDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean addCourse(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FOR_INSERT)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            statement.setString(1, course.getName());
            statement.setString(2, course.getTheme());
            themeDao.addTheme(new ThemeBuilderImpl().setName(course.getTheme()).build());
            statement.setDate(3, new java.sql.Date(course.getStartDate().getTime()));
            statement.setDate(4, new java.sql.Date(course.getEndDate().getTime()));
            statement.executeUpdate();
            log.info("Inserting into DB courses");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return false;
    }

    @Override
    public List<Course> selectAll() {
        List<Course> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALL)) {
            log.info("Created connection and statement");
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return all;
    }


    @Override
    public List<Course> selectAll(boolean useSort) {
        List<Course> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALLSORT)) {
            log.info("Created connection and statement");
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return all;
    }

    @Override
    public List<Course> selectAll(String login, boolean useSort) {
        List<Course> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALLSORTLOGIN)) {
            log.info("Created connection and statement");
            pStatement.setString(1, login);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return all;
    }

    @Override
    public Course selectById(int id) throws NoDataInDBException {
        Course course = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ID)) {
            log.info("Created connection and statement");
            pStatement.setInt(1, id);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                course = mapCourse(rSet);
            }
            return course;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no course with this id");
    }

    @Override
    public List<Course> selectAllNotStarted() {
        List<Course> beforeStart = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_BEFORE_START)) {
            log.info("Created connection and statement");
            pStatement.setDate(1, new java.sql.Date(currentDate.getTime()));
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    beforeStart.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return beforeStart;
    }

    @Override
    public List<Course> selectAllStarted() {
        List<Course> afterStart = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_AFTER_START)) {
            log.info("Created connection and statement");
            pStatement.setDate(1, new java.sql.Date(currentDate.getTime()));
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    afterStart.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }

            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return afterStart;
    }

    @Override
    public List<Course> selectAllEnded() {
        List<Course> ended = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_ENDED)) {
            log.info("Created connection and statement");
            pStatement.setDate(1, new java.sql.Date(currentDate.getTime()));
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    ended.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }

            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return ended;
    }

    @Override
    public Course selectCourseByTeacherId(int teacherId) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_TID)) {
            log.info("Created connection and statement");
            pStatement.setInt(1, teacherId);
            ResultSet rSet = pStatement.executeQuery();
            int courseId = 0;
            while (rSet.next()) {
                courseId = rSet.getInt("courseId");
            }
            return selectById(courseId);
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no teacher with this ID");
    }

    @Override
    public List<Course> selectAllNotStarted(String login) {
        List<Course> beforeStart = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_BEFORE_STARTID)) {
            log.info("Created connection and statement");
            pStatement.setString(1, login);
            pStatement.setDate(2, new java.sql.Date(currentDate.getTime()));
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    beforeStart.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return beforeStart;
    }

    @Override
    public List<Course> selectAllStarted(String login) {
        List<Course> afterStart = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_AFTER_STARTID)) {
            log.info("Created connection and statement");
            pStatement.setString(1, login);
            pStatement.setDate(2, new java.sql.Date(currentDate.getTime()));
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    afterStart.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }

            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return afterStart;
    }

    @Override
    public List<Course> selectAllEnded(String login) {
        List<Course> ended = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_ENDEDID)) {
            log.info("Created connection and statement");
            pStatement.setString(1, login);
            pStatement.setDate(2, new java.sql.Date(currentDate.getTime()));
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    ended.add(mapCourse(rSet));
                } catch (SQLException e) {
                    log.error("Can't map courses data from result set " + e);
                }

            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return ended;
    }

    @Override
    public boolean updateCourse(Course course, int id) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_UPDATE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setString(1, course.getName());
            pStatement.setString(2, course.getTheme());
            themeDao.addTheme(new ThemeBuilderImpl().setName(course.getTheme()).build());
            pStatement.setDate(3, new java.sql.Date(course.getStartDate().getTime()));
            pStatement.setDate(4, new java.sql.Date(course.getEndDate().getTime()));
            pStatement.setInt(5, id);
            int a = pStatement.executeUpdate();
            if (a == 0) {
                throw new NoDataInDBException("Can't update course with this id");
            }
            log.info("Updating in DB courses");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection");
        }
        return false;
    }

    @Override
    public boolean deleteCourse(int id) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_DELETE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setInt(1, id);
            int a = pStatement.executeUpdate();
            if (a == 0) {
                throw new NoDataInDBException("Can't delete course with this id");
            }
            log.info("Deleting from DB courses");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return false;
    }

    @Override
    public List<Course> selectAllByTheme(String theme) throws NoDataInDBException {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_THEMES)) {
            pStatement.setString(1, theme);
            ResultSet resultSet = pStatement.executeQuery();
            while (resultSet.next()) {
                try {
                    courses.add(mapCourse(resultSet));
                } catch (SQLException e) {
                    log.error("Can't map course from result set");
                }
            }
            return courses;
        } catch (SQLException e) {
            log.error("Can't create connection ", e);
        }
        throw new NoDataInDBException("There is no courses with this theme");
    }

    private Course mapCourse(ResultSet rSet) throws SQLException {
        CourseBuilder builder = new CourseBuilderImpl();
        builder.setId(rSet.getInt("id"));
        builder.setName(rSet.getString("name"));
        builder.setTheme(rSet.getString("theme"));
        builder.setStartDate(rSet.getDate("startDate"));
        builder.setEndDate(rSet.getDate("endDate"));
        return builder.build();
    }

}
