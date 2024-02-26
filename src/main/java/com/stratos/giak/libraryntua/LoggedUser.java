package com.stratos.giak.libraryntua;

import java.util.UUID;

public final class LoggedUser {
    private static LoggedUser instance;
    private UUID uuid;

    private LoggedUser() {
    }

    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    public UserModel getUser() {
        if (uuid == null) throw new RuntimeException("Not logged in");
        return Users.getInstance().getUserByUUID(uuid);
    }

    public void setUser(UserModel user) {
        this.uuid = user.getUUID();
    }
}
