package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.Main;
import com.stratos.giak.libraryntua.utilities.CustomEvents;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;

import java.io.IOException;
import java.util.Objects;

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

    private void changeRightPane(String FXMLFile) throws IOException {
        Parent newPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(FXMLFile)));
        splitPane.getItems().set(1, newPane);
    }
}
