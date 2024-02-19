package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

import static com.stratos.giak.libraryntua.Constants.BORDER_ERROR;
import static com.stratos.giak.libraryntua.Utilities.integerFilter;

public class CreateBookController {
    public TextField titleField;
    public TextField authorField;
    public TextField publisherField;
    public TextField ISBNField;
    public TextField yearOfPublicationField;
    public TextField genreField;
    public TextField copiesField;
    public Text errorText;


    public void initialize() {
        yearOfPublicationField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
        copiesField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0, integerFilter));
    }

    public void onFieldClicked(MouseEvent ignoredMouseEvent) {
        titleField.setBorder(Border.EMPTY);
        authorField.setBorder(Border.EMPTY);
        publisherField.setBorder(Border.EMPTY);
        ISBNField.setBorder(Border.EMPTY);
        yearOfPublicationField.setBorder(Border.EMPTY);
        copiesField.setBorder(Border.EMPTY);
    }

    public void handleCreateButtonAction(ActionEvent actionEvent) throws IOException {
        final String title = titleField.getText();
        final String author = authorField.getText();
        final String publisher = publisherField.getText();
        final String ISBN = ISBNField.getText();
        final String yearOfPublication = yearOfPublicationField.getText();
        final String genre = genreField.getText();
        final int copies = Integer.parseInt(copiesField.getText().isBlank() ? "0" : copiesField.getText());
        if (title.isBlank() || author.isBlank() || publisher.isBlank() || ISBN.isBlank() || yearOfPublication.isBlank()) {
            errorText.setText("Please fill in all fields");
            if (title.isBlank()) titleField.setBorder(BORDER_ERROR);
            if (author.isBlank()) authorField.setBorder(BORDER_ERROR);
            if (publisher.isBlank()) publisherField.setBorder(BORDER_ERROR);
            if (ISBN.isBlank()) ISBNField.setBorder(BORDER_ERROR);
            if (yearOfPublication.isBlank()) yearOfPublicationField.setBorder(BORDER_ERROR);
            return;
        }
        BookModel book = new BookModel(title, author, publisher, ISBN, Integer.parseInt(yearOfPublication), genre, copies);
        Books.getInstance().addBook(book);
        Utilities.changeScene(actionEvent, "main-view.fxml");
    }

    public void handleCancelButtonAction(ActionEvent actionEvent) throws IOException {
        Utilities.changeScene(actionEvent, "main-view.fxml");
    }
}
