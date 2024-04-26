module prodegus.musetasks {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires org.apache.poi.poi;
    requires org.apache.commons.lang3;
    requires jakarta.mail;


    opens prodegus.musetasks.login to javafx.fxml;
    exports prodegus.musetasks.login;

    opens prodegus.musetasks.database to javafx.fxml;
    exports prodegus.musetasks.database;

    opens prodegus.musetasks.workspace to javafx.fxml;
    exports prodegus.musetasks.workspace;

    opens prodegus.musetasks.contacts to javafx.fxml;
    exports prodegus.musetasks.contacts;

    opens prodegus.musetasks.test to javafx.fxml;
    exports prodegus.musetasks.test;
    exports prodegus.musetasks.workspace.cells;
    opens prodegus.musetasks.workspace.cells to javafx.fxml;
}