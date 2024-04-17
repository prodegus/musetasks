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


//    INSERT INTO mtstudents VALUES (
//                    'NULL',
//                    'Gans',
//                    'Gustav',
//                    'Schüler',
//                    'Lohmar',
//                    'Blümchenstr. 18',
//                    '50679',
//                    'Ost-Hammerheim',
//                    '0176/509849535',
//                    'gustavgans@gmx.net',
//                    'null',
//                    'null',
//                    '1962-08-22',
//                    'null',
//                    'null',
//                    'null',
//                    'null',
//                    '0',
//                    'Interessent',
//                    '13.04.2024',
//                    'NULL',
//                    '0',
//                    '0',
//                    '0',
//                    '0',
//                    '0',
//                    '0',
//                    '0',
//                    '0')
//
//
//
//    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +   'NULL',
//    lastname    TEXT," +                                    'Gans',
//    firstname   TEXT," +                                    'Gustav',
//    category    TEXT," +                                    'Schüler',
//    location    TEXT," +                                    'Lohmar',
//    street      TEXT," +                                    'Blümchenstr. 18',
//    postalcode  INTEGER," +                                 '50679',
//    city        TEXT," +                                    'Ost-Hammerheim',
//    phone       TEXT," +                                    '0176/509849535',
//    email       TEXT," +                                    'gustavgans@gmx.net',
//    zoom        TEXT," +                                    'null',
//    skype       TEXT," +                                    'null',
//    birthday    TEXT," +                                    '1962-08-22',
//    notes       TEXT" +                                     'null',
//    instrument1 TEXT," +                                    'null',
//    instrument2 TEXT," +                                    'null',
//    instrument3 TEXT," +                                    'null',
//    prospective INTEGER," +                                 '0',
//    status      TEXT," +                                    'Interessent',
//    statusfrom  TEXT," +                                    '13.04.2024',
//    statusto    TEXT," +                                    'NULL',
//    parentid1   INTEGER REFERENCES mtparents (id)," +       '0',
//    parentid2   INTEGER REFERENCES mtparents (id)" +        '0',
//    teacherid1  INTEGER REFERENCES mtteachers (id)," +      '0',
//    teacherid2  INTEGER REFERENCES mtteachers (id)," +      '0',
//    teacherid3  INTEGER REFERENCES mtteachers (id)," +      '0',
//    lessonid1   INTEGER REFERENCES mtlessons (id)," +       '0',
//    lessonid2   INTEGER REFERENCES mtlessons (id)," +       '0',
//    lessonid3   INTEGER REFERENCES mtlessons (id)," +       '0')

















    public static void main(String[] args) {
        launch(args);
    }
}

