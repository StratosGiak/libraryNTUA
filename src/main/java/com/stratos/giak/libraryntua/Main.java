package com.stratos.giak.libraryntua;

import com.stratos.giak.libraryntua.databases.Books;
import com.stratos.giak.libraryntua.databases.Genres;
import com.stratos.giak.libraryntua.databases.Loans;
import com.stratos.giak.libraryntua.databases.Users;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public void stop() throws IOException {
        Users.getInstance().saveUsers();
        Books.getInstance().saveBooks();
        Genres.getInstance().saveGenres();
        Loans.getInstance().saveLoans();
    }

    public void start(Stage stage) throws IOException {
        File f = new File("medialab");
        if (!f.isDirectory() && !f.mkdir()) {
            throw new RuntimeException("Cannot create directory");
        }
        stage.setTitle("NTUA-Library");
        stage.setResizable(false);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(600);
        stage.setMinWidth(700);
    }
}