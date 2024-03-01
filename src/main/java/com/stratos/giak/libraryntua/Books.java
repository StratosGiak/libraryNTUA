package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public final class Books {
    private static Books instance;
    private transient ObservableList<BookModel> booksList = FXCollections.observableArrayList();

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

    ObservableList<BookModel> getBooksList() {
        return booksList;
    }

    BookModel getBook(UUID uuid) {
        return getBooksList().stream().filter(book -> book.getUUID().equals(uuid)).findAny().orElse(null);
    }

    void addBook(BookModel book) {
        if (book.getTitle().isBlank()
                || book.getAuthor().isBlank()
                || book.getPublisher().isBlank()
                || book.getISBN().isBlank()
                || book.getGenre() == null)
            throw new IllegalArgumentException("Invalid book details");
        getBooksList().add(book);
    }

    void removeBook(BookModel book) {
        Loans.getInstance().removeAllWithBook(book);
        getBooksList().remove(book);
    }

    void removeAllWithGenre(GenreModel genre) {
        Iterator<BookModel> iterator = getBooksList().iterator();
        while (iterator.hasNext()) {
            BookModel book = iterator.next();
            if (!book.getGenre().equals(genre)) continue;
            Loans.getInstance().removeAllWithBook(book);
            iterator.remove();
        }
    }

    void editBook(BookModel book, String title, String author, String publisher, String ISBN, Integer yearOfPublication, GenreModel genre, Integer copies) {
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

    void saveBooks() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/books");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getBooksList()));
        objectStream.close();
        fileStream.close();
    }

    void loadBooks() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/books");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        booksList = FXCollections.observableArrayList((ArrayList<BookModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
