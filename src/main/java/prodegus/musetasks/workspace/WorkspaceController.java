package prodegus.musetasks.workspace;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import prodegus.musetasks.contacts.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.OtherModel.getOtherListFromDB;
import static prodegus.musetasks.contacts.ParentModel.getParentFromDB;
import static prodegus.musetasks.contacts.ParentModel.getParentListFromDB;
import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;
import static prodegus.musetasks.contacts.StudentModel.getStudentListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class WorkspaceController implements Initializable {

    @FXML private MenuItem addStudentMenuItem;
    @FXML private MenuItem addParentMenuItem;
    @FXML private MenuItem addTeacherMenuItem;
    @FXML private MenuItem addOtherMenuItem;

    @FXML private Button editCancelNotesButton;
    @FXML private TextField contactSearchBar;

    @FXML private TableView<Contact> contactTableView;
    @FXML private TableColumn<Contact, Boolean> selectColumn;
    @FXML private TableColumn<Contact, String> lastNameColumn;
    @FXML private TableColumn<Contact, String> firstNameColumn;
    @FXML private TableColumn<Contact, String> categoryColumn;
    @FXML private TableColumn<Contact, String> locationColumn;
    @FXML private TableColumn<Contact, String> emailColumn;
    @FXML private TableColumn<Contact, String> phoneColumn;

    @FXML private TableView<Student> studentTableView;
    @FXML private TableColumn<Student, Boolean> studentSelectColumn;
    @FXML private TableColumn<Student, String> studentLastNameColumn;
    @FXML private TableColumn<Student, String> studentFirstNameColumn;
    @FXML private TableColumn<Student, String> studentInstrumentColumn;
    @FXML private TableColumn<Student, String> studentLocationColumn;
    @FXML private TableColumn<Student, String> studentTeacherColumn;
    @FXML private TableColumn<Student, String> studentStatusColumn;

    @FXML private TableView<Parent> parentTableView;

    @FXML private TableView<Teacher> teacherTableView;
    @FXML private TableColumn<Teacher, Boolean> teacherSelectColumn;
    @FXML private TableColumn<Teacher, String> teacherLastNameColumn;
    @FXML private TableColumn<Teacher, String> teacherFirstNameColumn;
    @FXML private TableColumn<Teacher, String> teacherLocationColumn;
    @FXML private TableColumn<Teacher, String> teacherInstrumentsColumn;

    @FXML private TableView<Other> otherTableView;

    @FXML private TextArea newNoteTextField;
    @FXML private TextArea notesTextArea;
    @FXML private Button saveNotesButton;
    @FXML private ToggleGroup contactCategoryToggle;
    @FXML private ToggleButton contactToggleAll;
    @FXML private ToggleButton contactToggleOthers;
    @FXML private ToggleButton contactToggleParents;
    @FXML private ToggleButton contactToggleProspectives;
    @FXML private ToggleButton contactToggleStudents;
    @FXML private ToggleButton contactToggleTeachers;

    @FXML private Label contactName;
    @FXML private Label contactCategory;
    @FXML private Label contactStreet;
    @FXML private Label contactCity;
    @FXML private Label contactEmail;
    @FXML private Label contactPhone;
    @FXML private Label contactZoom;

    @FXML private HBox studentInfo;
    @FXML private Label studentAge;
    @FXML private Label studentInstruments;
    @FXML private Label studentLesson1Name;
    @FXML private Label studentLesson1Time;
    @FXML private Label studentLesson1Weekday;
    @FXML private Label studentLesson2Name;
    @FXML private Label studentLesson2Time;
    @FXML private Label studentLesson2Weekday;
    @FXML private Label studentLesson3Name;
    @FXML private Label studentLesson3Time;
    @FXML private Label studentLesson3Weekday;
    @FXML private Label studentLocation;
    @FXML private Label studentParents;
    @FXML private Label studentStatusDate;
    @FXML private Label studentTeacher1;
    @FXML private Label studentTeacher2;
    @FXML private Label studentTeacher3;

    @FXML private HBox parentInfo;
    @FXML private Label parentBirthday;
    @FXML private Label parentChild1;
    @FXML private Label parentChild1Lesson;
    @FXML private Label parentChild1Teacher;
    @FXML private Label parentChild2;
    @FXML private Label parentChild2Lesson;
    @FXML private Label parentChild2Teacher;
    @FXML private Label parentChild3;
    @FXML private Label parentChild3Lesson;
    @FXML private Label parentChild3Teacher;
    @FXML private Label parentChild4;
    @FXML private Label parentChild4Lesson;
    @FXML private Label parentChild4Teacher;
    @FXML private Label parentChild5;
    @FXML private Label parentChild5Lesson;
    @FXML private Label parentChild5Teacher;
    @FXML private Label parentCustomerId;
    @FXML private Label parentLocation;
    @FXML private Label parentStatus;
    @FXML private Label parentStatusSince;
    @FXML private Label parentTeachers;

    @FXML private HBox teacherInfo;
    @FXML private Label teacherActiveSince;
    @FXML private Label teacherCourse1;
    @FXML private Label teacherCourse2;
    @FXML private Label teacherInstruments;
    @FXML private Label teacherLocation;
    @FXML private Label teacherLocationFriday;
    @FXML private Label teacherLocationMonday;
    @FXML private Label teacherLocationSaturday;
    @FXML private Label teacherLocationThursday;
    @FXML private Label teacherLocationTuesday;
    @FXML private Label teacherLocationWednesday;
    @FXML private Label teacherNumberOfStudents;

    @FXML private HBox otherInfo;
    @FXML private Label otherBirthday;
    @FXML private Label otherDescription;
    @FXML private Label otherLocation;

    private File xlsFile;
    
    private ObservableList<Contact> contacts;
    private FilteredList<Contact> filteredContacts;
    private SortedList<Contact> sortableContacts;

    private ObservableList<Student> students;
    private FilteredList<Student> filteredStudents;
    private SortedList<Student> sortableStudents;

    private ObservableList<Parent> parents;
    private FilteredList<Parent> filteredParents;
    private SortedList<Parent> sortableParents;

    private ObservableList<Teacher> teachers;
    private FilteredList<Teacher> filteredTeachers;
    private SortedList<Teacher> sortableTeachers;

    private ObservableList<Other> others;
    private FilteredList<Other> filteredOthers;
    private SortedList<Other> sortableOthers;

    private Contact selectedContact;
    private Student selectedStudent;
    private Parent selectedParent;
    private Teacher selectedTeacher;
    private Other selectedOther;
    private TableView<? extends Contact> selectedTableView;

    private InvalidationListener contactSearchListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            String filter = contactSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredContacts.setPredicate(contact -> true);
            } else {
                filteredContacts.setPredicate(contact ->
                        containsIgnoreCase(contact.getLastname(), filter) ||
                                containsIgnoreCase(contact.getFirstname(), filter));
            }
        }
    };

    private InvalidationListener studentSearchListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            String filter = contactSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredStudents.setPredicate(student -> true);
            } else {
                filteredStudents.setPredicate(student ->
                        containsIgnoreCase(student.getLastname(), filter) ||
                                containsIgnoreCase(student.getFirstname(), filter));
            }
        }
    };

    private InvalidationListener parentSearchListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            String filter = contactSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredParents.setPredicate(parent -> true);
            } else {
                filteredParents.setPredicate(parent ->
                        containsIgnoreCase(parent.getLastname(), filter) ||
                                containsIgnoreCase(parent.getFirstname(), filter));
            }
        }
    };

    private InvalidationListener teacherSearchListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            String filter = contactSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredTeachers.setPredicate(teacher -> true);
            } else {
                filteredTeachers.setPredicate(teacher ->
                        containsIgnoreCase(teacher.getLastname(), filter) ||
                                containsIgnoreCase(teacher.getFirstname(), filter));
            }
        }
    };

    private InvalidationListener otherSearchListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            String filter = contactSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredOthers.setPredicate(other -> true);
            } else {
                filteredOthers.setPredicate(other ->
                        containsIgnoreCase(other.getLastname(), filter) ||
                                containsIgnoreCase(other.getFirstname(), filter));
            }
        }
    };

    private boolean notesEditMode = false;

    @FXML
    void showAddStudentWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addstudent-view.fxml"));
        Stage stage = newStage("Schüler anlegen", loader);
        stage.showAndWait();
        refreshStudents();
    }

    @FXML
    void showAddParentWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addparent-view.fxml"));
        Stage stage = newStage("Elternteil anlegen", loader);
        stage.showAndWait();
        refreshStudents();
    }

    @FXML
    void showAddTeacherWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addteacher-view.fxml"));
        Stage stage = newStage("Lehrer anlegen", loader);
        stage.showAndWait();
        refreshTeachers();
    }

    @FXML
    void showAddOtherWindow(ActionEvent event) {

    }

    @FXML
    void deleteContacts(ActionEvent event) {
        Set<Contact> selectedContacts = new HashSet<>();
        for (Contact contact : contactTableView.getItems()) {
            if (contact.isSelected()) {
                deleteContactFromDB(contact);
            }
        }
        refreshContacts();
    }

    @FXML
    void loadFromXls(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS-Datei", "*.xls"));
        xlsFile = fileChooser.showOpenDialog(stageOf(event));
        addContactsFromXLSToDB(xlsFile, CONTACT_TABLE);
        refreshContactList(contacts);
    }

    @FXML
    void editCancelNotesButtonClicked(ActionEvent event) {
        if (!notesEditMode && editCancelNotesButton.getText().equals("Notizen bearbeiten")) {
            activateNotesEditMode();
            return;
        }
        if (!notesEditMode && editCancelNotesButton.getText().equals("Abbrechen")) {
            newNoteTextField.clear();
            editCancelNotesButton.setText("Notizen bearbeiten");
            return;
        }
        if (notesEditMode) {
            deactivateNotesEditMode();
        }
    }

    @FXML
    void refresh(ActionEvent event) {
        refreshAll();
    }

    @FXML
    void saveListConfig(ActionEvent event) {

    }

    @FXML
    void saveNotes(ActionEvent event) {
        if (selectedContact == null) {
            newNoteTextField.clear();
            return;
        }

        if (notesEditMode) {
            updateContactInDB(selectedContact, "notes", notesTextArea.getText());
            refreshAll();
            deactivateNotesEditMode();
            return;
        }

        StringBuilder sb = new StringBuilder();
        String newNote;

        // Timestamp
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy   HH:mm");
        String timestamp = formatter.format(now);

        // Build new note
        if (newNoteTextField.getText().isBlank()) return;
        if (!notesTextArea.getText().isEmpty()) sb.append("\n\n"); // Adds line breaks if previous note exists
        sb.append(timestamp).append("\n").append(newNoteTextField.getText());
        newNote = sb.toString();

        addNoteInDB(selectedContact, newNote);
        refreshAll();
        newNoteTextField.clear();
        deactivateNotesEditMode();
    }

    @FXML
    void searchBarKeyTyped(KeyEvent event) {
        newNoteTextField.clear();
    }

    @FXML
    void viewAllContacts(ActionEvent event) {
        refreshContacts();
        showTableView(contactTableView);
    }

    @FXML
    void viewAllOthers(ActionEvent event) {

    }

    @FXML
    void viewAllStudents(ActionEvent event) {
        refreshStudents();
        replaceSearchListener(studentSearchListener);
        showTableView(studentTableView);
        studentTableView.setItems(sortableStudents);
    }

    @FXML
    void viewAllTeachers(ActionEvent event) {
        refreshTeachers();
        replaceSearchListener(teacherSearchListener);
        showTableView(teacherTableView);
        teacherTableView.setItems(sortableTeachers);
    }

    @FXML
    void viewAllParents(ActionEvent event) {

    }

    @FXML
    void viewAllProspectives(ActionEvent event) {

    }

    @FXML
    void chooseFilter(ActionEvent event) {

    }

    private void showTableView(TableView<? extends Contact> tableView) {
        contactTableView.setVisible(false);
        studentTableView.setVisible(false);
        parentTableView.setVisible(false);
        teacherTableView.setVisible(false);
        otherTableView.setVisible(false);
        tableView.setVisible(true);
        selectedTableView = tableView;
    }

    private void refreshAll() {
        refreshContacts();
        refreshStudents();
        refreshTeachers();
        refreshParents();
        refreshOthers();
        tableViewSelect(selectedTableView);
    }

    private void refreshStudents() {
        students = getStudentListFromDB();
        filteredStudents = new FilteredList<>(students, student -> true);
        sortableStudents = new SortedList<>(filteredStudents);
        sortableStudents.comparatorProperty().bind(studentTableView.comparatorProperty());
        studentTableView.setItems(sortableStudents);
    }

    private void refreshTeachers() {
        teachers = getTeacherListFromDB();
        filteredTeachers = new FilteredList<>(teachers, teacher -> true);
        sortableTeachers = new SortedList<>(filteredTeachers);
        sortableTeachers.comparatorProperty().bind(teacherTableView.comparatorProperty());
        teacherTableView.setItems(sortableTeachers);
    }

    private void refreshParents() {
        parents = getParentListFromDB();
        filteredParents = new FilteredList<>(parents, parent -> true);
        sortableParents = new SortedList<>(filteredParents);
        sortableParents.comparatorProperty().bind(parentTableView.comparatorProperty());
        parentTableView.setItems(sortableParents);
    }

    private void refreshOthers() {
        others = getOtherListFromDB();
        filteredOthers = new FilteredList<>(others, other -> true);
        sortableOthers = new SortedList<>(filteredOthers);
        sortableOthers.comparatorProperty().bind(otherTableView.comparatorProperty());
        otherTableView.setItems(sortableOthers);
    }

    private void replaceSearchListener(InvalidationListener newListener) {
        removeSearchListeners();
        contactSearchBar.textProperty().addListener(newListener);
    }

    private void removeSearchListeners() {
        contactSearchBar.textProperty().removeListener(contactSearchListener);
        contactSearchBar.textProperty().removeListener(studentSearchListener);
        contactSearchBar.textProperty().removeListener(parentSearchListener);
        contactSearchBar.textProperty().removeListener(teacherSearchListener);
        contactSearchBar.textProperty().removeListener(otherSearchListener);
    }

    void refreshContacts() {
        refreshContactList(contacts);
        filteredContacts = new FilteredList<>(contacts, contact -> true);
        sortableContacts = new SortedList<>(filteredContacts);
        contactTableView.setItems(sortableContacts);
        tableViewSelect(selectedTableView);
    }

    void activateNotesEditMode() {
        if (selectedContact == null) return;
        notesEditMode = true;
        notesTextArea.setEditable(true);
        setTransparency(notesTextArea, false);

        editCancelNotesButton.setText("Abbrechen");

        saveNotesButton.setDisable(false);

        newNoteTextField.clear();
        newNoteTextField.setVisible(false);
        newNoteTextField.setManaged(false);
    }

    void deactivateNotesEditMode() {
        if (selectedContact != null)
            notesTextArea.setText(selectedContact.getNotes());
        notesEditMode = false;
        notesTextArea.setEditable(false);
        setTransparency(notesTextArea, true);

        notesTextArea.setScrollTop(Double.MAX_VALUE);
        notesTextArea.appendText(""); // workaround to scroll to bottom of TextArea

        editCancelNotesButton.setText("Notizen bearbeiten");

        saveNotesButton.setDisable(true);

        newNoteTextField.clear();
        newNoteTextField.setManaged(true);
        newNoteTextField.setVisible(true);

    }

    void tableViewSelect(TableView<? extends Contact> tableView) {
        if (selectedContact == null) return;
        int index = 0;
        for (Contact contact : tableView.getItems()) {
            if (contact.table().equals(selectedContact.table()) && contact.id().equals(selectedContact.id())) {
                break;
            }
            index++;
        }
        tableView.getSelectionModel().select(index);
        selectedContact = tableView.getSelectionModel().getSelectedItem();
    }

    void setTransparency(TextArea area, boolean transparency) {
        String transparentUrl = getClass().getResource("/css/textareaTransparent.css").toExternalForm();
        String editedUrl = getClass().getResource("/css/textareaEdited.css").toExternalForm();
        String newUrl = transparency ? transparentUrl : editedUrl;

        area.getStylesheets().clear();
        area.getStylesheets().add(newUrl);
    }

    private void enableContactSelection(TableView<? extends Contact> tableView) {
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (tableView.getSelectionModel().isEmpty()) return;
                selectedContact = tableView.getSelectionModel().getSelectedItem();
                showContactInfo(selectedContact);
            }
        });
    }

    private void showContactInfo(Contact contact) {
        contactName.setText(contact.getFirstname() + " " + contact.getLastname());
        contactCategory.setText(contact.getCategory());
        contactStreet.setText(contact.getStreet());
        contactCity.setText(contact.getPostalCode() + " " + contact.getCity());
        contactEmail.setText(contact.getEmail());
        contactPhone.setText(contact.getPhone());
        contactZoom.setText(contact.getZoom());
        notesTextArea.setText(selectedContact.getNotes());
        notesTextArea.setScrollTop(Double.MAX_VALUE);
        notesTextArea.appendText(""); // workaround to scroll to bottom of TextArea
        deactivateNotesEditMode();

        switch (contact.getCategory()) {
            case CATEGORY_STUDENT -> showStudentInfo(contact);
//            case "Lehrer" -> showTeacherInfo(contact);
            case CATEGORY_PARENT -> showParentInfo(contact);
//            case "Sonstige" -> showOtherInfo(contact);
        }
    }

    private void showStudentInfo(Contact contact) {
        selectedStudent = getStudentFromDB(contact.getId());

        switchInfoArea(studentInfo);
        studentAge.setText(selectedStudent.ageAndBirthday());
        studentInstruments.setText(selectedStudent.instruments());
        studentLesson1Name.setText(selectedStudent.lesson1Name());
        studentLesson1Time.setText(selectedStudent.lesson1Time());
        studentLesson1Weekday.setText(selectedStudent.lesson1Weekday());
        studentLesson2Name.setText(selectedStudent.lesson2Name());
        studentLesson2Time.setText(selectedStudent.lesson2Time());
        studentLesson2Weekday.setText(selectedStudent.lesson2Weekday());
        studentLesson3Name.setText(selectedStudent.lesson3Name());
        studentLesson3Time.setText(selectedStudent.lesson3Time());
        studentLesson3Weekday.setText(selectedStudent.lesson3Weekday());
        studentLocation.setText(selectedStudent.getLocation());
        studentParents.setText(selectedStudent.parentsNames());
        studentStatusDate.setText(selectedStudent.status());
        studentTeacher1.setText(selectedStudent.teacher1Name());
        studentTeacher2.setText(selectedStudent.teacher2Name());
        studentTeacher3.setText(selectedStudent.teacher3Name());
    }
    
    private void showParentInfo(Contact contact) {
        selectedParent = getParentFromDB(contact.getId());
        Student child1 = selectedParent.child1();
        Student child2 = selectedParent.child2();
        Student child3 = selectedParent.child3();
        Student child4 = selectedParent.child4();
        Student child5 = selectedParent.child5();
        StringBuilder teachers = new StringBuilder();
        
        switchInfoArea(parentInfo);
        clearParentInfo();
        parentBirthday.setText(selectedParent.getBirthDate());

        if (child1 != null) {
            parentChild1.setText(child1.name());
            parentChild1Lesson.setText(child1.lesson1Name());
            parentChild1Teacher.setText(child1.teacher1Name());
        }

        if (child2 != null) {
            parentChild2.setText(child2.name());
            parentChild2Lesson.setText(child2.lesson1Name());
            parentChild2Teacher.setText(child2.teacher1Name());
        }

        if (child3 != null) {
            parentChild3.setText(child3.name());
            parentChild3Lesson.setText(child3.lesson1Name());
            parentChild3Teacher.setText(child3.teacher1Name());
        }

        if (child4 != null) {
            parentChild4.setText(child4.name());
            parentChild4Lesson.setText(child4.lesson1Name());
            parentChild4Teacher.setText(child4.teacher1Name());
        }

        if (child5 != null) {
            parentChild5.setText(child5.name());
            parentChild5Lesson.setText(child5.lesson1Name());
            parentChild5Teacher.setText(child5.teacher1Name());
        }
        
        parentCustomerId.setText("");
        parentLocation.setText(selectedParent.getLocation());
        parentStatus.setText("");
        parentStatusSince.setText("");
        parentTeachers.setText("");
    }
    
    private void clearParentInfo() {
        parentBirthday.setText("");
        parentChild1.setText("");
        parentChild1Lesson.setText("");
        parentChild1Teacher.setText("");
        parentChild2.setText("");
        parentChild2Lesson.setText("");
        parentChild2Teacher.setText("");
        parentChild3.setText("");
        parentChild3Lesson.setText("");
        parentChild3Teacher.setText("");
        parentChild4.setText("");
        parentChild4Lesson.setText("");
        parentChild4Teacher.setText("");
        parentChild5.setText("");
        parentChild5Lesson.setText("");
        parentChild5Teacher.setText("");
        parentCustomerId.setText("");
        parentLocation.setText("");
        parentStatus.setText("");
        parentStatusSince.setText("");
        parentTeachers.setText("");        
    }

    private void switchInfoArea(HBox newInfo) {
        studentInfo.setVisible(false);
        parentInfo.setVisible(false);
        teacherInfo.setVisible(false);
        otherInfo.setVisible(false);
        newInfo.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize contacts
        contacts = getContactList();
        filteredContacts = new FilteredList<>(contacts, contact -> true);
        sortableContacts = new SortedList<>(filteredContacts);
        sortableContacts.comparatorProperty().bind(contactTableView.comparatorProperty());

        // Initialize SearchBar
        contactSearchBar.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String filter = contactSearchBar.getText();
                if (filter == null || filter.isBlank()) {
                    filteredContacts.setPredicate(contact -> true);
                } else {
                    filteredContacts.setPredicate(contact ->
                            containsIgnoreCase(contact.getLastname(), filter) ||
                                    containsIgnoreCase(contact.getFirstname(), filter));
                }
            }
        });

        // Initialize TableView: All contacts
        selectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        lastNameColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.14));

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        firstNameColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.14));

        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.14));

        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.14));

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.19));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.19));

        contactTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        contactTableView.setItems(sortableContacts);
        contactTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());
        enableContactSelection(contactTableView);

        // Initialize TableView: Students
        studentSelectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        studentSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(studentSelectColumn));

        studentLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        studentLastNameColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.14));

        studentFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        studentFirstNameColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.14));

        studentInstrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrument1"));
        studentInstrumentColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.14));

        studentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        studentLocationColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.14));

        studentTeacherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().teacher1Name()));
        studentTeacherColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.19));

        studentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        studentStatusColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.19));

        studentTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentTableView.setItems(sortableStudents);
        studentTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());
        enableContactSelection(studentTableView);

        // Initialize TableView: Teachers
        teacherSelectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        teacherSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(teacherSelectColumn));

        teacherLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        teacherLastNameColumn.prefWidthProperty().bind(teacherTableView.widthProperty().multiply(0.15));

        teacherFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        teacherFirstNameColumn.prefWidthProperty().bind(teacherTableView.widthProperty().multiply(0.15));
        
        teacherLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        teacherLocationColumn.prefWidthProperty().bind(teacherTableView.widthProperty().multiply(0.32));

        teacherInstrumentsColumn.setCellValueFactory(new PropertyValueFactory<>("instruments"));
        teacherInstrumentsColumn.prefWidthProperty().bind(teacherTableView.widthProperty().multiply(0.32));

        teacherTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        teacherTableView.setItems(sortableTeachers);
        teacherTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());
        enableContactSelection(teacherTableView);

        // Initialize displayed table view
        showTableView(contactTableView);

        // Initialize TextArea: Notes
        setTransparency(notesTextArea, true);

        // Initialize TextArea: New note
        newNoteTextField.textProperty().addListener((obs, oldValue, newValue)  -> {
            if (newValue.isBlank()) {
                saveNotesButton.setDisable(true);
                editCancelNotesButton.setText("Notizen bearbeiten");
                return;
            }
            saveNotesButton.setDisable(false);
            editCancelNotesButton.setText("Abbrechen");
        });

        // Initialize Button: Save notes
        saveNotesButton.setDisable(true);

        // Initialize instruments
        SCHOOL_INSTRUMENTS.add("Gesang");
        SCHOOL_INSTRUMENTS.add("Klavier");
        SCHOOL_INSTRUMENTS.add("Gitarre");
        SCHOOL_INSTRUMENTS.add("Schlagzeug");
        SCHOOL_INSTRUMENTS.add("E-Bass");
        SCHOOL_INSTRUMENTS.add("Saxophon");
        SCHOOL_INSTRUMENTS.add("Klarinette");

        // Initialize locations
        SCHOOL_LOCATIONS.add("Lohmar");
        SCHOOL_LOCATIONS.add("Gummersbach");
        SCHOOL_LOCATIONS.add("Meckenheim");
    }
}
