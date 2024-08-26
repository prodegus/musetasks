package prodegus.musetasks.workspace;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prodegus.musetasks.mail.Email;
import prodegus.musetasks.mail.NewMailController;
import prodegus.musetasks.ui.popup.PopupWindow;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.mail.EmailModel.*;
import static prodegus.musetasks.mail.Templates.*;
import static prodegus.musetasks.ui.StageFactories.newStage;

public class MailController implements Initializable {

    @FXML private ToggleGroup viewMailToggles;
    @FXML private ToggleButton toggleSent;
    @FXML private ToggleButton toggleDraft;

    @FXML private MenuButton mailFromTemplate;

    @FXML private TitledPane filterPane;

    @FXML private CheckBox filterTimeDate;
    @FXML private DatePicker filterTimeDatePicker;
    @FXML private CheckBox filterTimeSpan;
    @FXML private DatePicker filterTimeSpanFrom;
    @FXML private DatePicker filterTimeSpanTo;
    @FXML private CheckBox filterTimeToday;

    @FXML private Button resetFilterButton;
    
    @FXML private TextField emailSearchBar;
    @FXML private TableView<Email> mailTableView;
    @FXML private CheckBox selectAllCheckBox;
    @FXML private TableColumn<Email, Boolean> selectColumn;
    @FXML private TableColumn<Email, String> sentDateColumn;
    @FXML private TableColumn<Email, String> subjectColumn;
    @FXML private TableColumn<Email, String> toColumn;

    @FXML private VBox mailPreview;
    @FXML private Label labelFrom;
    @FXML private Label labelTo;
    @FXML private Label labelCc;
    @FXML private Label labelAttachments;
    @FXML private Label labelDateTime;
    @FXML private Label labelSubject;
    @FXML private TextArea messageTextArea;

    private final ObservableList<Email> emails = getEmailListFromDB();
    private final FilteredList<Email> filteredEmails = new FilteredList<>(emails, email -> true);
    private final SortedList<Email> sortableEmails = new SortedList<>(filteredEmails);

    private final ObservableList<Email> emailsSent = getEmailSentListFromDB();
    private final FilteredList<Email> filteredEmailsSent = new FilteredList<>(emailsSent, email -> true);
    private final SortedList<Email> sortableEmailsSent = new SortedList<>(filteredEmailsSent);
    
    private final ObservableList<Email> drafts = getEmailDraftListFromDB();
    private final FilteredList<Email> filteredDrafts = new FilteredList<>(drafts, email -> true);
    private final SortedList<Email> sortableDrafts = new SortedList<>(filteredDrafts);
    
    private Email selectedEmail;

