package com.stratos.giak.libraryntua;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

public final class Books {
    private static Books instance;
    private transient ObservableList<BookModel> allBooksList;
    private transient ObservableMap<UUID, BookModel> allBooksMap = FXCollections.observableHashMap();

    public static Books getInstance() {
        if (instance == null) {
            instance = new Books();
            try {
                instance.loadBooks();
            } catch (IOException | ClassNotFoundException e) {
                return instance;
            } finally {
                instance.allBooksList = FXCollections.observableArrayList(instance.allBooksMap.values());
                instance.allBooksMap.addListener((MapChangeListener<UUID, BookModel>) change -> {
                    if (change.wasAdded()) {
                        instance.allBooksList.add(change.getValueAdded());
                    }
                    if (change.wasRemoved()) {
                        instance.allBooksList.remove(change.getValueRemoved());
                    }
                });
            }
        }
        return instance;
    }

    ObservableMap<UUID, BookModel> getAllBooksMap() {
        return allBooksMap;
    }

    ObservableList<BookModel> getAllBooksList() {
        return allBooksList;
    }

    BookModel getBook(UUID uuid) {
        return getAllBooksMap().get(uuid);
    }

    void addBook(BookModel book) {
        if (book.getTitle().isBlank()
                || book.getAuthor().isBlank()
                || book.getPublisher().isBlank()
                || book.getISBN().isBlank()
                || book.getGenre() == null)
            throw new IllegalArgumentException("Invalid book details");
        getAllBooksMap().putIfAbsent(book.getUUID(), book);
    }

    void removeBook(UUID uuid) {
        getAllBooksMap().remove(uuid);
    }

    void editBook(UUID uuid, BookModel book) {
        if (getAllBooksMap().replace(uuid, book) == null)
            throw new IllegalArgumentException("Book UUID does not exist");
    }

    void saveBooks() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/books");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new HashMap<>(getAllBooksMap()));
        objectStream.close();
        fileStream.close();
    }

    void loadBooks() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/books");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        allBooksMap = FXCollections.observableMap((HashMap<UUID, BookModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
