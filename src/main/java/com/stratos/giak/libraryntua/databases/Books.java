package com.stratos.giak.libraryntua.databases;

import com.stratos.giak.libraryntua.models.BookModel;
import com.stratos.giak.libraryntua.models.GenreModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public final class Books {
    private static Books instance;
    private transient ObservableList<BookModel> booksList = FXCollections.observableArrayList();

    private Books() {
    }

    /**
     * Returns a singleton instance of the Books database object.
     * The Books object manages all operations that concern the books in the library.
     *
     * @return a Books instance
     * @see BookModel
     */
    public static Books getInstance() {
        if (instance == null) {
            instance = new Books();
            try {
                instance.loadBooks();
            } catch (IOException ignored) {
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Serialized book file is not of type List<BookModel>");
            }
        }
        return instance;
    }

    /**
     * @return an observable list of all registered books, represented by {@link BookModel} objects
     */
    public ObservableList<BookModel> getBooksList() {
        return booksList;
    }

    /**
     * Returns a book with the given UUID.
     * This is only used for the (de)serialization of loans.
     *
     * @param uuid the {@link UUID} of the book
     * @return a {@link BookModel} object with the given UUID, or null if no such book exists
     */
    public BookModel getBook(UUID uuid) {
        return getBooksList().stream().filter(book -> book.getUUID().equals(uuid)).findAny().orElse(null);
    }

    /**
     * Adds the given book to the list of registered books.
     * Does not check whether the book is already in the list.
     *
     * @param book the book to be added
     */
    public void addBook(BookModel book) {
        getBooksList().add(book);
    }

    /**
     * Removes the given book from the database, if it exists.
     * Also ends all open loans involving that book.
     *
     * @param book the book to be removed
     */
    public void removeBook(BookModel book) {
        Loans.getInstance().removeAllWithBook(book);
        getBooksList().remove(book);
    }

    /**
     * Removes all books that belong to the given genre.
     * Also ends all loans involving all the books that are to be removed
     *
     * @param genre the genre of the books to be removed
     */
    public void removeAllWithGenre(GenreModel genre) {
        Iterator<BookModel> iterator = getBooksList().iterator();
        while (iterator.hasNext()) {
            BookModel book = iterator.next();
            if (!book.getGenre().equals(genre)) continue;
            Loans.getInstance().removeAllWithBook(book);
            iterator.remove();
        }
    }

    /**
     * Serializes the list of registered books.
     * Saves the list of books as a file named "books",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException if there is any error writing to the file
     */
    public void saveBooks() throws IOException {
        FileOutputStream fileStream = new FileOutputStream("medialab/books");
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        objectStream.writeObject(new ArrayList<>(getBooksList()));
        objectStream.close();
        fileStream.close();
    }

    /**
     * Deserializes the list of registered books.
     * Loads the list of books from a file named "books",
     * in a folder named medialab placed in the project directory
     *
     * @throws IOException            if there is any error reading the file
     * @throws ClassNotFoundException if the serialized object is not a list of {@link BookModel}s
     */
    public void loadBooks() throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream("medialab/books");
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);
        booksList = FXCollections.observableArrayList((ArrayList<BookModel>) objectStream.readObject());
        objectStream.close();
        fileStream.close();
    }
}
