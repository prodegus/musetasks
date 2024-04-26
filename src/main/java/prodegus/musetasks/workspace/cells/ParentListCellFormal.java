package prodegus.musetasks.workspace.cells;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Parent;

public class ParentListCellFormal extends ListCell<Parent> {
    public ParentListCellFormal() {}

    @Override protected void updateItem(Parent item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText("auswählen");
        } else {
            setText(item.getLastName() + ", " + item.getFirstName());
        }

    }
}