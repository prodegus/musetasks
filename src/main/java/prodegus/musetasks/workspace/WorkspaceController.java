package prodegus.musetasks.workspace;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.login.Settings.*;
import static prodegus.musetasks.school.LocationModel.getLocationListFromDB;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.school.SchoolModel.getInstrumentListFromDB;
import static prodegus.musetasks.useractions.Actions.processLessonChanges;

public class WorkspaceController implements Initializable {

    // Workspace - Fields ----------------------------------------------------------------------------------------------

    @FXML private HBox displayContacts;
    @FXML private HBox displayLessons;
    @FXML private StackPane displayPane;

    // Workspace - Methods ---------------------------------------------------------------------------------------------

    @FXML
    void displayContacts(ActionEvent event) {
        FXMLLoader contactsLoader = setScene(displayPane, getClass().getResource("/fxml/workspace-contacts.fxml"));

    }

    @FXML
    void displayLessons(ActionEvent event) {
        FXMLLoader lessonsLoader = setScene(displayPane, getClass().getResource("/fxml/workspace-lessons.fxml"));
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
    }
}
