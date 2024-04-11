package prodegus.musetasks.test;


import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Test extends Application {
    ObservableList<TestContact> contacts = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {

    }

    ObservableList<TestContact> getNewList() {
        ObservableList<TestContact> newList = FXCollections.observableArrayList();
        newList.add(new TestContact("1", "ASDdasdr", "Haasdasns"));
        newList.add(new TestContact("2", "Masdasr", "Fasdasdz"));
        newList.add(new TestContact("3", "Masdz", "Joasdasdim"));
        return newList;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

