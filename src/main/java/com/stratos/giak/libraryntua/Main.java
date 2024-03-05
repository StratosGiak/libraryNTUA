package com.stratos.giak.libraryntua;

import com.stratos.giak.libraryntua.databases.Books;
import com.stratos.giak.libraryntua.databases.Genres;
import com.stratos.giak.libraryntua.databases.Loans;
import com.stratos.giak.libraryntua.databases.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    /**
     * Called right before the application exits.
     * Calls the serialization methods of all database objects, namely Users, Books, Genres and Loans.
     *
     * @throws IOException if there is an error writing to a file
     */
    public void stop() throws IOException {
        Users.getInstance().saveUsers();
        Books.getInstance().saveBooks();
        Genres.getInstance().saveGenres();
        Loans.getInstance().saveLoans();
    }

    /**
     * Called when the application starts.
     * Sets the startup screen to the one described in welcome.fxml
     *
     * @throws IOException if welcome.fxml file is missing from resources folder
     */
    public void start(Stage stage) throws IOException {
        stage.setTitle("NTUA-Library");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(600);
        stage.setMinWidth(700);
        ((SplitPane) root).setDividerPositions(1);
    }
}