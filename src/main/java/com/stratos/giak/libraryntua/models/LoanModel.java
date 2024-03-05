package com.stratos.giak.libraryntua.models;

import com.stratos.giak.libraryntua.databases.Books;
import com.stratos.giak.libraryntua.databases.Users;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.*;
import java.time.LocalDate;
import java.util.UUID;

public class LoanModel implements Serializable {
    private final UUID uuid;
    private final LocalDate loanDate;
    private final int loanLength;
    private transient SimpleObjectProperty<BookModel> book = new SimpleObjectProperty<>();
    private transient SimpleObjectProperty<UserModel> user = new SimpleObjectProperty<>();
    private transient SimpleIntegerProperty rating = new SimpleIntegerProperty();
    private transient SimpleStringProperty comment = new SimpleStringProperty();

    /**
     * Constructs an object representing a book loan.
     * <p>
     * Aside from the info included in the constructor, the object also holds the
     * rating and comments the user has made about the loaned book.
     *
     * @param book       the book that was loaned
     * @param user       the user who requested the loan
     * @param loanDate   the date the loan was requested
     * @param loanLength the required return date of the book
     */
    public LoanModel(BookModel book, UserModel user, LocalDate loanDate, Integer loanLength) {
        if (book == null || user == null || loanDate == null || loanLength == null)
            throw new IllegalArgumentException("Invalid loan details");
        this.uuid = UUID.randomUUID();
        this.book.set(book);
        this.user.set(user);
        this.loanDate = loanDate;
        this.loanLength = loanLength;
    }

    /**
     * The property describing the loaned book.
     *
     * @see #getBook
     */
    public ReadOnlyObjectProperty<BookModel> bookProperty() {
        return book;
    }

    /**
     * The property describing the user who requested the loan.
     *
     * @see #getUser
     */
    public ReadOnlyObjectProperty<UserModel> userProperty() {
        return user;
    }

    /**
     * Gets the UUID of the loan.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the value of the book property of the loan
     */
    public BookModel getBook() {
        return book.get();
    }

    /**
     * Gets the value of the user property of the loan
     */
    public UserModel getUser() {
        return user.get();
    }

    /**
     * Gets the value of the loan date property of the loan
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * Gets the value of the loan length property of the loan
     */
    public int getLoanLength() {
        return loanLength;
    }

    /**
     * Gets the value of the rating property of the loan
     */
    public int getRating() {
        return rating.get();
    }

    /**
     * Sets the value of the rating property of the loan
     */
    public void setRating(int rating) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be in the range [1, 5]");
        this.rating.set(rating);
    }

    /**
     * The property describing the rating in the user review associated with this loan.
     *
     * @see #getRating
     * @see #setRating(int)
     */
    public SimpleIntegerProperty ratingProperty() {
        return rating;
    }

    /**
     * Gets the value of the comment property of the loan
     */
    public String getComment() {
        return comment.get();
    }

    /**
     * Sets the value of the comment property of the loan.
     */
    public void setComment(String comment) {
        this.comment.set(comment);
    }

    /**
     * The property describing the comments in the user review associated with this loan.
     *
     * @see #getComment
     * @see #setComment(String)
     */
    public SimpleStringProperty commentProperty() {
        return comment;
    }

    /**
     * Edits the rating and comments of the user review associated with this loan
     * Skips modifying any null parameters
     */
    public void editLoan(Integer rating, String comment) {
        if (rating != null) setRating(rating);
        if (comment != null) setComment(comment);
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
