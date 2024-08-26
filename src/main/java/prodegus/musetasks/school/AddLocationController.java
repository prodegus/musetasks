package prodegus.musetasks.school;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import prodegus.musetasks.ui.popup.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import static prodegus.musetasks.school.LocationModel.insertLocation;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.Nodes.getAllNodes;

public class AddLocationController {

    @FXML private Label title;
    @FXML private TextField locationNameTextField;
    @FXML private VBox roomsVBox;

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void confirm(ActionEvent event) {
        String locationName = locationNameTextField.getText();
        List<String> rooms = new ArrayList<>();

        for (Node node : getAllNodes(roomsVBox)) {
            if (node instanceof TextField textField) {
                if (!textField.getText().isBlank()) rooms.add(textField.getText());
            }
        }

        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();

        if (locationName.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Name des Standorts eingeben\n");
        }

        if (rooms.isEmpty()) {
            invalidData = true;
            errorMessage.append("- Bitte mindestens einen Raum eingeben\n");
        }

        if (invalidData) {
            PopupWindow.displayInformation("Standort konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }

        Location location = new Location();
        location.setName(locationName);
        location.setRoom1(rooms.get(0));
        if (rooms.size() > 1) location.setRoom2(rooms.get(1));
        if (rooms.size() > 2) location.setRoom3(rooms.get(2));
        if (rooms.size() > 3) location.setRoom4(rooms.get(3));
        if (rooms.size() > 4) location.setRoom5(rooms.get(4));
        if (rooms.size() > 5) location.setRoom6(rooms.get(5));
        if (rooms.size() > 6) location.setRoom7(rooms.get(6));
        if (rooms.size() > 7) location.setRoom8(rooms.get(7));
        if (rooms.size() > 8) location.setRoom9(rooms.get(8));
        if (rooms.size() > 9) location.setRoom10(rooms.get(9));
        insertLocation(location);
        stageOf(event).close();
    }

    public void init(Location location) {
        title.setText("Standort bearbeiten");
        locationNameTextField.setText(location.getName());
        List<String> rooms = location.rooms();
        List<TextField> roomTextFields = new ArrayList<>();

        for (Node node : getAllNodes(roomsVBox)) {
            if (node instanceof TextField textField) {
                roomTextFields.add(textField);
            }
        }

        roomTextFields.get(0).setText(rooms.get(0));
        if (rooms.size() > 1) roomTextFields.get(1).setText(rooms.get(1));
        if (rooms.size() > 2) roomTextFields.get(2).setText(rooms.get(2));
        if (rooms.size() > 3) roomTextFields.get(3).setText(rooms.get(3));
        if (rooms.size() > 4) roomTextFields.get(4).setText(rooms.get(4));
        if (rooms.size() > 5) roomTextFields.get(5).setText(rooms.get(5));
        if (rooms.size() > 6) roomTextFields.get(6).setText(rooms.get(6));
        if (rooms.size() > 7) roomTextFields.get(7).setText(rooms.get(7));
        if (rooms.size() > 8) roomTextFields.get(8).setText(rooms.get(8));
        if (rooms.size() > 9) roomTextFields.get(9).setText(rooms.get(9));
    }
}
