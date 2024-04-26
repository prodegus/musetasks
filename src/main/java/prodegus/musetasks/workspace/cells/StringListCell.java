package prodegus.musetasks.workspace.cells;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Student;

public class StringListCell extends ListCell<String> {
    public StringListCell() {}

    @Override protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText("auswählen");
        } else {
            setText(item);
        }

    }
}