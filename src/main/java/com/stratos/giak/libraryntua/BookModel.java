package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
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

//TODO ADD DOCS
public class BookModel implements Serializable {
    final DecimalFormat ratingsFormat = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));
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

    //TODO ADD DOCS
    public BookModel(String title, String author, String publisher, String ISBN, Integer yearOfPublication, GenreModel genre, Integer copies) {
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

    //TODO ADD DOCS
    public BookModel(String title, String author, String publisher, String ISBN, Integer yearOfPublication, String genreName, Integer copies) {
        GenreModel genre = Genres.getInstance().getGenresList().stream().filter(gnr -> gnr != null && gnr.getName().equals(genreName)).findAny().orElse(null);
        if (genre == null) {
            genre = new GenreModel(genreName);
            Genres.getInstance().addGenre(genre);
        }
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

    //TODO ADD DOCS
    public SimpleStringProperty titleProperty() {
        return title;
    }

    //TODO ADD DOCS
    public SimpleStringProperty authorProperty() {
        return author;
    }

    //TODO ADD DOCS
    public SimpleStringProperty publisherProperty() {
        return publisher;
    }

    //TODO ADD DOCS
    public SimpleStringProperty ISBNProperty() {
        return ISBN;
    }

    //TODO ADD DOCS
    public SimpleObjectProperty<GenreModel> genreProperty() {
        return genre;
    }

    //TODO ADD DOCS
    public SimpleIntegerProperty yearOfPublicationProperty() {
        return yearOfPublication;
    }

    //TODO ADD DOCS
    public SimpleIntegerProperty ratingsSumProperty() {
        return ratingsSum;
    }

    //TODO ADD DOCS
    public SimpleIntegerProperty ratingsCountProperty() {
        return ratingsCount;
    }

    //TODO ADD DOCS
    public ObservableList<ReviewModel> getReviews() {
        return reviews;
    }

    //TODO ADD DOCS
    public SimpleIntegerProperty copiesProperty() {
        return copies;
    }

    //TODO ADD DOCS
    public String getRating() {
        return rating.get();
    }

    //TODO ADD DOCS
    public void setRating(String rating) {
        this.rating.set(rating);
    }

    //TODO ADD DOCS
    public SimpleStringProperty ratingProperty() {
        return rating;
    }

    //TODO ADD DOCS
    public UUID getUUID() {
        return uuid;
    }

    //TODO ADD DOCS
    public String getTitle() {
        return title.get();
    }

    //TODO ADD DOCS
    public void setTitle(String title) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title cannot be blank");
        this.title.set(title);
    }

    //TODO ADD DOCS
    public String getAuthor() {
        return author.get();
    }

    //TODO ADD DOCS
    public void setAuthor(String author) {
        if (author == null || author.isBlank()) throw new IllegalArgumentException("Author cannot be blank");
        this.author.set(author);
    }

    //TODO ADD DOCS
    public String getPublisher() {
        return publisher.get();
    }

    //TODO ADD DOCS
    public void setPublisher(String publisher) {
        if (publisher == null || publisher.isBlank()) throw new IllegalArgumentException("Publisher cannot be blank");
        this.publisher.set(publisher);
    }

    //TODO ADD DOCS
    public String getISBN() {
        return ISBN.get();
    }

    //TODO ADD DOCS
    public void setISBN(String ISBN) {
        if (ISBN == null || ISBN.isBlank()) throw new IllegalArgumentException("ISBN cannot be blank");
        this.ISBN.set(ISBN);
    }

    //TODO ADD DOCS
    public int getYearOfPublication() {
        return yearOfPublication.get();
    }

    //TODO ADD DOCS
    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication.set(yearOfPublication);
    }

    //TODO ADD DOCS
    public GenreModel getGenre() {
        return genre.get();
    }

    //TODO ADD DOCS
    public void setGenre(GenreModel genre) {
        if (genre == null) throw new IllegalArgumentException("Genre UUID not found");
        this.genre.set(genre);
    }

    //TODO ADD DOCS
    public int getCopies() {
        return copies.get();
    }

    //TODO ADD DOCS
    public void setCopies(int copies) {
        if (copies < 0) throw new IllegalArgumentException("Number of copies cannot be negative");
        this.copies.set(copies);
    }

    //TODO ADD DOCS
    public int getRatingsSum() {
        return ratingsSum.get();
    }

    //TODO ADD DOCS
    public void setRatingsSum(int ratingsSum) {
        if (ratingsSum < 1 || ratingsSum > 5) throw new IllegalArgumentException("Rating must be in range [1, 5]");
        this.ratingsSum.set(ratingsSum);
    }

    //TODO ADD DOCS
    public int getRatingsCount() {
        return ratingsCount.get();
    }

    //TODO ADD DOCS
    public void setRatingsCount(int ratingsCount) {
        if (ratingsCount < 0) throw new IllegalArgumentException("Ratings count cannot be negative");
        this.ratingsCount.set(ratingsCount);
    }

    //TODO ADD DOCS
    public void addReview(ReviewModel review) {
        if (review == null || review.getRating() == 0 && (review.getComment() == null || review.getComment().isBlank()))
            return;
        if (review.getRating() != 0) {
            ratingsCountProperty().set(ratingsCountProperty().get() + 1);
            ratingsSumProperty().set(ratingsSumProperty().get() + review.getRating());
        }
        getReviews().add(review);
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

    //TODO ADD DOCS
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
