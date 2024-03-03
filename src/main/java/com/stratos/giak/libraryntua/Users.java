package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

//TODO ADD DOCS
public final class Users {
    private static Users instance;
    private transient ObservableList<UserModel> usersList = FXCollections.observableArrayList();

    //TODO ADD DOCS
    public static Users getInstance() {
        if (instance == null) {
            instance = new Users();
            try {
                instance.loadUsers();
            } catch (IOException | ClassNotFoundException e) {
                UserModel[] defaultUsers = {
                        new UserModel(
                                "medialab",
                                "medialab_2024",
                                "Default",
                                "Admin",
                                "0",
                                "admin@admin.com",
                                AccessLevel.ADMIN),
                        new UserModel(
                                "a",
                                "a",
                                "Temp",
                                "Admin",
                                "1",
                                "temp@admin.com",
                                AccessLevel.ADMIN),
                        new UserModel(
                                "stratos",
                                "pass",
                                "Stra",
                                "Tos",
                                "1234",
                                "s@g.c",
                                AccessLevel.USER)
                };
                instance.getUsersList().addAll(defaultUsers);
                return instance;
            }
        }
        return instance;
    }

    //TODO ADD DOCS
    public ObservableList<UserModel> getUsersList() {
        return usersList;
    }

    //TODO ADD DOCS
    public UserModel getUser(UUID uuid) {
        return getUsersList().stream().filter(user -> user.getUUID().equals(uuid)).findAny().orElse(null);
    }

    //TODO ADD DOCS
    public UserModel getUserByUsername(String username) {
        return getUsersList().stream().filter(user -> user.getUsername().equals(username)).findAny().orElse(null);
    }

    //TODO ADD DOCS
    public void addUser(UserModel user) {
        if (user.getUsername().isBlank()
                || user.getPassword().isBlank()
                || user.getEmail().isBlank()
                || user.getNameFirst().isBlank()
                || user.getNameLast().isBlank()
                || user.getID().isBlank())
            throw new IllegalArgumentException("Invalid account info");
        getUsersList().add(user);
    }

    //TODO ADD DOCS
    public void editUser(UserModel user, String username, String password, String nameFirst, String nameLast, String ID, String email, AccessLevel accessLevel) {
        if (user == null)
            throw new IllegalArgumentException("User object does not exists");
        if (username != null) user.setUsername(username);
        if (password != null) user.setPassword(password);
        if (nameFirst != null) user.setNameFirst(nameFirst);
        if (nameLast != null) user.setNameLast(nameLast);
        if (ID != null) user.setID(ID);
        if (email != null) user.setEmail(email);
        if (accessLevel != null) user.setAccessLevel(accessLevel);
    }

    //TODO ADD DOCS
    public void removeUser(UserModel user) {
        Loans.getInstance().removeAllWithUser(user);
        getUsersList().remove(user);
    }

    //TODO ADD DOCS
    public void saveUsers() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/users");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getUsersList()));
        objectStream.close();
        fileStream.close();
    }

    //TODO ADD DOCS
    public void loadUsers() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/users");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        usersList = FXCollections.observableArrayList((ArrayList<UserModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
