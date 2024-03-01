package com.stratos.giak.libraryntua;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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

    public static boolean showDeleteBookAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete book");
        alert.setContentText("Really delete book? This action cannot be undone");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }

    public static boolean showDeleteUserAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete user");
        alert.setContentText("Really delete user? This action cannot be undone");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }

    public static boolean showEndLoanAlert(LoanModel loan) {
        if (loan == null || loan.getUser() == null || loan.getBook() == null)
            throw new IllegalArgumentException("Null user or book");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("End book loan");
        alert.setContentText("Really end loan of " + loan.getBook().getTitle() + " by " + loan.getUser().getUsername() + "?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }

    public static boolean showLogoutAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setContentText("Really logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }
}
