package com.stratos.giak.libraryntua;


import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public final class Genres implements Serializable {
    private static Genres instance;
    private transient ObservableMap<UUID, String> genresMap = FXCollections.observableHashMap();
    private transient ObservableList<String> genresList;

    public static Genres getInstance() {
        if (instance == null) {
            instance = new Genres();
            try {
                instance.loadGenres();
            } catch (IOException | ClassNotFoundException e) {
                return instance;
            } finally {
                instance.genresList = FXCollections.observableArrayList(instance.genresMap.values());
                instance.genresMap.addListener((MapChangeListener<UUID, String>) change -> {
                    if (change.wasAdded()) {
                        instance.genresList.add(change.getValueAdded());
                    }
                    if (change.wasRemoved()) {
                        instance.genresList.remove(change.getValueRemoved());
                    }
                });
            }
        }
        return instance;
    }

    public ObservableMap<UUID, String> getGenresMap() {
        return genresMap;
    }

    public ObservableList<String> getGenresList() {
        return genresList;
    }

    public void addGenre(String genre) {
        if (LoggedUser.getInstance().getUser().getAccessLevel() != AccessLevel.ADMIN) {
            CustomAlerts.showPrivilegesAlert();
            return;
        }
        if (getGenresMap().containsValue(genre)) {
            return;
        }
        getGenresMap().put(UUID.randomUUID(), genre);
    }

    public void editGenre(UUID uuid, String newGenre) {
        if (LoggedUser.getInstance().getUser().getAccessLevel() != AccessLevel.ADMIN) {
            CustomAlerts.showPrivilegesAlert();
            return;
        }
        getGenresMap().replace(uuid, newGenre);
    }

    public void removeGenre(UUID uuid) {
        if (LoggedUser.getInstance().getUser().getAccessLevel() != AccessLevel.ADMIN) {
            CustomAlerts.showPrivilegesAlert();
            return;
        }
        getGenresMap().remove(uuid);
    }

    void saveGenres() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/genres");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new HashMap<>(getGenresMap()));
        objectStream.close();
        fileStream.close();
    }

    void loadGenres() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/genres");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        genresMap = FXCollections.observableMap((HashMap<UUID, String>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}