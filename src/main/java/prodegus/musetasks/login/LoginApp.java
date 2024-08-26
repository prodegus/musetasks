package prodegus.musetasks.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import prodegus.musetasks.ui.AppointmentHBox;
import prodegus.musetasks.ui.StageFactories;

import static prodegus.musetasks.ui.StageFactories.loadFonts;

public class LoginApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        loadFonts();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(StageFactories.class.getResource("/css/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
