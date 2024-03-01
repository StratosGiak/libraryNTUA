package com.stratos.giak.libraryntua;

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
    public HBox showcaseRow;

    public void initialize() {
        if (Books.getInstance().getBooksList().isEmpty()) {
            VBox vBox = new VBox(new Text("There are currently no books in the library"));
            vBox.alignmentProperty().set(Pos.CENTER);
            showcaseRow.getChildren().add(vBox);
        } else {
            List<BookModel> topBooks = Books.getInstance().getBooksList().sorted((o1, o2) -> {
                if (o1.getRatingsCount() == 0) return -1;
                if (o2.getRatingsCount() == 0) return 1;
                return Float.compare((float) o1.getRatingsSum() / o1.getRatingsCount(), (float) o2.getRatingsSum() / o2.getRatingsCount());
            }).subList(0, Math.min(Books.getInstance().getBooksList().size(), 5));
            for (BookModel book : topBooks) {
                Rating rating = new Rating(5, book.getRatingsCount() != 0 ? book.getRatingsSum() / book.getRatingsCount() : 0);
                rating.setMouseTransparent(true);
                rating.setFocusTraversable(false);
                Text text = new Text("(" + book.getRatingsCount() + ")");
                text.setFont(new Font(24));
                HBox hbox = new HBox(15, rating, text);
                hbox.getStyleClass().add("rating-bar");
                VBox vBox = new VBox(
                        new Text(book.getTitle()),
                        new Text(book.getAuthor()),
                        new Text("ISBN " + book.getISBN()),
                        new Group(hbox)
                );
                vBox.setSpacing(6);
                vBox.alignmentProperty().set(Pos.CENTER);
                showcaseRow.getChildren().add(vBox);
            }
        }
    }
}
