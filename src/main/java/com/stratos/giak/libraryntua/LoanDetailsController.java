package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

public class LoanDetailsController {
    public Text titleField;
    public Text ISBNField;
    public Text loanDateField;
    public Text returnDateField;
    public Rating ratingField;
    public TextArea commentsField;
    private LoanModel loan;

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

    public void handleEndLoanButtonAction(ActionEvent actionEvent) {
        if (loan == null || !CustomAlerts.showEndLoanAlert(loan)) return;
        loan.ratingProperty().set((int) Math.round(ratingField.getRating()));
        loan.commentProperty().set(commentsField.getText());
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_LOAN_EVENT));
        Loans.getInstance().endLoan(loan);
    }

    public void handleSaveLoanButtonAction(ActionEvent actionEvent) {
        loan.ratingProperty().set((int) Math.round(ratingField.getRating()));
        loan.commentProperty().set(commentsField.getText());
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_LOAN_EVENT));
    }
}
