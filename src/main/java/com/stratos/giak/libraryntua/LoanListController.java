package com.stratos.giak.libraryntua;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Arrays;

public class LoanListController {
    public TableView<LoanModel> tableViewLoans;

    public void initialize() {
        TableColumn<LoanModel, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(loan -> Books.getInstance().getBook(loan.getValue().getUuidBook()).titleProperty());
        TableColumn<LoanModel, String> userColumn = new TableColumn<>("Username");
        userColumn.setCellValueFactory(loan -> Users.getInstance().getUserByUUID(loan.getValue().getUuidUser()).usernameProperty());
        TableColumn<LoanModel, String> loanDateColumn = new TableColumn<>("Loan date");
        loanDateColumn.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getLoanDate().toString()));
        TableColumn<LoanModel, String> returnDateColumn = new TableColumn<>("Return date");
        returnDateColumn.setCellValueFactory(loan -> new SimpleStringProperty(loan.getValue().getLoanDate().plusDays(loan.getValue().getLoanLength()).toString()));
        tableViewLoans.getColumns().setAll(Arrays.asList(titleColumn, userColumn, loanDateColumn, returnDateColumn));
        tableViewLoans.setItems(Loans.getInstance().getLoanList());
    }
}
