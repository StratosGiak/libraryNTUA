package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.databases.Loans;
import com.stratos.giak.libraryntua.models.BookModel;
import com.stratos.giak.libraryntua.models.LoanModel;
import com.stratos.giak.libraryntua.models.ReviewModel;
import com.stratos.giak.libraryntua.utilities.CustomAlerts;
import com.stratos.giak.libraryntua.utilities.LoggedUser;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

import java.time.LocalDate;

public class BookDetailsController {
    private final BookModel book;
    @FXML
    private Text titleText;
    @FXML
    private Text titleField;
    @FXML
    private Text authorField;
    @FXML
    private Text publisherField;
    @FXML
    private Text ISBNField;
    @FXML
    private Text yearOfPublicationField;
    @FXML
    private Text genreField;
    @FXML
    private Text copiesField;
    @FXML
    private HBox ratingsField;
    @FXML
    private Button borrowButton;
    @FXML
    private Text loanLengthText;
    @FXML
    private Button decrementLoanLengthButton;
    @FXML
    private Button incrementLoanLengthButton;
    @FXML
    private Text errorText;
    @FXML
    private ListView<ReviewModel> commentList;

    BookDetailsController(BookModel book) {
        if (book == null) throw new IllegalArgumentException("Book must not be null");
        this.book = book;
    }

    @FXML
    private void initialize() {
        titleText.setText(book.getTitle() + " details");
        titleField.textProperty().bind(book.titleProperty());
        authorField.textProperty().bind(book.authorProperty());
        publisherField.textProperty().bind(book.publisherProperty());
        ISBNField.textProperty().bind(book.ISBNProperty());
        yearOfPublicationField.textProperty().bind(book.yearOfPublicationProperty().asString());
        genreField.textProperty().bind(book.genreProperty().asString());
        copiesField.textProperty().bind(book.copiesProperty().asString());
        Rating rating = new Rating(5);
        rating.setPartialRating(true);
        rating.ratingProperty().bind(Bindings.createDoubleBinding(() -> book.ratingsCountProperty().getValue() != 0 ? (double) book.ratingsSumProperty().getValue() / book.ratingsCountProperty().getValue() : 0, book.ratingsCountProperty(), book.ratingsSumProperty()));
        rating.setMouseTransparent(true);
        rating.setFocusTraversable(false);
        Text text = new Text();
        text.textProperty().bind(Bindings.createStringBinding(() -> "(" + book.ratingsCountProperty().get() + ")", book.ratingsCountProperty()));
        text.setFont(new Font(24));
        ratingsField.setStyle("-fx-scale-x: 0.6; -fx-scale-y: 0.6;");
        ratingsField.getChildren().addAll(rating, text);

        commentList.setCellFactory(listView -> new ListCell<>() {
                    {
                        setPrefWidth(0);
                    }

                    @Override
                    protected void updateItem(ReviewModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null || item.ratingProperty().get() == 0 && (item.getComment() == null || item.getComment().isBlank())) {
                            setText(null);
                            setGraphic(null);
                            return;
                        }

                        Text username = new Text();
                        username.textProperty().bind(item.usernameProperty());
                        username.wrappingWidthProperty().bind(widthProperty().subtract(10));
                        Text comment = new Text();
                        comment.textProperty().bind(item.commentProperty());
                        comment.wrappingWidthProperty().bind(widthProperty().subtract(10));
                        Rating ratingBar = new Rating(5);
                        ratingBar.setMouseTransparent(true);
                        ratingBar.setFocusTraversable(false);
                        ratingBar.styleProperty().set("-fx-scale-x:0.5; -fx-scale-y:0.5;");
                        ratingBar.ratingProperty().bind(item.ratingProperty());

                        VBox vBox = new VBox(username);
                        vBox.setSpacing(2);
                        if (item.ratingProperty().get() != 0) vBox.getChildren().add(new Group(ratingBar));
                        if (!comment.getText().isBlank()) vBox.getChildren().add(comment);
                        setGraphic(vBox);
                    }
                }
        );
        commentList.setItems(book.getReviews());

        borrowButton.disableProperty().bind(Bindings.createBooleanBinding(() -> book.copiesProperty().get() <= 0 || Loans.getInstance().getLoanList().filtered(loan -> loan.getUser().equals(LoggedUser.getInstance().getUser())).size() >= 2, book.copiesProperty(), Loans.getInstance().getLoanList()));
        errorText.textProperty().bind(Bindings.createStringBinding(() -> {
            String error = "";
            if (book.copiesProperty().get() < 1)
                error += "Book is currently unavailable\n";
            if (Loans.getInstance().getLoanList().filtered(loan -> loan.getUser().equals(LoggedUser.getInstance().getUser())).size() >= 2)
                error += "You already have 2 open book loans";
            return error;
        }, book.copiesProperty(), Loans.getInstance().getLoanList()));

        commentList.visibleProperty().bind(Bindings.createBooleanBinding(() -> !book.getReviews().isEmpty(), book.getReviews()));
        commentList.managedProperty().bind(commentList.visibleProperty());

        decrementLoanLengthButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Integer.parseInt(loanLengthText.textProperty().get()) <= 1, loanLengthText.textProperty()));
        incrementLoanLengthButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Integer.parseInt(loanLengthText.textProperty().get()) >= 5, loanLengthText.textProperty()));
        errorText.visibleProperty().bind(errorText.textProperty().isNotEmpty());
    }

    @FXML
    private void handleBorrowButtonAction() {
        if (Loans.getInstance().getLoanListByUser(LoggedUser.getInstance().getUser()).size() >= 2) {
            CustomAlerts.showMaxBorrowedAlert();
            return;
        }
        LoanModel borrowed = new LoanModel(book, LoggedUser.getInstance().getUser(), LocalDate.now(), Integer.parseInt(loanLengthText.getText()));
        Loans.getInstance().addLoan(borrowed);
        CustomAlerts.showRequestLoanAlert();
    }

    @FXML
    private void handleDecrementLoanLengthAction() {
        int loanLength = Integer.parseInt(loanLengthText.getText());
        if (loanLength <= 1) return;
        loanLengthText.setText(String.valueOf(loanLength - 1));
    }

    @FXML
    private void handleIncrementLoanLengthAction() {
        int loanLength = Integer.parseInt(loanLengthText.getText());
        if (loanLength >= 5) return;
        loanLengthText.setText(String.valueOf(loanLength + 1));
    }
}
