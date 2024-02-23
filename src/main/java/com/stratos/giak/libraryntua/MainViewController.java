package com.stratos.giak.libraryntua;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.util.Objects;

public class MainViewController {

    @FXML
    private TabPane tabPane;

    public void initialize() {
//        if(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN){
//            tabPane.getTabs().add(new Tab("Users"));
//        }
        tabPane.addEventFilter(CustomEvents.CreateUserEvent.CREATE_USER_EVENT, event -> {
            if (LoggedUser.getInstance().getUser().getAccessLevel() != AccessLevel.ADMIN) {
                CustomAlerts.showPrivilegesAlert();
                return;
            }
            Parent root;
            Tab tab;
            FXMLLoader loader;
            loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("edit-user.fxml")));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((EditUserController) loader.getController()).initializeFields(event.getUUID());
            tab = new Tab("Edit User", root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        tabPane.addEventFilter(CustomEvents.CreateBookEvent.CREATE_BOOK_EVENT, event -> {
            Parent root;
            Tab tab;
            FXMLLoader loader;
            if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
                loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("edit-book.fxml")));
            } else {
                loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("book-details.fxml")));
            }
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
                ((EditBookController) loader.getController()).initializeFields(event.getUUID());
                tab = new Tab("Edit Book", root);
            } else {
                ((BookDetailsController) loader.getController()).initializeFields(event.getUUID());
                tab = new Tab(Books.getInstance().getBook(event.getUUID()).getTitle(), root);
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
