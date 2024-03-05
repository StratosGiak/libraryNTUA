package com.stratos.giak.libraryntua.models;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.UUID;

public class GenreModel implements Serializable {
    private final UUID uuid;
    private transient SimpleStringProperty name = new SimpleStringProperty();

    /**
     * Creates a new genre with the given name.
     *
     * @param name the name of the genre
     * @throws IllegalArgumentException if the name is null or blank
     */
    public GenreModel(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Genre name cannot be blank");
        this.uuid = UUID.randomUUID();
        this.name.set(name);
    }

    /**
     * Gets the value of the name property of the genre.
     */
    public String getName() {
        return name.get();
    }

    /**
     * Sets the value of the author property of the book.
     *
     * @throws IllegalArgumentException if the given value is null or empty
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Genre name cannot be blank");
        this.name.set(name);
    }

    /**
     * The property describing the name of the genre.
     *
     * @see #getName
     * @see #setName(String)
     */
    public SimpleStringProperty nameProperty() {
        return name;
    }

    /**
     * Gets the UUID of the genre. Only used for (de)serialization.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Edits the name of the given genre.
     * Leaves the genre unchanged if the given name is null or empty
     *
     * @param name the new name of the genre
     */
    public void editGenre(String name) {
        if (name != null && !name.isBlank()) setName(name);
    }

    @Serial
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeUTF(name.getValueSafe());
    }

    @Serial
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        name = new SimpleStringProperty(stream.readUTF());
    }
    
    public String toString() {
        return name.getValueSafe();
    }
}
