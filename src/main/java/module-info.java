module com.stratos.giak.libraryntua {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires javafx.base;
    requires org.controlsfx.controls;

    opens com.stratos.giak.libraryntua to javafx.fxml;
    exports com.stratos.giak.libraryntua;
    exports com.stratos.giak.libraryntua.utilities;
    opens com.stratos.giak.libraryntua.utilities to javafx.fxml;
    exports com.stratos.giak.libraryntua.controllers;
    opens com.stratos.giak.libraryntua.controllers to javafx.fxml;
    exports com.stratos.giak.libraryntua.models;
    opens com.stratos.giak.libraryntua.models to javafx.fxml;
    exports com.stratos.giak.libraryntua.databases;
    opens com.stratos.giak.libraryntua.databases to javafx.fxml;
}