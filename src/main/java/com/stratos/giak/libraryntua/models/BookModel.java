package com.stratos.giak.libraryntua.models;

import com.stratos.giak.libraryntua.databases.Genres;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

public class BookModel implements Serializable {
    final private DecimalFormat ratingsFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
    private final UUID uuid;
    private transient SimpleStringProperty title = new SimpleStringProperty();
    private transient SimpleStringProperty author = new SimpleStringProperty();
    private transient SimpleStringProperty publisher = new SimpleStringProperty();
    private transient SimpleStringProperty ISBN = new SimpleStringProperty();
    private transient SimpleObjectProperty<GenreModel> genre = new SimpleObjectProperty<>();
    private transient SimpleIntegerProperty yearOfPublication = new SimpleIntegerProperty();
    private transient SimpleIntegerProperty copies = new SimpleIntegerProperty();
    private transient SimpleIntegerProperty ratingsSum = new SimpleIntegerProperty();
    private transient SimpleIntegerProperty ratingsCount = new SimpleIntegerProperty();
    private transient SimpleStringProperty rating = new SimpleStringProperty();
    private transient ObservableList<ReviewModel> reviews = FXCollections.observableArrayList();

    /**
     * Constructs an object representing a book with the given info.
     * Aside from the constructor parameters the book info also includes a list of all past reviews.
     *
     * @param title             the title of the book
     * @param author            the author of the book
     * @param publisher         the publisher of the book
     * @param ISBN              the ISBN of the book
     * @param yearOfPublication the year of publication of the book
     * @param genre             the genre of the book
     * @param copies            the number of available copies of the book
     * @throws IllegalArgumentException if any field is null or blank, or if {@code copies} is negative
     */
    public BookModel(String title, String author, String publisher, String ISBN, Integer yearOfPublication, GenreModel genre, Integer copies) {
        if (title == null || title.isBlank() ||
                author == null || author.isBlank() ||
                publisher == null || publisher.isBlank() ||
                ISBN == null || ISBN.isBlank() ||
                yearOfPublication == null ||
                genre == null ||
                copies == null || copies < 0
        ) throw new IllegalArgumentException("Invalid book details");
        this.uuid = UUID.randomUUID();
        this.title.set(title);
        this.author.set(author);
        this.publisher.set(publisher);
        this.ISBN.set(ISBN);
        this.genre.set(genre);
        this.yearOfPublication.set(yearOfPublication);
        this.copies.set(copies);
        this.ratingsSum.set(0);
        this.ratingsCount.set(0);
        rating.bind(Bindings.createStringBinding(() -> ratingsCount.get() != 0 ? ratingsFormat.format((float) ratingsSum.get() / ratingsCount.get()) + " (" + ratingsCount.get() + ")" : "–", ratingsSum, ratingsCount));
    }

    /**
     * The property describing the title of the book
     *
     * @see #getTitle
     * @see #setTitle(String)
     */
    public SimpleStringProperty titleProperty() {
        return title;
    }

    /**
     * The property describing the author of the book
     *
     * @see #getAuthor
     * @see #setAuthor(String)
     */
    public SimpleStringProperty authorProperty() {
        return author;
    }

    /**
     * The property describing the publisher of the book
     *
     * @see #getPublisher
     * @see #setPublisher(String)
     */
    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    /**
     * The property describing the ISBN of the book
     *
     * @see #getISBN
     * @see #setISBN(String)
     */
    public SimpleStringProperty ISBNProperty() {
        return ISBN;
    }

    /**
     * The property describing the genre of the book
     *
     * @see #getGenre
     * @see #setGenre(GenreModel)
     */
    public SimpleObjectProperty<GenreModel> genreProperty() {
        return genre;
    }

    /**
     * The property describing the year of publication of the book
     *
     * @see #getYearOfPublication
     * @see #setYearOfPublication(int)
     */
    public SimpleIntegerProperty yearOfPublicationProperty() {
        return yearOfPublication;
    }

    /**
     * The property describing the sum of ratings for the book
     *
     * @see #getRatingsSum
     * @see #setRatingsSum(int)
     */
    public SimpleIntegerProperty ratingsSumProperty() {
        return ratingsSum;
    }

