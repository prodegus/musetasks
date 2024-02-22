package prodegus.musetasks.database;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import prodegus.musetasks.ui.PopupWindow;

import java.io.File;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class DBSelectController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button createButton;

    @FXML
    private Button selectButton;

    @FXML
    void setDBLocation(ActionEvent event) {
        Node source = (Node) event.getSource();
        File dbFile;
        String dbPath = null;
        FileChooser fileChooser = new FileChooser();

        switch (source.getId()) {
            case "selectButton":
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Datenbank", "*.db"));
                dbFile = fileChooser.showOpenDialog(source.getScene().getWindow());
                if (dbFile == null) return;
                dbPath = dbFile.getAbsolutePath();
                break;
            case "createButton":
                dbFile = fileChooser.showSaveDialog(source.getScene().getWindow());
                if (dbFile == null) return;
                dbPath = dbFile.getAbsolutePath() + ".db";
                createUserTable(dbPath);
                createContactTable(dbPath);
                createLessonTable(dbPath);
                break;
        }
        saveDbPath(dbPath);
        PopupWindow.display("Datenbank ausgewählt! Bitte Anwendung erneut starten!");
        stageOf(event).close();
        Platform.exit();
    }

    @FXML
    void cancel(ActionEvent event) {
        if (!connected()) Platform.exit();
        stageOf(event).close();
    }

}
