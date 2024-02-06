package org.rubnikovich.servlet.service;

import org.rubnikovich.servlet.exception.ServiceException;

public interface UserService {
    boolean authenticate(String login, String password) throws ServiceException;
}
