package prodegus.musetasks.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import prodegus.musetasks.ui.PopupWindow;

import static prodegus.musetasks.database.Database.createUser;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class CreateUserController {

    @FXML
    private Button cancelButton;

    @FXML
    private Button createButton;

    @FXML
    private TextField pwConfirmTextfield;

    @FXML
    private TextField pwTextfield;

    @FXML
    private TextField userTextfield;

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void create(ActionEvent event) {
        String user = userTextfield.getText();
        String pw = pwTextfield.getText();
        String pwConfirm = pwConfirmTextfield.getText();

        if (!pw.equals(pwConfirm)) {
            PopupWindow.display("Passwort und bestätigtes Passwort stimmen nicht überein!");
            return;
        }

        if (user.trim().isEmpty() || pw.trim().isEmpty()) {
            PopupWindow.display("Bitte alle Felder ausfüllen!");
            return;
        }

        if (pw.length() < 6) {
            PopupWindow.display("Das Passwort muss mindestens sechs Zeichen lang sein!");
            return;
        }

        createUser(user, pw);
        PopupWindow.display("Benutzer " + user + " wurde erfolgreich angelegt. Bitte loggen Sie sich ein.");
        stageOf(event).close();
    }

}
