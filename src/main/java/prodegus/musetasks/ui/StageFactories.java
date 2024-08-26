package prodegus.musetasks.ui;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
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
        loadFonts();
        Parent root;
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

    public static void loadFonts() {
        Font.loadFont(StageFactories.class.getResource("/fonts/FreeSans.ttf").toExternalForm(), 12);
        Font.loadFont(StageFactories.class.getResource("/fonts/FreeSansBold.ttf").toExternalForm(), 12);
        Font.loadFont(StageFactories.class.getResource("/fonts/Manrope-Regular.otf").toExternalForm(), 12);
        Font.loadFont(StageFactories.class.getResource("/fonts/Manrope-Bold.otf").toExternalForm(), 12);
    }

    public static Stage newStage(FXMLLoader loader) throws IOException {
        return newStage(APP_NAME, loader);
    }

}
