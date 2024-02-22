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
        TestContact contact1 = new TestContact("1", "Meier", "Hans");
        TestContact contact2 = new TestContact("2", "Müller", "Fritz");
        TestContact contact3 = new TestContact("3", "Metz", "Joachim");

        TestContact contact4 = new TestContact("1", "ASDdasdr", "Haasdasns");
        TestContact contact5 = new TestContact("2", "Masdasr", "Fasdasdz");
        TestContact contact6 = new TestContact("3", "Masdz", "Joasdasdim");

        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);

        TableView<TestContact> tableView = new TableView<>(contacts);
        tableView.setItems(contacts);

        TableColumn<TestContact,String> firstNameCol = new TableColumn<>("firstname");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        TableColumn<TestContact,String> lastNameCol = new TableColumn<>("lastname");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        tableView.getColumns().setAll(firstNameCol, lastNameCol);

        TextArea textArea = new TextArea();
        System.out.println(textArea.getStylesheets().toString());

        Button button = new Button("Aktualisieren");
        button.setOnAction(event -> {
            contacts.add(contact4);
        });

        VBox root = new VBox(40);
        root.getChildren().addAll(tableView, button);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
        System.out.println(contacts.get(0).getLastname());
        System.out.println(contacts.get(1).getLastname());
        System.out.println(contacts.get(2).getLastname());
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

