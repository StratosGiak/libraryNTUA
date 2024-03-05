package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.databases.Users;
import com.stratos.giak.libraryntua.models.UserModel;
import com.stratos.giak.libraryntua.utilities.CustomAlerts;
import com.stratos.giak.libraryntua.utilities.CustomEvents;
import com.stratos.giak.libraryntua.utilities.LoggedUser;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;

public class UserListController {
    private final ObservableList<UserModel> users = Users.getInstance().getUsersList();
    @FXML
    private Button editUserButton;
    @FXML
    private Button removeUserButton;
    @FXML
    private TableView<UserModel> tableViewUsers;

    @FXML
    private void initialize() {
        editUserButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewUsers.getSelectionModel().selectedItemProperty().get() == null, tableViewUsers.getSelectionModel().selectedItemProperty()));
        removeUserButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewUsers.getSelectionModel().selectedItemProperty().get() == null || tableViewUsers.getSelectionModel().selectedItemProperty().get().equals(LoggedUser.getInstance().getUser()), tableViewUsers.getSelectionModel().selectedItemProperty()));

        FilteredList<UserModel> filteredUsers = new FilteredList<>(users, null);

        tableViewUsers.setRowFactory(tableView -> {
            TableRow<UserModel> row = new TableRow<>();
            row.setOnMouseClicked(clickEvent -> {
                if (clickEvent.getClickCount() > 1) {
                    if (row.getItem() == null) return;
                    ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.UserEvent(row.getItem()));
                }
            });
            return row;
        });
        SortedList<UserModel> sortableUsers = new SortedList<>(filteredUsers);
        sortableUsers.comparatorProperty().bind(tableViewUsers.comparatorProperty());
        tableViewUsers.setItems(sortableUsers);

        for (TableColumn columnName : tableViewUsers.getColumns()) {
            columnName.setCellFactory(column -> new TableCell<UserModel, Object>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }
                    setText(item.toString());
                    Tooltip t = new Tooltip(item.toString());
                    t.setShowDelay(new Duration(300));
                    setTooltip(t);
                }
            });
        }
    }

    @FXML
    private void handleAddUserButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.UserEvent(null));
    }

    @FXML
    private void handleEditUserButtonAction(ActionEvent actionEvent) {
        UserModel selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.UserEvent(selectedUser));
    }

    @FXML
    private void handleRemoveUserButtonAction() {
        UserModel selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser == null || selectedUser.equals(LoggedUser.getInstance().getUser())) return;
        if (CustomAlerts.showRemoveUserAlert()) {
            Users.getInstance().removeUser(selectedUser);
        }
    }
}
