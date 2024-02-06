package org.rubnikovich.servlet.command;

import org.rubnikovich.servlet.util.Pages;

public class Router {
    private String page = Pages.INDEX;
    private Type type = Type.FORWARD;
    enum Type{
        FORWARD, REDIRECT;
    }

    public Router(String page) {
        this.page = page;
    }

    public Router(String page, Type type) {
        this.page = page;
        this.type = type;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setRedirect() {
        this.type = Type.REDIRECT;
    }
}
