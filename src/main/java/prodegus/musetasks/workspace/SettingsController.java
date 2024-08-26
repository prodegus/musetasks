package prodegus.musetasks.workspace;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import prodegus.musetasks.database.Filter;
import prodegus.musetasks.school.AddLocationController;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.popup.PopupWindow;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.mail.EmailModel.getMailSender;
import static prodegus.musetasks.mail.EmailModel.getMailUser;
import static prodegus.musetasks.school.LocationModel.*;
import static prodegus.musetasks.school.SchoolModel.*;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.Nodes.*;

public class SettingsController implements Initializable {

    @FXML private TextField schoolNameTextField;
    @FXML private HBox instrumentHBox;
    @FXML private HBox location1HBox;
    @FXML private Label location1Label;
    @FXML private Label location1RoomsLabel;
    @FXML private HBox location2HBox;
    @FXML private Label location2Label;
    @FXML private Label location2RoomsLabel;
    @FXML private HBox location3HBox;
    @FXML private Label location3Label;
    @FXML private Label location3RoomsLabel;
    @FXML private HBox location4HBox;
    @FXML private Label location4Label;
    @FXML private Label location4RoomsLabel;
    @FXML private HBox location5HBox;
    @FXML private Label location5Label;
    @FXML private Label location5RoomsLabel;
    @FXML private Button addLocationButton;
    @FXML private Label mailUserLabel;
    @FXML private Label senderNameLabel;
    @FXML private Button addEditMailData;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private boolean cancelled = false;

    @FXML
    void addLocation(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings-addlocation.fxml"));
        Stage stage = newStage("Standort hinzufügen", loader);
        stage.showAndWait();
        refreshLocations();
    }

    @FXML
    void cancel(ActionEvent event) {
        setCancelled(true);
        stageOf(event).close();
    }

    @FXML
    void confirm(ActionEvent event) {
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();

        String name = schoolNameTextField.getText();
        List<String> instruments = new ArrayList<>();

        for (Node node : getAllNodes(instrumentHBox)) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) instruments.add(checkBox.getText());
            }
        }

        String mailUser = getMailUser();
        String senderName = getMailSender();

        if (name.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Name der Musikschule eingeben\n");
        }

        if (instruments.isEmpty()) {
            invalidData = true;
            errorMessage.append("- Bitte mindestens ein Unterrichtsfach auswählen\n");
        }

        if (getLocationListFromDB().isEmpty()) {
            invalidData = true;
            errorMessage.append("- Bitte mindestens einen Standort anlegen\n");
        }

        if (mailUser == null) {
            invalidData = true;
            errorMessage.append("- Bitte E-Mail-Account verknüpfen\n");
        }

        if (invalidData) {
            PopupWindow.displayInformation("Einstellungen konnten nicht gespeichert werden: \n\n" + errorMessage);
            return;
        }

        setSchoolName(name);
        for (String instrument : instruments) insertInstrument(instrument);
        stageOf(event).close();
    }

    @FXML
    void deleteLocation1(ActionEvent event) {
        deleteLocation(location1Label);
    }

    @FXML
    void deleteLocation2(ActionEvent event) {
        deleteLocation(location2Label);
    }

    @FXML
    void deleteLocation3(ActionEvent event) {
        deleteLocation(location3Label);
    }

    @FXML
    void deleteLocation4(ActionEvent event) {
        deleteLocation(location4Label);
    }

    @FXML
    void deleteLocation5(ActionEvent event) {
        deleteLocation(location5Label);
    }

    @FXML
    void editLocation1(ActionEvent event) {
        editLocation(location1Label);
    }

    @FXML
    void editLocation2(ActionEvent event) {
        editLocation(location2Label);
    }

    @FXML
    void editLocation3(ActionEvent event) {
        editLocation(location3Label);
    }

    @FXML
    void editLocation4(ActionEvent event) {
        editLocation(location4Label);
    }

    @FXML
    void editLocation5(ActionEvent event) {
        editLocation(location5Label);
    }

    @FXML
    void setMailAccount(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings-mail.fxml"));
        Stage stage = newStage("E-Mail-Konto hinzufügen", loader);
        stage.showAndWait();
        mailUserLabel.setText(getMailUser());
        senderNameLabel.setText(getMailSender());
    }

    private void deleteLocation(Label locationLabel) {
        if (PopupWindow.displayYesNo("Standort " + locationLabel.getText() + " wird gelöscht.\nFortfahren?")) {
            delete(LOCATION_TABLE, new Filter("name", locationLabel.getText()));
        }
        refreshLocations();
    }

    private void editLocation(Label locationLabel) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings-addlocation.fxml"));
        Stage stage = newStage("Standort bearbeiten", loader);
        AddLocationController controller = loader.getController();
        controller.init(fromString(locationLabel.getText()));
        stage.showAndWait();
        refreshLocations();
    }

    private void refreshLocations() {
        hide(location1HBox, location2HBox, location3HBox, location4HBox, location5HBox);
        show(addLocationButton);
        List<Location> locations = getLocationListFromDB();
        if (locations.size() > 0) {
            location1Label.setText(locations.get(0).getName());
            location1RoomsLabel.setText(" (" + locations.get(0).rooms().size() + (locations.get(0).rooms().size() > 1 ? " Räume" : " Raum") + ")");
            show(location1HBox);
        }
        if (locations.size() > 1) {
            location2Label.setText(locations.get(1).getName());
            location2RoomsLabel.setText(" (" + locations.get(1).rooms().size() + (locations.get(1).rooms().size() > 1 ? " Räume" : " Raum") + ")");
            show(location2HBox);
        }
        if (locations.size() > 2) {
            location3Label.setText(locations.get(2).getName());
            location3RoomsLabel.setText(" (" + locations.get(2).rooms().size() + (locations.get(2).rooms().size() > 1 ? " Räume" : " Raum") + ")");
            show(location3HBox);
        }
        if (locations.size() > 3) {
            location4Label.setText(locations.get(3).getName());
            location4RoomsLabel.setText(" (" + locations.get(3).rooms().size() + (locations.get(3).rooms().size() > 1 ? " Räume" : " Raum") + ")");
            show(location4HBox);
        }
        if (locations.size() > 4) {
            location5Label.setText(locations.get(4).getName());
            location5RoomsLabel.setText(" (" + locations.get(4).rooms().size() + (locations.get(4).rooms().size() > 1 ? " Räume" : " Raum") + ")");
            show(location5HBox);
            hide(addLocationButton);
        }
    }

    public void setSchoolName(String name) {
        if (isEmptyTable(SCHOOL_TABLE)) insert(SCHOOL_TABLE, "name", "'" + name + "'");
        else update(SCHOOL_TABLE, "1", "name", "'" + name + "'");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> instruments = getInstrumentListFromDB();
        schoolNameTextField.setText(getSchoolNameFromDB());
        for (Node node : getAllNodes(instrumentHBox)) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                checkBox.setSelected(instruments.contains(checkBox.getText()));
            }
        }
        refreshLocations();
        if (getMailUser() != null) {
            mailUserLabel.setText(getMailUser());
            senderNameLabel.setText(getMailSender());
        }
    }
}
