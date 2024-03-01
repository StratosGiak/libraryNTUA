package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

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
    public Button revertTitle;
    public Button revertAuthor;
    public Button revertPublisher;
    public Button revertISBN;
    public Button revertGenre;
    public Button revertYearOfPublication;
    public Button revertCopies;
    private BookModel book;

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

    public void initialize() {
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

    public void onFieldClicked(MouseEvent mouseEvent) {
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
        if (book == null) {
            final BookModel book = new BookModel(title, author, publisher, ISBN, Integer.parseInt(yearOfPublication), genre, Integer.parseInt(copies));
            Books.getInstance().addBook(book);
        } else {
            Books.getInstance().editBook(book, title, author, publisher, ISBN, Integer.parseInt(yearOfPublication), genre, Integer.parseInt(copies));
        }
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_BOOK_EVENT));
    }

    public void handleCancelButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_BOOK_EVENT));
    }
}
