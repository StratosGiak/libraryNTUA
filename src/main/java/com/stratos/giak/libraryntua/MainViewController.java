package com.stratos.giak.libraryntua;

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

//TODO ADD DOCS
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
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("user-list.fxml")));
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
        userDetailsLink.setOnAction(event -> userDetailsLink.fireEvent(new CustomEvents.EditUserEvent(LoggedUser.getInstance().getUser())));
        logoutLink.setOnAction(event -> {
            if (!CustomAlerts.showLogoutAlert()) return;
            try {
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).setTitle("NTUA-Library");
                Utilities.changeScene(event, "welcome.fxml");
                LoggedUser.getInstance().setUser(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        accountTextFlow.getChildren().addAll(new Text("Logged in as"), userDetailsLink, new Text(" | "), logoutLink);

        stackPane.addEventFilter(CustomEvents.EditUserEvent.EDIT_USER_EVENT, event -> {
            if (!LoggedUser.getInstance().getUser().equals(event.getUser()) && !LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN)) {
                CustomAlerts.showPrivilegesAlert();
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("edit-user.fxml")));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((EditUserController) loader.getController()).initializeFields(event.getUser());
            Tab tab = new Tab("Edit User", root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.EditBookEvent.EDIT_BOOK_EVENT, event -> {
            if (!LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN)) {
                CustomAlerts.showPrivilegesAlert();
                return;
            }
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("edit-book.fxml")));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((EditBookController) loader.getController()).initializeFields(event.getBook());
            Tab tab = new Tab("Edit Book", root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.ViewBookDetailsEvent.VIEW_BOOK_DETAILS_EVENT, event -> {
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("book-details.fxml")));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((BookDetailsController) loader.getController()).initializeFields(event.getBook());
            Tab tab = new Tab(event.getBook().getTitle(), root);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
            event.consume();
        });
        stackPane.addEventFilter(CustomEvents.ViewLoanDetailsEvent.VIEW_LOAN_DETAILS_EVENT, event -> {
            Parent root;
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("loan-details.fxml")));
            try {
                root = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((LoanDetailsController) loader.getController()).initializeFields(event.getLoan());
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
