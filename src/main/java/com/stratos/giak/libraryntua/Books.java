package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

//TODO ADD DOCS
public final class Books {
    private static Books instance;
    private transient ObservableList<BookModel> booksList = FXCollections.observableArrayList();

    //TODO ADD DOCS
    public static Books getInstance() {
        if (instance == null) {
            instance = new Books();
            try {
                instance.loadBooks();
            } catch (IOException | ClassNotFoundException e) {
                BookModel[] defaultBooks = {
                        new BookModel(
                                "Piranesi",
                                "Susanna Clarke",
                                "Bloomsbury",
                                "9781635575637",
                                2020,
                                "Fantasy",
                                1),
                        new BookModel(
                                "House of Leaves",
                                "Mark Z. Danielewski",
                                "Pantheon",
                                "9780375703768",
                                2000,
                                "Novel",
                                3),
                        new BookModel(
                                "Pale Fire",
                                "Vladimir Nabokov",
                                "Vintage",
                                "9780679723424",
                                1989,
                                "Novel",
                                4),
                };
                instance.getBooksList().addAll(defaultBooks);
                return instance;
            }
        }
        return instance;
    }

    //TODO ADD DOCS
    public ObservableList<BookModel> getBooksList() {
        return booksList;
    }

    //TODO ADD DOCS
    public BookModel getBook(UUID uuid) {
        return getBooksList().stream().filter(book -> book.getUUID().equals(uuid)).findAny().orElse(null);
    }

    //TODO ADD DOCS
    public void addBook(BookModel book) {
        if (book.getTitle().isBlank()
                || book.getAuthor().isBlank()
                || book.getPublisher().isBlank()
                || book.getISBN().isBlank()
                || book.getGenre() == null)
            throw new IllegalArgumentException("Invalid book details");
        getBooksList().add(book);
    }

    //TODO ADD DOCS
    public void removeBook(BookModel book) {
        Loans.getInstance().removeAllWithBook(book);
        getBooksList().remove(book);
    }

    //TODO ADD DOCS
    public void removeAllWithGenre(GenreModel genre) {
        Iterator<BookModel> iterator = getBooksList().iterator();
        while (iterator.hasNext()) {
            BookModel book = iterator.next();
            if (!book.getGenre().equals(genre)) continue;
            Loans.getInstance().removeAllWithBook(book);
            iterator.remove();
        }
    }

    //TODO ADD DOCS
    public void editBook(BookModel book, String title, String author, String publisher, String ISBN, Integer yearOfPublication, GenreModel genre, Integer copies) {
        if (book == null)
            throw new IllegalArgumentException("Book UUID does not exist");
        if (title != null) book.setTitle(title);
        if (author != null) book.setAuthor(author);
        if (publisher != null) book.setPublisher(publisher);
        if (ISBN != null) book.setISBN(ISBN);
        if (yearOfPublication != null) book.setYearOfPublication(yearOfPublication);
        if (genre != null) book.setGenre(genre);
        if (copies != null) book.setCopies(copies);
    }

    //TODO ADD DOCS
    public void saveBooks() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/books");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getBooksList()));
        objectStream.close();
        fileStream.close();
    }

    //TODO ADD DOCS
    public void loadBooks() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/books");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        booksList = FXCollections.observableArrayList((ArrayList<BookModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
