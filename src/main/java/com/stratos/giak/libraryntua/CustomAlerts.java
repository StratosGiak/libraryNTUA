package com.stratos.giak.libraryntua;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class CustomAlerts {
    public static void showPrivilegesAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Illegal action");
        alert.setContentText("You don't have the privileges required to perform this action");
        Optional<ButtonType> result = alert.showAndWait();
    }
}
