package org.rubnikovich.servlet.dao;

import org.rubnikovich.servlet.exception.DaoException;

public interface UserDao {
    boolean authenticate(String login, String password) throws DaoException;
}
