package org.rubnikovich.servlet.command;

import jakarta.servlet.http.HttpServletRequest;
import org.rubnikovich.servlet.exception.CommandException;

public interface Command {
    String execute(HttpServletRequest request) throws CommandException;
    default void refresh(){

    }
}
