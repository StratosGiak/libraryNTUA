module com.stratos.giak.libraryntua {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires javafx.base;
    requires org.controlsfx.controls;

    opens com.stratos.giak.libraryntua to javafx.fxml;
    exports com.stratos.giak.libraryntua;
}