package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.time.LocalDate;
import java.util.UUID;

public class LoanModel implements Serializable {
    private UUID uuid;
    private UUID uuidBook;
    private UUID uuidUser;
    private LocalDate loanDate;
    private int loanLength;
    private transient SimpleIntegerProperty rating = new SimpleIntegerProperty();
    private transient SimpleStringProperty comment = new SimpleStringProperty();

    public LoanModel(UUID uuid, UUID uuidBook, UUID uuidUser, LocalDate loanDate, Integer loanLength) {
        this.uuid = uuid;
        this.uuidBook = uuidBook;
        this.uuidUser = uuidUser;
        this.loanDate = loanDate;
        this.loanLength = loanLength;
    }

    public LoanModel(UUID uuidBook, UUID uuidUser, LocalDate loanDate, Integer loanLength) {
        this(UUID.randomUUID(), uuidBook, uuidUser, loanDate, loanLength);
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getUuidBook() {
        return uuidBook;
    }

    public UUID getUuidUser() {
        return uuidUser;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public int getLoanLength() {
        return loanLength;
    }

    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    @Serial
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeInt(rating.getValue());
        stream.writeUTF(comment.getValueSafe());
    }

    @Serial
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        rating = new SimpleIntegerProperty(stream.readInt());
        comment = new SimpleStringProperty(stream.readUTF());
    }
}
