package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public final class Users {
    private static Users instance;
    private transient ObservableList<UserModel> allUsersList;
    private transient ObservableMap<UUID, UserModel> allUsersMap = FXCollections.observableHashMap();

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
                instance.addUser(defaultAdmin);
                return instance;
            } finally {
                instance.allUsersList = FXCollections.observableArrayList(instance.allUsersMap.values());
                instance.allUsersMap.addListener((MapChangeListener<UUID, UserModel>) change -> {
                    if (change.wasAdded()) {
                        instance.allUsersList.add(change.getValueAdded());
                    }
                    if (change.wasRemoved()) {
                        instance.allUsersList.remove(change.getValueRemoved());
                    }
                });
            }
        }
        return instance;
    }

    ObservableMap<UUID, UserModel> getAllUsersMap() {
        return allUsersMap;
    }

    ObservableList<UserModel> getAllUsersList() {
        return allUsersList;
    }

    UserModel getUserByUUID(UUID uuid) {
        return getAllUsersMap().get(uuid);
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
        getAllUsersMap().putIfAbsent(user.getUUID(), user);
    }

    void editUser(UUID uuid, UserModel user) {
        if (getAllUsersMap().replace(uuid, user) == null)
            throw new IllegalArgumentException("User UUID does not exists");
    }

    void saveUsers() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/users");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new HashMap<>(getAllUsersMap()));
        objectStream.close();
        fileStream.close();
    }

    void loadUsers() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/users");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        allUsersMap = FXCollections.observableMap((HashMap<UUID, UserModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
