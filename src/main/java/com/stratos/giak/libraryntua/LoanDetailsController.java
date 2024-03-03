package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

//TODO ADD DOCS
public class LoanDetailsController {
    @FXML
    private Text titleField;
    @FXML
    private Text ISBNField;
    @FXML
    private Text loanDateField;
    @FXML
    private Text returnDateField;
    @FXML
    private Rating ratingField;
    @FXML
    private TextArea commentsField;
    private LoanModel loan;

    //TODO ADD DOCS
    public void initializeFields(LoanModel loan) {
        this.loan = loan;
        if (loan == null) return;
        titleField.textProperty().bind(loan.getBook().titleProperty());
        ISBNField.textProperty().bind(loan.getBook().ISBNProperty());
        loanDateField.setText(loan.getLoanDate().toString());
        returnDateField.setText(loan.getLoanDate().plusDays(loan.getLoanLength()).toString());
        ratingField.ratingProperty().set(loan.ratingProperty().getValue());
        commentsField.textProperty().set(loan.commentProperty().getValueSafe());
    }

    @FXML
    private void handleEndLoanButtonAction(ActionEvent actionEvent) {
        if (loan == null || !CustomAlerts.showEndLoanAlert(loan)) return;
        loan.ratingProperty().set((int) Math.round(ratingField.getRating()));
        loan.commentProperty().set(commentsField.getText());
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_LOAN_EVENT));
        Loans.getInstance().endLoan(loan);
    }

    @FXML
    private void handleSaveLoanButtonAction(ActionEvent actionEvent) {
        loan.ratingProperty().set((int) Math.round(ratingField.getRating()));
        loan.commentProperty().set(commentsField.getText());
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_LOAN_EVENT));
    }
}
