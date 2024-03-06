package com.stratos.giak.libraryntua.databases;

import com.stratos.giak.libraryntua.models.BookModel;
import com.stratos.giak.libraryntua.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public final class Users {
    private static Users instance;
    private transient ObservableList<UserModel> usersList = FXCollections.observableArrayList();

    private Users() {
    }

    /**
     * Returns a singleton instance of the Users database object.
     * The Users object manages all operations that concern the registered users.
     *
     * @return a Users instance
     * @see BookModel
     */
    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
            try {
                instance.loadUsers();
            } catch (IOException | ClassNotFoundException ignored) {
            }
        }
        return instance;
    }

    /**
     * @return an observable list of all registered users, represented by {@link UserModel} objects
     */
    public ObservableList<UserModel> getUsersList() {
        return usersList;
    }

    /**
     * Returns a user with the given UUID.
     * This is only used for the (de)serialization of loans.
     *
     * @param uuid the {@link UUID} of the UserModel object
     * @return a {@link UserModel} object with the given UUID, or null if no such user exists
     */
    public UserModel getUser(UUID uuid) {
        return getUsersList().stream().filter(user -> user.getUUID().equals(uuid)).findAny().orElse(null);
    }

    /**
     * Adds the given user to the list of registered users.
     *
     * @param user the user to be added
     */
    public void addUser(UserModel user) {
        getUsersList().add(user);
    }

    /**
     * Removes the given user from the database, if it exists.
     * Also ends all open loans by that user.
     *
     * @param user the user to be removed
     */
    public void removeUser(UserModel user) {
        Loans.getInstance().removeAllWithUser(user);
        getUsersList().remove(user);
    }

    /**
     * Serializes the list of registered users.
     * Saves the list of users as a file named "users",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException if there is any error writing to the file
     */
    public void saveUsers() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/users");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getUsersList()));
        objectStream.close();
        fileStream.close();
    }

    /**
     * Deserializes the list of registered users.
     * Loads the list of users from a file named "users",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException            if there is any error reading the file
     * @throws ClassNotFoundException if the serialized object is not a list of {@link UserModel}
     */
    public void loadUsers() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/users");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        usersList = FXCollections.observableArrayList((ArrayList<UserModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
