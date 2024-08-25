package prodegus.musetasks.mail;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import prodegus.musetasks.ui.popup.PopupWindow;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static prodegus.musetasks.mail.EmailModel.*;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.mail.TLSEmail.sendMail;

public class NewMailController implements Initializable {

    @FXML private TextField fromTextField;
    @FXML private TextField toTextField;
    @FXML private TextField ccTextField;
    @FXML private TextField subjectTextField;
    @FXML private Label attachmentFileName;
    @FXML private TextArea messageTextArea;
    private List<File> attachments = new ArrayList<>();

    @FXML
    void chooseAttachment(ActionEvent event) {
        List<File> files = new FileChooser().showOpenMultipleDialog(stageOf(event));
        StringJoiner fileNames = new StringJoiner(", ");
        if (!attachmentFileName.getText().isBlank()) fileNames.add(attachmentFileName.getText());
        attachments.addAll(files);

        for (File file : files) {
            fileNames.add(file.getName());
        }
        attachmentFileName.setText(fileNames.toString());
        attachmentFileName.setVisible(true);
        attachmentFileName.setManaged(true);
    }

    @FXML
    void cancel(ActionEvent event) {
        if (PopupWindow.displayYesNo("Entwurf speichern?")) {
            Email draft = new Email();
            draft.setDate(LocalDate.now());
            draft.setTime(LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute()));
            draft.setFrom(getMailSender() + " <" + getMailUser() + ">");
            draft.setTo(toTextField.getText());
            draft.setCc(ccTextField.getText());
            draft.setSubject(subjectTextField.getText());
            draft.setMessage(messageTextArea.getText());
            draft.setAttachments(attachmentFileName.getText());
            draft.setDraft(true);
            insert(draft);
        }
        stageOf(event).close();
    }

    @FXML
    void send(ActionEvent event) {
        String from = getMailUser();
        String to = toTextField.getText();
        String cc = ccTextField.getText();
        String subject = subjectTextField.getText();
        String message = messageTextArea.getText();

        if (to.isBlank()) {
            PopupWindow.displayInformation("Bitte Empfänger angeben!");
            return;
        }

        if (subject.isBlank()) {
            PopupWindow.displayInformation("Bitte Betreff angeben!");
            return;
        }

        if (message.isBlank() && !PopupWindow.displayYesNo("Nachricht enthält keinen Text. Trotzdem senden?")) {
            return;
        }

        if (sendMail(to, cc, subject, message, getMailSender(), attachments)) {
            Email email = new Email();
            email.setDate(LocalDate.now());
            email.setTime(LocalTime.of(LocalTime.now().getHour(), LocalTime.now().getMinute()));
            email.setFrom(getMailSender() + " <" + getMailUser() + ">");
            email.setTo(to);
            email.setCc(cc);
            email.setSubject(subject);
            email.setMessage(message);
            email.setAttachments(attachmentFileName.getText());
            email.setDraft(false);
            insert(email);

            PopupWindow.displayInformation("E-Mail versendet!");

            stageOf(event).close();
        } else {
            PopupWindow.displayInformation("Fehler", "E-Mail konnte nicht gesendet werden. " +
                    "Bitte überprüfen Sie die Empfänger-Adresse und Ihre E-Mail-Einstellungen.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fromTextField.setText(getMailSender() + " <" + getMailUser() + ">");
        attachmentFileName.setText("");
        attachmentFileName.setVisible(false);
        attachmentFileName.setManaged(false);
    }

    public void init(String subject, List<File> attachments) {
        StringJoiner fileNames = new StringJoiner(", ");
        subjectTextField.setText(subject);
        this.attachments = attachments;

        for (File attachment : attachments) {
            fileNames.add(attachment.getPath());
        }
        attachmentFileName.setText(fileNames.toString());
        attachmentFileName.setVisible(true);
        attachmentFileName.setManaged(true);
    }

    public void init(String to) {
        toTextField.setText(to);
    }

    public void init(String to, String subject) {
        toTextField.setText(to);
        subjectTextField.setText(subject);
    }

    public void init(String to, String subject, String message) {
        toTextField.setText(to);
        subjectTextField.setText(subject);
        messageTextArea.setText(message);
    }

    public void init(String to, String cc, String subject, String message) {
        toTextField.setText(to);
        ccTextField.setText(cc);
        subjectTextField.setText(subject);
        messageTextArea.setText(message);
    }

    public void init(String from, String to, String cc, String subject, String message) {
        fromTextField.setText(from);
        toTextField.setText(to);
        ccTextField.setText(cc);
        subjectTextField.setText(subject);
        messageTextArea.setText(message);
    }
}
