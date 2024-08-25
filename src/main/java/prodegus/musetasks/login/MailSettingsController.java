package prodegus.musetasks.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import prodegus.musetasks.ui.popup.PopupWindow;

import static prodegus.musetasks.mail.EmailModel.getMailUser;
import static prodegus.musetasks.mail.EmailModel.setMailCredentials;
import static prodegus.musetasks.mail.TLSEmail.confirmMail;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class MailSettingsController {

    @FXML
    private TextField pwTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private TextField senderTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;


    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void confirm(ActionEvent event) {
        String user = userTextField.getText();
        String password = pwTextField.getText();
        String sender = senderTextField.getText();

        if (user.isBlank() || password.isBlank() || sender.isBlank()) {
            PopupWindow.displayInformation("Bitte alle Felder ausfüllen!");
            return;
        }

        setMailCredentials(user, password, sender);

        if (!confirmMail(getMailUser())) {
            PopupWindow.displayInformation("Verbindung zum E-Mail-Provider fehlgeschlagen. Bitte Zugangsdaten prüfen!");
            return;
        }

        PopupWindow.displayInformation("E-Mail-Konto erfolgreich verknüpft!");
        stageOf(event).close();
    }

}
