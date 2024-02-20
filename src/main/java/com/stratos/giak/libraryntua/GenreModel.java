package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.UUID;

public class GenreModel implements Serializable {
    private final UUID uuid;
    private transient SimpleStringProperty name = new SimpleStringProperty();

    public GenreModel(UUID uuid, String name) {
        this.uuid = uuid;
        this.name.set(name);
    }

    public GenreModel(String name) {
        this(UUID.randomUUID(), name);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
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
