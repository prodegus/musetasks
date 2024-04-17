package prodegus.musetasks.contacts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import prodegus.musetasks.ui.PopupWindow;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class AddTeacherController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private Button cancelButton;

    @FXML
    private Button cancelButton2;

    @FXML
    private TextField cityTextField;

    @FXML
    private GridPane communicationForm;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private CheckBox fridayCheckBox;

    @FXML
    private ComboBox<String> instrumentComboBox1;

    @FXML
    private ComboBox<String> instrumentComboBox2;

    @FXML
    private ComboBox<String> instrumentComboBox3;

    @FXML
    private ComboBox<String> instrumentComboBox4;

    @FXML
    private ComboBox<String> instrumentComboBox5;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private ComboBox<String> locationComboBox1;

    @FXML
    private ComboBox<String> locationComboBox2;

    @FXML
    private ComboBox<String> locationComboBox3;

    @FXML
    private CheckBox mondayCheckBox;

    @FXML
    private TextArea notesTextArea;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private CheckBox saturdayCheckBox;

    @FXML
    private CheckBox skypeCheckBox;

    @FXML
    private ComboBox<String> skypeComboBox;

    @FXML
    private TextField streetTextField;

    @FXML
    private GridPane teacherDataForm;

    @FXML
    private CheckBox thursdayCheckBox;

    @FXML
    private Button toContactFormButton;

    @FXML
    private CheckBox tuesdayCheckBox;

    @FXML
    private CheckBox wednesdayCheckBox;

    @FXML
    private CheckBox zoomCheckBox;

    @FXML
    private ComboBox<String> zoomComboBox;

    @FXML
    void addTeacher(ActionEvent event) {
        Teacher newTeacher = new Teacher();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        String lastname = lastNameTextField.getText();
        String firstname = firstNameTextField.getText();
        String category = CATEGORY_TEACHER;
        String location1 = locationComboBox1.getSelectionModel().isEmpty() ? "" : locationComboBox1.getValue();
        String location2 = locationComboBox2.getSelectionModel().isEmpty() ? "" : locationComboBox2.getValue();
        String location3 = locationComboBox3.getSelectionModel().isEmpty() ? "" : locationComboBox3.getValue();
        String locations = location1 +
                (location2.isEmpty() ? "" : ", ") + location2 +
                (location3.isEmpty() ? "" : ", ") + location3;
        String street = streetTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String city = cityTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();
        String zoom = zoomComboBox.getSelectionModel().isEmpty() ? "" : zoomComboBox.getValue();
        String skype = skypeComboBox.getSelectionModel().isEmpty() ? "" : skypeComboBox.getValue();
        String birthDate = birthDatePicker.getEditor().getText();
        String notesInput = notesTextArea.getText();
        String notes = notesInput.isBlank() ? "" : timestamp + "\n" + notesInput;
        String instrument1 = instrumentComboBox1.getSelectionModel().isEmpty() ? "" : instrumentComboBox1.getValue();
        String instrument2 = instrumentComboBox2.getSelectionModel().isEmpty() ? "" : instrumentComboBox2.getValue();
        String instrument3 = instrumentComboBox3.getSelectionModel().isEmpty() ? "" : instrumentComboBox3.getValue();
        String instrument4 = instrumentComboBox4.getSelectionModel().isEmpty() ? "" : instrumentComboBox4.getValue();
        String instrument5 = instrumentComboBox5.getSelectionModel().isEmpty() ? "" : instrumentComboBox5.getValue();
        String instruments = instrument1 +
                (instrument2.isEmpty() ? "" : ", ") + instrument2 +
                (instrument3.isEmpty() ? "" : ", ") + instrument3 +
                (instrument4.isEmpty() ? "" : ", ") + instrument4 +
                (instrument5.isEmpty() ? "" : ", ") + instrument5;
        String activeDays =
                (mondayCheckBox.isSelected() ? "Montag " : "") +
                (tuesdayCheckBox.isSelected() ? "Dienstag " : "") +
                (wednesdayCheckBox.isSelected() ? "Mittwoch " : "") +
                (thursdayCheckBox.isSelected() ? "Donnerstag " : "") +
                (fridayCheckBox.isSelected() ? "Freitag " : "") +
                (saturdayCheckBox.isSelected() ? "Samstag " : "");
        String status = "";
        String statusFrom = "";
        String statusTo = "";

        if (lastname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Nachname eingeben\n");
        } else {
            newTeacher.setLastname(lastname);
        }

        if (firstname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Vorname eingeben\n");
        } else {
            newTeacher.setFirstname(firstname);
        }

        newTeacher.setCategory(category);
        newTeacher.setLocation(locations);
        newTeacher.setStreet(street);

        if (isInvalidPostalCode(postalCode)) {
            invalidData = true;
            errorMessage.append("- Bitte gültige Postleitzahl eingeben (5 Ziffern) oder Feld leer lassen\n");
        } else {
            newTeacher.setPostalCode(postalCodeToInt(postalCode));
        }

        newTeacher.setCity(city);
        newTeacher.setPhone(phone);
        newTeacher.setEmail(email);
        newTeacher.setZoom(zoom);
        newTeacher.setSkype(skype);

        if (isInvalidBirthDate(birthDate)) {
            invalidData = true;
            errorMessage.append("- Bitte gültiges Geburtsdatum eingeben (z.B. 01.01.2000) oder Feld leer lassen\n");
        } else {
            newTeacher.setBirthDate(birthDate);
        }
        newTeacher.setNotes(notes);

        if (instrument1.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte mindestens ein Instrument auswählen\n");
        } else {
            newTeacher.setInstruments(instruments);
        }

        newTeacher.setActiveDays(activeDays);
        newTeacher.setStatus(status);
        newTeacher.setStatusFrom(statusFrom);
        newTeacher.setStatusTo(statusTo);

        if (invalidData) {
            PopupWindow.display("Lehrer konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }
        addContact(newTeacher);
        stageOf(event).close();
    }

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void goToCommunicationForm(ActionEvent event) {
        teacherDataForm.setVisible(false);
        communicationForm.setVisible(true);
    }

    @FXML
    void goToTeacherForm(ActionEvent event) {
        teacherDataForm.setVisible(true);
        communicationForm.setVisible(false);
    }

    @FXML
    void skypeCheckBoxClicked(MouseEvent event) {
        skypeComboBox.setVisible(skypeCheckBox.isSelected());
    }

    @FXML
    void zoomCheckBoxClicked(MouseEvent event) {
        zoomComboBox.setVisible(zoomCheckBox.isSelected());
    }

    private boolean isInvalidBirthDate(String birthDate) {
        if (birthDate.isBlank()) return false;
        try {
            birthDatePicker.getConverter().fromString(birthDate);
        } catch (DateTimeParseException e) {
            return true;
        }
        return false;
    }

    private int postalCodeToInt(String postalCode) {
        if (postalCode.isBlank()) return 0;
        return Integer.parseInt(postalCode);
    }

    private boolean isInvalidPostalCode(String postalCode) {
        if (postalCode.isBlank()) return false;
        if (postalCode.length() != 5) return true;
        try {
            Integer.parseInt(postalCode);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherDataForm.setVisible(true);
        communicationForm.setVisible(false);

        instrumentComboBox1.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox2.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox3.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox4.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox5.setItems(SCHOOL_INSTRUMENTS);

        locationComboBox1.setItems(SCHOOL_LOCATIONS);
        locationComboBox2.setItems(SCHOOL_LOCATIONS);
        locationComboBox3.setItems(SCHOOL_LOCATIONS);
    }
}
