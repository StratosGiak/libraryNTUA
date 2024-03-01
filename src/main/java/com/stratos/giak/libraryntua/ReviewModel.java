package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.UUID;

public class ReviewModel implements Serializable {
    private final UUID uuid;
    private transient SimpleIntegerProperty rating = new SimpleIntegerProperty(0);
    private transient SimpleStringProperty username = new SimpleStringProperty();
    private transient SimpleStringProperty comment = new SimpleStringProperty();

    public ReviewModel() {
        this.uuid = UUID.randomUUID();
    }

    public ReviewModel(String username, Integer rating, String comment) {
        this();
        this.username.set(username);
        this.rating.set(rating);
        this.comment.set(comment);
    }

    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be in range [1, 5]");
        this.rating.set(rating);
    }

    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public SimpleStringProperty commentProperty() {
        return comment;
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
        stream.writeInt(rating.getValue());
        stream.writeUTF(comment.getValueSafe());
    }

    @Serial
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        username = new SimpleStringProperty(stream.readUTF());
        rating = new SimpleIntegerProperty(stream.readInt());
        comment = new SimpleStringProperty(stream.readUTF());
    }
}
