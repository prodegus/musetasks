package prodegus.musetasks.ui;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageFactories {
    public static final String APP_NAME = "MuseTasks";

    public static Stage stageOf(Event event) {
        Node source = (Node) event.getSource();
        return (Stage) source.getScene().getWindow();
    }

    public static Stage stageOf(Node node) {
        return (Stage) node.getScene().getWindow();
    }

    public static Stage newStage(String title, FXMLLoader loader) {
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);
        scene.getStylesheets().add(StageFactories.class.getResource("/css/application.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        return stage;
    }

    public static Stage newStage(FXMLLoader loader) throws IOException {
        return newStage(APP_NAME, loader);
    }

}
