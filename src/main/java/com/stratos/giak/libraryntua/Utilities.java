package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

//TODO ADD DOCS
public class Utilities {
    //TODO ADD DOCS
    public static final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) return change;
        return null;
    };

    //TODO ADD DOCS
    public static void setTextFieldError(Node textField, boolean error) {
        if (error && !textField.getStyleClass().contains("field-error"))
            textField.getStyleClass().add("field-error");
        else if (!error && textField.getStyleClass().contains("field-error"))
            textField.getStyleClass().removeAll("field-error");
    }

    //TODO ADD DOCS
    public static void changeScene(ActionEvent event, String FXMLFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource(FXMLFile)));
        Parent newRoot = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(newRoot);
        stage.show();
        if (FXMLFile.equals("welcome.fxml")) {
            stage.sizeToScene();
            stage.setMinHeight(600);
            stage.setMinWidth(stage.getWidth());
            return;
        }
        stage.sizeToScene();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(950);

    }
}
