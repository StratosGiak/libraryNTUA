package com.stratos.giak.libraryntua;

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
        tableViewLoans.getColumns().setAll(Arrays.asList(titleColumn, userColumn));
        tableViewLoans.setItems(Loans.getInstance().getLoanList());
    }
}
