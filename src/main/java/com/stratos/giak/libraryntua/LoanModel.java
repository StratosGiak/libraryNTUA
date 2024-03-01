package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.time.LocalDate;
import java.util.UUID;

public class LoanModel implements Serializable {
    private UUID uuid;
    private transient SimpleObjectProperty<BookModel> book = new SimpleObjectProperty<>();
    private transient SimpleObjectProperty<UserModel> user = new SimpleObjectProperty<>();
    private LocalDate loanDate;
    private int loanLength;
    private transient SimpleIntegerProperty rating = new SimpleIntegerProperty();
    private transient SimpleStringProperty comment = new SimpleStringProperty();

    public LoanModel(BookModel book, UserModel user, LocalDate loanDate, Integer loanLength) {
        this.uuid = UUID.randomUUID();
        this.book.set(book);
        this.user.set(user);
        this.loanDate = loanDate;
        this.loanLength = loanLength;
    }

    public SimpleObjectProperty<BookModel> bookProperty() {
        return book;
    }

    public SimpleObjectProperty<UserModel> userProperty() {
        return user;
    }

    public UUID getUuid() {
        return uuid;
    }

    public BookModel getBook() {
        return book.get();
    }

    public UserModel getUser() {
        return user.get();
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
        stream.writeObject(book.getValue().getUUID());
        stream.writeObject(user.getValue().getUUID());
        stream.writeInt(rating.getValue());
        stream.writeUTF(comment.getValueSafe());
    }

    @Serial
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        book = new SimpleObjectProperty<>(Books.getInstance().getBook((UUID) stream.readObject()));
        user = new SimpleObjectProperty<>(Users.getInstance().getUser((UUID) stream.readObject()));
        rating = new SimpleIntegerProperty(stream.readInt());
        comment = new SimpleStringProperty(stream.readUTF());
    }
}
