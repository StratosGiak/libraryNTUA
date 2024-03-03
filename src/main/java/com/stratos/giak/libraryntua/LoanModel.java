package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.time.LocalDate;
import java.util.UUID;

//TODO ADD DOCS
public class LoanModel implements Serializable {
    private final UUID uuid;
    private final LocalDate loanDate;
    private final int loanLength;
    private transient SimpleObjectProperty<BookModel> book = new SimpleObjectProperty<>();
    private transient SimpleObjectProperty<UserModel> user = new SimpleObjectProperty<>();
    private transient SimpleIntegerProperty rating = new SimpleIntegerProperty();
    private transient SimpleStringProperty comment = new SimpleStringProperty();

    //TODO ADD DOCS
    public LoanModel(BookModel book, UserModel user, LocalDate loanDate, Integer loanLength) {
        this.uuid = UUID.randomUUID();
        this.book.set(book);
        this.user.set(user);
        this.loanDate = loanDate;
        this.loanLength = loanLength;
    }

    //TODO ADD DOCS
    public SimpleObjectProperty<BookModel> bookProperty() {
        return book;
    }

    //TODO ADD DOCS
    public SimpleObjectProperty<UserModel> userProperty() {
        return user;
    }

    //TODO ADD DOCS
    public UUID getUUID() {
        return uuid;
    }

    //TODO ADD DOCS
    public BookModel getBook() {
        return book.get();
    }

    //TODO ADD DOCS
    public UserModel getUser() {
        return user.get();
    }

    //TODO ADD DOCS
    public LocalDate getLoanDate() {
        return loanDate;
    }

    //TODO ADD DOCS
    public int getLoanLength() {
        return loanLength;
    }

    //TODO ADD DOCS
    public int getRating() {
        return rating.get();
    }

    //TODO ADD DOCS
    public void setRating(int rating) {
        this.rating.set(rating);
    }

    //TODO ADD DOCS
    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    //TODO ADD DOCS
    public String getComment() {
        return comment.get();
    }

    //TODO ADD DOCS
    public void setComment(String comment) {
        this.comment.set(comment);
    }

    //TODO ADD DOCS
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
