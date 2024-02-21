package com.stratos.giak.libraryntua;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.util.function.Predicate;

import static com.stratos.giak.libraryntua.Utilities.integerFilter;

public class BookListController {
    private final ObservableList<BookModel> books = Books.getInstance().getAllBooksList();
    private final SimpleObjectProperty<Predicate<BookModel>> titlePredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> authorPredicate = new SimpleObjectProperty<>(t -> true);
    private final SimpleObjectProperty<Predicate<BookModel>> yearOfPublicationPredicate = new SimpleObjectProperty<>(t -> true);
    @FXML
    public TextField searchTitleField;
    @FXML
    public TableView<BookModel> tableViewBooks;
    @FXML
    public TextField searchAuthorField;
    @FXML
    public TextField searchYearOfPublicationField;
    @FXML
    public Button addBookButton;

    public void initialize() {
        addBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN);
        addBookButton.managedProperty().bind(addBookButton.visibleProperty());
        searchYearOfPublicationField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));


        FilteredList<BookModel> filteredBooks = new FilteredList<>(books, null);

        filteredBooks.predicateProperty().bind(new ObjectBinding<Predicate<BookModel>>() {
            {
                super.bind(titlePredicate, authorPredicate, yearOfPublicationPredicate);
            }

            @Override
            protected Predicate<BookModel> computeValue() {
                final Predicate<BookModel> title = titlePredicate.getValue() != null ? titlePredicate.getValue() : t -> true;
                final Predicate<BookModel> author = authorPredicate.getValue() != null ? authorPredicate.getValue() : t -> true;
                final Predicate<BookModel> yearOfPublication = yearOfPublicationPredicate.getValue() != null ? yearOfPublicationPredicate.getValue() : t -> true;
                return title.and(author).and(yearOfPublication);
            }
        });
        tableViewBooks.setRowFactory(tableView -> {
            TableRow<BookModel> row = new TableRow<>();
            row.setOnMouseClicked(clickEvent -> {
                if (clickEvent.getClickCount() > 1) {
                    if (row.getItem() == null) return;
                    ((Node) clickEvent.getSource()).fireEvent(new CustomEvents.CreateBookEvent(row.getItem()));
                }
            });
            return row;
        });
        tableViewBooks.setItems(filteredBooks);

        searchTitleField.textProperty().addListener((obs, oldValue, newValue) ->
                titlePredicate.set(book -> book.getTitle().toLowerCase().contains(newValue.toLowerCase().trim()))
        );
        searchAuthorField.textProperty().addListener((obs, oldValue, newValue) ->
                authorPredicate.set(book -> book.getAuthor().toLowerCase().contains(newValue.toLowerCase().trim()))
        );
        searchYearOfPublicationField.textProperty().addListener((obs, oldValue, newValue) ->
                yearOfPublicationPredicate.set(book -> newValue.isBlank() || book.getYearOfPublication() == Integer.parseInt(newValue))
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

    public void handleAddBookButtonAction(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).fireEvent(new CustomEvents.CreateBookEvent());
    }
}