    private final InvalidationListener emailSearchListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            String filter = emailSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredEmailsSent.setPredicate(email -> true);
                filteredDrafts.setPredicate(email -> true);
            } else {
                filteredEmailsSent.setPredicate(email ->
                        containsIgnoreCase(email.getFrom(), filter) ||
                        containsIgnoreCase(email.getTo(), filter) ||
                        containsIgnoreCase(email.getCc(), filter) ||
                        containsIgnoreCase(email.getSubject(), filter) ||
                        containsIgnoreCase(email.getMessage(), filter));
                filteredDrafts.setPredicate(email ->
                        containsIgnoreCase(email.getFrom(), filter) ||
                        containsIgnoreCase(email.getTo(), filter) ||
                        containsIgnoreCase(email.getCc(), filter) ||
                        containsIgnoreCase(email.getSubject(), filter) ||
                        containsIgnoreCase(email.getMessage(), filter));
            }
        }
    };


    @FXML
    void delete(ActionEvent event) {
        if (!PopupWindow.displayYesNo("E-Mail löschen?")) return;
        deleteEmailFromDB(selectedEmail);
    }

    @FXML
    void deleteMail(ActionEvent event) {
        ArrayList<Email> selectedEmails = new ArrayList<>();

        for (Email email : mailTableView.getItems()) {
            if (email.isSelected()) {
                selectedEmails.add(email);
            }
        }

        if (selectedEmails.size() == 0) {
            PopupWindow.displayInformation("Keine E-Mails ausgewählt!");
            return;
        }

        String message = ((selectedEmails.size() == 1 ? "E-Mail wird" : "E-Mails werden") + " gelöscht.\n\nFortfahren?");
        if (!PopupWindow.displayYesNo(message)) return;
        deleteEmailsFromDB(selectedEmails);
        refreshEmails();
    }

    private void refreshEmails() {
        emails.setAll(getEmailListFromDB());
        emailsSent.setAll(getEmailSentListFromDB());
        drafts.setAll(getEmailDraftListFromDB());
    }

    @FXML
    void edit(ActionEvent event) {
        if (selectedEmail == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("E-Mail bearbeiten", loader);
        NewMailController controller = loader.getController();
        controller.init(selectedEmail.getFrom(), selectedEmail.getTo(), selectedEmail.getCc(),
                selectedEmail.getSubject(), selectedEmail.getMessage());
        stage.showAndWait();
    }

    @FXML
    void forwardMail(ActionEvent event) {
        if (selectedEmail == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("E-Mail bearbeiten", loader);
        NewMailController controller = loader.getController();
        controller.init(selectedEmail.getFrom(), "", "",
                selectedEmail.getSubject(), selectedEmail.getMessage());
        stage.showAndWait();
    }

    @FXML
    void newMail(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        stage.showAndWait();
    }

    @FXML
    void refresh(ActionEvent event) {
        refreshEmails();
    }

    @FXML
    void resetFilter(ActionEvent event) {
        mailTableView.setItems(sortableEmailsSent);
    }


    @FXML
    void fromTemplateChanged(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init("", "Änderung zum Termin: [Datum, Uhrzeit]", templateChanged());
        stage.showAndWait();
    }

    @FXML
    void fromTemplateDropped(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init("", "Unterricht abgesagt: [Datum, Uhrzeit]", templateDropped());
        stage.showAndWait();
    }

    @FXML
    void fromTemplateReschedule(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init("", "Unterricht abgesagt: [Datum, Uhrzeit]", templateRescheduled());
        stage.showAndWait();
    }

    @FXML
    void fromTemplateToReschedule(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init("", "Unterricht abgesagt: [Datum, Uhrzeit]", templateToReschedule());
        stage.showAndWait();
    }

    @FXML
    void viewAllDrafts(ActionEvent event) {
        refreshEmails();
        mailTableView.setItems(sortableDrafts);
    }

    @FXML
    void viewAllSent(ActionEvent event) {
        refreshEmails();
        mailTableView.setItems(sortableEmailsSent);
    }

    @FXML
    void viewLessonsFiltered(ActionEvent event) {
        refreshEmails();
        mailTableView.setItems(sortableEmails);
        emails.removeIf(email -> !fitsTimePeriod(email));
    }

    private boolean fitsTimePeriod(Email email) {
        if (!filterTimeToday.isSelected() && !filterTimeDate.isSelected() && !filterTimeSpan.isSelected()) return true;
        boolean filterTodayFits = filterTimeToday.isSelected() && email.getDate().equals(LocalDate.now());
        boolean filterDateFits = filterTimeDate.isSelected() && email.getDate().equals(filterTimeDatePicker.getValue());
        boolean filterSpanFits = filterTimeSpan.isSelected() && email.getDate().isAfter(filterTimeSpanFrom.getValue()) &&
                email.getDate().isBefore(filterTimeSpanTo.getValue());
        return filterTodayFits || filterDateFits || filterSpanFits;
    }

    private void enableEmailSelection(TableView<? extends Email> tableView) {
        tableView.setOnMouseClicked(event -> {
            if (tableView.getSelectionModel().isEmpty()) return;
            selectedEmail = tableView.getSelectionModel().getSelectedItem();
            showEmailInfo(selectedEmail);
            mailPreview.setVisible(true);
        });
    }

    private void showEmailInfo(Email selectedEmail) {
        labelFrom.setText(selectedEmail.getFrom());
        labelTo.setText(selectedEmail.getTo());
        labelCc.setText(selectedEmail.getCc());
        labelAttachments.setText(selectedEmail.getAttachments());
        labelDateTime.setText(selectedEmail.dateTime());
        labelSubject.setText(selectedEmail.getSubject());
        messageTextArea.setText(selectedEmail.getMessage());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Mail TableView
        selectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        toColumn.setCellValueFactory(new PropertyValueFactory<>("to"));
        toColumn.prefWidthProperty().bind(mailTableView.widthProperty().multiply(0.27));

        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        subjectColumn.prefWidthProperty().bind(mailTableView.widthProperty().multiply(0.42));

        sentDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().dateTime()));
        sentDateColumn.prefWidthProperty().bind(mailTableView.widthProperty().multiply(0.27));
        
        mailTableView.setItems(sortableEmailsSent);
        sortableEmailsSent.comparatorProperty().bind(mailTableView.comparatorProperty());
        sortableDrafts.comparatorProperty().bind(mailTableView.comparatorProperty());
        enableEmailSelection(mailTableView);

        toggleSent.setSelected(true);
        
        // Initialize SearchBar
        emailSearchBar.textProperty().addListener(emailSearchListener);

        // Initialize SelectAllCheckBox
        selectAllCheckBox.setOnAction(event -> {
            for (Email email : mailTableView.getItems()) {
                email.setSelected(selectAllCheckBox.isSelected());
            }
        });

        mailPreview.setVisible(false);
    }
}
