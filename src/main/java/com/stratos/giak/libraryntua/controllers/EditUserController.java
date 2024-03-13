package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.databases.Users;
import com.stratos.giak.libraryntua.models.UserModel;
import com.stratos.giak.libraryntua.utilities.AccessLevel;
import com.stratos.giak.libraryntua.utilities.CustomEvents;
import com.stratos.giak.libraryntua.utilities.LoggedUser;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import static com.stratos.giak.libraryntua.utilities.Miscellaneous.setTextFieldError;

public class EditUserController {
    private final UserModel user;
    @FXML
    private Label adminLabel;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nameFirstField;
    @FXML
    private TextField nameLastField;
    @FXML
    private TextField IDField;
    @FXML
    private Text errorText;
    @FXML
    private Text titleText;
    @FXML
    private CheckBox adminField;
    @FXML
    private Button revertUsername;
    @FXML
    private Button revertPassword;
    @FXML
    private Button revertEmail;
    @FXML
    private Button revertNameFirst;
    @FXML
    private Button revertNameLast;
    @FXML
    private Button revertID;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    EditUserController(UserModel user) {
        this.user = user;
    }

    @FXML
    private void initialize() {
        if (user == null) return;
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        emailField.setText(user.getEmail());
        nameFirstField.setText(user.getNameFirst());
        nameLastField.setText(user.getNameLast());
        IDField.setText(user.getID());
        titleText.setText("Edit user " + user.getUsername());
        adminField.setSelected(user.getAccessLevel().equals(AccessLevel.ADMIN));

        revertUsername.visibleProperty().bind(Bindings.createBooleanBinding(() -> !usernameField.textProperty().get().equals(user.getUsername()), usernameField.textProperty()));
        revertPassword.visibleProperty().bind(Bindings.createBooleanBinding(() -> !passwordField.textProperty().get().equals(user.getPassword()), passwordField.textProperty()));
        revertEmail.visibleProperty().bind(Bindings.createBooleanBinding(() -> !emailField.textProperty().get().equals(user.getEmail()), emailField.textProperty()));
        revertNameFirst.visibleProperty().bind(Bindings.createBooleanBinding(() -> !nameFirstField.textProperty().get().equals(user.getNameFirst()), nameFirstField.textProperty()));
        revertNameLast.visibleProperty().bind(Bindings.createBooleanBinding(() -> !nameLastField.textProperty().get().equals(user.getNameLast()), nameLastField.textProperty()));
        revertID.visibleProperty().bind(Bindings.createBooleanBinding(() -> !IDField.textProperty().get().equals(user.getID()), IDField.textProperty()));

        revertUsername.setOnAction(event -> usernameField.setText(user.getUsername()));
        revertPassword.setOnAction(event -> passwordField.setText(user.getPassword()));
        revertEmail.setOnAction(event -> emailField.setText(user.getEmail()));
        revertNameFirst.setOnAction(event -> nameFirstField.setText(user.getNameFirst()));
        revertNameLast.setOnAction(event -> nameLastField.setText(user.getNameLast()));
        revertID.setOnAction(event -> IDField.setText(user.getID()));

        adminField.visibleProperty().bind(LoggedUser.getInstance().getUser().accessLevelProperty().isEqualTo(AccessLevel.ADMIN));
        adminField.managedProperty().bind(adminField.visibleProperty());
        adminLabel.visibleProperty().bind(LoggedUser.getInstance().getUser().accessLevelProperty().isEqualTo(AccessLevel.ADMIN));
        adminLabel.managedProperty().bind(adminLabel.visibleProperty());
        adminField.setDisable(LoggedUser.getInstance().getUser().equals(user));
    }

    @FXML
    private void onFieldClicked(MouseEvent ignoredMouseEvent) {
        setTextFieldError(usernameField, false);
        setTextFieldError(passwordField, false);
        setTextFieldError(emailField, false);
        setTextFieldError(nameFirstField, false);
        setTextFieldError(nameLastField, false);
        setTextFieldError(IDField, false);
        errorText.setText(null);
    }

    @FXML
    private void handleSubmitButtonAction(ActionEvent actionEvent) {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final String email = emailField.getText();
        final String nameFirst = nameFirstField.getText();
        final String nameLast = nameLastField.getText();
        final String ID = IDField.getText();
        final AccessLevel accessLevel = adminField.isSelected() ? AccessLevel.ADMIN : AccessLevel.USER;
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
        if ((user == null || !username.equals(user.getUsername()))) {
            if (Users.getInstance().getUsersList().stream().anyMatch(user1 -> user1.getUsername().equals(username))) {
                errorText.setText("Username already exists");
                setTextFieldError(usernameField, true);
                return;
            }
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
        if ((user == null || !email.equals(user.getEmail())) && Users.getInstance().getUsersList().stream().anyMatch(user -> user.getEmail().equals(email))) {
            errorText.setText("Email already exists");
            setTextFieldError(emailField, true);
            return;
        }
        if ((user == null || !ID.equals(user.getID())) && Users.getInstance().getUsersList().stream().anyMatch(user -> user.getID().equals(ID))) {
            errorText.setText("ID already exists");
            setTextFieldError(IDField, true);
            return;
        }
        if (user == null) {
            final UserModel user = new UserModel(username, password, nameFirst, nameLast, ID, email, accessLevel);
            Users.getInstance().addUser(user);
        } else {
            user.editUser(username, password, nameFirst, nameLast, ID, email, accessLevel);
        }
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_USER_EVENT));
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_USER_EVENT));
    }
}
