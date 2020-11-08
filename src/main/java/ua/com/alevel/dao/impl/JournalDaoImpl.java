package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.builders.JournalBuilder;
import ua.com.alevel.builders.impl.JournalBuilderImpl;
import ua.com.alevel.dao.JournalDao;
import ua.com.alevel.dto.Course;
import ua.com.alevel.dto.Journal;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JournalDaoImpl implements JournalDao {
    private static JournalDaoImpl instance;
    private static final Logger log = Logger.getLogger(JournalDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private static final String SQL_FOR_CREATE = "INSERT INTO journal (courseId, teacherId, studentId, mark)" +
            " values(?, ?, ?, ?)";
    private static final String SQL_FOR_ALL = "SELECT * FROM journal";
    private static final String SQL_FOR_LOGIN = "SELECT * FROM journal WHERE studentLogin = ?";
    private static final String SQL_FOR_CID = "SELECT * FROM journal WHERE courseId = ?";
    private static final String SQL_FOR_TID = "SELECT * FROM journal WHERE teacherId = ?";
    private static final String SQL_FOR_MARK = "SELECT * FROM journal WHERE mark > ?";
    private static final String SQL_FOR_UPDATE = "UPDATE journal SET courseId = ?, teacherId = ?, studentLogin = ?, mark = ?";

    private JournalDaoImpl() {
    }

    public static JournalDaoImpl getInstance() {
        if (instance == null) {
            instance = new JournalDaoImpl();
        }
        return instance;
    }

    @Override
    public boolean createNewEntry(Journal journal) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_CREATE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setInt(1, journal.getCourseId());
            pStatement.setInt(2, journal.getTeacherId());
            pStatement.setInt(3, journal.getStudentId());
            pStatement.setInt(4, journal.getMark());
            try {
                pStatement.executeUpdate();
            }
            catch (SQLException e) {
                connection.rollback();
                throw new NoDataInDBException("Can't create entry, you entered teacher, or student or course which doesn't exists", e);
            }
            log.info("Inserting in DB journal");
            connection.commit();
            return true;
        } catch (
                SQLException e) {
            log.error("Can't create connection ", e);
        }
        return false;
    }

    @Override
    public List<Journal> selectAllEntries() {
        List<Journal> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_ALL)) {
            log.info("Created connection and statement");
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapJournal(rSet));
                } catch (SQLException e) {
                    log.error("Can't map journal data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return all;
    }

    @Override
    public List<Journal> selectAllByStudentLogin(String login) throws NoDataInDBException {
        List<Journal> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_LOGIN)) {
            log.info("Created connection and statement");
            pStatement.setString(1, login);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapJournal(rSet));
                } catch (SQLException e) {
                    log.error("Can't map journal data from result set " + e);
                }
            }
            return all;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no student with this login");
    }

    @Override
    public List<Journal> selectAllByCourseId(int id) throws NoDataInDBException {
        List<Journal> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_CID)) {
            log.info("Created connection and statement");
            pStatement.setInt(1, id);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapJournal(rSet));
                } catch (SQLException e) {
                    log.error("Can't map journal data from result set " + e);
                }
            }
            return all;
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        throw new NoDataInDBException("There is no courses with this id");
    }

    @Override
    public List<Journal> selectAllByTeacherId(int id) {
        List<Journal> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_TID)) {
            log.info("Created connection and statement");
            pStatement.setInt(1, id);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapJournal(rSet));
                } catch (SQLException e) {
                    log.error("Can't map journal data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return all;
    }

    @Override
    public List<Journal> selectAllHigherThanMark(int mark) {
        List<Journal> all = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_MARK)) {
            log.info("Created connection and statement");
            pStatement.setInt(1, mark);
            ResultSet rSet = pStatement.executeQuery();
            while (rSet.next()) {
                try {
                    all.add(mapJournal(rSet));
                } catch (SQLException e) {
                    log.error("Can't map journal data from result set " + e);
                }
            }
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
        return all;
    }

    @Override
    public boolean updateJournalEntry(Journal journal) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_FOR_UPDATE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setInt(1, journal.getCourseId());
            pStatement.setInt(2, journal.getTeacherId());
            pStatement.setInt(3, journal.getStudentId());
            pStatement.setInt(4, journal.getMark());
            try {
                pStatement.executeUpdate();
            }
            catch (SQLException e) {
                connection.rollback();
                throw new NoDataInDBException("Can't create entry, you entered teacher, or student or course which doesn't exists", e);
            }
            log.info("Updating in DB journal");
            connection.commit();
            return true;
        } catch (SQLException e) {
            log.error("Can't create connection");
        }
        return false;
    }


    private Journal mapJournal(ResultSet rSet) throws SQLException {
        JournalBuilder builder = new JournalBuilderImpl();
        builder.setCourseId(rSet.getInt("courseId"));
        builder.setStudentId(rSet.getInt("studentId"));
        builder.setTeacherId(rSet.getInt("teacherId"));
        builder.setMark(rSet.getInt("mark"));
        return builder.build();
    }
}
