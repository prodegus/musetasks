package prodegus.musetasks.test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import prodegus.musetasks.contacts.Student;

import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.StudentModel.getStudentListFromDB;

public class TestController implements Initializable {

    @FXML
    private ComboBox<Student> editableComboBox;

    @FXML
    private Label editableValue;

    @FXML
    private ComboBox<Student> nonEditableComboBox;

    @FXML
    private Label nonEditableValue;

    @FXML
    void editableOk(ActionEvent event) {
        editableValue.setText(editableComboBox.getValue().getClass().getSimpleName());
    }

    @FXML
    void nonEditableOk(ActionEvent event) {
        nonEditableValue.setText(nonEditableComboBox.getValue().getClass().getSimpleName());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editableComboBox.setItems(getStudentListFromDB());
        nonEditableComboBox.setItems(getStudentListFromDB());
    }
}
