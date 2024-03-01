package com.stratos.giak.libraryntua;

public final class LoggedUser {
    private static LoggedUser instance;
    private UserModel user;

    private LoggedUser() {
    }

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    public UserModel getUser() {
        if (user == null) throw new RuntimeException("Not logged in");
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
