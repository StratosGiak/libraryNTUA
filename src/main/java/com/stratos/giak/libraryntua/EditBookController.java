package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

import java.util.UUID;

import static com.stratos.giak.libraryntua.Constants.BORDER_ERROR;
import static com.stratos.giak.libraryntua.Utilities.integerFilter;

public class EditBookController {
    @FXML
    public Text titleText;
    @FXML
    public TextField titleField;
    @FXML
    public TextField authorField;
    @FXML
    public TextField publisherField;
    @FXML
    public TextField ISBNField;
    @FXML
    public TextField yearOfPublicationField;
    @FXML
    public ChoiceBox<GenreModel> genreField;
    @FXML
    public TextField copiesField;
    @FXML
    public Text errorText;
    private String initialTitle;
    private String initialAuthor;
    private String initialPublisher;
    private String initialISBN;
    private int initialYearOfPublication;
    private GenreModel initialGenre;
    private int initialCopies;
    private UUID uuid;

    public void initializeFields(UUID uuid) {
        this.uuid = uuid;
        if (this.uuid == null) return;
        BookModel book = Books.getInstance().getBook(uuid);
        initialTitle = book.getTitle();
        initialAuthor = book.getAuthor();
        initialPublisher = book.getPublisher();
        initialISBN = book.getISBN();
        initialYearOfPublication = book.getYearOfPublication();
        initialGenre = book.getGenre();
        initialCopies = book.getCopies();
        titleField.setText(initialTitle);
        authorField.setText(initialAuthor);
        publisherField.setText(initialPublisher);
        ISBNField.setText(initialISBN);
        yearOfPublicationField.setText(String.valueOf(initialYearOfPublication));
        genreField.setValue(initialGenre);
        copiesField.setText(String.valueOf(initialCopies));
        titleText.setText("Edit book " + initialTitle);
    }

    public void initialize() {
        yearOfPublicationField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));
        copiesField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 1, integerFilter));
        genreField.setItems(Genres.getInstance().getGenresList());
    }

    public void onFieldClicked(MouseEvent ignoredMouseEvent) {
        titleField.setBorder(Border.EMPTY);
        authorField.setBorder(Border.EMPTY);
        publisherField.setBorder(Border.EMPTY);
        ISBNField.setBorder(Border.EMPTY);
        genreField.setBorder(Border.EMPTY);
        yearOfPublicationField.setBorder(Border.EMPTY);
        copiesField.setBorder(Border.EMPTY);
    }

    public void handleCreateButtonAction(ActionEvent actionEvent) {
        final String title = titleField.getText();
        final String author = authorField.getText();
        final String publisher = publisherField.getText();
        final String ISBN = ISBNField.getText();
        final String yearOfPublication = yearOfPublicationField.getText();
        final GenreModel genre = genreField.getValue();
        final String copies = copiesField.getText();
        if (title.isBlank() || author.isBlank() || publisher.isBlank() || ISBN.isBlank() || genre == null || yearOfPublication.isBlank() || copies.isBlank()) {
            errorText.setText("Please fill in all fields");
            if (title.isBlank()) titleField.setBorder(BORDER_ERROR);
            if (author.isBlank()) authorField.setBorder(BORDER_ERROR);
            if (publisher.isBlank()) publisherField.setBorder(BORDER_ERROR);
            if (ISBN.isBlank()) ISBNField.setBorder(BORDER_ERROR);
            if (genre == null) genreField.setBorder(BORDER_ERROR);
            if (yearOfPublication.isBlank()) yearOfPublicationField.setBorder(BORDER_ERROR);
            if (copies.isBlank()) copiesField.setBorder(BORDER_ERROR);
            return;
        }
        if (uuid == null) {
            final BookModel book = new BookModel(title, author, publisher, ISBN, Integer.parseInt(yearOfPublication), genre, Integer.parseInt(copies));
            Books.getInstance().addBook(book);
        } else {
            Books.getInstance().editBook(uuid, title, author, publisher, ISBN, Integer.parseInt(yearOfPublication), genre.getUUID(), Integer.parseInt(copies));
        }
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_BOOK_EVENT));
    }

    public void handleCancelButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_BOOK_EVENT));
    }
}
