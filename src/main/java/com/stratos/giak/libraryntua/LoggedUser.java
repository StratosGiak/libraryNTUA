package com.stratos.giak.libraryntua;

//TODO ADD DOCS
public final class LoggedUser {
    private static LoggedUser instance;
    private UserModel user;

    private LoggedUser() {
    }

    //TODO ADD DOCS
    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    //TODO ADD DOCS
    public UserModel getUser() {
        if (user == null) throw new RuntimeException("Not logged in");
        return user;
    }

    //TODO ADD DOCS
    public void setUser(UserModel user) {
        this.user = user;
    }
}
