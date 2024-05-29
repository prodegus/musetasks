package prodegus.musetasks.workspace.cells;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.school.Location;

public class LocationListCell extends ListCell<Location> {
    public LocationListCell() {}

    @Override protected void updateItem(Location item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText("Standort auswählen");
        } else {
            setText(item.getName());
        }

    }
}