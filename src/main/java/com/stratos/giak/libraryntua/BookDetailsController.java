package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
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
    @FXML
    public Button borrowButton;
    @FXML
    public Text loanLengthText;
    @FXML
    public Button decrementLoanLengthButton;
    @FXML
    public Button incrementLoanLengthButton;
    @FXML
    public Text errorText;
    public ListView<ReviewModel> commentList;
    private BookModel book;

    public void initializeFields(BookModel book) {
        this.book = book;
        if (book == null) return;
        titleText.setText(book.getTitle() + " details");
        titleField.textProperty().bind(book.titleProperty());
        authorField.textProperty().bind(book.authorProperty());
        publisherField.textProperty().bind(book.publisherProperty());
        ISBNField.textProperty().bind(book.ISBNProperty());
        yearOfPublicationField.textProperty().bind(book.yearOfPublicationProperty().asString());
        genreField.textProperty().bind(book.genreProperty().asString());
        copiesField.textProperty().bind(book.copiesProperty().asString());
        org.controlsfx.control.Rating rating = new Rating(5, book.getRatingsCount() != 0 ? book.getRatingsSum() / book.getRatingsCount() : 0);
        rating.setMouseTransparent(true);
        rating.setFocusTraversable(false);
        Text text = new Text("(" + book.getRatingsCount() + ")");
        text.setFont(new Font(24));
        ratingsField.getStyleClass().add("rating-bar");
        ratingsField.getChildren().addAll(rating, text);

        commentList.setCellFactory(listView -> new ListCell<>() {
                    {
                        setPrefWidth(0);
                    }

                    @Override
                    protected void updateItem(ReviewModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
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
        commentList.setItems(book.getComments());

        borrowButton.disableProperty().bind(Bindings.createBooleanBinding(() -> book.copiesProperty().get() <= 0 || Loans.getInstance().getLoanList().filtered(loan -> loan.getUser().equals(LoggedUser.getInstance().getUser())).size() >= 2, book.copiesProperty(), Loans.getInstance().getLoanList()));
        errorText.textProperty().bind(Bindings.createStringBinding(() -> {
            String error = "";
            if (book.copiesProperty().get() < 1)
                error += "Book is currently unavailable\n";
            if (Loans.getInstance().getLoanList().filtered(loan -> loan.getUser().equals(LoggedUser.getInstance().getUser())).size() >= 2)
                error += "You already have 2 open book loans";
            return error;
        }, book.authorProperty(), Loans.getInstance().getLoanList()));
    }

    public void initialize() {
        decrementLoanLengthButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Integer.parseInt(loanLengthText.textProperty().get()) <= 1, loanLengthText.textProperty()));
        incrementLoanLengthButton.disableProperty().bind(Bindings.createBooleanBinding(() -> Integer.parseInt(loanLengthText.textProperty().get()) >= 5, loanLengthText.textProperty()));
        errorText.visibleProperty().bind(errorText.textProperty().isNotEmpty());
        //Bindings.createBooleanBinding(() -> Loans.getInstance().getLoanList().stream().filter(loan -> loan.getUser().equals(LoggedUser.getInstance().getUser())).findAny().isEmpty())
    }

    public void handleBorrowButtonAction(ActionEvent actionEvent) {
        if (LoggedUser.getInstance().getUser().getBorrowedList().size() >= 2) {
            CustomAlerts.showMaxBorrowedAlert();
            return;
        }
        if (book == null) throw new RuntimeException("Book UUID is null");
        LoanModel borrowed = new LoanModel(book, LoggedUser.getInstance().getUser(), LocalDate.now(), Integer.parseInt(loanLengthText.getText()));
        Loans.getInstance().addLoan(borrowed);
    }

    public void handleDecrementLoanLengthAction(ActionEvent actionEvent) {
        int loanLength = Integer.parseInt(loanLengthText.getText());
        if (loanLength <= 1) return;
        loanLengthText.setText(String.valueOf(loanLength - 1));
    }

    public void handleIncrementLoanLengthAction(ActionEvent actionEvent) {
        int loanLength = Integer.parseInt(loanLengthText.getText());
        if (loanLength >= 5) return;
        loanLengthText.setText(String.valueOf(loanLength + 1));
    }
}
