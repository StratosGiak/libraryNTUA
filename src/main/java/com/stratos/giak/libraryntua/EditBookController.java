package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import static com.stratos.giak.libraryntua.Utilities.integerFilter;
import static com.stratos.giak.libraryntua.Utilities.setTextFieldError;

//TODO ADD DOCS
public class EditBookController {
    @FXML
    private Button revertTitle;
    @FXML
    private Button revertAuthor;
    @FXML
    private Button revertPublisher;
    @FXML
    private Button revertISBN;
    @FXML
    private Button revertGenre;
    @FXML
    private Button revertYearOfPublication;
    @FXML
    private Button revertCopies;
    @FXML
    private Text titleText;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField ISBNField;
    @FXML
    private TextField yearOfPublicationField;
    @FXML
    private ComboBox<GenreModel> genreField;
    @FXML
    private TextField copiesField;
    @FXML
    private Text errorText;
    private BookModel book;

    //TODO ADD DOCS
    public void initializeFields(BookModel book) {
        this.book = book;
        if (book == null) return;
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        publisherField.setText(book.getPublisher());
        ISBNField.setText(book.getISBN());
        yearOfPublicationField.setText(String.valueOf(book.getYearOfPublication()));
        genreField.setValue(book.getGenre());
        copiesField.setText(String.valueOf(book.getCopies()));
        titleText.setText("Edit book " + book.getTitle());

        revertTitle.visibleProperty().bind(Bindings.createBooleanBinding(() -> !titleField.textProperty().get().equals(book.getTitle()), titleField.textProperty()));
        revertAuthor.visibleProperty().bind(Bindings.createBooleanBinding(() -> !authorField.textProperty().get().equals(book.getAuthor()), authorField.textProperty()));
        revertPublisher.visibleProperty().bind(Bindings.createBooleanBinding(() -> !publisherField.textProperty().get().equals(book.getPublisher()), publisherField.textProperty()));
        revertISBN.visibleProperty().bind(Bindings.createBooleanBinding(() -> !ISBNField.textProperty().get().equals(book.getISBN()), ISBNField.textProperty()));
        revertGenre.visibleProperty().bind(Bindings.createBooleanBinding(() -> genreField.valueProperty().get() == null || !genreField.valueProperty().get().equals(book.getGenre()), genreField.valueProperty()));
        revertYearOfPublication.visibleProperty().bind(Bindings.createBooleanBinding(() -> !yearOfPublicationField.textProperty().get().equals(String.valueOf(book.getYearOfPublication())), yearOfPublicationField.textProperty()));
        revertCopies.visibleProperty().bind(Bindings.createBooleanBinding(() -> !copiesField.textProperty().get().equals(String.valueOf(book.getCopies())), copiesField.textProperty()));

        revertTitle.setOnAction(event -> titleField.setText(book.getTitle()));
        revertAuthor.setOnAction(event -> authorField.setText(book.getAuthor()));
        revertPublisher.setOnAction(event -> publisherField.setText(book.getPublisher()));
        revertISBN.setOnAction(event -> ISBNField.setText(book.getISBN()));
        revertGenre.setOnAction(event -> genreField.setValue(book.getGenre()));
        revertYearOfPublication.setOnAction(event -> yearOfPublicationField.setText(String.valueOf(book.getYearOfPublication())));
        revertCopies.setOnAction(event -> copiesField.setText(String.valueOf(book.getCopies())));
    }

    @FXML
    private void initialize() {
        yearOfPublicationField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));
        copiesField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 1, integerFilter));
        genreField.setItems(Genres.getInstance().getGenresList());
        genreField.converterProperty().set(new StringConverter<>() {
            @Override
            public String toString(GenreModel genre) {
                return genre != null ? genre.toString() : "—";
            }

            @Override
            public GenreModel fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void onFieldClicked() {
        setTextFieldError(titleField, false);
        setTextFieldError(authorField, false);
        setTextFieldError(publisherField, false);
        setTextFieldError(ISBNField, false);
        setTextFieldError(genreField, false);
        setTextFieldError(yearOfPublicationField, false);
        setTextFieldError(copiesField, false);
        errorText.setText(null);
    }

    @FXML
    private void handleCreateButtonAction(ActionEvent actionEvent) {
        final String title = titleField.getText();
        final String author = authorField.getText();
        final String publisher = publisherField.getText();
        final String ISBN = ISBNField.getText();
        final String yearOfPublication = yearOfPublicationField.getText();
        final GenreModel genre = genreField.getValue();
        final String copies = copiesField.getText();
        if (title.isBlank() || author.isBlank() || publisher.isBlank() || ISBN.isBlank() || genre == null || yearOfPublication.isBlank() || copies.isBlank()) {
            errorText.setText("Please fill in all fields");
            if (title.isBlank()) setTextFieldError(titleField, true);
            if (author.isBlank()) setTextFieldError(authorField, true);
            if (publisher.isBlank()) setTextFieldError(publisherField, true);
            if (ISBN.isBlank()) setTextFieldError(ISBNField, true);
            if (genre == null) setTextFieldError(genreField, true);
            if (yearOfPublication.isBlank()) setTextFieldError(yearOfPublicationField, true);
            if (copies.isBlank()) setTextFieldError(copiesField, true);
            return;
        }
        if (book == null) {
            final BookModel book = new BookModel(title, author, publisher, ISBN, Integer.parseInt(yearOfPublication), genre, Integer.parseInt(copies));
            Books.getInstance().addBook(book);
        } else {
            Books.getInstance().editBook(book, title, author, publisher, ISBN, Integer.parseInt(yearOfPublication), genre, Integer.parseInt(copies));
        }
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_BOOK_EVENT));
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_BOOK_EVENT));
    }
}
