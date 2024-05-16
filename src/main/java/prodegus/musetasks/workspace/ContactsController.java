package prodegus.musetasks.workspace;

        import javafx.beans.InvalidationListener;
        import javafx.beans.Observable;
        import javafx.beans.property.SimpleStringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.collections.transformation.FilteredList;
        import javafx.collections.transformation.SortedList;
        import javafx.event.ActionEvent;
        import javafx.event.EventHandler;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.fxml.Initializable;
        import javafx.scene.Node;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.CheckBoxTableCell;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.input.KeyEvent;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.GridPane;
        import javafx.scene.layout.HBox;
        import javafx.scene.layout.VBox;
        import javafx.scene.text.Font;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;
        import prodegus.musetasks.contacts.*;
        import prodegus.musetasks.mail.NewMailController;
        import prodegus.musetasks.ui.PopupWindow;
        import prodegus.musetasks.workspace.cells.StringListCell;
        import prodegus.musetasks.workspace.cells.TeacherListCellShort;

        import java.io.File;
        import java.net.URL;
        import java.time.LocalDateTime;
        import java.time.format.DateTimeFormatter;
        import java.util.*;

        import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
        import static prodegus.musetasks.contacts.ContactModel.*;
        import static prodegus.musetasks.contacts.OtherModel.getOtherFromDB;
        import static prodegus.musetasks.contacts.OtherModel.getOtherListFromDB;
        import static prodegus.musetasks.contacts.ParentModel.getParentFromDB;
        import static prodegus.musetasks.contacts.ParentModel.getParentListFromDB;
        import static prodegus.musetasks.contacts.StudentModel.*;
        import static prodegus.musetasks.contacts.TeacherModel.*;
        import static prodegus.musetasks.contacts.VCard.vCard;
        import static prodegus.musetasks.database.Database.*;
        import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
        import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
        import static prodegus.musetasks.ui.StageFactories.newStage;
        import static prodegus.musetasks.ui.StageFactories.stageOf;

public class ContactsController implements Initializable {

//    ContactsController - Fields ---------------------------------------------------------------------------------------

    @FXML private MenuItem addStudentMenuItem;
    @FXML private MenuItem addParentMenuItem;
    @FXML private MenuItem addTeacherMenuItem;
    @FXML private MenuItem addOtherMenuItem;

    @FXML private Button editCancelNotesButton;
    @FXML private CheckBox selectAllCheckBox;
    @FXML private TextField contactSearchBar;

    @FXML private TableView<Contact> contactTableView;
    @FXML private TableColumn<Contact, Boolean> selectColumn;
    @FXML private TableColumn<Contact, String> nameColumn;
    @FXML private TableColumn<Contact, String> categoryColumn;
    @FXML private TableColumn<Contact, String> locationColumn;
    @FXML private TableColumn<Contact, String> emailColumn;
    @FXML private TableColumn<Contact, String> phoneColumn;

    @FXML private TableView<Student> studentTableView;
    @FXML private TableColumn<Student, Boolean> studentSelectColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, String> studentInstrumentColumn;
    @FXML private TableColumn<Student, String> studentLocationColumn;
    @FXML private TableColumn<Student, String> studentTeacherColumn;
    @FXML private TableColumn<Student, String> studentStatusColumn;

    @FXML private TableView<Parent> parentTableView;
    @FXML private TableColumn<Parent, Boolean> parentSelectColumn;
    @FXML private TableColumn<Parent, String> parentNameColumn;
    @FXML private TableColumn<Parent, String> parentLocationColumn;
    @FXML private TableColumn<Parent, String> parentTeacherColumn;
    @FXML private TableColumn<Parent, String> parentStatusColumn;

    @FXML private TableView<Teacher> teacherTableView;
    @FXML private TableColumn<Teacher, Boolean> teacherSelectColumn;
    @FXML private TableColumn<Teacher, String> teacherNameColumn;
    @FXML private TableColumn<Teacher, String> teacherLocationColumn;
    @FXML private TableColumn<Teacher, String> teacherInstrumentsColumn;

    @FXML private TableView<Other> otherTableView;
    @FXML private TableColumn<Other, Boolean> otherSelectColumn;
    @FXML private TableColumn<Other, String> otherNameColumn;
    @FXML private TableColumn<Other, String> otherDescriptionColumn;
    @FXML private TableColumn<Other, String> otherPhoneColumn;
    @FXML private TableColumn<Other, String> otherEmailColumn;

