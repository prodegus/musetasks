package prodegus.musetasks.contacts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.school.LocationModel.*;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.Strings.string;

public class AddOtherController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private Label titleTextField2;

    @FXML private GridPane otherDataForm;
    @FXML private TextField descriptionTextField;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField cityTextField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<Location> locationComboBox1;
    @FXML private ComboBox<Location> locationComboBox2;
    @FXML private ComboBox<Location> locationComboBox3;
    @FXML private Button toContactFormButton;
    @FXML private Button cancelButton;

    @FXML private GridPane communicationForm;
    @FXML private TextField phoneTextField;
    @FXML private TextField emailTextField;
    @FXML private CheckBox zoomCheckBox;
    @FXML private ComboBox<String> zoomComboBox;
    @FXML private CheckBox skypeCheckBox;
    @FXML private ComboBox<String> skypeComboBox;
    @FXML private TextArea notesTextArea;
    @FXML private Button backButton;
    @FXML private Button confirmButton;
    @FXML private Button cancelButton2;

    private boolean editMode;
    private int editId;

    @FXML
    void addOther(ActionEvent event) {
        Other newOther = new Other();
        boolean invalidData = false;
        StringJoiner errorMessage = new StringJoiner("\n");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        String lastname = lastNameTextField.getText();
        String firstname = firstNameTextField.getText();
        int category = CATEGORY_OTHER;
        int customerId = 0;
        StringJoiner locationsJoiner = new StringJoiner(", ");
        if (!locationComboBox1.getSelectionModel().isEmpty()) locationsJoiner.add(locationComboBox1.getValue().getName());
        if (!locationComboBox2.getSelectionModel().isEmpty()) locationsJoiner.add(locationComboBox2.getValue().getName());
        if (!locationComboBox3.getSelectionModel().isEmpty()) locationsJoiner.add(locationComboBox3.getValue().getName());
        String locations = locationsJoiner.toString();
        String street = streetTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String city = cityTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();
        String zoom = zoomComboBox.getValue() == null ? "" : zoomComboBox.getValue();
        String skype = skypeComboBox.getValue() == null ? "" : skypeComboBox.getValue();
        String birthDate = birthDatePicker.getEditor().getText();
        String notesInput = notesTextArea.getText();
        String notes = notesInput.isBlank() ? "" : timestamp + "\n" + notesInput;
        String description = descriptionTextField.getText();

        if (description.isBlank()) {
            invalidData = true;
            errorMessage.add("- Bitte Beschreibung des Kontakts eingeben");
        } else {
            newOther.setDescription(description);
        }

        if (lastname.isBlank()) {
            invalidData = true;
            errorMessage.add("- Bitte Nachname eingeben");
        } else {
            newOther.setLastName(lastname);
        }

        if (firstname.isBlank()) {
            invalidData = true;
            errorMessage.add("- Bitte Vorname eingeben");
        } else {
            newOther.setFirstName(firstname);
        }

        newOther.setCategory(category);
        newOther.setCustomerId(customerId);
        newOther.setLocation(locations);
        newOther.setStreet(street);

        if (isInvalidPostalCode(postalCode)) {
            invalidData = true;
            errorMessage.add("- Bitte gültige Postleitzahl eingeben (5 Ziffern) oder Feld leer lassen");
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
            errorMessage.add("- Bitte gültiges Geburtsdatum eingeben (z.B. 01.01.2000) oder Feld leer lassen");
        } else {
            newOther.setBirthDate(birthDate);
        }
        newOther.setNotes(notes);

        if (invalidData) {
            PopupWindow.displayInformation("Kontakt konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }
        if (!editMode) {
            insertContact(newOther);
        }
        else {
            updateContact(newOther, editId);
        }
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
        if (!skypeComboBox.getItems().contains(emailTextField.getText())) {
            skypeComboBox.getItems().add(emailTextField.getText());
        }
    }

    @FXML
    void zoomCheckBoxClicked(MouseEvent event) {
        zoomComboBox.setVisible(zoomCheckBox.isSelected());
        if (!zoomComboBox.getItems().contains(emailTextField.getText())) {
            zoomComboBox.getItems().add(emailTextField.getText());
        }
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

    public void initOther(Other other) {
        String[] locations = other.getLocation().split(", ");
        editMode = true;
        editId = other.getId();
        titleTextField.setText("Kontakt bearbeiten");
        titleTextField2.setText("Kontakt bearbeiten");
        descriptionTextField.setText(other.getDescription());
        firstNameTextField.setText(other.getFirstName());
        lastNameTextField.setText(other.getLastName());
        streetTextField.setText(other.getStreet());
        postalCodeTextField.setText(string(other.getPostalCode()));
        cityTextField.setText(other.getCity());
        birthDatePicker.getEditor().setText(other.getBirthDate());
        if (locations.length > 0) locationComboBox1.setValue(fromString(locations[0]));
        if (locations.length > 1) locationComboBox2.setValue(fromString(locations[1]));
        if (locations.length > 2) locationComboBox3.setValue(fromString(locations[2]));
        phoneTextField.setText(other.getPhone());
        emailTextField.setText(other.getEmail());

        zoomComboBox.setItems(other.getAllMail());
        if (!other.getZoom().isEmpty()) {
            zoomCheckBox.setSelected(true);
            zoomComboBox.setVisible(true);
            zoomComboBox.setValue(other.getZoom());
        }

        skypeComboBox.setItems(other.getAllMail());
        if (!other.getSkype().isEmpty()) {
            skypeCheckBox.setSelected(true);
            skypeComboBox.setVisible(true);
            skypeComboBox.setValue(other.getSkype());
        }

        notesTextArea.setText(other.getNotes());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        otherDataForm.setVisible(true);
        communicationForm.setVisible(false);

        initializeForLocations(locationComboBox1);
        initializeForLocations(locationComboBox2);
        initializeForLocations(locationComboBox3);
    }
}
