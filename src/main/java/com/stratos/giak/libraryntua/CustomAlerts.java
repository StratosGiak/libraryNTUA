package com.stratos.giak.libraryntua;

import javafx.scene.control.Alert;

public class CustomAlerts {
    public static void showPrivilegesAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Illegal action");
        alert.setContentText("You don't have the privileges required to perform this action");
        alert.showAndWait();
    }

    public static void showMaxBorrowedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Illegal action");
        alert.setContentText("You can only borrow a maximum of 2 books at a time");
        alert.showAndWait();
    }
}
