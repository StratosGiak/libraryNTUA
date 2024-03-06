package com.stratos.giak.libraryntua.utilities;

import com.stratos.giak.libraryntua.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class Miscellaneous {
    /**
     * Filters out non-digit characters from a text field.
     */
    public static final UnaryOperator<TextFormatter.Change> integerFilter = change -> {
        String newText = change.getControlNewText();
        if (newText.matches("\\d*")) return change;
        return null;
    };

    /**
     * Sets the border of the given node to red, indicating an error,
     * or resets to the default border if there is no error.
     *
     * @param error indicates if there is an error or not
     */
    public static void setTextFieldError(Node node, boolean error) {
        if (error && !node.getStyleClass().contains("field-error"))
            node.getStyleClass().add("field-error");
        else if (!error && node.getStyleClass().contains("field-error"))
            node.getStyleClass().removeAll("field-error");
    }

    /**
     * Switches the scene in the current stage.
     *
     * @param event    the event that triggered the change of scene
     * @param FXMLFile the path to the FXML file for the new scene
     * @throws IOException if the FXML file is not found
     */
    public static void changeScene(ActionEvent event, String FXMLFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(FXMLFile)));
        Parent newRoot = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(newRoot);
        stage.show();
        if (FXMLFile.equals("welcome.fxml")) {
            stage.sizeToScene();
            stage.setMinHeight(600);
            stage.setMinWidth(700);
            ((SplitPane) newRoot).setDividerPositions(1);
            return;
        }
        stage.sizeToScene();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(950);
    }
}
