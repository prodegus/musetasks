package prodegus.musetasks.workspace;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import prodegus.musetasks.contacts.Contact;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.database.Database.CONTACT_TABLE;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class WorkspaceController implements Initializable {

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
    @FXML private TextArea newNoteTextField;
    @FXML private TextArea notesTextArea;
    @FXML private Button saveNotesButton;
    @FXML private ToggleGroup contactCategoryToggle;
    @FXML private ToggleButton toggleAll;
    @FXML private ToggleButton toggleOthers;
    @FXML private ToggleButton toggleParents;
    @FXML private ToggleButton toggleProspectives;
    @FXML private ToggleButton toggleStudents;
    @FXML private ToggleButton toggleTeachers;

    private File xlsFile;
    private ObservableList<Contact> contacts;
    private FilteredList<Contact> filteredContacts;
    private SortedList<Contact> sortableContacts;
    private Contact selectedContact;
    private boolean notesEditMode = false;

    @FXML
    void deleteContacts(ActionEvent event) {
        Set<Contact> selectedContacts = new HashSet<>();
        for (Contact contact : contactTableView.getItems()) {
            if (contact.isSelected()) {
                deleteContact(contact);
            }
        }
        refreshContacts();
    }

    @FXML
    void loadFromXls(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS-Datei", "*.xls"));
        xlsFile = fileChooser.showOpenDialog(stageOf(event));
        addContactsFromXLS(xlsFile, CONTACT_TABLE);
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
        refreshContacts();
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
            saveEditedNotes(selectedContact, notesTextArea.getText());
            refreshContacts();
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

        addNote(selectedContact, newNote);
        refreshContacts();
        newNoteTextField.clear();
        deactivateNotesEditMode();
    }

    @FXML
    void searchBarKeyTyped(KeyEvent event) {
        newNoteTextField.clear();
    }

    void refreshContacts() {
        refreshContactList(contacts);
        refreshContactView();
        tableViewSelect();
    }

    void refreshContactView() {
        filteredContacts = new FilteredList<>(contacts, contact -> true);
        sortableContacts = new SortedList<>(filteredContacts);
        contactTableView.setItems(sortableContacts);
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

    void tableViewSelect() {
        if (selectedContact == null) return;
        int index = 0;
        for (Contact contact : contactTableView.getItems()) {
            if (contact.id().equals(selectedContact.id())) {
                break;
            }
            index++;
        }
        contactTableView.getSelectionModel().select(index);
        selectedContact = contactTableView.getSelectionModel().getSelectedItem();
    }

    void setTransparency(TextArea area, boolean transparency) {
        String transparentUrl = getClass().getResource("/css/textareaTransparent.css").toExternalForm();
        String editedUrl = getClass().getResource("/css/textareaEdited.css").toExternalForm();
        String newUrl = transparency ? transparentUrl : editedUrl;

        area.getStylesheets().clear();
        area.getStylesheets().add(newUrl);
    }

    void selectContact(Contact contact) {
        if (contactTableView.getSelectionModel().isEmpty()) return;
        selectedContact = contactTableView.getSelectionModel().getSelectedItem();

        notesTextArea.setText(selectedContact.getNotes());
        notesTextArea.setScrollTop(Double.MAX_VALUE);
        notesTextArea.appendText(""); // workaround to scroll to bottom of TextArea



        deactivateNotesEditMode();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize contacts
        contacts = getContactList();
        filteredContacts = new FilteredList<>(contacts, contact -> true);
        sortableContacts = new SortedList<>(filteredContacts);
        sortableContacts.comparatorProperty().bind(contactTableView.comparatorProperty());

        // Initialize ToggleGroup
//        ToggleGroup filterToggleGroup = new ToggleGroup();
//        toggleAll.setToggleGroup(filterToggleGroup);
//        toggleOthers.setToggleGroup(filterToggleGroup);
//        toggleParents.setToggleGroup(filterToggleGroup);
//        toggleProspectives.setToggleGroup(filterToggleGroup);
//        toggleStudents.setToggleGroup(filterToggleGroup);
//        toggleTeachers.setToggleGroup(filterToggleGroup);


        // Initialize SearchBar
        contactSearchBar.textProperty().addListener(observable -> {
            String filter = contactSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredContacts.setPredicate(contact -> true);
            } else {
                filteredContacts.setPredicate(contact ->
                        containsIgnoreCase(contact.getLastname(), filter) ||
                        containsIgnoreCase(contact.getFirstname(), filter));
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
        contactTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (contactTableView.getSelectionModel().isEmpty()) return;
                selectedContact = contactTableView.getSelectionModel().getSelectedItem();
                notesTextArea.setText(selectedContact.getNotes());
                notesTextArea.setScrollTop(Double.MAX_VALUE);
                notesTextArea.appendText(""); // workaround to scroll to bottom of TextArea
                deactivateNotesEditMode();
            }
        });

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
    }
}
