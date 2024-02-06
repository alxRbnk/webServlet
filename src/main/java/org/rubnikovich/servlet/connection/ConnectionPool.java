package org.rubnikovich.servlet.connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.rubnikovich.servlet.connection.DatabaseParameters.CONNECTION_POOL_SIZE;

public enum ConnectionPool {
    INSTANCE;

    private final Logger logger = LogManager.getLogger();
    private BlockingQueue<Connection> freeConnections;
    private Queue<Connection> usedConnections;

    ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(CONNECTION_POOL_SIZE);
        usedConnections = new ArrayDeque<>(CONNECTION_POOL_SIZE);
        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            Connection connection = ConnectionFactory.createConnection();
            if (connection == null) {
                continue;
            }
            Connection proxyConnection = new ProxyConnection(connection);
            freeConnections.add(proxyConnection);
        }
        checkPoolState();
    }

    private void checkPoolState() {
        if (freeConnections.size() == CONNECTION_POOL_SIZE) {
            logger.log(Level.INFO, "Connection pool was created.");
        } else if (freeConnections.size() < CONNECTION_POOL_SIZE) {
            createMissingConnections(CONNECTION_POOL_SIZE - freeConnections.size());
        } else if (freeConnections.isEmpty()) {
            logger.log(Level.FATAL, "Not a single connection was created.");
            throw new ExceptionInInitializerError("Not a single connection was created.");
        }
    }

    private void createMissingConnections(int countOfNotCreatedConnections) {
        for (int i = 0; i < countOfNotCreatedConnections; i++) {
            Connection connection = ConnectionFactory.createConnection();
            if (connection == null) {
                continue;
            }
            Connection proxyConnection = new ProxyConnection(connection);
            freeConnections.add(proxyConnection);
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = freeConnections.take();
            usedConnections.add(connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Current thread was interrupted");
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        boolean isProxy;
        if (connection.getClass() == ProxyConnection.class) {
            usedConnections.remove(connection);
            freeConnections.add(connection);
            isProxy = true;
        } else {
            logger.log(Level.WARN, "Attempting to add \"wild\" connection.");
            isProxy = false;
        }
        return isProxy;
    }

    public void destroyPool() {
        int notClosed = 0;
        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            try {
                freeConnections.take().close();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Problem with closing connection: " + e.getMessage());
                notClosed++;
            } catch (InterruptedException e) {
                logger.log(Level.WARN, "Current thread was interrupted");
                Thread.currentThread().interrupt();
            }
        }
        if (notClosed == 0) {
            logger.log(Level.INFO, "Connection pool was successfully destroyed.");
        } else {
            logger.log(Level.WARN, "Connection pool was destroyed, but " + notClosed + " connections were not properly closed.");
        }
        deregisterDrivers();
    }

    private void deregisterDrivers(){
        DriverManager.drivers().forEach(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Error during driver deregister: " + e.getMessage());
            }
        });
    }
}
