package com.stratos.giak.libraryntua;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public final class Genres implements Serializable {
    private static Genres instance;
    private transient ObservableList<GenreModel> genresList = FXCollections.observableArrayList();

    public static Genres getInstance() {
        if (instance == null) {
            instance = new Genres();
            try {
                instance.loadGenres();
            } catch (IOException | ClassNotFoundException e) {
                GenreModel[] defaultGenres = {
                        null,
                        new GenreModel("Fantasy"),
                        new GenreModel("Science Fiction"),
                        new GenreModel("Novel"),
                        new GenreModel("Poetry")
                };
                instance.getGenresList().addAll(defaultGenres);
                return instance;
            }
        }
        return instance;
    }

    public ObservableList<GenreModel> getGenresList() {
        return genresList;
    }

    public GenreModel getGenre(UUID uuid) {
        return getGenresList().stream().filter(genre -> genre != null && genre.getUUID().equals(uuid)).findAny().orElse(null);
    }

    public void addGenre(GenreModel genre) {
        if (LoggedUser.getInstance().getUser().getAccessLevel() != AccessLevel.ADMIN) {
            CustomAlerts.showPrivilegesAlert();
            return;
        }
        getGenresList().add(genre);
    }

    public void editGenre(GenreModel genre, String name) {
        if (LoggedUser.getInstance().getUser().getAccessLevel() != AccessLevel.ADMIN) {
            CustomAlerts.showPrivilegesAlert();
            return;
        }
        if (genre == null)
            throw new IllegalArgumentException("Genre UUID not found");
        if (name != null) genre.setName(name);
    }

    public void removeGenre(GenreModel genre) {
        if (LoggedUser.getInstance().getUser().getAccessLevel() != AccessLevel.ADMIN) {
            CustomAlerts.showPrivilegesAlert();
            return;
        }
        Books.getInstance().removeAllWithGenre(genre);
        getGenresList().remove(genre);
    }

    void saveGenres() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/genres");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getGenresList()));
        objectStream.close();
        fileStream.close();
    }

    void loadGenres() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/genres");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        genresList = FXCollections.observableArrayList((ArrayList<GenreModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}