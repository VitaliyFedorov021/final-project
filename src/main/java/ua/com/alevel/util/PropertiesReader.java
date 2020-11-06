package ua.com.alevel.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    private static final Logger LOG = Logger.getLogger(PropertiesReader.class);
    public static Properties readProperties() {
        Properties properties = new Properties();
        try (InputStream reader = PropertiesReader.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(reader);
        } catch (IOException e) {
            LOG.error("Properties reader error " + e);
        }
        return properties;
    }
}
