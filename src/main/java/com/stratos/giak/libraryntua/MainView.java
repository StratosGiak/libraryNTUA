package com.stratos.giak.libraryntua;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.Objects;

public class MainView {

    @FXML
    private TabPane tabPane;

    public void initialize() {
        tabPane.addEventFilter(CustomEvents.CreateUserEvent.CREATE_USER_EVENT, event -> {
            Parent root;
            Tab tab;
            FXMLLoader loader;
            if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
                loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource("edit-user.fxml")));
            } else {
                loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource("edit-user.fxml")));
            }
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
                ((EditUserController) loader.getController()).initializeFields(event.getUser());
                tab = new Tab("Edit User", root);
            } else {
                ((EditUserController) loader.getController()).initializeFields(event.getUser());
                tab = new Tab("Edit User", root);
            }
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        tabPane.addEventFilter(CustomEvents.CreateBookEvent.CREATE_BOOK_EVENT, event -> {
            Parent root;
            Tab tab;
            FXMLLoader loader;
            if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
                loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource("edit-book.fxml")));
            } else {
                loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource("edit-book.fxml")));
            }
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
                ((EditBookController) loader.getController()).initializeFields(event.getBook());
                tab = new Tab("Edit Book", root);
            } else {
                ((EditBookController) loader.getController()).initializeFields(event.getBook());
                tab = new Tab("Edit Book", root);
            }
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        tabPane.addEventFilter(CustomEvents.EXIT_BOOK_EVENT, event -> {
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            tabPane.getSelectionModel().select(0);
            event.consume();
        });
        tabPane.addEventFilter(CustomEvents.EXIT_USER_EVENT, event -> {
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            tabPane.getSelectionModel().select(1);
            event.consume();
        });
    }

}
