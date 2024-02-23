module com.stratos.giak.libraryntua {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.controlsfx.controls;

    opens com.stratos.giak.libraryntua to javafx.fxml;
    exports com.stratos.giak.libraryntua;
}