package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.UUID;

//TODO ADD DOCS
public class GenreModel implements Serializable {
    private final UUID uuid;
    private transient SimpleStringProperty name = new SimpleStringProperty();

    //TODO ADD DOCS
    public GenreModel(UUID uuid, String name) {
        this.uuid = uuid;
        this.name.set(name);
    }

    //TODO ADD DOCS
    public GenreModel(String name) {
        this(UUID.randomUUID(), name);
    }

    //TODO ADD DOCS
    public String getName() {
        return name.get();
    }

    //TODO ADD DOCS
    public void setName(String name) {
        this.name.set(name);
    }

    //TODO ADD DOCS
    public SimpleStringProperty nameProperty() {
        return name;
    }

    //TODO ADD DOCS
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

    //TODO ADD DOCS
    public String toString() {
        return name.getValueSafe();
    }
}
