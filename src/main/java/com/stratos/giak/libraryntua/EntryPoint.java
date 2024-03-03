package com.stratos.giak.libraryntua;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

//TODO ADD DOCS
public class EntryPoint extends Application {
    //TODO ADD DOCS
    public void stop() {
        try {
            Users.getInstance().saveUsers();
            Books.getInstance().saveBooks();
            Genres.getInstance().saveGenres();
            Loans.getInstance().saveLoans();
        } catch (IOException e) {
            throw new RuntimeException("File not found");
        }
    }

    //TODO ADD DOCS
    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("NTUA-Library");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("welcome.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(600);
        stage.setMinWidth(stage.getWidth());
    }
}