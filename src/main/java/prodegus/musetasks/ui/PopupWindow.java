package prodegus.musetasks.ui;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prodegus.musetasks.popup.PopupInfoController;
import prodegus.musetasks.popup.PopupYesNoController;

import static prodegus.musetasks.ui.StageFactories.newStage;


public class PopupWindow {

    public static void displayInformation(String message) {
        displayInformation("Information", message);
    }

    public static void displayInformation(String title, String message) {
        FXMLLoader loader = new FXMLLoader(PopupWindow.class.getResource("/fxml/popup-information.fxml"));
        Stage stage = newStage(title, loader);
        PopupInfoController controller = loader.getController();
        controller.init(message);
        stage.showAndWait();
    }

    public static boolean displayYesNo(String message) {
        FXMLLoader loader = new FXMLLoader(PopupWindow.class.getResource("/fxml/popup-yesno.fxml"));
        Stage stage = newStage("Hinweis", loader);
        PopupYesNoController controller = loader.getController();
        controller.init(message);
        stage.showAndWait();
        return controller.isConfirmed();
    }

}