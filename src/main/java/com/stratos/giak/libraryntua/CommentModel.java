package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.UUID;

public class CommentModel implements Serializable {
    private final UUID uuid;
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty comment = new SimpleStringProperty();

    public CommentModel() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    @Serial
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeUTF(username.getValueSafe());
        stream.writeUTF(comment.getValueSafe());
    }

    @Serial
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        username = new SimpleStringProperty(stream.readUTF());
        comment = new SimpleStringProperty(stream.readUTF());
    }
}
