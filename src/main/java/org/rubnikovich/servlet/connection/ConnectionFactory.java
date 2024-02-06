package org.rubnikovich.servlet.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.rubnikovich.servlet.connection.DatabaseParameters.*;

public class ConnectionFactory {
    private static final Logger logger = LogManager.getLogger();

    static {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            logger.log(Level.FATAL, "Can't register driver");
            throw new ExceptionInInitializerError("Can't register driver." + e.getMessage());
        }
    }

    static Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Connection wasn't created: " + e.getMessage());
        }
        return connection;
    }
}