    @FXML private TextArea newNoteTextField;
    @FXML private TextArea notesTextArea;
    @FXML private Button saveNotesButton;
    @FXML private ToggleGroup contactCategoryToggle;
    @FXML private ToggleButton contactToggleAll;
    @FXML private ToggleButton contactToggleCustomers;
    @FXML private ToggleButton contactToggleOthers;
    @FXML private ToggleButton contactToggleParents;
    @FXML private ToggleButton contactToggleProspectives;
    @FXML private ToggleButton contactToggleStudents;
    @FXML private ToggleButton contactToggleTeachers;

    @FXML private TitledPane filterPane;
    @FXML private CheckBox filterCategoryOther;
    @FXML private CheckBox filterCategoryParent;
    @FXML private CheckBox filterCategoryStudent;
    @FXML private CheckBox filterCategoryTeacher;
    @FXML private CheckBox filterInstrument1;
    @FXML private ComboBox<String> filterInstrument1ComboBox;
    @FXML private CheckBox filterInstrument2;
    @FXML private ComboBox<String> filterInstrument2ComboBox;
    @FXML private CheckBox filterInstrument3;
    @FXML private ComboBox<String> filterInstrument3ComboBox;
    @FXML private CheckBox filterLocation1;
    @FXML private CheckBox filterLocation2;
    @FXML private CheckBox filterLocation3;
    @FXML private CheckBox filterTeacher1;
    @FXML private ComboBox<Teacher> filterTeacher1ComboBox;
    @FXML private CheckBox filterTeacher2;
    @FXML private ComboBox<Teacher> filterTeacher2ComboBox;
    @FXML private CheckBox filterTeacher3;
    @FXML private ComboBox<Teacher> filterTeacher3ComboBox;
    @FXML private CheckBox filterProspectives;
    @FXML private CheckBox filterCustomers;

    @FXML private VBox contactDetails;
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
    @FXML private GridPane parentChildrenInfo;
    @FXML private Label parentCustomerId;
    @FXML private Label parentLocation;
    @FXML private Label parentStatus;
    @FXML private Label parentStatusSince;
    @FXML private Label parentSpouse;

    @FXML private HBox teacherInfo;
    @FXML private Label teacherStatus;
    @FXML private Label teacherStatusDate;
    @FXML private Label teacherCourses;
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

    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private final FilteredList<Contact> filteredContacts = new FilteredList<>(contacts, contact -> true);
    private final SortedList<Contact> sortableContacts = new SortedList<>(filteredContacts);

    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private final FilteredList<Student> filteredStudents = new FilteredList<>(students, student -> true);
    private final SortedList<Student> sortableStudents = new SortedList<>(filteredStudents);

    private final ObservableList<Parent> parents = FXCollections.observableArrayList();
    private final FilteredList<Parent> filteredParents = new FilteredList<>(parents, parent -> true);
    private final SortedList<Parent> sortableParents = new SortedList<>(filteredParents);

    private final ObservableList<Teacher> teachers = FXCollections.observableArrayList();
    private final FilteredList<Teacher> filteredTeachers = new FilteredList<>(teachers, teacher -> true);
    private final SortedList<Teacher> sortableTeachers = new SortedList<>(filteredTeachers);

    private final ObservableList<Other> others = FXCollections.observableArrayList();
    private final FilteredList<Other> filteredOthers = new FilteredList<>(others, other -> true);
    private final SortedList<Other> sortableOthers = new SortedList<>(filteredOthers);

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
                        containsIgnoreCase(contact.getLastName(), filter) ||
                                containsIgnoreCase(contact.getFirstName(), filter));
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
                        containsIgnoreCase(student.getLastName(), filter) ||
                                containsIgnoreCase(student.getFirstName(), filter));
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
                        containsIgnoreCase(parent.getLastName(), filter) ||
                                containsIgnoreCase(parent.getFirstName(), filter));
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
                        containsIgnoreCase(teacher.getLastName(), filter) ||
                                containsIgnoreCase(teacher.getFirstName(), filter));
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
                        containsIgnoreCase(other.getLastName(), filter) ||
                                containsIgnoreCase(other.getFirstName(), filter));
            }
        }
    };

    private boolean notesEditMode = false;


