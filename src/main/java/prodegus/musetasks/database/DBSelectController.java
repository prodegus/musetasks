package prodegus.musetasks.database;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import prodegus.musetasks.ui.popup.PopupWindow;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.Strings.string;

public class DBSelectController implements Initializable {

    @FXML private Label currentDBLabel;

    @FXML
    void setDBLocation(ActionEvent event) {
        Node source = (Node) event.getSource();
        File dbFile;
        String dbPath = null;
        FileChooser fileChooser = new FileChooser();

        switch (source.getId()) {
            case "selectButton" -> {
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Datenbank", "*.db"));
                dbFile = fileChooser.showOpenDialog(source.getScene().getWindow());
                if (dbFile == null) return;
                dbPath = dbFile.getAbsolutePath();
            }
            case "createButton" -> {
                dbFile = fileChooser.showSaveDialog(source.getScene().getWindow());
                if (dbFile == null) return;
                boolean hasSuffix = dbFile.getAbsolutePath().endsWith(".db");
                dbPath = dbFile.getAbsolutePath() + (hasSuffix ? "" : ".db");
                createUserTable(dbPath);
                createStudentTable(dbPath);
                createTeacherTable(dbPath);
                createParentTable(dbPath);
                createOtherTable(dbPath);
                createLessonTable(dbPath);
                createLessonChangeTable(dbPath);
                createAppointmentTable(dbPath);
                createTaskTable(dbPath);
                createHolidayTable(dbPath);
                createLocationTable(dbPath);
                createInstrumentTable(dbPath);
                createEmailTable(dbPath);
                addConstraintsStudentTable(dbPath);
            }
        }
        saveDbPath(dbPath);
        PopupWindow.displayInformation("Datenbank ausgewählt! Bitte Anwendung erneut starten!");
        stageOf(event).close();
        Platform.exit();
    }

    @FXML
    void cancel(ActionEvent event) {
        if (!connected()) Platform.exit();
        stageOf(event).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String message = (DB_PATH.equals("Path not found") ? "Keine Datenbank ausgewählt." : "Aktuell ausgewählte Datenbank:\n" + DB_PATH) +
                "\n\nBitte wählen Sie eine bestehende Datenbank oder legen eine neue an:";
        currentDBLabel.setText(message);
    }
}
