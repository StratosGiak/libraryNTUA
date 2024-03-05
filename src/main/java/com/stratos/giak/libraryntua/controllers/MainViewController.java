package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.Main;
import com.stratos.giak.libraryntua.utilities.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainViewController {

    private final Hyperlink logoutLink = new Hyperlink("Logout?");
    private final Hyperlink userDetailsLink = new Hyperlink();
    @FXML
    private TextFlow accountTextFlow;
    @FXML
    private StackPane stackPane;
    @FXML
    private TabPane tabPane;

    @FXML
    private void initialize() {
        if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("user-list.fxml")));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Tab tab = new Tab("Users", root);
            tab.setClosable(false);
            tabPane.getTabs().add(1, tab);
        }
        userDetailsLink.visitedProperty().bind(new SimpleBooleanProperty(false));
        logoutLink.visitedProperty().bind(new SimpleBooleanProperty(false));
        userDetailsLink.textProperty().bind(LoggedUser.getInstance().getUser().usernameProperty());
        userDetailsLink.setOnAction(event -> userDetailsLink.fireEvent(new CustomEvents.UserEvent(LoggedUser.getInstance().getUser())));
        logoutLink.setOnAction(event -> {
            if (!CustomAlerts.showLogoutAlert()) return;
            try {
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).setTitle("NTUA-Library");
                Miscellaneous.changeScene(event, "welcome.fxml");
                LoggedUser.getInstance().setUser(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        accountTextFlow.getChildren().addAll(new Text("Logged in as"), userDetailsLink, new Text(" | "), logoutLink);

        stackPane.addEventFilter(CustomEvents.UserEvent.EDIT_USER_EVENT, event -> {
            if (!LoggedUser.getInstance().getUser().equals(event.getUser()) && !LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN)) {
                CustomAlerts.showPrivilegesAlert();
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("edit-user.fxml")));
            loader.setControllerFactory(param -> new EditUserController(event.getUser()));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Tab tab = new Tab("Edit User", root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.BookEvent.EDIT_BOOK_EVENT, event -> {
            if (!LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN)) {
                CustomAlerts.showPrivilegesAlert();
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("edit-book.fxml")));
            loader.setControllerFactory(controller -> new EditBookController(event.getBook()));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Tab tab = new Tab("Edit Book", root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.BookEvent.VIEW_BOOK_DETAILS_EVENT, event -> {
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("book-details.fxml")));
            loader.setControllerFactory(controller -> new BookDetailsController(event.getBook()));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Tab tab = new Tab(event.getBook().getTitle(), root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.LoanEvent.VIEW_LOAN_DETAILS_EVENT, event -> {
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource("loan-details.fxml")));
            loader.setControllerFactory(controller -> new LoanDetailsController(event.getLoan()));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Tab tab = new Tab("Loan Details", root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.EXIT_BOOK_EVENT, event -> {
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            tabPane.getSelectionModel().select(0);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.EXIT_USER_EVENT, event -> {
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            tabPane.getSelectionModel().select(1);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.EXIT_LOAN_EVENT, event -> {
            tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedIndex());
            tabPane.getSelectionModel().select(1);
            event.consume();
        });
    }

}
