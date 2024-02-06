package org.rubnikovich.servlet.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class DatabaseParameters {
    private static final Logger logger = LogManager.getLogger();
    static final String URL;
    static final String USER;
    static final String PASSWORD;
    static final int CONNECTION_POOL_SIZE;

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("database");
        try {
            URL = resourceBundle.getString("url");
            USER = resourceBundle.getString("user");
            PASSWORD = resourceBundle.getString("password");
            CONNECTION_POOL_SIZE = Integer.parseInt(resourceBundle.getString("pool_size"));
        } catch (MissingResourceException e) {
            logger.log(Level.FATAL, "Can't read parameters from file: " + resourceBundle.getBaseBundleName() + e.getMessage());
            throw new ExceptionInInitializerError("Can't read parameters from file: " + resourceBundle.getBaseBundleName() + e.getMessage());
        }
    }
}
