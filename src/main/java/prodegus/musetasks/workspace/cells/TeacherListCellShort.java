package prodegus.musetasks.workspace.cells;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Teacher;

public class TeacherListCellShort extends ListCell<Teacher> {
    public TeacherListCellShort() {}

    @Override protected void updateItem(Teacher item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText("auswählen");
        } else {
            setText(item.getLastName() + ", " + item.getFirstName().charAt(0) + ".");
        }

    }
}