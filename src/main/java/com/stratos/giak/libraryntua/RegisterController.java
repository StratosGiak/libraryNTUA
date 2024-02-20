package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;

import static com.stratos.giak.libraryntua.Constants.BORDER_ERROR;

public class RegisterController {
    private final Hyperlink loginLink = new Hyperlink("Login");
    @FXML
    public TextField emailField;
    @FXML
    public TextField nameFirstField;
    @FXML
    public TextField nameLastField;
    @FXML
    public TextField IDField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text registerErrorText;
    @FXML
    private TextFlow loginTextFlow;

    public void initialize() {
        loginTextFlow.getChildren().add(new TextFlow(new Text("Already have an account? "), loginLink));
        loginLink.setOnAction(event -> loginTextFlow.fireEvent(new Event(CustomEvents.LINK_LOGIN_EVENT)));
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent actionEvent) throws IOException {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final String email = emailField.getText();
        final String nameFirst = nameFirstField.getText();
        final String nameLast = nameLastField.getText();
        final String ID = IDField.getText();
        if (username.isBlank() || password.isBlank() || email.isBlank() || nameFirst.isBlank() || nameLast.isBlank() || ID.isBlank()) {
            registerErrorText.setText("Please fill in all fields");
            if (username.isBlank()) usernameField.setBorder(BORDER_ERROR);
            if (password.isBlank()) passwordField.setBorder(BORDER_ERROR);
            if (email.isBlank()) emailField.setBorder(BORDER_ERROR);
            if (nameFirst.isBlank()) nameFirstField.setBorder(BORDER_ERROR);
            if (nameLast.isBlank()) nameLastField.setBorder(BORDER_ERROR);
            if (ID.isBlank()) IDField.setBorder(BORDER_ERROR);
            return;
        }
        if (Users.getInstance().getUserByUsername(username) != null) {
            registerErrorText.setText("Username already exists");
            usernameField.setBorder(BORDER_ERROR);
            return;
        }
        if (!email.matches(".+@.+")) {
            registerErrorText.setText("Please enter a valid email address");
            emailField.setBorder(BORDER_ERROR);
            return;
        }
        if (!ID.matches("\\d+")) {
            registerErrorText.setText("Please enter a valid ID");
            IDField.setBorder(BORDER_ERROR);
            return;
        }
        for (UserModel user : Users.getInstance().getAllUsersList()) {
            if (user.getEmail().equals(email)) {
                registerErrorText.setText("Email already exists");
                emailField.setBorder(BORDER_ERROR);
                return;
            }
            if (user.getID().equals(ID)) {
                registerErrorText.setText("ID already exists");
                IDField.setBorder(BORDER_ERROR);
                return;
            }
        }
        final UserModel user = new UserModel(username, password, nameFirst, nameLast, ID, email, AccessLevel.USER);
        Users.getInstance().addUser(user);
        LoggedUser.getInstance().setUser(user);
        Utilities.changeScene(actionEvent, "main-view.fxml");
    }

    @FXML
    protected void onFieldClicked(MouseEvent event) {
        usernameField.setBorder(Border.EMPTY);
        passwordField.setBorder(Border.EMPTY);
        emailField.setBorder(Border.EMPTY);
        nameFirstField.setBorder(Border.EMPTY);
        nameLastField.setBorder(Border.EMPTY);
        IDField.setBorder(Border.EMPTY);
    }
}