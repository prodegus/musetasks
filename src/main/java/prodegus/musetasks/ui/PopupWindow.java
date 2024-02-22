package prodegus.musetasks.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class PopupWindow {

    public static void display(String message) {
        Stage stage = new Stage();
        stage.setTitle("Information");

        Label label = new Label(message);
        label.setWrapText(true);

        Button button = new Button("OK");
        button.setPrefWidth(80);
        button.setOnAction(event -> stage.close());

        VBox vbox = new VBox(40);
        vbox.getChildren().addAll(label, button);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 500, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }

}