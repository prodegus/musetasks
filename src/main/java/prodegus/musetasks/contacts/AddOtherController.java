package prodegus.musetasks.contacts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import prodegus.musetasks.ui.PopupWindow;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class AddOtherController implements Initializable {

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
    private TextField descriptionTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private ComboBox<String> locationComboBox1;

    @FXML
    private ComboBox<String> locationComboBox2;

    @FXML
    private ComboBox<String> locationComboBox3;

    @FXML
    private TextArea notesTextArea;

    @FXML
    private GridPane otherDataForm;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField postalCodeTextField;

    @FXML
    private CheckBox skypeCheckBox;

    @FXML
    private ComboBox<String> skypeComboBox;

    @FXML
    private TextField streetTextField;

    @FXML
    private Button toContactFormButton;

    @FXML
    private CheckBox zoomCheckBox;

    @FXML
    private ComboBox<String> zoomComboBox;

    @FXML
    void addOther(ActionEvent event) {
        Other newOther = new Other();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        String lastname = lastNameTextField.getText();
        String firstname = firstNameTextField.getText();
        int category = CATEGORY_OTHER;
        int customerId = 0;
        String location1 = locationComboBox1.getSelectionModel().isEmpty() ? "" : locationComboBox1.getValue();
        String location2 = locationComboBox2.getSelectionModel().isEmpty() ? "" : locationComboBox2.getValue();
        String location3 = locationComboBox3.getSelectionModel().isEmpty() ? "" : locationComboBox3.getValue();
        String locations = location1 +
                (location2.isEmpty() ? "" : ", " + location2) +
                (location3.isEmpty() ? "" : ", " + location3);
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
        String description = descriptionTextField.getText();

        if (description.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Beschreibung des Kontakts eingeben\n");
        } else {
            newOther.setDescription(description);
        }

        if (lastname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Nachname eingeben\n");
        } else {
            newOther.setLastName(lastname);
        }

        if (firstname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Vorname eingeben\n");
        } else {
            newOther.setFirstName(firstname);
        }

        newOther.setCategory(category);
        newOther.setCustomerId(customerId);
        newOther.setLocation(locations);
        newOther.setStreet(street);

        if (isInvalidPostalCode(postalCode)) {
            invalidData = true;
            errorMessage.append("- Bitte gültige Postleitzahl eingeben (5 Ziffern) oder Feld leer lassen\n");
        } else {
            newOther.setPostalCode(postalCodeToInt(postalCode));
        }

        newOther.setCity(city);
        newOther.setPhone(phone);
        newOther.setEmail(email);
        newOther.setZoom(zoom);
        newOther.setSkype(skype);

        if (isInvalidBirthDate(birthDate)) {
            invalidData = true;
            errorMessage.append("- Bitte gültiges Geburtsdatum eingeben (z.B. 01.01.2000) oder Feld leer lassen\n");
        } else {
            newOther.setBirthDate(birthDate);
        }
        newOther.setNotes(notes);

        if (invalidData) {
            PopupWindow.display("Kontakt konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }
        addContactToDB(newOther);
        stageOf(event).close();
    }

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void goToCommunicationForm(ActionEvent event) {
        otherDataForm.setVisible(false);
        communicationForm.setVisible(true);
    }

    @FXML
    void goToOtherForm(ActionEvent event) {
        otherDataForm.setVisible(true);
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
        otherDataForm.setVisible(true);
        communicationForm.setVisible(false);

        locationComboBox1.setItems(SCHOOL_LOCATIONS);
        locationComboBox2.setItems(SCHOOL_LOCATIONS);
        locationComboBox3.setItems(SCHOOL_LOCATIONS);
    }

}
