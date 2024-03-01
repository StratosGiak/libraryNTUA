package com.stratos.giak.libraryntua;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.Predicate;

import static com.stratos.giak.libraryntua.Utilities.integerFilter;

public class BookListController {
    private final ObservableList<BookModel> books = Books.getInstance().getBooksList();
    private final SimpleObjectProperty<Predicate<BookModel>> titlePredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> authorPredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> yearOfPublicationPredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> genrePredicate = new SimpleObjectProperty<>(t -> true);
    @FXML
    public TextField searchTitleField;
    @FXML
    public TableView<BookModel> tableViewBooks;
    @FXML
    public TextField searchAuthorField;
    @FXML
    public TextField searchYearOfPublicationField;
    @FXML
    public ChoiceBox<GenreModel> searchGenreField;
    @FXML
    public Button addBookButton;
    @FXML
    public Button editBookButton;
    @FXML
    public Button removeBookButton;

    public void initialize() {
        addBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN);
        addBookButton.managedProperty().bind(addBookButton.visibleProperty());
        editBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN);
        editBookButton.managedProperty().bind(editBookButton.visibleProperty());
        removeBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN);
        removeBookButton.managedProperty().bind(removeBookButton.visibleProperty());
        editBookButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewBooks.getSelectionModel().selectedItemProperty().get() == null, tableViewBooks.getSelectionModel().selectedItemProperty()));
        removeBookButton.disableProperty().bind(Bindings.createBooleanBinding(() -> tableViewBooks.getSelectionModel().selectedItemProperty().get() == null, tableViewBooks.getSelectionModel().selectedItemProperty()));

        searchYearOfPublicationField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));
        searchGenreField.setItems(Genres.getInstance().getGenresList());
        searchGenreField.converterProperty().set(new StringConverter<>() {
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
                        ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.EditBookEvent(row.getItem()));
                    } else {
                        ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.ViewBookDetailsEvent(row.getItem()));
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
        for (TableColumn columnName : tableViewBooks.getColumns()) {
            columnName.setReorderable(false);
            columnName.setCellFactory(column -> new TableCell<UserModel, Object>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                        return;
                    }
                    setText(item.toString());
                    Tooltip t = new Tooltip(item.toString());
                    t.setShowDelay(new Duration(300));
                    setTooltip(t);
                }
            });
        }
//        AutoCompletionBinding<String> autocompletion = TextFields.bindAutoCompletion(searchTitleField, Genres.getInstance().getGenresList());
//        searchTitleField.setOnMouseClicked(event -> {
//            if (searchTitleField.getText().isBlank()) {
//                autocompletion.getAutoCompletionPopup().getSuggestions().setAll(Genres.getInstance().getGenresList());
//            }
//            autocompletion.getAutoCompletionPopup().show(searchTitleField);
//        });
    }

    @FXML
    public void handleAddBookButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.EditBookEvent());
    }

    @FXML
    public void handleEditBookButtonAction(ActionEvent actionEvent) {
        BookModel selectedBook = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) return;
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.EditBookEvent(selectedBook));
    }

    @FXML
    public void handleRemoveBookButtonAction(ActionEvent actionEvent) {
        BookModel selectedBook = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) return;
        if (CustomAlerts.showDeleteBookAlert()) {
            Books.getInstance().removeBook(selectedBook);
        }
    }

    public void handleViewBookDetailsButtonAction(ActionEvent actionEvent) {
        BookModel selectedBook = tableViewBooks.getSelectionModel().getSelectedItem();
        if (selectedBook == null) return;
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.ViewBookDetailsEvent(selectedBook));
    }
}
