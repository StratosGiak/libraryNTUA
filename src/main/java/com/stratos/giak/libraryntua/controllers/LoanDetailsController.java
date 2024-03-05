package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.databases.Loans;
import com.stratos.giak.libraryntua.models.LoanModel;
import com.stratos.giak.libraryntua.utilities.CustomAlerts;
import com.stratos.giak.libraryntua.utilities.CustomEvents;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

public class LoanDetailsController {
    private final LoanModel loan;
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

    LoanDetailsController(LoanModel loan) {
        this.loan = loan;
    }

    @FXML
    private void initialize() {
        if (loan == null) throw new RuntimeException("Loan is null");
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
        loan.editLoan((int) Math.round(ratingField.getRating()), commentsField.getText());
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_LOAN_EVENT));
        Loans.getInstance().endLoan(loan);
    }

    @FXML
    private void handleSaveLoanButtonAction(ActionEvent actionEvent) {
        loan.editLoan((int) Math.round(ratingField.getRating()), commentsField.getText());
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_LOAN_EVENT));
    }
}
