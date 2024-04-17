package prodegus.musetasks.workspace;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Teacher;

public class TeacherListCell extends ListCell<Teacher> {
    public TeacherListCell() {}

    @Override protected void updateItem(Teacher item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
        } else {
            setText(item.getLastname() + ", " + item.getFirstname());
        }

    }
}