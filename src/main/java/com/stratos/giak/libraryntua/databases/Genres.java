package com.stratos.giak.libraryntua.databases;


import com.stratos.giak.libraryntua.models.GenreModel;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public final class Genres {
    private static Genres instance;
    private transient ObservableList<GenreModel> genresList = FXCollections.observableArrayList(genre -> new Observable[]{genre.nameProperty()});

    private Genres() {
    }

    /**
     * Returns a singleton instance of the Genres database object.
     * The Genres object manages all operations that concern the saved genres.
     *
     * @return a Genres instance
     * @see GenreModel
     */
    public static Genres getInstance() {
        if (instance == null) {
            instance = new Genres();
            try {
                instance.loadGenres();
            } catch (IOException ignored) {
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Serialized book file is not of type List<GenreModel>");
            }
        }
        return instance;
    }

    /**
     * @return an observable list of all genres, represented by {@link GenreModel} objects
     */
    public ObservableList<GenreModel> getGenresList() {
        return genresList;
    }

    /**
     * Returns a genre with the given UUID.
     * This is only used for (de)serialization of books.
     *
     * @param uuid the {@link UUID} of the genre
     * @return a {@link GenreModel} object with the given UUID, or null if no such genre exists
     */
    public GenreModel getGenre(UUID uuid) {
        return getGenresList().stream().filter(genre -> genre != null && genre.getUUID().equals(uuid)).findAny().orElse(null);
    }

    /**
     * Adds the given genre to the list of genres.
     * Does not check whether the genre is already in the list.
     *
     * @param genre the genre to be added
     */
    public void addGenre(GenreModel genre) {
        getGenresList().add(genre);
    }

    /**
     * Removes the given genre from the database, if it exists.
     * Also removes all books that belong to that genre.
     *
     * @param genre the genre to be removed
     */
    public void removeGenre(GenreModel genre) {
        Books.getInstance().removeAllWithGenre(genre);
        getGenresList().remove(genre);
    }

    /**
     * Serializes the list of genres.
     * Saves the list of genres as a file named "genres",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException if there is any error writing to the file
     */
    public void saveGenres() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/genres");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getGenresList()));
        objectStream.close();
        fileStream.close();
    }

    /**
     * Deserializes the list of genres.
     * Loads the list of genres from a file named "genres",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException            if there is any error reading the file
     * @throws ClassNotFoundException if the serialized object is not a list of {@link GenreModel}
     */
    public void loadGenres() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/genres");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        genresList = FXCollections.observableArrayList(genre -> new Observable[]{genre.nameProperty()});
        genresList.setAll((ArrayList<GenreModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}