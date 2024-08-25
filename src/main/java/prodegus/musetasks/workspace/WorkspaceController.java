package prodegus.musetasks.workspace;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.Today;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.login.Settings.*;
import static prodegus.musetasks.school.LocationModel.getLocationListFromDB;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.school.SchoolModel.getInstrumentListFromDB;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.useractions.Actions.processLessonChanges;

public class WorkspaceController implements Initializable {

    // Workspace - Fields ----------------------------------------------------------------------------------------------

    @FXML private ToggleButton toggleContacts;
    @FXML private ToggleButton toggleEmail;
    @FXML private ToggleButton toggleLessons;
    @FXML private ToggleButton toggleSettings;
    @FXML private ToggleButton toggleToday;

    @FXML private StackPane displayPane;
    private final SimpleIntegerProperty selectedDisplay = new SimpleIntegerProperty();

    private final int DISPLAY_TODAY = 1;
    private final int DISPLAY_LESSONS = 2;
    private final int DISPLAY_CONTACTS = 3;
    private final int DISPLAY_EMAIL = 4;

    // Workspace - Methods ---------------------------------------------------------------------------------------------

    @FXML
    void displayToday(ActionEvent event) {
        toggleToday.setSelected(true);
        selectedDisplay.set(DISPLAY_TODAY);
        setScene(displayPane, getClass().getResource("/fxml/workspace-today.fxml"));
    }

    @FXML
    void displayContacts(ActionEvent event) {
        toggleContacts.setSelected(true);
        selectedDisplay.set(DISPLAY_CONTACTS);
        setScene(displayPane, getClass().getResource("/fxml/workspace-contacts.fxml"));
    }

    @FXML
    void displayLessons(ActionEvent event) {
        toggleLessons.setSelected(true);
        selectedDisplay.set(DISPLAY_LESSONS);
        setScene(displayPane, getClass().getResource("/fxml/workspace-lessons.fxml"));
    }

    @FXML
    void displayEmail(ActionEvent event) {
        toggleEmail.setSelected(true);
        selectedDisplay.set(DISPLAY_EMAIL);
        setScene(displayPane, getClass().getResource("/fxml/workspace-email.fxml"));
    }

    @FXML
    void displaySettings(ActionEvent event) {
        toggleSettings.setSelected(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/workspace-settings.fxml"));
        Stage stage = newStage("Einstellungen", loader);
        stage.showAndWait();
        selectToggle(selectedDisplay.get());
    }

    private void selectToggle(int selectedDisplay) {
        if (selectedDisplay == 0) return;
        switch (selectedDisplay) {
            case DISPLAY_TODAY -> toggleToday.setSelected(true);
            case DISPLAY_LESSONS -> toggleLessons.setSelected(true);
            case DISPLAY_CONTACTS -> toggleContacts.setSelected(true);
            case DISPLAY_EMAIL -> toggleEmail.setSelected(true);
        }
    }

    FXMLLoader setScene(Pane root, URL url) {
        FXMLLoader loader = new FXMLLoader(url);
        try {
            HBox newView = loader.load();
            root.getChildren().setAll(newView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return loader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // process changes
        processLessonChanges();

        // Initialize instruments
        SCHOOL_INSTRUMENTS.setAll(getInstrumentListFromDB());

        // Initialize locations
        SCHOOL_LOCATIONS.setAll(getLocationListFromDB());

        displayToday(new ActionEvent());
    }
}
