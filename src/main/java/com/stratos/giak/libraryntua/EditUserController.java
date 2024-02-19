package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.UUID;

import static com.stratos.giak.libraryntua.Constants.BORDER_ERROR;

public class EditUserController {

    @FXML
    public TextField emailField;
    @FXML
    public TextField nameFirstField;
    @FXML
    public TextField nameLastField;
    @FXML
    public TextField IDField;
    @FXML
    public Text errorText;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    private String initialUsername;
    private String initialPassword;
    private String initialEmail;
    private String initialNameFirst;
    private String initialNameLast;
    private String initialID;
    private UUID uuid;


    public void initializeFields(UserModel user) {
        uuid = user.getUUID();
        initialUsername = user.getUsername();
        initialPassword = user.getPassword();
        initialEmail = user.getEmail();
        initialNameFirst = user.getNameFirst();
        initialNameLast = user.getNameLast();
        initialID = user.getID();
        usernameField.setText(initialUsername);
        passwordField.setText(initialPassword);
        emailField.setText(initialEmail);
        nameFirstField.setText(initialNameFirst);
        nameLastField.setText(initialNameLast);
        IDField.setText(initialID);
    }

    public void initialize() {

    }

    public void onFieldClicked(MouseEvent ignoredMouseEvent) {
        usernameField.setBorder(Border.EMPTY);
        passwordField.setBorder(Border.EMPTY);
        emailField.setBorder(Border.EMPTY);
        nameFirstField.setBorder(Border.EMPTY);
        nameLastField.setBorder(Border.EMPTY);
        IDField.setBorder(Border.EMPTY);
    }

    public void handleSubmitButtonAction(ActionEvent actionEvent) throws IOException {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final String email = emailField.getText();
        final String nameFirst = nameFirstField.getText();
        final String nameLast = nameLastField.getText();
        final String ID = IDField.getText();
        if (username.isBlank() || password.isBlank() || email.isBlank() || nameFirst.isBlank() || nameLast.isBlank() || ID.isBlank()) {
            errorText.setText("Please fill in all fields");
            if (username.isBlank()) usernameField.setBorder(BORDER_ERROR);
            if (password.isBlank()) passwordField.setBorder(BORDER_ERROR);
            if (email.isBlank()) emailField.setBorder(BORDER_ERROR);
            if (nameFirst.isBlank()) nameFirstField.setBorder(BORDER_ERROR);
            if (nameLast.isBlank()) nameLastField.setBorder(BORDER_ERROR);
            if (ID.isBlank()) IDField.setBorder(BORDER_ERROR);
            return;
        }
        if (!username.equals(initialUsername) && Users.getInstance().getUserByUsername(username) != null) {
            errorText.setText("Username already exists");
            usernameField.setBorder(BORDER_ERROR);
            return;
        }
        if (!email.matches("[\\w.]+@[\\w.]+\\.\\w+")) {
            errorText.setText("Please enter a valid email address");
            emailField.setBorder(BORDER_ERROR);
            return;
        }
        if (!ID.matches("\\d+")) {
            errorText.setText("Please enter a valid ID");
            IDField.setBorder(BORDER_ERROR);
            return;
        }
        if (!email.equals(initialEmail)) {
            for (UserModel user : Users.getInstance().getAllUsersList()) {
                if (user.getEmail().equals(email)) {
                    errorText.setText("Email already exists");
                    emailField.setBorder(BORDER_ERROR);
                    return;
                }
            }
        }
        if (!ID.equals(initialID)) {
            for (UserModel user : Users.getInstance().getAllUsersList()) {
                if (user.getID().equals(ID)) {
                    errorText.setText("ID already exists");
                    IDField.setBorder(BORDER_ERROR);
                    return;
                }
            }
        }
        final UserModel user = new UserModel(username, password, nameFirst, nameLast, ID, email, AccessLevel.USER);
        Users.getInstance().editUser(uuid, user);
        Utilities.changeScene(actionEvent, "main-view.fxml");
    }

    public void handleCancelButtonAction(ActionEvent actionEvent) throws IOException {
        Utilities.changeScene(actionEvent, "main-view.fxml");
    }
}
