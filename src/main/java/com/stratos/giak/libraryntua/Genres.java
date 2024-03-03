package com.stratos.giak.libraryntua;


import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

//TODO ADD DOCS
public final class Genres {
    private static Genres instance;
    private transient ObservableList<GenreModel> genresList = FXCollections.observableArrayList(genre -> new Observable[]{genre.nameProperty()});

    //TODO ADD DOCS
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

    //TODO ADD DOCS
    public ObservableList<GenreModel> getGenresList() {
        return genresList;
    }

    //TODO ADD DOCS
    public GenreModel getGenre(UUID uuid) {
        return getGenresList().stream().filter(genre -> genre != null && genre.getUUID().equals(uuid)).findAny().orElse(null);
    }

    //TODO ADD DOCS
    public void addGenre(GenreModel genre) {
        getGenresList().add(genre);
    }

    //TODO ADD DOCS
    public void editGenre(GenreModel genre, String name) {
        if (genre == null)
            throw new IllegalArgumentException("Genre UUID not found");
        if (name != null) genre.setName(name);
    }

    //TODO ADD DOCS
    public void removeGenre(GenreModel genre) {
        Books.getInstance().removeAllWithGenre(genre);
        getGenresList().remove(genre);
    }

    //TODO ADD DOCS
    public void saveGenres() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/genres");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getGenresList()));
        objectStream.close();
        fileStream.close();
    }

    public void loadGenres() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/genres");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        genresList = FXCollections.observableArrayList(genre -> new Observable[]{genre.nameProperty()});
        genresList.setAll((ArrayList<GenreModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}