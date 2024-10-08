package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;
import prodegus.musetasks.workspace.cells.ParentListCellFormal;
import prodegus.musetasks.workspace.cells.StringListCell;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.ParentModel.*;
import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.school.LocationModel.*;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.school.SchoolModel.initializeForInstruments;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.Strings.string;

public class AddStudentController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private Label titleTextField2;

    @FXML private GridPane studentDataForm;
    @FXML private CheckBox prospectiveCheckBox;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField cityTextField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private ComboBox<String> instrument1ComboBox;
    @FXML private ComboBox<String> instrument2ComboBox;
    @FXML private ComboBox<String> instrument3ComboBox;

    @FXML private GridPane communicationForm;
    @FXML private Label parent1Label;
    @FXML private ComboBox<Parent> parent1ComboBox;
    @FXML private Button newParent1Button;
    @FXML private VBox parent2VBox;
    @FXML private ComboBox<Parent> parent2ComboBox;
    @FXML private Button newParent2Button;
    @FXML private TextField phoneTextField;
    @FXML private TextField emailTextField;
    @FXML private CheckBox zoomCheckBox;
    @FXML private ComboBox<String> zoomComboBox;
    @FXML private CheckBox skypeCheckBox;
    @FXML private ComboBox<String> skypeComboBox;
    @FXML private ComboBox<String> contactMailComboBox;
    @FXML private TextArea notesTextArea;
    @FXML private Button backButton;
    @FXML private Button cancelButton;
    @FXML private Button cancelButton2;
    @FXML private Button confirmButton;
    @FXML private Button toContactFormButton;

    private boolean editMode;
    private int id;

    @FXML
    void submitStudentData(ActionEvent event) {
        Student newStudent = (editMode ? getStudentFromDB(id) : new Student());
        boolean invalidData = false;
        StringJoiner errorMessage = new StringJoiner("\n");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        String lastname = lastNameTextField.getText();
        String firstname = firstNameTextField.getText();
        int category = prospectiveCheckBox.isSelected() ? CATEGORY_PROSPECTIVE_STUDENT : CATEGORY_STUDENT;
        int customerId = 0;
        Location location = locationComboBox.getValue();
        String street = streetTextField.getText();
        String postalCode = postalCodeTextField.getText();
        String city = cityTextField.getText();
        String phone = phoneTextField.getText();
        String email = emailTextField.getText();
        String zoom = zoomComboBox.getSelectionModel().isEmpty() ? "" : zoomComboBox.getValue();
        String skype = skypeComboBox.getSelectionModel().isEmpty() ? "" : skypeComboBox.getValue();
        String contactMail = contactMailComboBox.getValue() == null ? "" : contactMailComboBox.getValue();
        String birthDate = birthDatePicker.getEditor().getText();
        String notesInput = notesTextArea.getText();
        String notes = notesInput.isBlank() ? "" : timestamp + "\n" + notesInput;
        String instrument1 = instrument1ComboBox.getSelectionModel().isEmpty() ? "" : instrument1ComboBox.getValue();
        String instrument2 = instrument2ComboBox.getSelectionModel().isEmpty() ? "" : instrument2ComboBox.getValue();
        String instrument3 = instrument3ComboBox.getSelectionModel().isEmpty() ? "" : instrument3ComboBox.getValue();
        boolean prospective = prospectiveCheckBox.isSelected();
        String status = "";
        String statusFrom = "";
        String statusTo = "";
        Parent parent1 = parent1ComboBox.getValue();
        Parent parent2 = parent2ComboBox.getValue();

        if (lastname.isBlank()) {
            invalidData = true;
            errorMessage.add("- Bitte Nachname eingeben");
        } else {
            newStudent.setLastName(lastname);
        }

        if (firstname.isBlank()) {
            invalidData = true;
            errorMessage.add("- Bitte Vorname eingeben");
        } else {
            newStudent.setFirstName(firstname);
        }

        newStudent.setCategory(category);
        newStudent.setCustomerId(customerId);
        if (location != null) newStudent.setLocation(location.getName());
        newStudent.setStreet(street);

        if (isInvalidPostalCode(postalCode)) {
            invalidData = true;
            errorMessage.add("- Bitte gültige Postleitzahl eingeben (5 Ziffern) oder Feld leer lassen");
        } else {
            newStudent.setPostalCode(postalCodeToInt(postalCode));
        }

        newStudent.setCity(city);
        newStudent.setPhone(phone);
        newStudent.setEmail(email);
        newStudent.setZoom(zoom);
        newStudent.setSkype(skype);

        if (contactMail.isBlank()) {
            invalidData = true;
            errorMessage.add("- Bitte Kontakt-E-Mail-Adresse auswählen");
        }
        newStudent.setContactEmail(contactMail);

        if (isInvalidBirthDate(birthDate)) {
            invalidData = true;
            errorMessage.add("- Bitte gültiges Geburtsdatum eingeben (z.B. 01.01.2000) oder Feld leer lassen");
        } else {
            newStudent.setBirthDate(birthDate);
        }

        newStudent.setNotes(notes);

        if (instrument1.isBlank()) {
            invalidData = true;
            errorMessage.add("- Bitte mindestens ein Instrument auswählen");
        } else {
            newStudent.setInstrument1(instrument1);
        }

        newStudent.setInstrument2(instrument2);
        newStudent.setInstrument3(instrument3);
        newStudent.setProspective(prospective);
        newStudent.setStatus(status);
        newStudent.setStatusFrom(statusFrom);
        newStudent.setStatusTo(statusTo);
        if (parent1 != null) newStudent.setParentId1(parent1.getId());
        if (parent2 != null) newStudent.setParentId2(parent2.getId());

        if (invalidData) {
            PopupWindow.displayInformation("Schüler konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }
        if (!editMode) {
            insertContact(newStudent);
            id = findContactID(newStudent);
        } else {
            updateContact(newStudent, id);
        }
        if (parent1 != null) parent1.addChildInDB(id);
        if (parent2 != null) parent2.addChildInDB(id);
        stageOf(event).close();
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

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void goToCommunicationForm(ActionEvent event) {
        studentDataForm.setVisible(false);
        communicationForm.setVisible(true);
    }

    @FXML
    void goToStudentForm(ActionEvent event) {
        studentDataForm.setVisible(true);
        communicationForm.setVisible(false);
    }

    @FXML
    void newParent1ButtonClicked(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addparent-view.fxml"));
        Stage stage = newStage("Elternteil anlegen", loader);
        AddParentController controller = loader.getController();
        controller.initFromChildData(
                lastNameTextField.getText(),
                lastNameTextField.getText() + ", " + firstNameTextField.getText(),
                streetTextField.getText(),
                postalCodeTextField.getText(),
                cityTextField.getText());
        stage.showAndWait();
        selectNewParent1();
    }

    @FXML
    void newParent2ButtonClicked(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addparent-view.fxml"));
        Stage stage = newStage("Elternteil anlegen", loader);
        AddParentController controller = loader.getController();
        controller.initFromChildData(
                lastNameTextField.getText(),
                lastNameTextField.getText() + ", " + firstNameTextField.getText(),
                streetTextField.getText(),
                postalCodeTextField.getText(),
                cityTextField.getText());
        stage.showAndWait();
        selectNewParent2();
    }

    private void selectNewParent1() {
        parent1ComboBox.setItems(getParentListFromDB());
        parent1ComboBox.getSelectionModel().selectLast();
    }

    private void selectNewParent2() {
        parent2ComboBox.setItems(getParentListFromDB());
        parent2ComboBox.getSelectionModel().selectLast();
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

    public void initFromParentData(String parent1Name, String street, String postalCode, String city) {
        streetTextField.setText(street);
        postalCodeTextField.setText(postalCode);
        cityTextField.setText(city);
        parent1ComboBox.setPromptText(parent1Name);
        parent1ComboBox.setDisable(true);
        parent1Label.setVisible(false);
        newParent1Button.setVisible(false);
        parent2VBox.setVisible(false);
    }

    public void initStudent(Student student) {
        editMode = true;
        id = student.getId();
        titleTextField.setText("Schüler bearbeiten");
        titleTextField2.setText("Schüler bearbeiten");
        prospectiveCheckBox.setSelected(student.getProspective());
        firstNameTextField.setText(student.getFirstName());
        lastNameTextField.setText(student.getLastName());
        streetTextField.setText(student.getStreet());
        postalCodeTextField.setText(string(student.getPostalCode()));
        cityTextField.setText(student.getCity());
        birthDatePicker.getEditor().setText(student.getBirthDate());
        if (!student.getLocation().isBlank()) locationComboBox.setValue(fromString(student.getLocation()));
        if (!student.getInstrument1().isBlank()) instrument1ComboBox.setValue(student.getInstrument1());
        if (!student.getInstrument2().isBlank()) instrument2ComboBox.setValue(student.getInstrument2());
        if (!student.getInstrument3().isBlank()) instrument3ComboBox.setValue(student.getInstrument3());
        phoneTextField.setText(student.getPhone());
        emailTextField.setText(student.getEmail());

        zoomComboBox.setItems(student.getAllMail());
        if (!student.getZoom().isEmpty()) {
            zoomCheckBox.setSelected(true);
            zoomComboBox.setVisible(true);
            zoomComboBox.setValue(student.getZoom());
        }

        skypeComboBox.setItems(student.getAllMail());
        if (!student.getSkype().isEmpty()) {
            skypeCheckBox.setSelected(true);
            skypeComboBox.setVisible(true);
            skypeComboBox.setValue(student.getSkype());
        }
        
        contactMailComboBox.setValue(student.getContactEmail());
        if (student.getParentId1() != 0) {
            parent1ComboBox.setEditable(false);
            parent1ComboBox.setValue(student.parent1());
        }
        if (student.getParentId2() != 0) {
            parent2ComboBox.setEditable(false);
            parent2ComboBox.setValue(student.parent2());
        }
        notesTextArea.setText(student.getNotes());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactMailComboBox.setOnMouseClicked(e -> {
            ObservableList<String> email = FXCollections.observableArrayList();
            if (parent1ComboBox.getValue() != null && !parent1ComboBox.getValue().getEmail().isBlank())
                email.add(parent1ComboBox.getValue().getEmail());
            if (parent2ComboBox.getValue() != null && !parent2ComboBox.getValue().getEmail().isBlank())
                email.add(parent2ComboBox.getValue().getEmail());
            if (!emailTextField.getText().isBlank())
                email.add(emailTextField.getText());
            email.add("keine Angabe");
            contactMailComboBox.setItems(email);
        });

        studentDataForm.setVisible(true);
        communicationForm.setVisible(false);

        initializeForInstruments(instrument1ComboBox);
        initializeForInstruments(instrument2ComboBox);
        initializeForInstruments(instrument3ComboBox);

        initializeForLocations(locationComboBox);

        initializeForParents(parent1ComboBox);
        initializeForParents(parent2ComboBox);
    }

}


