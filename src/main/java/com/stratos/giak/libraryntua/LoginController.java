package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    final BorderStroke BORDER_STROKE_ERROR = new BorderStroke(Color.RED, new BorderStrokeStyle(StrokeType.OUTSIDE, null, null, 10, 0, null), new CornerRadii(3), null);
    private final Hyperlink registerLink = new Hyperlink("Register");
    @FXML
    private Text loginErrorText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextFlow registerTextFlow;

    public void initialize() {
        registerTextFlow.getChildren().add(new TextFlow(new Text("Don't have an account? "), registerLink));
        registerLink.setOnAction(event -> {
            registerTextFlow.fireEvent(new Event(CustomEvents.LINK_REGISTER_EVENT));
        });
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) throws IOException {
        final String username = usernameField.getText();
        final String password = passwordField.getText();
        if (username.isBlank() || password.isBlank()) {
            loginErrorText.setText("Please fill in all fields");
            if (username.isBlank()) {
                usernameField.setBorder(new Border(BORDER_STROKE_ERROR));
            }
            if (password.isBlank()) {
                passwordField.setBorder(new Border(BORDER_STROKE_ERROR));
            }
            return;
        }
        final UserModel user = Users.getInstance().getUserByUsername(username);
        if (user == null) {
            loginErrorText.setText("Username doesn't exist");
            usernameField.setBorder(new Border(BORDER_STROKE_ERROR));
            return;
        }
        if (!user.getPassword().equals(password)) {
            loginErrorText.setText("Wrong password");
            usernameField.setBorder(new Border(BORDER_STROKE_ERROR));
            passwordField.setBorder(new Border(BORDER_STROKE_ERROR));
            return;
        }
        LoggedUser.getInstance().setUser(user);
        if (user.getAccessLevel() == AccessLevel.ADMIN) {
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setTitle("NTUA-Library (Admin)");
        }
        Utilities.changeScene(event, "main-view.fxml");
    }

    @FXML
    protected void onFieldClicked(MouseEvent event) {
        usernameField.setBorder(Border.EMPTY);
        passwordField.setBorder(Border.EMPTY);
    }
}