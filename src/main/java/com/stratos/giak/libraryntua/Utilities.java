package com.stratos.giak.libraryntua;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class Utilities {
    public static final DecimalFormat ratingsFormat = new DecimalFormat("#.##");

    public static final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) return change;
        return null;
    };

    public static void changeScene(ActionEvent event, String FXMLFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource(FXMLFile)));
        Parent newRoot = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(newRoot);
        stage.show();
    }
}
