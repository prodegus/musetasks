package prodegus.musetasks.contacts;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;
import prodegus.musetasks.workspace.cells.StudentListCell;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.StudentModel.*;
import static prodegus.musetasks.school.LocationModel.fromString;
import static prodegus.musetasks.school.LocationModel.locationStringConverter;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.Strings.string;

public class AddParentController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private Label titleTextField2;

    @FXML private GridPane parentDataForm;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField streetTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField cityTextField;
    @FXML private DatePicker birthDatePicker;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private ComboBox<Student> child1ComboBox;
    @FXML private Button child1DeleteButton;
    @FXML private ComboBox<Student> child2ComboBox;
    @FXML private Button child2DeleteButton;
    @FXML private HBox child2HBox;
    @FXML private ComboBox<Student> child3ComboBox;
    @FXML private Button child3DeleteButton;
    @FXML private HBox child3HBox;
    @FXML private ComboBox<Student> child4ComboBox;
    @FXML private Button child4DeleteButton;
    @FXML private HBox child4HBox;
    @FXML private ComboBox<Student> child5ComboBox;
    @FXML private Button child5DeleteButton;
    @FXML private HBox child5HBox;
    @FXML private Button newChildButton;
    @FXML private Label newChildLabel;
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
    @FXML private Button cancelButton2;
    @FXML private Button confirmButton;

    private boolean editMode;
    private int id;

    @FXML
    void addParent(ActionEvent event) {
        Parent newParent = new Parent();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        String lastname = lastNameTextField.getText();
        String firstname = firstNameTextField.getText();
        int category = CATEGORY_PARENT;
        int customerId = 0;
        String location = locationComboBox.getSelectionModel().isEmpty() ? "" : locationComboBox.getValue().getName();
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
        Student child1 = child1ComboBox.getValue();
        Student child2 = child2ComboBox.getValue();
        Student child3 = child3ComboBox.getValue();
        Student child4 = child4ComboBox.getValue();
        Student child5 = child5ComboBox.getValue();

        if (lastname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Nachname eingeben\n");
        } else {
            newParent.setLastName(lastname);
        }

        if (firstname.isBlank()) {
            invalidData = true;
            errorMessage.append("- Bitte Vorname eingeben\n");
        } else {
            newParent.setFirstName(firstname);
        }

        newParent.setCategory(category);
        newParent.setCustomerId(customerId);
        newParent.setLocation(location);
        newParent.setStreet(street);

        if (isInvalidPostalCode(postalCode)) {
            invalidData = true;
            errorMessage.append("- Bitte gültige Postleitzahl eingeben (5 Ziffern) oder Feld leer lassen\n");
        } else {
            newParent.setPostalCode(postalCodeToInt(postalCode));
        }

        newParent.setCity(city);
        newParent.setPhone(phone);
        newParent.setEmail(email);
        newParent.setZoom(zoom);
        newParent.setSkype(skype);

        if (isInvalidBirthDate(birthDate)) {
            invalidData = true;
            errorMessage.append("- Bitte gültiges Geburtsdatum eingeben (z.B. 01.01.2000) oder Feld leer lassen\n");
        } else {
            newParent.setBirthDate(birthDate);
        }

        newParent.setNotes(notes);

        if (!child1ComboBox.isDisable()) {
            if (child1 == null) {
                invalidData = true;
                errorMessage.append("- Bitte mindestens ein Kind auswählen oder anlegen\n");
            } else {
                newParent.setChildId1(child1.getId());
            }
        }
        if (child2 != null) newParent.setChildId2(child2.getId());
        if (child3 != null) newParent.setChildId3(child3.getId());
        if (child4 != null) newParent.setChildId4(child4.getId());
        if (child5 != null) newParent.setChildId5(child5.getId());

        if (invalidData) {
            PopupWindow.displayInformation("Elternteil konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }
        if (!editMode) {
            insertContact(newParent);
            id = findContactID(newParent);
        }
        else updateContact(newParent, id);

        if (!child1ComboBox.isDisable()) child1.addParentInDB(id);
        if (child2 != null) child2.addParentInDB(id);
        if (child3 != null) child3.addParentInDB(id);
        if (child4 != null) child4.addParentInDB(id);
        if (child5 != null) child5.addParentInDB(id);
        stageOf(event).close();
    }

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void clearChild1(ActionEvent event) {
        child1ComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void clearChild2(ActionEvent event) {
        child2ComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void clearChild3(ActionEvent event) {
        child3ComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void clearChild4(ActionEvent event) {
        child4ComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void clearChild5(ActionEvent event) {
        child5ComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void goToCommunicationForm(ActionEvent event) {
        parentDataForm.setVisible(false);
        communicationForm.setVisible(true);
    }

    @FXML
    void goToParentForm(ActionEvent event) {
        parentDataForm.setVisible(true);
        communicationForm.setVisible(false);
    }

    @FXML
    void newChildButtonClicked(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addstudent-view.fxml"));
        Stage stage = newStage("Schüler anlegen", loader);
        AddStudentController controller = loader.getController();
        controller.initFromParentData(
                lastNameTextField.getText() + ", " + firstNameTextField.getText(),
                streetTextField.getText(),
                postalCodeTextField.getText(),
                cityTextField.getText());
        stage.showAndWait();
        selectNewChild();
    }

    private void selectNewChild() {
        ObservableList<Student> students = getStudentListFromDB();
        if (child1ComboBox.getSelectionModel().isEmpty()) {
            child1ComboBox.setItems(students);
            child1ComboBox.getSelectionModel().selectLast();
            return;
        }
        if (child2ComboBox.getSelectionModel().isEmpty()) {
            child2ComboBox.setItems(students);
            child2ComboBox.getSelectionModel().selectLast();
            return;
        }
        if (child3ComboBox.getSelectionModel().isEmpty()) {
            child3ComboBox.setItems(students);
            child3ComboBox.getSelectionModel().selectLast();
            return;
        }
        if (child4ComboBox.getSelectionModel().isEmpty()) {
            child4ComboBox.setItems(students);
            child4ComboBox.getSelectionModel().selectLast();
            return;
        }
        child5ComboBox.setItems(students);
        child5ComboBox.getSelectionModel().selectLast();
    }

    @FXML
    void skypeCheckBoxClicked(MouseEvent event) {

    }

    @FXML
    void zoomCheckBoxClicked(MouseEvent event) {

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

    private int postalCodeToInt(String postalCode) {
        if (postalCode.isBlank()) return 0;
        return Integer.parseInt(postalCode);
    }

    private void selectChild(ComboBox<Student> comboBox, int childId) {
        if (childId == 0) return;
        int index = 0;
        for (Student student : comboBox.getItems()) {
            if (student.getId() == childId) break;
            index ++;
        }
        comboBox.getSelectionModel().select(index);
    }

    public void initFromChildData(String lastName, String childName, String street, String postalCode, String city) {
        lastNameTextField.setText(lastName);
        streetTextField.setText(street);
        postalCodeTextField.setText(postalCode);
        cityTextField.setText(city);
        child1ComboBox.setPromptText(childName);
        child1ComboBox.setDisable(true);
        newChildLabel.setVisible(false);
        newChildButton.setVisible(false);
    }

    public void initParent(Parent parent) {
        editMode = true;
        id = parent.getId();
        titleTextField.setText("Elternteil bearbeiten");
        titleTextField2.setText("Elternteil bearbeiten");

        firstNameTextField.setText(parent.getFirstName());
        lastNameTextField.setText(parent.getLastName());
        streetTextField.setText(parent.getStreet());
        postalCodeTextField.setText(string(parent.getPostalCode()));
        cityTextField.setText(parent.getCity());
        birthDatePicker.getEditor().setText(parent.getBirthDate());
        if (!parent.getLocation().isBlank()) locationComboBox.setValue(fromString(parent.getLocation()));
        selectChild(child1ComboBox, parent.getChildId1());
        selectChild(child2ComboBox, parent.getChildId2());
        selectChild(child3ComboBox, parent.getChildId3());
        selectChild(child4ComboBox, parent.getChildId4());
        selectChild(child5ComboBox, parent.getChildId5());
        phoneTextField.setText(parent.getPhone());
        emailTextField.setText(parent.getEmail());
        zoomCheckBox.setSelected(!parent.getZoom().isEmpty());
        zoomComboBox.setValue(parent.getZoom());
        skypeCheckBox.setSelected(!parent.getSkype().isEmpty());
        skypeComboBox.setValue(parent.getSkype());
        notesTextArea.setText(parent.getNotes());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Student> students = getStudentListFromDB();

        locationComboBox.setItems(SCHOOL_LOCATIONS);
        locationComboBox.setCellFactory(string -> new LocationListCell());
        locationComboBox.setConverter(locationStringConverter);

        child1ComboBox.setItems(students);
        child1ComboBox.setButtonCell(new StudentListCell());
        child1ComboBox.setCellFactory(student -> new StudentListCell());
        child1ComboBox.setConverter(studentStringConverter);

        child2ComboBox.setItems(students);
        child2ComboBox.setButtonCell(new StudentListCell());
        child2ComboBox.setCellFactory(student -> new StudentListCell());
        child2ComboBox.setConverter(studentStringConverter);

        child3ComboBox.setItems(students);
        child3ComboBox.setButtonCell(new StudentListCell());
        child3ComboBox.setCellFactory(student -> new StudentListCell());
        child3ComboBox.setConverter(studentStringConverter);

        child4ComboBox.setItems(students);
        child4ComboBox.setButtonCell(new StudentListCell());
        child4ComboBox.setCellFactory(student -> new StudentListCell());
        child4ComboBox.setConverter(studentStringConverter);

        child5ComboBox.setItems(students);
        child5ComboBox.setButtonCell(new StudentListCell());
        child5ComboBox.setCellFactory(student -> new StudentListCell());
        child5ComboBox.setConverter(studentStringConverter);

        child2DeleteButton.setVisible(false);
        child3DeleteButton.setVisible(false);
        child4DeleteButton.setVisible(false);
        child5DeleteButton.setVisible(false);

        child2HBox.setVisible(false);
        child2HBox.setManaged(false);
        child3HBox.setVisible(false);
        child3HBox.setManaged(false);
        child4HBox.setVisible(false);
        child4HBox.setManaged(false);
        child5HBox.setVisible(false);
        child5HBox.setManaged(false);

        child1ComboBox.getSelectionModel().selectedIndexProperty().addListener(index -> {
            child1DeleteButton.setVisible(!child1ComboBox.getSelectionModel().isEmpty());
            child2HBox.setVisible(!child1ComboBox.getSelectionModel().isEmpty());
            child2HBox.setManaged(!child1ComboBox.getSelectionModel().isEmpty());
        });

        child2ComboBox.getSelectionModel().selectedIndexProperty().addListener(index -> {
            child2DeleteButton.setVisible(!child2ComboBox.getSelectionModel().isEmpty());
            child3HBox.setVisible(!child2ComboBox.getSelectionModel().isEmpty());
            child3HBox.setManaged(!child2ComboBox.getSelectionModel().isEmpty());
        });

        child3ComboBox.getSelectionModel().selectedIndexProperty().addListener(index -> {
            child3DeleteButton.setVisible(!child3ComboBox.getSelectionModel().isEmpty());
            child4HBox.setVisible(!child3ComboBox.getSelectionModel().isEmpty());
            child4HBox.setManaged(!child3ComboBox.getSelectionModel().isEmpty());
        });

        child4ComboBox.getSelectionModel().selectedIndexProperty().addListener(index -> {
            child4DeleteButton.setVisible(!child4ComboBox.getSelectionModel().isEmpty());
            child5HBox.setVisible(!child4ComboBox.getSelectionModel().isEmpty());
            child5HBox.setManaged(!child4ComboBox.getSelectionModel().isEmpty());
        });

        child5ComboBox.getSelectionModel().selectedIndexProperty().addListener(index -> {
            child5DeleteButton.setVisible(!child5ComboBox.getSelectionModel().isEmpty());
            newChildLabel.setVisible(child5ComboBox.getSelectionModel().isEmpty());
            newChildButton.setVisible(child5ComboBox.getSelectionModel().isEmpty());
        });

        parentDataForm.setVisible(true);
        communicationForm.setVisible(false);
    }
}
