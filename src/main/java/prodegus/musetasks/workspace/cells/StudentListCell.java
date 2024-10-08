package prodegus.musetasks.workspace.cells;

import javafx.scene.control.ListCell;
import prodegus.musetasks.contacts.Student;

public class StudentListCell extends ListCell<Student> {
    public StudentListCell() {}

    @Override protected void updateItem(Student item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText("Schüler auswählen");
        } else {
            setText(item.getLastName() + ", " + item.getFirstName());
        }

    }
}