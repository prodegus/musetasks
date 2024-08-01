package prodegus.musetasks.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.lessons.AppointmentHBox;

public class LoginApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        AppointmentHBox appointmentHBox = new AppointmentHBox(0);
        System.out.println(appointmentHBox.getClass().getSimpleName());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
