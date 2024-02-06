package org.rubnikovich.servlet.command.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.rubnikovich.servlet.command.Command;
import org.rubnikovich.servlet.util.Pages;

public class LogoutCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return Pages.INDEX;
    }
}