    /**
     * The property describing the number of ratings for the book
     *
     * @see #getRatingsCount
     * @see #setRatingsCount(int)
     */
    public SimpleIntegerProperty ratingsCountProperty() {
        return ratingsCount;
    }

    /**
     * Gets an observable list containing all the submitted user reviews for the book
     */
    public ObservableList<ReviewModel> getReviews() {
        return reviews;
    }

    /**
     * The property describing the number of available copies of the book
     *
     * @see #getCopies
     * @see #setCopies(int)
     */
    public SimpleIntegerProperty copiesProperty() {
        return copies;
    }

    /**
     * Gets a string representing the average rating of the book along with the number of ratings
     */
    public String getRating() {
        return rating.get();
    }

    /**
     * The property describing the overall rating of the book as a single string for convenience.
     * <p>
     * If the number of ratings is zero the string is "(–)".
     * <p>
     * If the number of ratings is non-zero the string has the format "S (C)"
     * where S is the average of all ratings and C is the number of ratings.
     * S is a decimal number truncated to two decimal places
     *
     * @see #getCopies
     * @see #setCopies(int)
     */
    public ReadOnlyStringProperty ratingProperty() {
        return rating;
    }

    /**
     * Gets the UUID of the book. Only used for (de)serialization.
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the value of the title property of the book
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * Sets the value of the title property of the book.
     *
     * @throws IllegalArgumentException if the given value is null or empty
     */
    public void setTitle(String title) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be blank");
        this.title.set(title);
    }

    /**
     * Gets the value of the author property of the book
     */
    public String getAuthor() {
        return author.get();
    }

    /**
     * Sets the value of the author property of the book.
     *
     * @throws IllegalArgumentException if the given value is null or empty
     */
    public void setAuthor(String author) {
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author cannot be blank");
        this.author.set(author);
    }

    /**
     * Gets the value of the publisher property of the book
     */
    public String getPublisher() {
        return publisher.get();
    }

    /**
     * Sets the value of the publisher property of the book.
     *
     * @throws IllegalArgumentException if the given value is null or empty
     */
    public void setPublisher(String publisher) {
        if (publisher == null || publisher.isBlank()) throw new IllegalArgumentException("Publisher cannot be blank");
        this.publisher.set(publisher);
    }

    /**
     * Gets the value of the ISBN property of the book
     */
    public String getISBN() {
        return ISBN.get();
    }

    /**
     * Sets the value of the ISBN property of the book.
     *
     * @throws IllegalArgumentException if the given value is null or empty
     */
    public void setISBN(String ISBN) {
        if (ISBN == null || ISBN.isBlank()) throw new IllegalArgumentException("ISBN cannot be blank");
        this.ISBN.set(ISBN);
    }

    /**
     * Gets the value of the year of publication property of the book
     */
    public int getYearOfPublication() {
        return yearOfPublication.get();
    }

    /**
     * Sets the value of the year of publication property of the book.
     */
    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication.set(yearOfPublication);
    }

    /**
     * Gets the value of the genre property of the book
     */
    public GenreModel getGenre() {
        return genre.get();
    }

    /**
     * Sets the value of the genre property of the book.
     *
     * @throws IllegalArgumentException if the given value is null
     */
    public void setGenre(GenreModel genre) {
        if (genre == null) throw new IllegalArgumentException("Genre cannot be null");
        this.genre.set(genre);
    }

    /**
     * Gets the value of the copies property of the book
     */
    public int getCopies() {
        return copies.get();
    }

    /**
     * Sets the value of the copies property of the book.
     *
     * @throws IllegalArgumentException if the given value is negative
     */
    public void setCopies(int copies) {
        if (copies < 0) throw new IllegalArgumentException("Number of copies cannot be negative");
        this.copies.set(copies);
    }

    /**
     * Gets the value of the ratings sum property of the book
     */
    public int getRatingsSum() {
        return ratingsSum.get();
    }

    /**
     * Sets the value of the ratings sum property of the book.
     *
     * @throws IllegalArgumentException if the given value is negative
     */
    public void setRatingsSum(int ratingsSum) {
        if (ratingsSum < 0) throw new IllegalArgumentException("Rating sum cannot be negative");
        this.ratingsSum.set(ratingsSum);
    }

    /**
     * Gets the value of the ratings count property of the book.
     */
    public int getRatingsCount() {
        return ratingsCount.get();
    }

    /**
     * Sets the value of the ratings count property of the book.
     *
     * @throws IllegalArgumentException if the given value is negative
     */
    public void setRatingsCount(int ratingsCount) {
        if (ratingsCount < 0) throw new IllegalArgumentException("Rating count cannot be negative");
        this.ratingsCount.set(ratingsCount);
    }

    /**
     * Adds a user review to the book.
     * Only adds the review if it is not empty (either a rating or comments)
     *
     * @param review the review to be added
     */
    public void addReview(ReviewModel review) {
        if (review == null || review.getRating() == 0 && (review.getComment() == null || review.getComment().isBlank()))
            return;
        if (review.getRating() != 0) {
            ratingsCountProperty().set(ratingsCountProperty().get() + 1);
            ratingsSumProperty().set(ratingsSumProperty().get() + review.getRating());
        }
        getReviews().add(review);
    }

    /**
     * Edits all the details of the book.
     * Skips modifying any details that are invalid.
     * <p>
     * This is a convenience method to avoid calling all the setters each time a book is edited.
     */
    public void editBook(String title, String author, String publisher, String ISBN, Integer yearOfPublication, GenreModel genre, Integer copies) {
        if (title != null && !title.isBlank()) setTitle(title);
        if (author != null && !author.isBlank()) setAuthor(author);
        if (publisher != null && !publisher.isBlank()) setPublisher(publisher);
        if (ISBN != null && !ISBN.isBlank()) setISBN(ISBN);
        if (yearOfPublication != null) setYearOfPublication(yearOfPublication);
        if (genre != null) setGenre(genre);
        if (copies != null && copies >= 0) setCopies(copies);
    }

    @Serial
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        stream.writeUTF(title.getValueSafe());
        stream.writeUTF(author.getValueSafe());
        stream.writeUTF(publisher.getValueSafe());
        stream.writeUTF(ISBN.getValueSafe());
        stream.writeObject(genre.getValue().getUUID());
        stream.writeInt(yearOfPublication.getValue());
        stream.writeInt(copies.getValue());
        stream.writeObject(new ArrayList<>(reviews));
        stream.writeInt(ratingsSum.getValue());
        stream.writeInt(ratingsCount.getValue());
    }

    @Serial
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        title = new SimpleStringProperty(stream.readUTF());
        author = new SimpleStringProperty(stream.readUTF());
        publisher = new SimpleStringProperty(stream.readUTF());
        ISBN = new SimpleStringProperty(stream.readUTF());
        genre = new SimpleObjectProperty<>(Genres.getInstance().getGenre((UUID) stream.readObject()));
        yearOfPublication = new SimpleIntegerProperty(stream.readInt());
        copies = new SimpleIntegerProperty(stream.readInt());
        reviews = FXCollections.observableArrayList((ArrayList<ReviewModel>) stream.readObject());
        ratingsSum = new SimpleIntegerProperty(stream.readInt());
        ratingsCount = new SimpleIntegerProperty(stream.readInt());
        rating = new SimpleStringProperty();
        rating.bind(Bindings.createStringBinding(() -> ratingsCount.get() != 0 ? ratingsFormat.format((float) ratingsSum.get() / ratingsCount.get()) + " (" + ratingsCount.get() + ")" : "–", ratingsSum, ratingsCount));
    }

    public String toString() {
        return String.format("""
                        Title: %s
                        Author: %s
                        Publisher: %s
                        Year of publication: %d
                        Genre: %s
                        ISBN: %s
                        Ratings: %s
                        Copies: %d""",
                this.getTitle(),
                this.getAuthor(),
                this.getPublisher(),
                this.getYearOfPublication(),
                this.getGenre(),
                this.getISBN(),
                this.getRating(),
                this.getCopies());
    }
}
