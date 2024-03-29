package com.stratos.giak.libraryntua.utilities;

import com.stratos.giak.libraryntua.databases.Books;
import com.stratos.giak.libraryntua.models.GenreModel;
import com.stratos.giak.libraryntua.models.LoanModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlerts {
    /**
     * Shows an alert dialog warning the user that they don't have the required access privileges for an action.
     */
    public static void showPrivilegesAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Illegal action");
        alert.setContentText("You don't have the privileges required to perform this action");
        alert.showAndWait();
    }

    /**
     * Shows an alert dialog warning the user that they have exceeded their maximum allowance of book loans.
     */
    public static void showMaxBorrowedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Illegal action");
        alert.setContentText("You can only borrow a maximum of 2 books at a time");
        alert.showAndWait();
    }

    /**
     * Shows an alert dialog informing the user they have successfully requested a book loan.
     */
    public static void showRequestLoanAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Book loan requested");
        alert.setContentText("Book loan requested successfully!");
        alert.showAndWait();
    }

    /**
     * Shows an alert warning the user before removing a book.
     *
     * @return true or false based on whether the user confirmed the removal
     */
    public static boolean showRemoveBookAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete book");
        alert.setContentText("Really delete book? This action cannot be undone");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }

    /**
     * Shows an alert warning the user before removing a user account.
     *
     * @return true or false based on whether the user confirmed the removal
     */
    public static boolean showRemoveUserAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete user");
        alert.setContentText("Really delete user? This action cannot be undone");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }

    /**
     * Shows an alert warning the user before removing a genre.
     *
     * @return true or false based on whether the user confirmed the removal
     */
    public static boolean showRemoveGenreAlert(GenreModel genre) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete genre");
        int number = Books.getInstance().getBooksList().filtered(book -> book.getGenre().equals(genre)).size();
        alert.setContentText("Really delete genre? All books in this genre (" + number + " books) will also be deleted. This action cannot be undone");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }

    /**
     * Shows an alert warning the user before ending a loan.
     *
     * @return true or false based on whether the user confirmed the action
     */
    public static boolean showEndLoanAlert(LoanModel loan) {
        if (loan == null || loan.getUser() == null || loan.getBook() == null)
            throw new IllegalArgumentException("Null user or book");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("End book loan");
        alert.setContentText("Really end loan of " + loan.getBook().getTitle() + " by " + loan.getUser().getUsername() + "?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }

    /**
     * Shows an alert warning the user before logging out.
     *
     * @return true or false based on whether the user confirmed the action
     */
    public static boolean showLogoutAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setContentText("Really logout?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.CANCEL);
        return alert.showAndWait().orElse(ButtonType.CANCEL).equals(ButtonType.YES);
    }
}
