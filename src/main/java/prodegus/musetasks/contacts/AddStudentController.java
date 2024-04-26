package prodegus.musetasks.contacts;

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
import prodegus.musetasks.ui.PopupWindow;
import prodegus.musetasks.workspace.cells.ParentListCellFormal;
import prodegus.musetasks.workspace.cells.TeacherListCellFormal;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.ParentModel.getParentListFromDB;
import static prodegus.musetasks.contacts.ParentModel.parentStringConverterFormal;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.teacherStringConverterFormal;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class AddStudentController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private Label titleTextField2;

    @FXML private CheckBox prospectiveCheckBox;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField cityTextField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<String> locationComboBox;
    @FXML private ComboBox<String> instrument1ComboBox;
    @FXML private ComboBox<String> instrument2ComboBox;
    @FXML private ComboBox<String> instrument3ComboBox;
    @FXML private ComboBox<Teacher> teacher1ComboBox;
    @FXML private ComboBox<Teacher> teacher2ComboBox;
    @FXML private ComboBox<Teacher> teacher3ComboBox;
    @FXML private TextField phoneTextField;
    @FXML private TextField emailTextField;
    @FXML private CheckBox zoomCheckBox;
    @FXML private ComboBox<String> zoomComboBox;
    @FXML private CheckBox skypeCheckBox;
    @FXML private ComboBox<String> skypeComboBox;
    @FXML private TextArea notesTextArea;

    @FXML private GridPane studentDataForm;
    @FXML private GridPane communicationForm;
    @FXML private VBox parent2VBox;
    @FXML private ComboBox<Parent> parent1ComboBox;
    @FXML private ComboBox<Parent> parent2ComboBox;
    @FXML private Label parent1Label;
    @FXML private Button backButton;
    @FXML private Button cancelButton;
    @FXML private Button cancelButton2;
    @FXML private Button confirmButton;
    @FXML private Button newParent1Button;
    @FXML private Button newParent2Button;
    @FXML private Button toContactFormButton;
    private boolean editMode;
    private int editId;

    @FXML
    void submitStudentData(ActionEvent event) {
        Student newStudent = new Student();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        String lastname = lastNameTextField.getText();
        String firstname = firstNameTextField.getText();
        int category = prospectiveCheckBox.isSelected() ? CATEGORY_PROSPECTIVE_STUDENT : CATEGORY_STUDENT;
        int customerId = 0;
        String location = locationComboBox.getSelectionModel().isEmpty() ? "" : locationComboBox.getValue();
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
        String instrument1 = instrument1ComboBox.getSelectionModel().isEmpty() ? "" : instrument1ComboBox.getValue();
        String instrument2 = instrument2ComboBox.getSelectionModel().isEmpty() ? "" : instrument2ComboBox.getValue();
        String instrument3 = instrument3ComboBox.getSelectionModel().isEmpty() ? "" : instrument3ComboBox.getValue();
        boolean prospective = prospectiveCheckBox.isSelected();
        String status = "";
        String statusFrom = "";
        String statusTo = "";
        Parent parent1 = parent1ComboBox.getValue();
        Parent parent2 = parent2ComboBox.getValue();
        Teacher teacher1 = teacher1ComboBox.getValue();
        Teacher teacher2 = teacher2ComboBox.getValue();
        Teacher teacher3 = teacher3ComboBox.getValue();

        if (lastname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Nachname eingeben\n");
        } else {
            newStudent.setLastName(lastname);
        }

        if (firstname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Vorname eingeben\n");
        } else {
            newStudent.setFirstName(firstname);
        }

        newStudent.setCategory(category);
        newStudent.setCustomerId(customerId);
        newStudent.setLocation(location);
        newStudent.setStreet(street);

        if (isInvalidPostalCode(postalCode)) {
            invalidData = true;
            errorMessage.append("- Bitte gültige Postleitzahl eingeben (5 Ziffern) oder Feld leer lassen\n");
        } else {
            newStudent.setPostalCode(postalCodeToInt(postalCode));
        }

        newStudent.setCity(city);
        newStudent.setPhone(phone);
        newStudent.setEmail(email);
        newStudent.setZoom(zoom);
        newStudent.setSkype(skype);

        if (isInvalidBirthDate(birthDate)) {
            invalidData = true;
            errorMessage.append("- Bitte gültiges Geburtsdatum eingeben (z.B. 01.01.2000) oder Feld leer lassen\n");
        } else {
            newStudent.setBirthDate(birthDate);
        }

        newStudent.setNotes(notes);

        if (instrument1.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte mindestens ein Instrument auswählen\n");
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
        if (teacher1 != null) newStudent.setTeacherId1(teacher1.getId());
        if (teacher2 != null) newStudent.setTeacherId2(teacher2.getId());
        if (teacher3 != null) newStudent.setTeacherId3(teacher3.getId());

        if (invalidData) {
            PopupWindow.display("Schüler konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }
        if (!editMode) {
            addContactToDB(newStudent);
            if (parent1 != null) parent1.addChildInDB(newStudent);
            if (parent2 != null) parent2.addChildInDB(newStudent);
        } else {
            updateContactInDB(newStudent, editId);
        }
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
    }

    @FXML
    void zoomCheckBoxClicked(MouseEvent event) {
        zoomComboBox.setVisible(zoomCheckBox.isSelected());
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
        editId = student.getId();
        titleTextField.setText("Schüler bearbeiten");
        titleTextField2.setText("Schüler bearbeiten");
        prospectiveCheckBox.setSelected(student.getProspective());
        firstNameTextField.setText(student.getFirstName());
        lastNameTextField.setText(student.getLastName());
        streetTextField.setText(student.getStreet());
        postalCodeTextField.setText(String.valueOf(student.getPostalCode()));
        cityTextField.setText(student.getCity());
        birthDatePicker.getEditor().setText(student.getBirthDate());
        locationComboBox.setValue(student.getLocation());
        instrument1ComboBox.setValue(student.getInstrument1());
        instrument2ComboBox.setValue(student.getInstrument2());
        instrument3ComboBox.setValue(student.getInstrument3());
        teacher1ComboBox.setValue(student.teacher1());
        teacher2ComboBox.setValue(student.teacher2());
        teacher3ComboBox.setValue(student.teacher3());
        phoneTextField.setText(student.getPhone());
        emailTextField.setText(student.getEmail());
        zoomCheckBox.setSelected(!student.getZoom().isEmpty());
        zoomComboBox.setValue(student.getZoom());
        skypeCheckBox.setSelected(!student.getSkype().isEmpty());
        skypeComboBox.setValue(student.getSkype());
        parent1ComboBox.setValue(student.parent1());
        parent2ComboBox.setValue(student.parent2());
        notesTextArea.setText(student.getNotes());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Teacher> teachers = getTeacherListFromDB();
        ObservableList<Parent> parents = getParentListFromDB();

        studentDataForm.setVisible(true);
        communicationForm.setVisible(false);

        instrument1ComboBox.setItems(SCHOOL_INSTRUMENTS);
        instrument2ComboBox.setItems(SCHOOL_INSTRUMENTS);
        instrument3ComboBox.setItems(SCHOOL_INSTRUMENTS);

        locationComboBox.setItems(SCHOOL_LOCATIONS);

        teacher1ComboBox.setItems(teachers);
        teacher1ComboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        teacher1ComboBox.setConverter(teacherStringConverterFormal);

        teacher2ComboBox.setItems(teachers);
        teacher2ComboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        teacher2ComboBox.setConverter(teacherStringConverterFormal);

        teacher3ComboBox.setItems(teachers);
        teacher3ComboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        teacher3ComboBox.setConverter(teacherStringConverterFormal);

        parent1ComboBox.setItems(parents);
        parent2ComboBox.setItems(parents);

        parent1ComboBox.setItems(parents);
        parent1ComboBox.setCellFactory(parent -> new ParentListCellFormal());
        parent1ComboBox.setConverter(parentStringConverterFormal);

        parent2ComboBox.setItems(parents);
        parent2ComboBox.setCellFactory(parent -> new ParentListCellFormal());
        parent2ComboBox.setConverter(parentStringConverterFormal);

//        contactsAllListView.setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>() {
//            @Override
//            public ListCell<Contact> call(ListView<Contact> list) {
//                return new ContactListCell();
//            }
//        });
    }

}


