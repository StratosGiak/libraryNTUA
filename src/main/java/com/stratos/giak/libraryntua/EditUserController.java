package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;

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
    public Text titleText;
    @FXML
    public CheckBox adminField;
    public Button revertUsername;
    public Button revertPassword;
    public Button revertEmail;
    public Button revertNameFirst;
    public Button revertNameLast;
    public Button revertID;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private UserModel user;

    public void initializeFields(UserModel user) {
        this.user = user;
        if (user == null) return;
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        emailField.setText(user.getEmail());
        nameFirstField.setText(user.getNameFirst());
        nameLastField.setText(user.getNameLast());
        IDField.setText(user.getID());
        titleText.setText("Edit user " + user.getUsername());
        adminField.setDisable(LoggedUser.getInstance().getUser().equals(user));
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

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        final String email = emailField.getText();
        final String nameFirst = nameFirstField.getText();
        final String nameLast = nameLastField.getText();
        final String ID = IDField.getText();
        final AccessLevel accessLevel = adminField.isSelected() ? AccessLevel.ADMIN : AccessLevel.USER;
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
        if ((user == null || !username.equals(user.getUsername())) && Users.getInstance().getUserByUsername(username) != null) {
            errorText.setText("Username already exists");
            usernameField.setBorder(BORDER_ERROR);
            return;
        }
        if (!email.matches(".+@.+")) {
            errorText.setText("Please enter a valid email address");
            emailField.setBorder(BORDER_ERROR);
            return;
        }
        if (!ID.matches(".+")) {
            errorText.setText("Please enter a valid ID");
            IDField.setBorder(BORDER_ERROR);
            return;
        }
        if ((user == null || !email.equals(user.getEmail())) && Users.getInstance().getUsersList().stream().anyMatch(user -> user.getEmail().equals(email))) {
            errorText.setText("Email already exists");
            emailField.setBorder(BORDER_ERROR);
            return;
        }
        if ((user == null || !ID.equals(user.getID())) && Users.getInstance().getUsersList().stream().anyMatch(user -> user.getID().equals(ID))) {
            errorText.setText("ID already exists");
            IDField.setBorder(BORDER_ERROR);
            return;
        }
        if (user == null) {
            final UserModel user = new UserModel(username, password, nameFirst, nameLast, ID, email, accessLevel);
            Users.getInstance().addUser(user);
        } else {
            Users.getInstance().editUser(user, username, password, nameFirst, nameLast, ID, email, accessLevel);
        }
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_USER_EVENT));
    }

    public void handleCancelButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new Event(CustomEvents.EXIT_USER_EVENT));
    }

    public void handleUsernameRevertButtonAction(ActionEvent actionEvent) {
        System.out.println("HELLO");
    }
}
