package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.dao.ThemeDao;
import ua.com.alevel.dto.Theme;
import ua.com.alevel.exceptions.NoDataInDBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ThemeDaoImpl implements ThemeDao {
    private static ThemeDaoImpl instance;
    private static final Logger log = Logger.getLogger(ThemeDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private static final String SQL_INSERT = "INSERT IGNORE INTO themes (name) values (?)";
    private static final String SQL_DELETE = "DELETE FROM themes WHERE name = ?";

    private ThemeDaoImpl() {
    }

    public static ThemeDaoImpl getInstance() {
        if (instance == null) {
            instance = new ThemeDaoImpl();
        }
        return instance;
    }

    @Override
    public void addTheme(Theme theme) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_INSERT)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setString(1, theme.getName());
            pStatement.executeUpdate();
            log.info("Inserting into DB themes");
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
    }

    @Override
    public void deleteTheme(String themeName) throws NoDataInDBException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_DELETE)) {
            log.info("Created connection and statement");
            connection.setAutoCommit(false);
            pStatement.setString(1, themeName);
            int a = pStatement.executeUpdate();
            if (a == 0) {
                throw new NoDataInDBException("Can't delete theme with this name");
            }
            log.info("Deleting from DB themes");
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create a connection " + e);
        }
    }
}
