package prodegus.musetasks.contacts;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;
import prodegus.musetasks.workspace.cells.StringListCell;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.school.LocationModel.fromString;
import static prodegus.musetasks.school.LocationModel.locationStringConverter;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.Strings.string;

public class AddTeacherController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private Label titleTextField2;

    @FXML private GridPane teacherDataForm;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField cityTextField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> instrumentComboBox1;
    @FXML private ComboBox<String> instrumentComboBox2;
    @FXML private ComboBox<String> instrumentComboBox3;
    @FXML private ComboBox<String> instrumentComboBox4;
    @FXML private ComboBox<String> instrumentComboBox5;
    @FXML private ComboBox<Location> locationComboBox1;
    @FXML private ComboBox<Location> locationComboBox2;
    @FXML private ComboBox<Location> locationComboBox3;
    @FXML private CheckBox mondayCheckBox;
    @FXML private CheckBox tuesdayCheckBox;
    @FXML private CheckBox wednesdayCheckBox;
    @FXML private CheckBox thursdayCheckBox;
    @FXML private CheckBox fridayCheckBox;
    @FXML private CheckBox saturdayCheckBox;
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
    void addTeacher(ActionEvent event) {
        Teacher newTeacher = new Teacher();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        String lastname = lastNameTextField.getText();
        String firstname = firstNameTextField.getText();
        int category = CATEGORY_TEACHER;
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
        String zoom = zoomComboBox.getSelectionModel().isEmpty() ? "" : zoomComboBox.getValue();
        String skype = skypeComboBox.getSelectionModel().isEmpty() ? "" : skypeComboBox.getValue();
        String birthDate = birthDatePicker.getEditor().getText();
        String notesInput = notesTextArea.getText();
        String notes = notesInput.isBlank() ? "" : timestamp + "\n" + notesInput;
        StringJoiner instrumentsJoiner = new StringJoiner(", ");
        if (!instrumentComboBox1.getSelectionModel().isEmpty()) instrumentsJoiner.add(instrumentComboBox1.getValue());
        if (!instrumentComboBox2.getSelectionModel().isEmpty()) instrumentsJoiner.add(instrumentComboBox2.getValue());
        if (!instrumentComboBox3.getSelectionModel().isEmpty()) instrumentsJoiner.add(instrumentComboBox3.getValue());
        if (!instrumentComboBox4.getSelectionModel().isEmpty()) instrumentsJoiner.add(instrumentComboBox4.getValue());
        if (!instrumentComboBox5.getSelectionModel().isEmpty()) instrumentsJoiner.add(instrumentComboBox5.getValue());
        String instruments = instrumentsJoiner.toString();
        StringJoiner activeDaysJoiner = new StringJoiner(", ");
        if (mondayCheckBox.isSelected()) activeDaysJoiner.add("Montag");
        if (tuesdayCheckBox.isSelected()) activeDaysJoiner.add("Dienstag");
        if (wednesdayCheckBox.isSelected()) activeDaysJoiner.add("Mittwoch");
        if (thursdayCheckBox.isSelected()) activeDaysJoiner.add("Donnerstag");
        if (fridayCheckBox.isSelected()) activeDaysJoiner.add("Freitag");
        if (saturdayCheckBox.isSelected()) activeDaysJoiner.add("Samstag");
        String activeDays = activeDaysJoiner.toString();
        String status = "";
        String statusFrom = "";
        String statusTo = "";

        if (lastname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Nachname eingeben\n");
        } else {
            newTeacher.setLastName(lastname);
        }

        if (firstname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Vorname eingeben\n");
        } else {
            newTeacher.setFirstName(firstname);
        }

        newTeacher.setCategory(category);
        newTeacher.setCustomerId(customerId);
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

        if (instruments.isBlank()) {
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
            PopupWindow.displayInformation("Lehrer konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }
        if (!editMode) {
            insertContact(newTeacher);
        } else {
            updateContact(newTeacher, editId);
        }
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

    public void initTeacher(Teacher teacher) {
        String[] instruments = teacher.getInstruments().split(", ");
        String[] locations = teacher.getLocation().split(", ");
        editMode = true;
        editId = teacher.getId();

        titleTextField.setText("Lehrer bearbeiten");
        titleTextField2.setText("Lehrer bearbeiten");

        firstNameTextField.setText(teacher.getFirstName());
        lastNameTextField.setText(teacher.getLastName());
        streetTextField.setText(teacher.getStreet());
        postalCodeTextField.setText(string(teacher.getPostalCode()));
        cityTextField.setText(teacher.getCity());
        birthDatePicker.getEditor().setText(teacher.getBirthDate());
        instrumentComboBox1.setValue(instruments[0]);
        if (instruments.length > 1) instrumentComboBox2.setValue(instruments[1]);
        if (instruments.length > 2) instrumentComboBox3.setValue(instruments[2]);
        if (instruments.length > 3) instrumentComboBox4.setValue(instruments[3]);
        if (instruments.length > 4) instrumentComboBox5.setValue(instruments[4]);

        if (locations.length > 0) locationComboBox1.setValue(fromString(locations[0]));
        if (locations.length > 1) locationComboBox2.setValue(fromString(locations[1]));
        if (locations.length > 2) locationComboBox3.setValue(fromString(locations[2]));
        if (teacher.getActiveDays().contains("Montag")) mondayCheckBox.setSelected(true);
        if (teacher.getActiveDays().contains("Dienstag")) tuesdayCheckBox.setSelected(true);
        if (teacher.getActiveDays().contains("Mittwoch")) wednesdayCheckBox.setSelected(true);
        if (teacher.getActiveDays().contains("Donnerstag")) thursdayCheckBox.setSelected(true);
        if (teacher.getActiveDays().contains("Freitag")) fridayCheckBox.setSelected(true);
        if (teacher.getActiveDays().contains("Samstag")) saturdayCheckBox.setSelected(true);
        phoneTextField.setText(teacher.getPhone());
        emailTextField.setText(teacher.getEmail());
        zoomCheckBox.setSelected(!teacher.getZoom().isEmpty());
        zoomComboBox.setValue(teacher.getZoom());
        skypeCheckBox.setSelected(!teacher.getSkype().isEmpty());
        skypeComboBox.setValue(teacher.getSkype());
        notesTextArea.setText(teacher.getNotes());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacherDataForm.setVisible(true);
        communicationForm.setVisible(false);

        instrumentComboBox1.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox1.setCellFactory(string -> new StringListCell());

        instrumentComboBox2.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox2.setCellFactory(string -> new StringListCell());

        instrumentComboBox3.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox3.setCellFactory(string -> new StringListCell());

        instrumentComboBox4.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox4.setCellFactory(string -> new StringListCell());

        instrumentComboBox5.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox5.setCellFactory(string -> new StringListCell());


        locationComboBox1.setItems(SCHOOL_LOCATIONS);
        locationComboBox1.setCellFactory(string -> new LocationListCell());
        locationComboBox1.setConverter(locationStringConverter);

        locationComboBox2.setItems(SCHOOL_LOCATIONS);
        locationComboBox2.setCellFactory(string -> new LocationListCell());
        locationComboBox2.setConverter(locationStringConverter);

        locationComboBox3.setItems(SCHOOL_LOCATIONS);
        locationComboBox3.setCellFactory(string -> new LocationListCell());
        locationComboBox3.setConverter(locationStringConverter);
    }
}
