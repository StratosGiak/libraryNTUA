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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;

import static com.stratos.giak.libraryntua.utilities.Miscellaneous.setTextFieldError;

public class RegisterController {
    private final Hyperlink loginLink = new Hyperlink("Login");

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameFirstField;

    @FXML
    private TextField nameLastField;

    @FXML
    private TextField IDField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorText;

    @FXML
    private TextFlow loginTextFlow;

    @FXML
    private void initialize() {
        loginTextFlow.getChildren().add(new TextFlow(new Text("Already have an account? "), loginLink));
        loginLink.setOnAction(event -> loginTextFlow.fireEvent(new Event(CustomEvents.LINK_LOGIN_EVENT)));
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent actionEvent) throws IOException {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final String email = emailField.getText();
        final String nameFirst = nameFirstField.getText();
        final String nameLast = nameLastField.getText();
        final String ID = IDField.getText();
        if (username.isBlank() || password.isBlank() || email.isBlank() || nameFirst.isBlank() || nameLast.isBlank() || ID.isBlank()) {
            errorText.setText("Please fill in all fields");
            if (username.isBlank()) setTextFieldError(usernameField, true);
            if (password.isBlank()) setTextFieldError(passwordField, true);
            if (email.isBlank()) setTextFieldError(emailField, true);
            if (nameFirst.isBlank()) setTextFieldError(nameFirstField, true);
            if (nameLast.isBlank()) setTextFieldError(nameLastField, true);
            if (ID.isBlank()) setTextFieldError(IDField, true);
            return;
        }
        Users users = Users.getInstance();
        if (users.getUsersList().stream().filter(user1 -> user1.getUsername().equals(username)).findAny().orElse(null) != null) {
            errorText.setText("Username already exists");
            setTextFieldError(usernameField, true);
            return;
        }
        if (!email.matches(".+@.+")) {
            errorText.setText("Email address is invalid");
            setTextFieldError(emailField, true);
            return;
        }
        if (!ID.matches(".+")) {
            errorText.setText("ID is invalid");
            setTextFieldError(IDField, true);
            return;
        }
        if (Users.getInstance().getUsersList().stream().anyMatch(user -> user.getEmail().equals(email))) {
            errorText.setText("Email already exists");
            setTextFieldError(emailField, true);
            return;
        }
        if (Users.getInstance().getUsersList().stream().anyMatch(user -> user.getID().equals(ID))) {
            errorText.setText("ID already exists");
            setTextFieldError(IDField, true);
            return;
        }
        final UserModel user = new UserModel(username, password, nameFirst, nameLast, ID, email, AccessLevel.USER);
        Users.getInstance().addUser(user);
        LoggedUser.getInstance().setUser(user);
        Miscellaneous.changeScene(actionEvent, "main-view.fxml");
    }

    @FXML
    private void onFieldClicked() {
        setTextFieldError(usernameField, false);
        setTextFieldError(passwordField, false);
        setTextFieldError(emailField, false);
        setTextFieldError(nameFirstField, false);
        setTextFieldError(nameLastField, false);
        setTextFieldError(IDField, false);
        errorText.setText(null);
    }
}