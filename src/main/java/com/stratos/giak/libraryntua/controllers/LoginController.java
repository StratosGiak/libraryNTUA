package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.databases.Users;
import com.stratos.giak.libraryntua.models.UserModel;
import com.stratos.giak.libraryntua.utilities.AccessLevel;
import com.stratos.giak.libraryntua.utilities.CustomEvents;
import com.stratos.giak.libraryntua.utilities.LoggedUser;
import com.stratos.giak.libraryntua.utilities.Miscellaneous;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

import static com.stratos.giak.libraryntua.utilities.Miscellaneous.setTextFieldError;

public class LoginController {
    private final Hyperlink registerLink = new Hyperlink("Register");

    @FXML
    private Text errorText;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextFlow registerTextFlow;

    @FXML
    private void initialize() {
        registerTextFlow.getChildren().addAll(new Text("Don't have an account? "), registerLink);
        registerLink.setOnAction(event -> registerTextFlow.fireEvent(new Event(CustomEvents.LINK_REGISTER_EVENT)));
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        if (username.isBlank() || password.isBlank()) {
            errorText.setText("Please fill in all fields");
            setTextFieldError(usernameField, true);
            setTextFieldError(passwordField, true);
            return;
        }
        final UserModel user = Users.getInstance().getUsersList().stream().filter(user1 -> user1.getUsername().equals(username)).findAny().orElse(null);
        if (user == null) {
            errorText.setText("Username doesn't exist");
            setTextFieldError(usernameField, true);
            return;
        }
        if (!user.getPassword().equals(password)) {
            errorText.setText("Wrong password");
            setTextFieldError(usernameField, true);
            setTextFieldError(passwordField, true);
            return;
        }
        LoggedUser.getInstance().setUser(user);
        if (user.getAccessLevel() == AccessLevel.ADMIN) {
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setTitle("NTUA-Library (Admin)");
        }
        Miscellaneous.changeScene(event, "main-view.fxml");
    }

    @FXML
    private void onFieldClicked() {
        setTextFieldError(usernameField, false);
        setTextFieldError(passwordField, false);
        errorText.setText(null);
    }
}