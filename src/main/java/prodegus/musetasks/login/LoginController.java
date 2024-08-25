package prodegus.musetasks.login;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.database.DBSelectController;
import prodegus.musetasks.database.Database;
import prodegus.musetasks.lessons.CustomAptController;
import prodegus.musetasks.lessons.CustomAptWindow;
import prodegus.musetasks.lessons.Lesson;
import prodegus.musetasks.school.Holiday;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.workspace.SettingsController;
import prodegus.musetasks.workspace.WorkspaceController;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.ContactModel.insertContact;
import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.mail.EmailModel.*;
import static prodegus.musetasks.school.HolidayModel.insertHoliday;
import static prodegus.musetasks.school.School.setActiveUser;
import static prodegus.musetasks.ui.StageFactories.*;

public class LoginController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private Button createUserButton;

    @FXML
    private Button loginButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Label loginLabel;

    @FXML
    private PasswordField pwTextfield;

    @FXML
    private TextField userTextfield;

    private Database database = new Database();
    private boolean dbConnected;

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public boolean isDbConnected() {
        return dbConnected;
    }

    public void setDbConnected(boolean dbConnected) {
        this.dbConnected = dbConnected;
    }

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void createUserButtonClicked(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createuser-view.fxml"));
        Stage stage = newStage("Neuen Benutzer anlegen", loader);
        stage.showAndWait();
        userTextfield.clear();
        pwTextfield.clear();
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        String user = userTextfield.getText();
        String pw = pwTextfield.getText();
        userTextfield.clear();
        pwTextfield.clear();

        if (isEmptyTable(USER_TABLE)) {
            PopupWindow.displayInformation("Bitte zuerst einen Benutzer anlegen!");
            return;
        }
        if (!loginValid(user, pw)) {
            PopupWindow.displayInformation("Login fehlgeschlagen. Bitte Benutzername und Passwort erneut eingeben!");
            return;
        }

        // Enter school parameters (locations / rooms, instruments etc..)
        if (isEmptyTable(SCHOOL_TABLE)) {
            if(!initializeSettings()) return;
        }

//        if (isEmptyTable(INSTRUMENT_TABLE)) {
//            insert(INSTRUMENT_TABLE, "instrument", "'Gitarre'");
//            insert(INSTRUMENT_TABLE, "instrument", "'Klavier'");
//            insert(INSTRUMENT_TABLE, "instrument", "'Gesang'");
//            insert(INSTRUMENT_TABLE, "instrument", "'E-Bass'");
//            insert(INSTRUMENT_TABLE, "instrument", "'Schlagzeug'");
//            insert(INSTRUMENT_TABLE, "instrument", "'Violine'");
//            insert(INSTRUMENT_TABLE, "instrument", "'Saxofon'");
//            insert(INSTRUMENT_TABLE, "instrument", "'Klarinette'");
//        }
//        if (isEmptyTable(LOCATION_TABLE)) {
//            insert(LOCATION_TABLE, "name, room1, room2, room3", "'Lohmar', 'Lokal', 'Studio', 'Büro'");
//            insert(LOCATION_TABLE, "name, room1, room2, room3, room4, room5", "'Gummersbach', 'R 110', 'R 114', 'R 122', 'R 210', 'R 212'");
//            insert(LOCATION_TABLE, "name, room1, room2", "'Meckenheim', 'OG', 'UG'");
//        }

        // Enter/load regional holidays
        if (isEmptyTable(HOLIDAY_TABLE)) {
            // 2023
            insertHoliday(new Holiday("Weihnachtsferien", LocalDate.of(2023, 12, 21), LocalDate.of(2024, 1, 5)));

            // 2024
            insertHoliday(new Holiday("Neujahrstag", LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 1)));
            insertHoliday(new Holiday("Osterferien", LocalDate.of(2024, 3, 25), LocalDate.of(2024, 4, 6)));
            insertHoliday(new Holiday("Karfreitag", LocalDate.of(2024, 3, 29), LocalDate.of(2024, 3, 29)));
            insertHoliday(new Holiday("Ostermontag", LocalDate.of(2024, 4, 1), LocalDate.of(2024, 4, 1)));
            insertHoliday(new Holiday("Tag der Arbeit", LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 1)));
            insertHoliday(new Holiday("Christi Himmelfahrt", LocalDate.of(2024, 5, 9), LocalDate.of(2024, 5, 9)));
            insertHoliday(new Holiday("Pfingstmontag", LocalDate.of(2024, 5, 20), LocalDate.of(2024, 5, 20)));
            insertHoliday(new Holiday("Pfingstferien", LocalDate.of(2024, 5, 21), LocalDate.of(2024, 5, 21)));
            insertHoliday(new Holiday("Fronleichnam", LocalDate.of(2024, 5, 21), LocalDate.of(2024, 5, 21)));
            insertHoliday(new Holiday("Sommerferien", LocalDate.of(2024, 7, 8), LocalDate.of(2024, 8, 20)));
            insertHoliday(new Holiday("Tag der deutschen Einheit", LocalDate.of(2024, 10, 3), LocalDate.of(2024, 10, 3)));
            insertHoliday(new Holiday("Herbstferien", LocalDate.of(2024, 10, 14), LocalDate.of(2024, 10, 26)));
            insertHoliday(new Holiday("Allerheiligen", LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 1)));
            insertHoliday(new Holiday("Weihnachtsferien", LocalDate.of(2024, 12, 23), LocalDate.of(2025, 1, 6)));
            insertHoliday(new Holiday("1. Weihnachtsfeiertag", LocalDate.of(2024, 12, 25), LocalDate.of(2024, 12, 25)));
            insertHoliday(new Holiday("2. Weihnachtsfeiertag", LocalDate.of(2024, 12, 26), LocalDate.of(2024, 12, 26)));

            // 2025
            insertHoliday(new Holiday("Neujahrstag", LocalDate.of(2025, 1, 1), LocalDate.of(2025, 1, 1)));
            insertHoliday(new Holiday("Osterferien", LocalDate.of(2025, 4, 14), LocalDate.of(2025, 4, 26)));
            insertHoliday(new Holiday("Karfreitag", LocalDate.of(2025, 4, 18), LocalDate.of(2025, 4, 18)));
            insertHoliday(new Holiday("Ostermontag", LocalDate.of(2025, 4, 21), LocalDate.of(2025, 4, 21)));
            insertHoliday(new Holiday("Tag der Arbeit", LocalDate.of(2025, 5, 1), LocalDate.of(2025, 5, 1)));
            insertHoliday(new Holiday("Christi Himmelfahrt", LocalDate.of(2025, 5, 29), LocalDate.of(2025, 5, 29)));
            insertHoliday(new Holiday("Pfingstmontag", LocalDate.of(2025, 6, 9), LocalDate.of(2025, 6, 9)));
            insertHoliday(new Holiday("Pfingstferien", LocalDate.of(2025, 6, 10), LocalDate.of(2025, 6, 10)));
            insertHoliday(new Holiday("Fronleichnam", LocalDate.of(2025, 6, 19), LocalDate.of(2025, 6, 19)));
            insertHoliday(new Holiday("Sommerferien", LocalDate.of(2025, 7, 14), LocalDate.of(2025, 8, 26)));
            insertHoliday(new Holiday("Tag der deutschen Einheit", LocalDate.of(2025, 10, 3), LocalDate.of(2025, 10, 3)));
            insertHoliday(new Holiday("Herbstferien", LocalDate.of(2025, 10, 13), LocalDate.of(2025, 10, 25)));
            insertHoliday(new Holiday("Allerheiligen", LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 1)));
            insertHoliday(new Holiday("Weihnachtsferien", LocalDate.of(2025, 12, 22), LocalDate.of(2026, 1, 6)));
            insertHoliday(new Holiday("1. Weihnachtsfeiertag", LocalDate.of(2025, 12, 25), LocalDate.of(2025, 12, 25)));
            insertHoliday(new Holiday("2. Weihnachtsfeiertag", LocalDate.of(2025, 12, 26), LocalDate.of(2025, 12, 26)));

            // 2026
            insertHoliday(new Holiday("Neujahrstag", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 1)));
            insertHoliday(new Holiday("Osterferien", LocalDate.of(2026, 3, 30), LocalDate.of(2026, 4, 11)));
            insertHoliday(new Holiday("Karfreitag", LocalDate.of(2026, 4, 3), LocalDate.of(2026, 4, 3)));
            insertHoliday(new Holiday("Ostermontag", LocalDate.of(2026, 4, 6), LocalDate.of(2026, 4, 6)));
            insertHoliday(new Holiday("Tag der Arbeit", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 1)));
            insertHoliday(new Holiday("Christi Himmelfahrt", LocalDate.of(2026, 5, 14), LocalDate.of(2026, 5, 14)));
            insertHoliday(new Holiday("Pfingstmontag", LocalDate.of(2026, 5, 25), LocalDate.of(2026, 5, 25)));
            insertHoliday(new Holiday("Pfingstferien", LocalDate.of(2026, 5, 26), LocalDate.of(2026, 5, 26)));
            insertHoliday(new Holiday("Fronleichnam", LocalDate.of(2026, 4, 6), LocalDate.of(2026, 4, 6)));
            insertHoliday(new Holiday("Sommerferien", LocalDate.of(2026, 7, 20), LocalDate.of(2026, 9, 1)));
            insertHoliday(new Holiday("Tag der deutschen Einheit", LocalDate.of(2026, 10, 3), LocalDate.of(2026, 10, 3)));
            insertHoliday(new Holiday("Herbstferien", LocalDate.of(2026, 10, 17), LocalDate.of(2026, 10, 31)));
            insertHoliday(new Holiday("Allerheiligen", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 1)));
            insertHoliday(new Holiday("Weihnachtsferien", LocalDate.of(2026, 12, 23), LocalDate.of(2027, 1, 6)));
            insertHoliday(new Holiday("1. Weihnachtsfeiertag", LocalDate.of(2026, 12, 25), LocalDate.of(2026, 12, 25)));
            insertHoliday(new Holiday("2. Weihnachtsfeiertag", LocalDate.of(2026, 12, 26), LocalDate.of(2026, 12, 26)));

            // 2027
            insertHoliday(new Holiday("Neujahrstag", LocalDate.of(2027, 1, 1), LocalDate.of(2027, 1, 1)));
            insertHoliday(new Holiday("Osterferien", LocalDate.of(2027, 3, 22), LocalDate.of(2027, 4, 3)));
            insertHoliday(new Holiday("Karfreitag", LocalDate.of(2027, 3, 26), LocalDate.of(2027, 3, 26)));
            insertHoliday(new Holiday("Ostermontag", LocalDate.of(2027, 3, 29), LocalDate.of(2027, 3, 29)));
            insertHoliday(new Holiday("Tag der Arbeit", LocalDate.of(2027, 5, 1), LocalDate.of(2027, 5, 1)));
            insertHoliday(new Holiday("Christi Himmelfahrt", LocalDate.of(2027, 5, 6), LocalDate.of(2027, 5, 6)));
            insertHoliday(new Holiday("Pfingstmontag", LocalDate.of(2027, 5, 17), LocalDate.of(2027, 5, 17)));
            insertHoliday(new Holiday("Pfingstferien", LocalDate.of(2027, 5, 18), LocalDate.of(2027, 5, 18)));
            insertHoliday(new Holiday("Fronleichnam", LocalDate.of(2027, 5, 27), LocalDate.of(2027, 5, 27)));
            insertHoliday(new Holiday("Sommerferien", LocalDate.of(2027, 7, 19), LocalDate.of(2027, 8, 31)));
            insertHoliday(new Holiday("Tag der deutschen Einheit", LocalDate.of(2027, 10, 3), LocalDate.of(2027, 10, 3)));
            insertHoliday(new Holiday("Herbstferien", LocalDate.of(2027, 10, 23), LocalDate.of(2027, 11, 6)));
            insertHoliday(new Holiday("Allerheiligen", LocalDate.of(2027, 11, 1), LocalDate.of(2027, 11, 1)));
            insertHoliday(new Holiday("Weihnachtsferien", LocalDate.of(2027, 12, 24), LocalDate.of(2028, 1, 8)));
            insertHoliday(new Holiday("1. Weihnachtsfeiertag", LocalDate.of(2027, 12, 25), LocalDate.of(2027, 12, 25)));
            insertHoliday(new Holiday("2. Weihnachtsfeiertag", LocalDate.of(2027, 12, 26), LocalDate.of(2027, 12, 26)));
        }

        setActiveUser(user);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/workspace.fxml"));
        Stage stage = newStage(APP_NAME, loader);
        stage.show();
        stageOf(event).close();
    }

    @FXML
    void settingsButtonClicked(ActionEvent event) {
        selectDB();
    }

    public void selectDB() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dbselect-view.fxml"));
        Stage stage = newStage("Datenbank auswählen", loader);
        stage.showAndWait();
    }

    public boolean initializeSettings() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/workspace-settings.fxml"));
        Stage stage = newStage("Einstellungen", loader);
        SettingsController controller = loader.getController();
        stage.showAndWait();
        return !controller.isCancelled();
    }

    private void insertDummyEntries() {
        createUser("a", "123123");

        Student dummyStudent = new Student();
        dummyStudent.setLastName("Meier");
        dummyStudent.setFirstName("Fritz");
        dummyStudent.setCategory(CATEGORY_STUDENT);
        dummyStudent.setLocation("");
        dummyStudent.setStreet("");
        dummyStudent.setCity("");
        dummyStudent.setPhone("");
        dummyStudent.setEmail("");
        dummyStudent.setZoom("");
        dummyStudent.setSkype("");
        dummyStudent.setBirthDate("");
        dummyStudent.setNotes("");
        dummyStudent.setSelected(false);
        dummyStudent.setInstrument1("Gitarre");
        dummyStudent.setInstrument2("");
        dummyStudent.setInstrument3("");
        dummyStudent.setProspective(false);
        dummyStudent.setStatus("");
        dummyStudent.setStatusFrom("");
        dummyStudent.setStatusTo("");
        dummyStudent.setContactEmail("keine Angabe");
        insertContact(dummyStudent);

        dummyStudent.setLastName("Müller");
        dummyStudent.setFirstName("Tim");
        dummyStudent.setInstrument1("Klavier");
        insertContact(dummyStudent);

        dummyStudent.setLastName("Brutalo");
        dummyStudent.setFirstName("Bob");
        dummyStudent.setInstrument1("Schlagzeug");
        insertContact(dummyStudent);

        dummyStudent.setLastName("Buffay");
        dummyStudent.setFirstName("Phoebe");
        dummyStudent.setInstrument1("E-Bass");
        insertContact(dummyStudent);

        dummyStudent.setLastName("Wackelstein");
        dummyStudent.setFirstName("Jonas");
        dummyStudent.setInstrument1("Gesang");
        insertContact(dummyStudent);

        Teacher dummyTeacher = new Teacher();
        dummyTeacher.setFirstName("John");
        dummyTeacher.setLastName("Frusciante");
        dummyTeacher.setCategory(CATEGORY_TEACHER);
        dummyTeacher.setLocation("");
        dummyTeacher.setStreet("");
        dummyTeacher.setCity("");
        dummyTeacher.setPhone("");
        dummyTeacher.setEmail("");
        dummyTeacher.setZoom("");
        dummyTeacher.setSkype("");
        dummyTeacher.setBirthDate("");
        dummyTeacher.setNotes("");
        dummyTeacher.setSelected(false);
        dummyTeacher.setInstruments("Gitarre");
        dummyTeacher.setActiveDays("");
        dummyTeacher.setStatus("");
        dummyTeacher.setStatusFrom("");
        dummyTeacher.setStatusTo("");
        insertContact(dummyTeacher);

        dummyTeacher.setFirstName("Flea");
        dummyTeacher.setLastName("Fly");
        dummyTeacher.setInstruments("E-Bass");
        insertContact(dummyTeacher);

        dummyTeacher.setFirstName("Chad");
        dummyTeacher.setLastName("Smith");
        dummyTeacher.setInstruments("Schlagzeug");
        insertContact(dummyTeacher);

        dummyTeacher.setFirstName("Anthony");
        dummyTeacher.setLastName("Kiedis");
        dummyTeacher.setInstruments("Gesang");
        insertContact(dummyTeacher);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("connected: " + connected());
        System.out.println("DB_PATH: " + DB_PATH);
        System.out.println("mail_user: " + getMailUser());
        System.out.println("mail_password: " + getMailPassword());
        System.out.println("mail_sender: " + getMailSender());

        if (!connected()) {
            selectDB();
            return;
        }

        if (!tableExists(USER_TABLE)) {
            PopupWindow.displayInformation("Datenbank-Fehler! Bitte andere Datenbank auswählen oder neu anlegen!");
            selectDB();
            return;
        }

//        if (isEmptyTable(USER_TABLE)) {
//            insertDummyEntries();
//        }
    }
}
