package ua.com.alevel.dao.impl;

import org.apache.log4j.Logger;
import ua.com.alevel.DataSource;
import ua.com.alevel.DataSourceHikariImpl;
import ua.com.alevel.dao.ThemeDao;
import ua.com.alevel.dto.Theme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ThemeDaoImpl implements ThemeDao {
    private static final Logger log = Logger.getLogger(ThemeDaoImpl.class);
    private final DataSource dataSource = new DataSourceHikariImpl();
    private static final String SQL_INSERT = "INSERT IGNORE INTO themes (name) values (?)";
    private static final String SQL_DELETE = "DELETE FROM themes WHERE name = ?";
    @Override
    public void addTheme(Theme theme) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pStatement = connection.prepareStatement(SQL_INSERT)) {
            connection.setAutoCommit(false);
            pStatement.setString(1, theme.getName());
            pStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create connection " + e);
        }
    }

    @Override
    public void deleteTheme(String themeName) {
        try (Connection connection = dataSource.getConnection();
        PreparedStatement pStatement = connection.prepareStatement(SQL_DELETE)) {
            connection.setAutoCommit(false);
            pStatement.setString(1, themeName);
            pStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            log.error("Can't create a connection " + e);
        }
    }
}
