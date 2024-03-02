package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;

import java.util.Arrays;

public class LoanListController {
    public TableView<LoanModel> tableViewLoans;
    public Button viewLoanButton;
    public Button endLoanButton;

    public void initialize() {
        viewLoanButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewLoans.getSelectionModel().selectedItemProperty().get() == null, tableViewLoans.getSelectionModel().selectedItemProperty()));
        endLoanButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewLoans.getSelectionModel().selectedItemProperty().get() == null, tableViewLoans.getSelectionModel().selectedItemProperty()));

        TableColumn<LoanModel, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(loan -> loan.getValue().getBook().titleProperty());
        TableColumn<LoanModel, String> userColumn = new TableColumn<>("Username");
        userColumn.setCellValueFactory(loan -> loan.getValue().getUser().usernameProperty());
        TableColumn<LoanModel, String> loanDateColumn = new TableColumn<>("Loan date");
        loanDateColumn.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getLoanDate().toString()));
        TableColumn<LoanModel, String> returnDateColumn = new TableColumn<>("Return date");
        returnDateColumn.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getLoanDate().plusDays(loan.getValue().getLoanLength()).toString()));
        if (LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN)) {
            tableViewLoans.getColumns().setAll(Arrays.asList(titleColumn, userColumn, loanDateColumn, returnDateColumn));
            tableViewLoans.setItems(Loans.getInstance().getLoanList());
        } else {
            tableViewLoans.getColumns().setAll(Arrays.asList(titleColumn, loanDateColumn, returnDateColumn));
            tableViewLoans.setItems(Loans.getInstance().getLoanList().filtered(loan -> loan.getUser().equals(LoggedUser.getInstance().getUser())));
        }

        tableViewLoans.setRowFactory(tableView -> {
            TableRow<LoanModel> row = new TableRow<>();
            row.setOnMouseClicked(clickEvent -> {
                if (clickEvent.getClickCount() > 1) {
                    if (row.getItem() == null) return;
                    ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.ViewLoanDetailsEvent(row.getItem()));

                }
            });
            return row;
        });
    }

    public void handleViewLoanButtonAction(ActionEvent actionEvent) {
        LoanModel selectedLoan = tableViewLoans.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) return;
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.ViewLoanDetailsEvent(selectedLoan));
    }

    public void handleEndLoanButtonAction(ActionEvent actionEvent) {
        LoanModel selectedLoan = tableViewLoans.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) return;
        if (CustomAlerts.showEndLoanAlert(selectedLoan)) {
            Loans.getInstance().endLoan(selectedLoan);
        }
    }
}
