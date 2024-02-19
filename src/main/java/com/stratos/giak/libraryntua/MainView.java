package com.stratos.giak.libraryntua;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Predicate;

import static com.stratos.giak.libraryntua.Utilities.integerFilter;

public class MainView {
    private final ObservableList<UserModel> users = Users.getInstance().getAllUsersList();
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
    @FXML
    public Button addUserButton;
    @FXML
    private TableView<UserModel> tableViewUsers;
    @FXML
    private TabPane tabPane;

    public void initialize() {
        addBookButton.setOnAction(event -> {
            try {
                handleAddBookButtonAction(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        addBookButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN);
        addBookButton.managedProperty().bind(addBookButton.visibleProperty());

        addUserButton.setOnAction(event -> {
            try {
                handleAddUserButtonAction(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        addUserButton.visibleProperty().set(LoggedUser.getInstance().getUser().getAccessLevel() == AccessLevel.ADMIN);
        addUserButton.managedProperty().bind(addUserButton.visibleProperty());
        searchYearOfPublicationField.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, integerFilter));
        FilteredList<UserModel> filteredUsers = new FilteredList<>(users, null);
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
                    System.out.println("CLICKED");
                }
            });
            return row;
        });

        tableViewUsers.setItems(filteredUsers);
        tableViewBooks.setItems(filteredBooks);

        searchTitleField.textProperty().addListener((obs, oldValue, newValue) -> {
            titlePredicate.set(book -> book.getTitle().toLowerCase().contains(newValue.toLowerCase().trim()));
        });
        searchAuthorField.textProperty().addListener((obs, oldValue, newValue) -> {
            authorPredicate.set(book -> book.getAuthor().toLowerCase().contains(newValue.toLowerCase().trim()));
        });
        searchYearOfPublicationField.textProperty().addListener((obs, oldValue, newValue) -> {
            yearOfPublicationPredicate.set(book -> newValue.isBlank() || book.getYearOfPublication() == Integer.parseInt(newValue));
        });
//        AutoCompletionBinding<String> autocompletion = TextFields.bindAutoCompletion(searchTitleField, Genres.getInstance().getGenresList());
//        searchTitleField.setOnMouseClicked(event -> {
//            if (searchTitleField.getText().isBlank()) {
//                autocompletion.getAutoCompletionPopup().getSuggestions().setAll(Genres.getInstance().getGenresList());
//            }
//            autocompletion.getAutoCompletionPopup().show(searchTitleField);
//        });
        for (TableColumn columnName : tableViewUsers.getColumns()) {
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
    }

    @FXML
    protected void handleAddUserButtonAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource("edit-user.fxml")));
        Parent root = loader.load();
        Tab tab = new Tab("Add User", root);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }

    public void handleAddBookButtonAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(Utilities.class.getResource("create-book.fxml")));
        Parent root = loader.load();
        Tab tab = new Tab("Add Book", root);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
    }
}
