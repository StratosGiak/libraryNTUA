package com.stratos.giak.libraryntua;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.util.Objects;

//TODO ADD DOCS
public class WelcomeController {

    @FXML
    private SplitPane splitPane;

    @FXML
    private void initialize() {
        splitPane.addEventFilter(CustomEvents.LINK_REGISTER_EVENT, event -> {
            try {
                changeRightPane("register.fxml");
                event.consume();
            } catch (IOException ex) {
                throw new RuntimeException("File not found");
            }
        });
        splitPane.addEventFilter(CustomEvents.LINK_LOGIN_EVENT, event -> {
            try {
                changeRightPane("login.fxml");
                event.consume();
            } catch (IOException ex) {
                throw new RuntimeException("File not found");
            }
        });
    }

    //TODO ADD DOCS
    public void changeRightPane(String FXMLFile) throws IOException {
        Parent newPane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FXMLFile)));
        splitPane.getItems().set(1, newPane);
    }
}
