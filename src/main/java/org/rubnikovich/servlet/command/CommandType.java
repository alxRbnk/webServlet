package org.rubnikovich.servlet.command;

import org.rubnikovich.servlet.command.impl.AddUserCommand;
import org.rubnikovich.servlet.command.impl.DefaultCommand;
import org.rubnikovich.servlet.command.impl.LoginCommand;
import org.rubnikovich.servlet.command.impl.LogoutCommand;

public enum CommandType {
    ADD_USER(new AddUserCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    DEFAULT(new DefaultCommand());
    Command command;
    CommandType(Command command) {
        this.command = command;
    }
    public static Command define(String commandStr){
        CommandType current = CommandType.valueOf(commandStr.toUpperCase());
        return current.command;
    }
}
