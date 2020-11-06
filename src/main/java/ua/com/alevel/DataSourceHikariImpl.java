package ua.com.alevel;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ua.com.alevel.util.PropertiesReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataSourceHikariImpl implements DataSource {
    private static final HikariConfig config;
    private static final HikariDataSource dataSource;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        config = new HikariConfig();
        Properties properties = PropertiesReader.readProperties();
        config.setDataSourceProperties(properties);
        config.setJdbcUrl(properties.getProperty("url"));
        config.setUsername(properties.getProperty("username"));
        config.setPassword(properties.getProperty("password"));
        dataSource = new HikariDataSource(config);
    }


    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
