package prodegus.musetasks.workspace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import prodegus.musetasks.lessons.Lesson;
import prodegus.musetasks.contacts.Teacher;

import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.ui.StageFactories.newStage;

public class LessonsController implements Initializable {

    // Fields: Lessons -------------------------------------------------------------------------------------------------

    @FXML
    private CheckBox lessonFilterCatCourse;
    @FXML private CheckBox lessonFilterCatGroup;
    @FXML private CheckBox lessonFilterCatSingle;
    @FXML private CheckBox lessonFilterCatWorkGroup;
    @FXML private CheckBox lessonFilterInstrument1;
    @FXML private ComboBox<String> lessonFilterInstrument1ComboBox;
    @FXML private CheckBox lessonFilterInstrument2;
    @FXML private ComboBox<String> lessonFilterInstrument2ComboBox;
    @FXML private CheckBox lessonFilterInstrument3;
    @FXML private ComboBox<String> lessonFilterInstrument3ComboBox;
    @FXML private CheckBox lessonFilterLocation1;
    @FXML private CheckBox lessonFilterLocation2;
    @FXML private CheckBox lessonFilterLocation3;
    @FXML private TitledPane lessonFilterPane;
    @FXML private CheckBox lessonFilterTeacher1;
    @FXML private ComboBox<Teacher> lessonFilterTeacher1ComboBox;
    @FXML private CheckBox lessonFilterTeacher2;
    @FXML private ComboBox<Teacher> lessonFilterTeacher2ComboBox;
    @FXML private CheckBox lessonFilterTeacher3;
    @FXML private ComboBox<Teacher> lessonFilterTeacher3ComboBox;
    @FXML private CheckBox lessonFilterTimeDate;
    @FXML private DatePicker lessonFilterTimeDatePicker;
    @FXML private CheckBox lessonFilterTimeSpan;
    @FXML private DatePicker lessonFilterTimeSpanFrom;
    @FXML private DatePicker lessonFilterTimeSpanTo;
    @FXML private CheckBox lessonFilterTimeToday;
    @FXML private MenuButton lessonInstrumentMenuButton;
    @FXML private TextField lessonSearchBar;
    @FXML private MenuButton lessonTeacherMenuButton;
    @FXML private ToggleButton lessonToggleLocation1;
    @FXML private ToggleButton lessonToggleLocation2;
    @FXML private ToggleButton lessonToggleLocation3;
    @FXML private ToggleButton lessonToggleToday;

    private final ObservableList<Lesson> lessons = FXCollections.observableArrayList();
    private final FilteredList<Lesson> filteredLessons = new FilteredList<>(lessons, lesson -> true);
    private final SortedList<Lesson> sortableLessons = new SortedList<>(filteredLessons);

    // ----------------- Methods: Lessons ------------------------------------------------------------------------------

    @FXML
    void resetLessonFilter(ActionEvent event) {

    }

    @FXML
    void lessonShowAddCourseWindow(ActionEvent event) {

    }

    @FXML
    void lessonShowAddGroupWindow(ActionEvent event) {

    }

    @FXML
    void lessonShowAddSingleWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addlessonsingle-view.fxml"));
        Stage stage = newStage("Einzelunterricht anlegen", loader);
        stage.showAndWait();
        refreshLessons();
    }

    private void refreshLessons() {

    }

    @FXML
    void lessonShowAddWorkgroupWindow(ActionEvent event) {

    }

    @FXML
    void viewLessonsFiltered(ActionEvent event) {

    }

    @FXML
    void searchBarKeyTyped(KeyEvent event) {

    }

    @FXML
    void viewLessonsLocation1(ActionEvent event) {

    }

    @FXML
    void viewLessonsLocation2(ActionEvent event) {

    }

    @FXML
    void viewLessonsLocation3(ActionEvent event) {

    }

    @FXML
    void viewLessonsToday(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
