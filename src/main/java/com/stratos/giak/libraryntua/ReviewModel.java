package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.UUID;

//TODO ADD DOCS
public class ReviewModel implements Serializable {
    private final UUID uuid;
    private transient SimpleIntegerProperty rating = new SimpleIntegerProperty(0);
    private transient SimpleStringProperty username = new SimpleStringProperty();
    private transient SimpleStringProperty comment = new SimpleStringProperty();

    //TODO ADD DOCS
    public ReviewModel(String username, Integer rating, String comment) {
        this.uuid = UUID.randomUUID();
        this.username.set(username);
        this.rating.set(rating);
        this.comment.set(comment);
    }

    //TODO ADD DOCS
    public int getRating() {
        return rating.get();
    }

    //TODO ADD DOCS
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be in range [1, 5]");
        this.rating.set(rating);
    }

    //TODO ADD DOCS
    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    //TODO ADD DOCS
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    //TODO ADD DOCS
    public SimpleStringProperty commentProperty() {
        return comment;
    }

    //TODO ADD DOCS
    public UUID getUUID() {
        return uuid;
    }

    //TODO ADD DOCS
    public String getUsername() {
        return username.get();
    }

    //TODO ADD DOCS
    public void setUsername(String username) {
        this.username.set(username);
    }

    //TODO ADD DOCS
    public String getComment() {
        return comment.get();
    }

    //TODO ADD DOCS
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
