package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class BookDetailsController {
    @FXML
    public Text titleText;
    @FXML
    public Text titleField;
    @FXML
    public Text authorField;
    @FXML
    public Text publisherField;
    @FXML
    public Text ISBNField;
    @FXML
    public Text yearOfPublicationField;
    @FXML
    public Text genreField;
    @FXML
    public Text copiesField;
    @FXML
    public HBox ratingsField;
    public Button borrowButton;
    private UUID uuid;

    public void initializeFields(UUID uuid) {
        this.uuid = uuid;
        if (this.uuid == null) return;
        BookModel book = Books.getInstance().getBook(this.uuid);
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        publisherField.setText(book.getPublisher());
        ISBNField.setText(book.getISBN());
        yearOfPublicationField.setText(String.valueOf(book.getYearOfPublication()));
        genreField.setText(book.getGenre().toString());
        copiesField.setText(String.valueOf(book.getCopies()));

        org.controlsfx.control.Rating rating = new Rating(5, book.getRatingsCount() != 0 ? book.getRatingsSum() / book.getRatingsCount() : 0);
        rating.setMouseTransparent(true);
        rating.setFocusTraversable(false);
        Text text = new Text("(" + book.getRatingsCount() + ")");
        text.setFont(new Font(24));
        ratingsField.getStyleClass().add("rating-bar");
        ratingsField.getChildren().addAll(rating, text);

        borrowButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Books.getInstance().getBook(uuid).copiesProperty().get() <= 0, Books.getInstance().getBook(uuid).copiesProperty()));
    }

    public void handleBorrowButtonAction(ActionEvent actionEvent) {
        if (LoggedUser.getInstance().getUser().getBorrowedList().size() >= 2) {
            CustomAlerts.showMaxBorrowedAlert();
            return;
        }
        if (this.uuid == null) throw new RuntimeException("Book UUID is null");
        LoanModel borrowed = new LoanModel(this.uuid, LoggedUser.getInstance().getUser().getUUID(), Date.from(Instant.now()), 3);
        Loans.getInstance().addLoan(borrowed);
    }
}
