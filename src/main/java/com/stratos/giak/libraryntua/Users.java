package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public final class Users {
    private static Users instance;
    private transient ObservableList<UserModel> allUsersList = FXCollections.observableArrayList();

    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
            try {
                instance.loadUsers();
            } catch (IOException | ClassNotFoundException e) {
                UserModel defaultAdmin = new UserModel(
                        "medialab",
                        "medialab_2024",
                        "Default",
                        "Admin",
                        "0",
                        "admin@admin.com",
                        AccessLevel.ADMIN);
                UserModel tempAdmin = new UserModel(
                        "a",
                        "a",
                        "Temp",
                        "Admin",
                        "1",
                        "temp@admin.com",
                        AccessLevel.ADMIN);
                instance.addUser(defaultAdmin);
                instance.addUser(tempAdmin);
                return instance;
            }
        }
        return instance;
    }

    ObservableList<UserModel> getAllUsersList() {
        return allUsersList;
    }

    UserModel getUserByUUID(UUID uuid) {
        return getAllUsersList().stream().filter(user -> user.getUUID().equals(uuid)).findAny().orElse(null);
    }

    UserModel getUserByUsername(String username) {
        return getAllUsersList().stream().filter(user -> user.getUsername().equals(username)).findAny().orElse(null);
    }

    void addUser(UserModel user) {
        if (user.getUsername().isBlank()
                || user.getPassword().isBlank()
                || user.getEmail().isBlank()
                || user.getNameFirst().isBlank()
                || user.getNameLast().isBlank()
                || user.getID().isBlank())
            throw new IllegalArgumentException("Invalid account info");
        getAllUsersList().add(user);
    }

    void editUser(UUID uuid, String username, String password, String nameFirst, String nameLast, String ID, String email, AccessLevel accessLevel) {
        UserModel user = getAllUsersList().stream().filter(usr -> usr.getUUID().equals(uuid)).findAny().orElse(null);
        if (user == null)
            throw new IllegalArgumentException("User UUID does not exists");
        if (username != null) user.setUsername(username);
        if (password != null) user.setPassword(password);
        if (nameFirst != null) user.setNameFirst(nameFirst);
        if (nameLast != null) user.setNameLast(nameLast);
        if (ID != null) user.setID(ID);
        if (email != null) user.setEmail(email);
        if (accessLevel != null) user.setAccessLevel(accessLevel);
    }

    void saveUsers() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/users");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getAllUsersList()));
        objectStream.close();
        fileStream.close();
    }

    void loadUsers() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/users");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        allUsersList = FXCollections.observableArrayList((ArrayList<UserModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
