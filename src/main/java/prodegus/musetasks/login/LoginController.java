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
import prodegus.musetasks.database.Database;
import prodegus.musetasks.ui.PopupWindow;

import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.login.Settings.*;
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

        if (getMailUser() == null && PopupWindow.displayYesNo("Kein E-Mail-Konto gefunden. Jetzt verknüpfen?")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mailsettings-view.fxml"));
                Stage stage = newStage("E-Mail-Konto hinzufügen", loader);
                stage.showAndWait();
        }

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
        }


    }
}
