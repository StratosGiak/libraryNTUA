package com.stratos.giak.libraryntua;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;

public class UserListController {
    private final ObservableList<UserModel> users = Users.getInstance().getAllUsersList();
    @FXML
    public Button addUserButton;
    @FXML
    private TableView<UserModel> tableViewUsers;

    public void initialize() {
        addUserButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN);
        addUserButton.managedProperty().bind(addUserButton.visibleProperty());

        FilteredList<UserModel> filteredUsers = new FilteredList<>(users, null);

        tableViewUsers.setRowFactory(tableView -> {
            TableRow<UserModel> row = new TableRow<>();
            row.setOnMouseClicked(clickEvent -> {
                if (clickEvent.getClickCount() > 1) {
                    if (row.getItem() == null) return;
                    ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.CreateUserEvent(row.getItem()));
                }
            });
            return row;
        });

        tableViewUsers.setItems(filteredUsers);

        for (TableColumn columnName : tableViewUsers.getColumns()) {
            columnName.setReorderable(false);
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

    public void handleAddUserButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.CreateUserEvent());
    }
}
