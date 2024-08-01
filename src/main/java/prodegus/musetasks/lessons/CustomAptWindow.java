package prodegus.musetasks.lessons;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.ui.popup.PopupYesNoController;

import java.util.List;

import static prodegus.musetasks.ui.StageFactories.newStage;

public class CustomAptWindow {

    public static List<Appointment> customAppointments(Lesson lesson) {
        FXMLLoader loader = new FXMLLoader(CustomAptWindow.class.getResource("/fxml/lesson-customapts.fxml"));
        Stage stage = newStage("Termine hinzufügen", loader);
        CustomAptController controller = loader.getController();
        if (lesson != null) controller.init(lesson);
        stage.showAndWait();
        return controller.appointments();
    }

}
