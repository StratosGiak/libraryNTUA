package com.stratos.giak.libraryntua.controllers;

import com.stratos.giak.libraryntua.databases.Books;
import com.stratos.giak.libraryntua.databases.Genres;
import com.stratos.giak.libraryntua.models.BookModel;
import com.stratos.giak.libraryntua.models.GenreModel;
import com.stratos.giak.libraryntua.utilities.AccessLevel;
import com.stratos.giak.libraryntua.utilities.CustomAlerts;
import com.stratos.giak.libraryntua.utilities.CustomEvents;
import com.stratos.giak.libraryntua.utilities.LoggedUser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.controlsfx.control.Rating;

import java.util.Objects;
import java.util.function.Predicate;

import static com.stratos.giak.libraryntua.utilities.Miscellaneous.integerFilter;

public class BookListController {
    private final ObservableList<BookModel> books = Books.getInstance().getBooksList();
    private final SimpleObjectProperty<Predicate<BookModel>> titlePredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> authorPredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> yearOfPublicationPredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> genrePredicate = new SimpleObjectProperty<>(t -> true);
    @FXML
    private TextField addGenreField;
    @FXML
    private GridPane gridPane;
    @FXML
    private GridPane genreListGridPane;
    @FXML
    private ListView<GenreModel> genreList;
    @FXML
    private Button addGenreButton;
    @FXML
    private Button editGenreButton;
    @FXML
    private Button removeGenreButton;
    @FXML
    private Button viewBookButton;
    @FXML
    private TextField searchTitleField;
    @FXML
    private TableView<BookModel> tableViewBooks;
    @FXML
    private TextField searchAuthorField;
    @FXML
    private TextField searchYearOfPublicationField;
    @FXML
    private ComboBox<GenreModel> searchGenreField;
    @FXML
    private Button addBookButton;
    @FXML
    private Button editBookButton;
    @FXML
    private Button removeBookButton;

