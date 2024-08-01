package prodegus.musetasks.ui.popup;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import static prodegus.musetasks.ui.StageFactories.stageOf;

public class PopupYesNoController {

    @FXML private Label messageLabel;
    private final SimpleBooleanProperty confirmed = new SimpleBooleanProperty(false);

    @FXML
    void noButtonClicked(ActionEvent event) {
        setConfirmed(false);
        stageOf(event).close();
    }

    @FXML
    void yesButtonClicked(ActionEvent event) {
        setConfirmed(true);
        stageOf(event).close();
    }


    public boolean isConfirmed() {
        return confirmed.get();
    }

    public SimpleBooleanProperty confirmedProperty() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed.set(confirmed);
    }

    public void init(String message) {
        messageLabel.setText(message);
    }
}
