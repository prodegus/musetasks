package prodegus.musetasks.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.ui.StageFactories.stageOf;

public class PopupInfoController {

    @FXML private Label messageLabel;

    @FXML
    void okButtonClicked(ActionEvent event) {
        stageOf(event).close();
    }

    public void init(String message) {
        messageLabel.setText(message);
    }

}