    @FXML
    private void initialize() {
        viewBookButton.visibleProperty().set(!LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN));
        viewBookButton.managedProperty().bind(viewBookButton.visibleProperty());
        addBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN));
        addBookButton.managedProperty().bind(addBookButton.visibleProperty());
        editBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN));
        editBookButton.managedProperty().bind(editBookButton.visibleProperty());
        removeBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN));
        removeBookButton.managedProperty().bind(removeBookButton.visibleProperty());
        genreListGridPane.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel().equals(AccessLevel.ADMIN));
        genreListGridPane.managedProperty().bind(genreListGridPane.visibleProperty());
        gridPane.alignmentProperty().bind(Bindings.createObjectBinding(() -> genreListGridPane.managedProperty().get() ? Pos.CENTER_LEFT : Pos.CENTER, genreListGridPane.managedProperty()));

        viewBookButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewBooks.getSelectionModel().selectedItemProperty().get() == null, tableViewBooks.getSelectionModel().selectedItemProperty()));
        editBookButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewBooks.getSelectionModel().selectedItemProperty().get() == null, tableViewBooks.getSelectionModel().selectedItemProperty()));
        removeBookButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewBooks.getSelectionModel().selectedItemProperty().get() == null, tableViewBooks.getSelectionModel().selectedItemProperty()));
        addGenreButton.disableProperty().bind(addGenreField.textProperty().isEmpty());
        editGenreButton.disableProperty().bind(Bindings.createBooleanBinding(() -> genreList.getSelectionModel().selectedItemProperty().get() == null, genreList.getSelectionModel().selectedItemProperty()));
        removeGenreButton.disableProperty().bind(Bindings.createBooleanBinding(() -> genreList.getSelectionModel().selectedItemProperty().get() == null, genreList.getSelectionModel().selectedItemProperty()));

        genreList.setItems(Genres.getInstance().getGenresList().filtered(Objects::nonNull));
        genreList.setCellFactory(list -> {
            TextFieldListCell<GenreModel> t = new TextFieldListCell<>(new StringConverter<>() {
                @Override
                public String toString(GenreModel genre) {
                    return genre.toString();
                }

                @Override
                public GenreModel fromString(String string) {
                    GenreModel genre = list.getSelectionModel().getSelectedItem();
                    genre.editGenre(string);
                    return genre;
                }
            });
            t.prefWidthProperty().bind(list.widthProperty().subtract(2));
            return t;
        });
        genreList.setOnEditCommit(event -> {
        });

        searchYearOfPublicationField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));
        searchGenreField.setItems(Genres.getInstance().getGenresList());
        searchGenreField.setConverter(new StringConverter<>() {
            @Override
            public String toString(GenreModel genre) {
                return genre != null ? genre.toString() : "All genres";
            }

            @Override
            public GenreModel fromString(String string) {
                return null;
            }
        });
        FilteredList<BookModel> filteredBooks = new FilteredList<>(books, null);
        filteredBooks.predicateProperty().bind(new ObjectBinding<Predicate<BookModel>>() {
            {
                super.bind(titlePredicate, authorPredicate, yearOfPublicationPredicate, genrePredicate);
            }

            @Override
            protected Predicate<BookModel> computeValue() {
                final Predicate<BookModel> title = titlePredicate.getValue() != null ? titlePredicate.getValue() : t -> true;
                final Predicate<BookModel> author = authorPredicate.getValue() != null ? authorPredicate.getValue() : t -> true;
                final Predicate<BookModel> yearOfPublication = yearOfPublicationPredicate.getValue() != null ? yearOfPublicationPredicate.getValue() : t -> true;
                final Predicate<BookModel> genre = genrePredicate.getValue() != null ? genrePredicate.getValue() : t -> true;
                return title.and(author).and(yearOfPublication).and(genre);
            }
        });
        tableViewBooks.setRowFactory(tableView -> {
            TableRow<BookModel> row = new TableRow<>();
            row.setOnMouseClicked(clickEvent -> {
                if (clickEvent.getClickCount() > 1) {
                    if (row.getItem() == null) return;
                    if (LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN) {
                        ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.BookEvent(CustomEvents.BookEvent.EDIT_BOOK_EVENT, row.getItem()));
                    } else {
                        ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.BookEvent(CustomEvents.BookEvent.VIEW_BOOK_DETAILS_EVENT, row.getItem()));
                    }
                }
            });
            return row;
        });
        SortedList<BookModel> sortableBooks = new SortedList<>(filteredBooks);
        sortableBooks.comparatorProperty().bind(tableViewBooks.comparatorProperty());
        tableViewBooks.setItems(sortableBooks);

        searchTitleField.textProperty().addListener((obs, oldValue, newValue) ->
                titlePredicate.set(book -> book.getTitle().toLowerCase().contains(newValue.toLowerCase().trim()))
        );
        searchAuthorField.textProperty().addListener((obs, oldValue, newValue) ->
                authorPredicate.set(book -> book.getAuthor().toLowerCase().contains(newValue.toLowerCase().trim()))
        );
        searchYearOfPublicationField.textProperty().addListener((obs, oldValue, newValue) ->
                yearOfPublicationPredicate.set(book -> newValue.isBlank() || String.valueOf(book.getYearOfPublication()).toLowerCase().contains(newValue.toLowerCase().trim()))
        );
        searchGenreField.valueProperty().addListener((obs, oldValue, newValue) ->
                genrePredicate.set(book -> newValue == null || book.getGenre().equals(newValue))
        );
        for (TableColumn column : tableViewBooks.getColumns()) {
            if (column.getText().equals("Rating")) {
                ((TableColumn<BookModel, String>) column).setComparator((a, b) -> {
                    if (a.equals(b) && a.equals("–")) return 0;
                    if (a.equals("–")) return 1;
                    if (b.equals("–")) return -1;
                    double ratingA = Double.parseDouble(a.split(" ")[0]);
                    double ratingB = Double.parseDouble(b.split(" ")[0]);
                    return ratingA < ratingB ? 1 : -1;
                });
            }
            column.setCellFactory(cellData -> new TableCell<BookModel, Object>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }
                    if (column.getText().equals("Rating")) {
                        Rating rating = new Rating(5);
                        rating.setPartialRating(true);
                        rating.setRating(!item.toString().equals("–") ? Double.parseDouble(item.toString().split(" ")[0]) : 0);
                        rating.setMouseTransparent(true);
                        rating.setFocusTraversable(false);
                        Text text = new Text();
                        text.setText(!item.toString().equals("–") ? item.toString().split(" ")[1] : "(–)");
                        text.setFont(new Font(24));
                        HBox ratingField = new HBox(15, rating, text);
                        ratingField.setStyle("-fx-scale-x: 0.5; -fx-scale-y: 0.5;");
                        setGraphic(new Group(ratingField));
                    } else {
                        setText(item.toString());
                    }
                    Tooltip t = new Tooltip(item.toString());
                    t.setShowDelay(new Duration(300));
                    setTooltip(t);
                }
            });
        }
    }

    @FXML
    private void handleAddBookButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.BookEvent(CustomEvents.BookEvent.EDIT_BOOK_EVENT, null));
    }

    @FXML
    private void handleEditBookButtonAction(ActionEvent actionEvent) {
        BookModel selectedBook = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) return;
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.BookEvent(CustomEvents.BookEvent.EDIT_BOOK_EVENT, selectedBook));
    }

    @FXML
    private void handleRemoveBookButtonAction() {
        BookModel selectedBook = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null || !CustomAlerts.showRemoveBookAlert()) return;
        Books.getInstance().removeBook(selectedBook);
    }

    @FXML
    private void handleViewBookDetailsButtonAction(ActionEvent actionEvent) {
        BookModel selectedBook = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) return;
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.BookEvent(CustomEvents.BookEvent.VIEW_BOOK_DETAILS_EVENT, selectedBook));
    }

    @FXML
    private void handleAddGenreButtonAction() {
        if (addGenreField.getText().isEmpty()) return;
        GenreModel existing = genreList.getItems().stream().filter(genre -> genre.getName().toLowerCase().trim().equals(addGenreField.getText().toLowerCase().trim())).findAny().orElse(null);
        if (existing != null) {
            genreList.requestFocus();
            genreList.getSelectionModel().select(existing);
            addGenreField.clear();
            return;
        }
        Genres.getInstance().addGenre(new GenreModel(addGenreField.getText().trim()));
        addGenreField.clear();
    }

    @FXML
    private void handleEditGenreButtonAction() {
        genreList.edit(genreList.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void handleRemoveGenreButtonAction() {
        GenreModel selectedGenre = genreList.getSelectionModel().getSelectedItem();
        if (selectedGenre == null || !CustomAlerts.showRemoveGenreAlert(selectedGenre)) return;
        Genres.getInstance().removeGenre(selectedGenre);
    }
}