//    ContactsController - Methods -------------------------------------------------------------------------------------

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addother-view.fxml"));
        Stage stage = newStage("Sonstigen Kontakt anlegen", loader);
        stage.showAndWait();
        refreshOthers();
    }

    @FXML
    void deleteContact(ActionEvent event) {
        if (!PopupWindow.displayYesNo("Kontakt '" + selectedContact.name() + "' wird gelöscht. Fortfahren?")) return;
        ContactModel.deleteContact(selectedContact);
        contactDetails.setVisible(false);
        refreshContacts();
    }

    @FXML
    void deleteContacts(ActionEvent event) {
        ArrayList<Contact> selectedContacts = new ArrayList<>();
        StringJoiner names = new StringJoiner("\n");

        for (Contact contact : selectedTableView.getItems()) {
            if (contact.isSelected()) {
                selectedContacts.add(contact);
                names.add(contact.name());
            }
        }

        if (selectedContacts.size() == 0) {
            PopupWindow.displayInformation("Keine Kontakte ausgewählt!");
            return;
        }

        String message = selectedContacts.size() == 1 ? "Kontakt '" + names + "' wird gelöscht. Fortfahren?" :
                "Folgende Kontakte werden gelöscht:\n\n" + names + "\n\nFortfahren?";
        if (!PopupWindow.displayYesNo(message)) return;
        for (Contact contact : selectedContacts) ContactModel.deleteContact(contact);
        refreshContacts();
    }

    @FXML
    void loadFromXls(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS-Datei", "*.xls"));
        xlsFile = fileChooser.showOpenDialog(stageOf(event));
        addContactsFromXLSToDB(xlsFile, CONTACT_TABLE);
        contacts.setAll(getContactListFromDB());
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
    void editContact(ActionEvent event) {
        switch (selectedContact.getCategory()) {
            case CATEGORY_STUDENT, CATEGORY_PROSPECTIVE_STUDENT -> showEditStudentWindow(selectedContact);
            case CATEGORY_PARENT, CATEGORY_PROSPECTIVE_PARENT -> showEditParentWindow(selectedContact);
            case CATEGORY_TEACHER -> showEditTeacherWindow(selectedContact);
            case CATEGORY_OTHER -> showEditOtherWindow(selectedContact);
        }
    }

    private void showEditStudentWindow(Contact selectedContact) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addstudent-view.fxml"));
        Stage stage = newStage("Schüler bearbeiten", loader);
        AddStudentController controller = loader.getController();
        controller.initStudent(selectedContact.toStudent());
        stage.showAndWait();
        refreshStudents();
    }

    private void showEditParentWindow(Contact selectedContact) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addparent-view.fxml"));
        Stage stage = newStage("Elternteil bearbeiten", loader);
        AddParentController controller = loader.getController();
        controller.initParent(selectedContact.toParent());
        stage.showAndWait();
        refreshParents();
    }

    private void showEditTeacherWindow(Contact selectedContact) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addteacher-view.fxml"));
        Stage stage = newStage("Lehrer bearbeiten", loader);
        AddTeacherController controller = loader.getController();
        controller.initTeacher(selectedContact.toTeacher());
        stage.showAndWait();
        refreshTeachers();
    }

    private void showEditOtherWindow(Contact selectedContact) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addother-view.fxml"));
        Stage stage = newStage("Lehrer bearbeiten", loader);
        AddOtherController controller = loader.getController();
        controller.initOther(selectedContact.toOther());
        stage.showAndWait();
        refreshOthers();
    }

    @FXML
    void forwardContact(ActionEvent event) {
        File vCard = vCard(selectedContact, "");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Kontakt weiterleiten", loader);
        NewMailController controller = loader.getController();
        controller.init("Visitenkarte: " + selectedContact.name(), Collections.singletonList(vCard));
        stage.showAndWait();
    }

    @FXML
    void forwardContacts(ActionEvent event) {
        List<Contact> selectedContacts = new ArrayList<>();
        List<File> vCards = new ArrayList<>();
        StringBuilder subject = new StringBuilder("Visitenkarte: ");

        for (Contact contact : selectedTableView.getItems()) {
            if (contact.isSelected()) {
                selectedContacts.add(contact);
                vCards.add(vCard(contact, ""));
            }
        }

        if (selectedContacts.isEmpty()) {
            PopupWindow.displayInformation("Keine Kontakte ausgewählt!");
            return;
        }

        subject.append(selectedContacts.get(0).name());
        if (vCards.size() > 1) subject.append(" und ").append(vCards.size() - 1).append(" weitere");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Kontakt weiterleiten", loader);
        NewMailController controller = loader.getController();
        controller.init(subject.toString(), vCards);
        stage.showAndWait();
    }

    @FXML
    void mailToContacts(ActionEvent event) {
        StringJoiner recipients = new StringJoiner(", ");

        for (Contact contact : selectedTableView.getItems()) {
            if (contact.isSelected() && !contact.getEmail().isBlank()) recipients.add(contact.getEmail());
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init(recipients.toString());
        stage.showAndWait();
    }

    @FXML
    void refresh(ActionEvent event) {
        refreshAll();
    }

    @FXML
    void resetFilter(ActionEvent event) {
        contacts.setAll(getContactListFromDB());
        replaceSearchListener(contactSearchListener);
        contactTableView.getSortOrder().setAll(nameColumn);
        showTableView(contactTableView);
        contactToggleAll.setSelected(true);
        filterPane.setExpanded(false);

        for (Node node : getAllNodes(filterPane)) {
            if (node.getClass().getSimpleName().equals("TitledPane")) ((TitledPane)node).setExpanded(false);
            if (node.getClass().getSimpleName().equals("CheckBox")) ((CheckBox)node).setSelected(false);
            if (node.getClass().getSimpleName().equals("ComboBox")) ((ComboBox)node).getSelectionModel().clearSelection();
        }

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
            ContactModel.updateContact(selectedContact, "notes", notesTextArea.getText());
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
    void sendMailToContact(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init(selectedContact.getEmail());
        stage.showAndWait();
    }

    @FXML
    void viewAllContacts(ActionEvent event) {
        refreshContacts();
        replaceSearchListener(contactSearchListener);
        contactTableView.getSortOrder().setAll(nameColumn);
        showTableView(contactTableView);
    }

    @FXML
    void viewAllStudents(ActionEvent event) {
        refreshStudents();
        replaceSearchListener(studentSearchListener);
        studentTableView.getSortOrder().setAll(studentNameColumn);
        showTableView(studentTableView);
    }

    @FXML
    void viewAllTeachers(ActionEvent event) {
        refreshTeachers();
        replaceSearchListener(teacherSearchListener);
        teacherTableView.getSortOrder().setAll(teacherNameColumn);
        showTableView(teacherTableView);
    }

    @FXML
    void viewAllParents(ActionEvent event) {
        refreshParents();
        replaceSearchListener(parentSearchListener);
        parentTableView.getSortOrder().setAll(parentNameColumn);
        showTableView(parentTableView);
    }

    @FXML
    void viewAllOthers(ActionEvent event) {
        refreshOthers();
        replaceSearchListener(otherSearchListener);
        otherTableView.getSortOrder().setAll(otherNameColumn);
        showTableView(otherTableView);
    }

    @FXML
    void viewAllCustomers(ActionEvent event) {
        contacts.setAll(getContactListFromDB());
        contacts.removeIf(contact -> !contact.isCustomer());
        replaceSearchListener(contactSearchListener);
        contactTableView.getSortOrder().setAll(nameColumn);
        showTableView(contactTableView);
    }

    @FXML
    void viewAllProspectives(ActionEvent event) {
        students.setAll(getStudentListFromDB());
        students.removeIf(student -> !student.isProspective());
        replaceSearchListener(studentSearchListener);
        studentTableView.getSortOrder().setAll(studentNameColumn);
        showTableView(studentTableView);
    }


    @FXML
    void viewFilteredContacts(ActionEvent event) {
        for (Toggle toggle : contactCategoryToggle.getToggles()) {
            toggle.setSelected(false);
        }
        contacts.setAll(getContactListFromDB());
        replaceSearchListener(contactSearchListener);
        contactTableView.getSortOrder().setAll(nameColumn);
        showTableView(contactTableView);

        contacts.removeIf(contact -> !fitsCategoryFilter(contact));
        contacts.removeIf(contact -> !fitsLocationFilter(contact));
        contacts.removeIf(contact -> !fitsTeacherFilter(contact));
        contacts.removeIf(contact -> !fitsInstrumentFilter(contact));
        contacts.removeIf(contact -> !fitsStatusFilter(contact));
    }

    private boolean fitsCategoryFilter(Contact contact) {
        if (!filterCategoryStudent.isSelected() && !filterCategoryParent.isSelected() &&
                !filterCategoryTeacher.isSelected() && !filterCategoryOther.isSelected()) return true;
        boolean filterStudent = filterCategoryStudent.isSelected() && contact.isStudent();
        boolean filterParent =  filterCategoryParent.isSelected() && contact.isParent();
        boolean filterTeacher = filterCategoryTeacher.isSelected() && contact.isTeacher();
        boolean filterOther = filterCategoryOther.isSelected() && contact.isOther();
        return filterStudent || filterParent || filterTeacher || filterOther;
    }

    private boolean fitsLocationFilter(Contact contact) {
        if (!filterLocation1.isSelected() && !filterLocation2.isSelected() && !filterLocation3.isSelected()) return true;
        boolean filterLoc1 = filterLocation1.isSelected() && contact.getLocation().contains(filterLocation1.getText());
        boolean filterLoc2 = filterLocation2.isSelected() && contact.getLocation().contains(filterLocation2.getText());
        boolean filterLoc3 = filterLocation3.isSelected() && contact.getLocation().contains(filterLocation3.getText());
        return filterLoc1 || filterLoc2 || filterLoc3;
    }

    private boolean fitsTeacherFilter(Contact contact) {
        if (!filterTeacher1.isSelected() && !filterTeacher2.isSelected() && !filterTeacher3.isSelected()) return true;
        boolean filterT1 = filterTeacher1.isSelected() && contact.hasTeacher(filterTeacher1ComboBox.getValue());
        boolean filterT2 = filterTeacher2.isSelected() && contact.hasTeacher(filterTeacher2ComboBox.getValue());
        boolean filterT3 = filterTeacher3.isSelected() && contact.hasTeacher(filterTeacher3ComboBox.getValue());
        return filterT1 || filterT2 || filterT3;
    }

    private boolean fitsInstrumentFilter(Contact contact) {
        if (!filterInstrument1.isSelected() && !filterInstrument2.isSelected() && !filterInstrument3.isSelected()) return true;
        boolean filterInst1 = filterInstrument1.isSelected() && contact.hasInstrument(filterInstrument1ComboBox.getValue());
        boolean filterInst2 = filterInstrument2.isSelected() && contact.hasInstrument(filterInstrument2ComboBox.getValue());
        boolean filterInst3 = filterInstrument3.isSelected() && contact.hasInstrument(filterInstrument3ComboBox.getValue());
        return filterInst1 || filterInst2 || filterInst3;
    }

    private boolean fitsStatusFilter(Contact contact) {
        if (!filterProspectives.isSelected() && !filterCustomers.isSelected()) return true;
        boolean filterProspect = filterProspectives.isSelected() && contact.isProspective();
        boolean filterCustom = filterCustomers.isSelected() && contact.isCustomer();
        return filterProspect || filterCustom;
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

    private void refreshContacts() {
        contacts.setAll(getContactListFromDB());
        tableViewSelect(selectedTableView);
    }

    private void refreshStudents() {
        students.setAll(getStudentListFromDB());
        contacts.setAll(getContactListFromDB());
        tableViewSelect(selectedTableView);
    }

    private void refreshTeachers() {
        teachers.setAll(getTeacherListFromDB());
        contacts.setAll(getContactListFromDB());
        tableViewSelect(selectedTableView);
    }

    private void refreshParents() {
        parents.setAll(getParentListFromDB());
        contacts.setAll(getContactListFromDB());
        tableViewSelect(selectedTableView);
    }

    private void refreshOthers() {
        others.setAll(getOtherListFromDB());
        contacts.setAll(getContactListFromDB());
        tableViewSelect(selectedTableView);
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
                contactDetails.setVisible(true);
            }
        });
    }

    private void showContactInfo(Contact contact) {
        contactName.setText(contact.name());
        contactCategory.setText(switch (contact.getCategory()) {
            case CATEGORY_STUDENT -> "Schüler";
            case CATEGORY_PROSPECTIVE_STUDENT -> "Schüler (Interessent)";
            case CATEGORY_PARENT -> "Elternteil";
            case CATEGORY_PROSPECTIVE_PARENT -> "Elternteil (Interessent)";
            case CATEGORY_TEACHER -> "Lehrer";
            case CATEGORY_OTHER -> "Sonstiger Kontakt";
            default -> "";
        });
        contactStreet.setText(contact.getStreet());
        contactCity.setText(contact.getPostalCodeCity());
        contactEmail.setText(contact.getEmail());
        contactPhone.setText(contact.getPhone());
        contactZoom.setText(contact.getZoom());
        notesTextArea.setText(selectedContact.getNotes());
        notesTextArea.setScrollTop(Double.MAX_VALUE);
        notesTextArea.appendText(""); // workaround to scroll to bottom of TextArea
        deactivateNotesEditMode();

        switch (contact.getCategory()) {
            case CATEGORY_STUDENT, CATEGORY_PROSPECTIVE_STUDENT -> showStudentInfo(contact);
            case CATEGORY_TEACHER -> showTeacherInfo(contact);
            case CATEGORY_PARENT, CATEGORY_PROSPECTIVE_PARENT -> showParentInfo(contact);
            case CATEGORY_OTHER -> showOtherInfo(contact);
        }
    }

    public static ArrayList<Node> getAllNodes(javafx.scene.Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendants(root, nodes);
        return nodes;
    }

    private static void addAllDescendants(javafx.scene.Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof javafx.scene.Parent)
                addAllDescendants((javafx.scene.Parent)node, nodes);
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
        Student[] children = {child1, child2, child3, child4, child5};
        StringBuilder teachers = new StringBuilder();

        switchInfoArea(parentInfo);
        clearParentInfo();
        parentBirthday.setText(selectedParent.getBirthDate());
        parentCustomerId.setText("");
        parentLocation.setText(selectedParent.getLocation());
        parentStatus.setText("");
        parentStatusSince.setText("");
        parentSpouse.setText("");

        int row = 1;
        for (Student child : children) {
            if (child == null) continue;
            Label name = new Label(child.name());
            Label lesson1 = new Label(child.lesson1().getLessonName());
            Label lesson1Teacher = new Label(child.lesson1().teacher().name());
            name.setFont(new Font(14));
            lesson1.setFont(new Font(14));
            lesson1Teacher.setFont(new Font(14));

            parentChildrenInfo.addRow(row);
            parentChildrenInfo.add(new Separator(), 0, row, 3, 1);
            parentChildrenInfo.getRowConstraints().get(row).setMinHeight(10);
            parentChildrenInfo.getRowConstraints().get(row).setMaxHeight(10);
            row++;

            parentChildrenInfo.addRow(row, name, lesson1, lesson1Teacher);
            parentChildrenInfo.getRowConstraints().get(row).setMinHeight(20);
            parentChildrenInfo.getRowConstraints().get(row).setMaxHeight(20);
            row++;

            if (child.getLessonId2() == 0) continue;
            Label lesson2 = new Label(child.lesson2().getLessonName());
            Label lesson2Teacher = new Label(child.lesson2().teacher().name());
            lesson2.setFont(new Font(14));
            lesson2Teacher.setFont(new Font(14));
            parentChildrenInfo.addRow(row, new Label(""), lesson2, lesson2Teacher);
            parentChildrenInfo.getRowConstraints().get(row).setMinHeight(20);
            parentChildrenInfo.getRowConstraints().get(row).setMaxHeight(20);
            row++;

            if (child.getLessonId3() == 0) continue;
            Label lesson3 = new Label(child.lesson3().getLessonName());
            Label lesson3Teacher = new Label(child.lesson3().teacher().name());
            lesson3.setFont(new Font(14));
            lesson3Teacher.setFont(new Font(14));
            parentChildrenInfo.addRow(row, new Label(""), lesson3, lesson3Teacher);
            parentChildrenInfo.getRowConstraints().get(row).setMinHeight(20);
            parentChildrenInfo.getRowConstraints().get(row).setMaxHeight(20);
            row++;
        }
    }

    private void clearParentInfo() {
        parentBirthday.setText("");
        parentCustomerId.setText("");
        parentLocation.setText("");
        parentStatus.setText("");
        parentStatusSince.setText("");
        parentSpouse.setText("");

        for (int i = 1; i <= (parentChildrenInfo.getRowCount() - 1); i++) {
            int row = i;
            parentChildrenInfo.getChildren().removeIf(node -> GridPane.getRowIndex(node) == row);
            parentChildrenInfo.getRowConstraints().get(i).setMinHeight(0);
            parentChildrenInfo.getRowConstraints().get(i).setMaxHeight(0);
        }
    }

    private void showTeacherInfo(Contact contact) {
        selectedTeacher = getTeacherFromDB(contact.getId());
        switchInfoArea(teacherInfo);
        teacherStatus.setText("Status:");
        teacherStatusDate.setText(selectedTeacher.status());
        teacherInstruments.setText(selectedTeacher.getInstruments());
        teacherLocation.setText(selectedTeacher.getLocation());
        teacherNumberOfStudents.setText(selectedTeacher.numberOfStudents() + " (im Einzelunterricht)");
        teacherCourses.setText(selectedTeacher.courses());
        teacherLocationMonday.setText(selectedTeacher.weekdayLocation("Montag"));
        teacherLocationTuesday.setText(selectedTeacher.weekdayLocation("Dienstag"));
        teacherLocationWednesday.setText(selectedTeacher.weekdayLocation("Mittwoch"));
        teacherLocationThursday.setText(selectedTeacher.weekdayLocation("Donnerstag"));
        teacherLocationFriday.setText(selectedTeacher.weekdayLocation("Freitag"));
        teacherLocationSaturday.setText(selectedTeacher.weekdayLocation("Samstag"));
    }

    private void showOtherInfo(Contact contact) {
        selectedOther = getOtherFromDB(contact.getId());
        switchInfoArea(otherInfo);
        otherDescription.setText(selectedOther.getDescription());
        otherLocation.setText(selectedOther.getLocation());
        otherBirthday.setText(selectedOther.getBirthDate());
    }

    private void switchInfoArea(HBox newInfo) {
        studentInfo.setVisible(false);
        parentInfo.setVisible(false);
        teacherInfo.setVisible(false);
        otherInfo.setVisible(false);
        newInfo.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        // Initialize contacts
        contacts.setAll(getContactListFromDB());
        students.setAll(getStudentListFromDB());
        teachers.setAll(getTeacherListFromDB());
        parents.setAll(getParentListFromDB());
        others.setAll(getOtherListFromDB());

        // Initialize contact filters
        filterLocation1.setText(SCHOOL_LOCATIONS.get(0));
        filterLocation2.setText(SCHOOL_LOCATIONS.get(1));
        filterLocation3.setText(SCHOOL_LOCATIONS.get(2));

        filterInstrument1ComboBox.setItems(SCHOOL_INSTRUMENTS);
        filterInstrument1ComboBox.setButtonCell(new StringListCell());
        filterInstrument1ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterInstrument1.setDisable(newValue == null));

        filterInstrument2ComboBox.setItems(SCHOOL_INSTRUMENTS);
        filterInstrument2ComboBox.setButtonCell(new StringListCell());
        filterInstrument2ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterInstrument2.setDisable(newValue == null));

        filterInstrument3ComboBox.setItems(SCHOOL_INSTRUMENTS);
        filterInstrument3ComboBox.setButtonCell(new StringListCell());
        filterInstrument3ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterInstrument3.setDisable(newValue == null));

        filterInstrument1.setDisable(filterInstrument1ComboBox.getSelectionModel().isEmpty());
        filterInstrument2.setDisable(filterInstrument2ComboBox.getSelectionModel().isEmpty());
        filterInstrument3.setDisable(filterInstrument3ComboBox.getSelectionModel().isEmpty());

        filterTeacher1ComboBox.setItems(getTeacherListFromDB());
        filterTeacher1ComboBox.setButtonCell(new TeacherListCellShort());
        filterTeacher1ComboBox.setCellFactory(teacher -> new TeacherListCellShort());
        filterTeacher1ComboBox.setConverter(teacherStringConverterShort);
        filterTeacher1ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher1.setDisable(newValue == null));

        filterTeacher2ComboBox.setItems(getTeacherListFromDB());
        filterTeacher2ComboBox.setButtonCell(new TeacherListCellShort());
        filterTeacher2ComboBox.setCellFactory(teacher -> new TeacherListCellShort());
        filterTeacher2ComboBox.setConverter(teacherStringConverterShort);
        filterTeacher2ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher2.setDisable(newValue == null));

        filterTeacher3ComboBox.setItems(getTeacherListFromDB());
        filterTeacher3ComboBox.setButtonCell(new TeacherListCellShort());
        filterTeacher3ComboBox.setCellFactory(teacher -> new TeacherListCellShort());
        filterTeacher3ComboBox.setConverter(teacherStringConverterShort);
        filterTeacher3ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher3.setDisable(newValue == null));

        // Initialize SelectAllCheckBox
        selectAllCheckBox.setOnAction(event -> {
            for (Contact contact : selectedTableView.getItems()) {
                contact.setSelected(selectAllCheckBox.isSelected());
            }
        });

        // Initialize SearchBar
        contactSearchBar.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String filter = contactSearchBar.getText();
                if (filter == null || filter.isBlank()) {
                    filteredContacts.setPredicate(contact -> true);
                } else {
                    filteredContacts.setPredicate(contact ->
                            containsIgnoreCase(contact.getLastName(), filter) ||
                            containsIgnoreCase(contact.getFirstName(), filter));
                }
            }
        });

        // Initialize TableView: All contacts
        selectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().formalName()));
        nameColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.28));

        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().category()));
        categoryColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.14));

        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.14));

        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.19));

        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.prefWidthProperty().bind(contactTableView.widthProperty().multiply(0.19));

        contactTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        contactTableView.setItems(sortableContacts);
        sortableContacts.comparatorProperty().bind(contactTableView.comparatorProperty());
        enableContactSelection(contactTableView);
        contactTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());

        // Initialize TableView: Students
        studentSelectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        studentSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(studentSelectColumn));

        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().formalName()));
        studentNameColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.33));

        studentInstrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrument1"));
        studentInstrumentColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.14));

        studentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        studentLocationColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.14));

        studentTeacherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().teacher1() == null ? "" : cellData.getValue().teacher1().shortName()));
        studentTeacherColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.14));

        studentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        studentStatusColumn.prefWidthProperty().bind(studentTableView.widthProperty().multiply(0.19));

        studentTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentTableView.setItems(sortableStudents);
        sortableStudents.comparatorProperty().bind(studentTableView.comparatorProperty());
        enableContactSelection(studentTableView);
        studentTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());

        // Initialize TableView: Teachers
        teacherSelectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        teacherSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(teacherSelectColumn));

        teacherNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().formalName()));
        teacherNameColumn.prefWidthProperty().bind(teacherTableView.widthProperty().multiply(0.30));

        teacherLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        teacherLocationColumn.prefWidthProperty().bind(teacherTableView.widthProperty().multiply(0.32));

        teacherInstrumentsColumn.setCellValueFactory(new PropertyValueFactory<>("instruments"));
        teacherInstrumentsColumn.prefWidthProperty().bind(teacherTableView.widthProperty().multiply(0.32));

        teacherTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        teacherTableView.setItems(sortableTeachers);
        sortableTeachers.comparatorProperty().bind(teacherTableView.comparatorProperty());
        enableContactSelection(teacherTableView);
        teacherTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());

        // Initialize TableView: Parents
        parentSelectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        parentSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(parentSelectColumn));

        parentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().formalName()));
        parentNameColumn.prefWidthProperty().bind(parentTableView.widthProperty().multiply(0.36));

        parentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        parentLocationColumn.prefWidthProperty().bind(parentTableView.widthProperty().multiply(0.18));

        parentTeacherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().mainTeacher() == null ? "" : cellData.getValue().mainTeacher().shortName()));
        parentTeacherColumn.prefWidthProperty().bind(parentTableView.widthProperty().multiply(0.18));

        parentStatusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().status()));
        parentStatusColumn.prefWidthProperty().bind(parentTableView.widthProperty().multiply(0.18));

        parentTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        parentTableView.setItems(sortableParents);
        sortableParents.comparatorProperty().bind(parentTableView.comparatorProperty());
        enableContactSelection(parentTableView);
        parentTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());

        // Initialize TableView: Others
        otherSelectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        otherSelectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(otherSelectColumn));

        otherNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().formalName()));
        otherNameColumn.prefWidthProperty().bind(otherTableView.widthProperty().multiply(0.36));

        otherDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        otherDescriptionColumn.prefWidthProperty().bind(otherTableView.widthProperty().multiply(0.18));

        otherPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        otherPhoneColumn.prefWidthProperty().bind(otherTableView.widthProperty().multiply(0.18));

        otherEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        otherEmailColumn.prefWidthProperty().bind(otherTableView.widthProperty().multiply(0.18));

        otherTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        otherTableView.setItems(sortableOthers);
        sortableOthers.comparatorProperty().bind(otherTableView.comparatorProperty());
        enableContactSelection(otherTableView);
        otherTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());

        // Initialize displayed table view
        contactTableView.getSortOrder().setAll(nameColumn);
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

        // Initialize InfoArea
        contactDetails.setVisible(false);
    }
}
