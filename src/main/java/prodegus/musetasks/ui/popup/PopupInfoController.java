package prodegus.musetasks.ui.popup;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
