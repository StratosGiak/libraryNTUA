package com.stratos.giak.libraryntua.utilities;

import com.stratos.giak.libraryntua.models.UserModel;
import javafx.beans.property.SimpleObjectProperty;

public final class LoggedUser {
    private static LoggedUser instance;
    private final SimpleObjectProperty<UserModel> user = new SimpleObjectProperty<>();

    private LoggedUser() {
    }

    /**
     * Returns a singleton instance of the LoggedUser database object.
     * The LoggedUser object holds the user who is currently logged into the application.
     *
     * @return a LoggedUser instance
     */
    public static LoggedUser getInstance() {
        if (instance == null) {
            instance = new LoggedUser();
        }
        return instance;
    }

    /**
     * Gets the value of the logged user property.
     */
    public UserModel getUser() {
        return user.get();
    }

    /**
     * Sets the value of the logged user property.
     */
    public void setUser(UserModel user) {
        this.user.set(user);
    }

    /**
     * The property describing the user that is currently logged into the application.
     *
     * @see #getUser
     * @see #setUser(UserModel)
     */
    public SimpleObjectProperty<UserModel> userProperty() {
        return user;
    }
}
