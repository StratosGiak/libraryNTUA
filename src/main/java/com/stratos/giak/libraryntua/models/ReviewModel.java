package com.stratos.giak.libraryntua.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.util.UUID;

public class ReviewModel implements Serializable {
    private final UUID uuid;
    private transient SimpleIntegerProperty rating = new SimpleIntegerProperty(0);
    private transient SimpleStringProperty username = new SimpleStringProperty();
    private transient SimpleStringProperty comment = new SimpleStringProperty();

    /**
     * Constructs an object representing a book review made by a user.
     * The review includes the username of the user who made the review,
     * the comments the user added, and the rating that was given
     *
     * @param username the username of the user who authored the review
     * @param rating   the rating the user gave to the book, an integer in the range [1, 5]
     * @param comment  the comments the user included in the review
     */
    public ReviewModel(String username, Integer rating, String comment) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be blank");
        if (rating == null || rating < 1 || rating > 5)
            throw new IllegalArgumentException("Rating must be in the range [1, 5]");
        this.uuid = UUID.randomUUID();
        this.username.set(username);
        this.rating.set(rating);
        this.comment.set(comment);
    }

    /**
     * Gets the value of the rating property of the review.
     */
    public int getRating() {
        return rating.get();
    }

    /**
     * Sets the value of the rating property of the review.
     *
     * @throws IllegalArgumentException if the given value is not in the range [1, 5]
     */
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be in the range [1, 5]");
        this.rating.set(rating);
    }

    /**
     * The property describing the rating in the review.
     *
     * @see #getRating
     * @see #setRating(int)
     */
    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    /**
     * The property describing the username of the user who made the review.
     *
     * @see #getUsername
     * @see #setUsername(String)
     */
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    /**
     * The property describing the comments that were included in the review.
     *
     * @see #getComment
     * @see #setComment(String)
     */
    public SimpleStringProperty commentProperty() {
        return comment;
    }

    /**
     * Gets the UUID of the review.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the value of the username property of the review.
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * Sets the value of the username property of the review.
     *
     * @throws IllegalArgumentException if the given value is null or empty
     */
    public void setUsername(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username cannot be blank");
        this.username.set(username);
    }

    /**
     * Gets the value of the name property of the genre.
     */
    public String getComment() {
        return comment.get();
    }

    /**
     * Sets the value of the comment property of the review.
     */
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
