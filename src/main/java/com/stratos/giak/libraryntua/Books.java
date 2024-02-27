package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public final class Books {
    private static Books instance;
    private transient ObservableList<BookModel> allBooksList = FXCollections.observableArrayList();

    public static Books getInstance() {
        if (instance == null) {
            instance = new Books();
            try {
                instance.loadBooks();
            } catch (IOException | ClassNotFoundException e) {
                return instance;
            }
        }
        return instance;
    }

    ObservableList<BookModel> getAllBooksList() {
        return allBooksList;
    }

    BookModel getBook(UUID uuid) {
        return getAllBooksList().stream().filter(book -> book.getUUID().equals(uuid)).findAny().orElse(null);
    }

    void addBook(BookModel book) {
        if (book.getTitle().isBlank()
                || book.getAuthor().isBlank()
                || book.getPublisher().isBlank()
                || book.getISBN().isBlank()
                || book.getGenre() == null)
            throw new IllegalArgumentException("Invalid book details");
        getAllBooksList().add(book);
    }

    void removeBook(UUID uuid) {
        getAllBooksList().removeIf(book -> book.getUUID().equals(uuid));
    }

    void editBook(UUID uuid, String title, String author, String publisher, String ISBN, Integer yearOfPublication, UUID genre, Integer copies) {
        BookModel book = getAllBooksList().stream().filter(bk -> bk.getUUID().equals(uuid)).findAny().orElse(null);
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
        objectStream.writeObject(new ArrayList<>(getAllBooksList()));
        objectStream.close();
        fileStream.close();
    }

    void loadBooks() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/books");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        allBooksList = FXCollections.observableArrayList((ArrayList<BookModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
