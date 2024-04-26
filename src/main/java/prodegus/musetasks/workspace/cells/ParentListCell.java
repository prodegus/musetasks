package prodegus.musetasks.workspace.cells;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Parent;

public class ParentListCell extends ListCell<Parent> {
    public ParentListCell() {}

    @Override protected void updateItem(Parent item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
        } else {
            setText(item.getLastName() + ", " + item.getFirstName());
        }

    }
}