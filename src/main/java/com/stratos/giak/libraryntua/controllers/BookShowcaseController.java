package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.databases.Books;
import com.stratos.giak.libraryntua.models.BookModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.controlsfx.control.Rating;

import java.util.List;

public class BookShowcaseController {
    @FXML
    private HBox showcaseRow;

    @FXML
    private void initialize() {
        if (Books.getInstance().getBooksList().isEmpty()) {
            VBox vBox = new VBox(new Text("There are currently no books in the library"));
            vBox.alignmentProperty().set(Pos.CENTER);
            showcaseRow.getChildren().add(vBox);
        } else {
            List<BookModel> topBooks = Books.getInstance().getBooksList().sorted((o1, o2) -> {
                if (o1.getRatingsCount() == 0) return 1;
                if (o2.getRatingsCount() == 0) return -1;
                return Float.compare((float) o2.getRatingsSum() / o2.getRatingsCount(), (float) o1.getRatingsSum() / o1.getRatingsCount());
            }).subList(0, Math.min(Books.getInstance().getBooksList().size(), 5));
            for (BookModel book : topBooks) {
                Rating rating = new Rating(5);
                rating.setPartialRating(true);
                rating.ratingProperty().bind(Bindings.createDoubleBinding(() -> book.ratingsCountProperty().getValue() != 0 ? (double) book.ratingsSumProperty().getValue() / book.ratingsCountProperty().getValue() : 0, book.ratingsCountProperty(), book.ratingsSumProperty()));
                rating.setMouseTransparent(true);
                rating.setFocusTraversable(false);
                Text text = new Text();
                text.textProperty().bind(Bindings.createStringBinding(() -> "(" + book.ratingsCountProperty().get() + ")", book.ratingsCountProperty()));
                text.setFont(new Font(24));
                HBox ratingField = new HBox(15, rating, text);
                ratingField.setStyle("-fx-scale-x: 0.6; -fx-scale-y: 0.6;");
                VBox vBox = new VBox(
                        new Text(book.getTitle()),
                        new Text("by " + book.getAuthor()),
                        new Text("ISBN - " + book.getISBN()),
                        new Group(ratingField)
                );
                vBox.setSpacing(6);
                vBox.alignmentProperty().set(Pos.CENTER);
                showcaseRow.getChildren().add(vBox);
            }
        }
    }
}
