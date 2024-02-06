package org.rubnikovich.servlet.service.impl;

import org.rubnikovich.servlet.dao.impl.UserDaoImpl;
import org.rubnikovich.servlet.exception.DaoException;
import org.rubnikovich.servlet.exception.ServiceException;
import org.rubnikovich.servlet.service.UserService;

public class UserServiceImpl implements UserService {
    private static UserServiceImpl instance = new UserServiceImpl();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authenticate(String login, String password) throws ServiceException {
        //validate login, pass, md5
        UserDaoImpl userDao = UserDaoImpl.getInstance();
        boolean match = false;
        try {
            match = userDao.authenticate(login, password);
        } catch (DaoException e) {
            throw new ServiceException("", e);
        }
        return match;
    }
}
