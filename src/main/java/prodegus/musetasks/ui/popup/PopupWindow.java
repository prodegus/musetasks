package prodegus.musetasks.ui.popup;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

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