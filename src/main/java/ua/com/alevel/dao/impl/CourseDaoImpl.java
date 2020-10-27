package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.builders.CourseBuilder;
import ua.com.alevel.builders.ThemeBuilder;
import ua.com.alevel.builders.impl.CourseBuilderImpl;
import ua.com.alevel.builders.impl.ThemeBuilderImpl;
import ua.com.alevel.dao.CourseDao;
import ua.com.alevel.dao.ThemeDao;
import ua.com.alevel.dto.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Scanner;

public class CourseDaoImpl implements CourseDao {
    private static final Logger log = Logger.getLogger(CourseDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private final Date currentDate = new Date();
    private final ThemeDao themeDao = new ThemeDaoImpl();
    private static final String SQL_FOR_INSERT = "INSERT INTO courses (name, theme, startDate, endDate) " +
            "values (?, ?, ?, ?)";
    private static final String SQL_FOR_ALL = "SELECT * FROM courses";
    private static final String SQL_BEFORE_START = "SELECT * FROM courses WHERE startDate > ?";
    private static final String SQL_AFTER_START = "SELECT * FROM courses WHERE startDate <= ?";
    private static final String SQL_FOR_ID = "SELECT * FROM courses WHERE id = ?";
    private static final String SQL_ENDED = "SELECT * FROM courses WHERE endDate <= ?";
    private static final String SQL_UPDATE = "UPDATE courses SET name = ?, theme = ?, startDate = ?, endDate = ? " +
            "WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM courses WHERE id = ?";

    @Override
    public void addCourse(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FOR_INSERT)) {
            connection.setAutoCommit(false);
            statement.setString(1, course.getName());
            statement.setString(2, course.getTheme());
            themeDao.addTheme(new ThemeBuilderImpl().setName(course.getTheme()).build());
            statement.setDate(3, (java.sql.Date) course.getStartDate());
            statement.setDate(4, (java.sql.Date) course.getEndDate());
            statement.executeQuery();
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
    }

    @Override
    public List<Course> selectAll() {
        List<Course> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALL)) {
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
    public Course selectById(int id) {
        Course course = null;
        try (Connection connection = dataSource.getConnection();
        PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ID)) {
            pStatement.setInt(1, id);
            ResultSet rSet = pStatement.executeQuery();
            course = mapCourse(rSet);
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return course;
    }

    @Override
    public List<Course> selectAllNotStarted() {
        List<Course> beforeStart = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_BEFORE_START)) {
            pStatement.setDate(1, (java.sql.Date) currentDate);
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
            pStatement.setDate(1, (java.sql.Date) currentDate);
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
            pStatement.setDate(1, (java.sql.Date) currentDate);
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
    public void updateCourse(Course course) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_UPDATE)) {
            connection.setAutoCommit(false);
            pStatement.setString(1, course.getName());
            pStatement.setString(2, course.getTheme());
            pStatement.setDate(3, (java.sql.Date) course.getStartDate());
            pStatement.setDate(4, (java.sql.Date) course.getEndDate());
            pStatement.setInt(5, course.getId());
            pStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create connection");
        }
    }

    @Override
    public void deleteCourse(int id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_DELETE)) {
            connection.setAutoCommit(false);
            pStatement.setInt(1, id);
            pStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
    }

    private Course mapCourse(ResultSet rSet) throws SQLException {
        CourseBuilder builder = new CourseBuilderImpl();
        builder.setId(rSet.getInt( "id"));
        builder.setName(rSet.getString("name"));
        builder.setTheme(rSet.getString("theme"));
        builder.setStartDate(rSet.getDate("startDate"));
        builder.setEndDate(rSet.getDate("endDate"));
        return builder.build();
    }


    private static Date getDate(SimpleDateFormat format) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                final String startDateLine = sc.nextLine();
                final Date startDate = format.parse(startDateLine);
                return startDate;
            } catch (ParseException e) {
                log.error("Can't parse date " + e);
                System.out.println("Try again");
            }
        }
    }
}
