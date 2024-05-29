package prodegus.musetasks.workspace;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.lessons.Lesson;
import prodegus.musetasks.utils.HalfYear;
import prodegus.musetasks.workspace.cells.StringListCell;
import prodegus.musetasks.workspace.cells.TeacherListCellShort;

import java.net.URL;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.teacherStringConverterShort;
import static prodegus.musetasks.lessons.LessonModel.getLessonListFromDB;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.utils.DateTime.H_2024_1;
import static prodegus.musetasks.utils.DateTime.asString;

public class LessonsController implements Initializable {

    @FXML private TableView<Lesson> lessonTableView;
    @FXML private TableColumn<Lesson, Boolean> selectColumn;
    @FXML private TableColumn<Lesson, String> nameColumn;
    @FXML private TableColumn<Lesson, String> instrumentColumn;
    @FXML private TableColumn<Lesson, String> categoryColumn;
    @FXML private TableColumn<Lesson, String> teacherColumn;
    @FXML private TableColumn<Lesson, String> locationColumn;

    @FXML private CheckBox selectAllCheckBox;

    @FXML private CheckBox filterCatCourse;
    @FXML private CheckBox filterCatGroup;
    @FXML private CheckBox filterCatSingle;
    @FXML private CheckBox filterCatWorkGroup;
    @FXML private CheckBox filterInstrument1;
    @FXML private ComboBox<String> filterInstrument1ComboBox;
    @FXML private CheckBox filterInstrument2;
    @FXML private ComboBox<String> filterInstrument2ComboBox;
    @FXML private CheckBox filterInstrument3;
    @FXML private ComboBox<String> filterInstrument3ComboBox;
    @FXML private CheckBox filterLocation1;
    @FXML private CheckBox filterLocation2;
    @FXML private CheckBox filterLocation3;
    @FXML private TitledPane filterPane;
    @FXML private CheckBox filterTeacher1;
    @FXML private ComboBox<Teacher> filterTeacher1ComboBox;
    @FXML private CheckBox filterTeacher2;
    @FXML private ComboBox<Teacher> filterTeacher2ComboBox;
    @FXML private CheckBox filterTeacher3;
    @FXML private ComboBox<Teacher> filterTeacher3ComboBox;
    @FXML private CheckBox filterTimeDate;
    @FXML private DatePicker filterTimeDatePicker;
    @FXML private CheckBox filterTimeSpan;
    @FXML private DatePicker filterTimeSpanFrom;
    @FXML private DatePicker filterTimeSpanTo;
    @FXML private CheckBox filterTimeToday;
    @FXML private MenuButton lessonInstrumentMenuButton;
    @FXML private TextField lessonSearchBar;
    @FXML private MenuButton lessonTeacherMenuButton;
    @FXML private ToggleButton lessonToggleLocation1;
    @FXML private ToggleButton lessonToggleLocation2;
    @FXML private ToggleButton lessonToggleLocation3;
    @FXML private ToggleButton lessonToggleToday;

    @FXML private TableColumn<Appointment, String> aptColumnDate;
    @FXML private TableColumn<Appointment, String> aptColumnNote;
    @FXML private TableColumn<Appointment, String> aptColumnStatus;
    @FXML private TableColumn<Appointment, String> aptColumnTime;
    @FXML private Label aptLabel;
    @FXML private TableView<Appointment> aptTableView;


    private final ObservableList<Lesson> lessons = FXCollections.observableArrayList();
    private final FilteredList<Lesson> filteredLessons = new FilteredList<>(lessons, lesson -> true);
    private final SortedList<Lesson> sortableLessons = new SortedList<>(filteredLessons);
    private Lesson selectedLesson;
    private Appointment selectedAppointment;
    private TableView<? extends Lesson> selectedTableView;
    private HalfYear selectedHalfYear;

    private final InvalidationListener lessonSearchListener = new InvalidationListener() {
        @Override
        public void invalidated(Observable observable) {
            String filter = lessonSearchBar.getText();
            if (filter == null || filter.isBlank()) {
                filteredLessons.setPredicate(lesson -> true);
            } else {
                filteredLessons.setPredicate(lesson ->
                        containsIgnoreCase(lesson.getLessonName(), filter) ||
                        containsIgnoreCase(lesson.getInstrument(), filter) ||
                        containsIgnoreCase(lesson.teacher().name(), filter) ||
                        containsIgnoreCase(lesson.studentsNamesString(), filter));
            }
        }
    };

    @FXML
    void aptEdit(ActionEvent event) {

    }

    @FXML
    void aptForwardTo(ActionEvent event) {

    }

    @FXML
    void aptNextHalfYear(ActionEvent event) {

    }

    @FXML
    void aptShowPreviousHalfYear(ActionEvent event) {

    }

    @FXML
    void showAddCourseWindow(ActionEvent event) {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addcourse.fxml"));
//        Stage stage = newStage("Kurs/Workshop anlegen", loader);
//        stage.showAndWait();
//        refreshLessons();
    }

    @FXML
    void showAddGroupWindow(ActionEvent event) {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addgroup.fxml"));
//        Stage stage = newStage("Gruppen-Unterricht anlegen", loader);
//        stage.showAndWait();
//        refreshLessons();
    }

    @FXML
    void showAddSingleWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addsingle.fxml"));
        Stage stage = newStage("Einzel-Unterricht anlegen", loader);
        stage.showAndWait();
        refreshLessons();
    }

    @FXML
    void showAddWorkgroupWindow(ActionEvent event) {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addworkgroup.fxml"));
//        Stage stage = newStage("AG anlegen", loader);
//        stage.showAndWait();
//        refreshLessons();
    }

    @FXML
    void resetFilter(ActionEvent event) {

    }

    @FXML
    void searchBarKeyTyped(KeyEvent event) {

    }

    @FXML
    void viewLessonsFiltered(ActionEvent event) {

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

    private void enableLessonSelection(TableView<? extends Lesson> tableView) {
        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (tableView.getSelectionModel().isEmpty()) return;
                selectedLesson = tableView.getSelectionModel().getSelectedItem();
                showLessonInfo(selectedLesson);
//                lessonDetails.setVisible(true);
            }
        });
    }

    public void showLessonInfo(Lesson lesson) {
        aptTableView.setItems(lesson.appointments(selectedHalfYear.getStart(), selectedHalfYear.getEnd()));
    }
    
    private void refreshLessons() {
        lessons.setAll(getLessonListFromDB());
        tableViewSelect(selectedTableView);
    }

    private void showTableView(TableView<? extends Lesson> tableView) {
        lessonTableView.setVisible(false);
//        ...tableView.setVisible(false);
//        ...tableView.setVisible(false);
//        ...tableView.setVisible(false);
//        ...tableView.setVisible(false);
        tableView.setVisible(true);
        selectedTableView = tableView;
    }
    
    private void tableViewSelect(TableView<? extends Lesson> tableView) {
        if (selectedLesson == null) return;
        int index = 0;
        for (Lesson lesson : tableView.getItems()) {
            if (lesson.id().equals(selectedLesson.id())) {
                break;
            }
            index++;
        }
        tableView.getSelectionModel().select(index);
        selectedLesson = tableView.getSelectionModel().getSelectedItem();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Lesson List
        lessons.setAll(getLessonListFromDB());
        selectedHalfYear = H_2024_1;

        // Initialize Lesson Filters
        filterLocation1.setText(SCHOOL_LOCATIONS.get(0).getName());
        filterLocation2.setText(SCHOOL_LOCATIONS.get(1).getName());
        filterLocation3.setText(SCHOOL_LOCATIONS.get(2).getName());

        filterTeacher1ComboBox.setItems(getTeacherListFromDB());
        filterTeacher1ComboBox.setButtonCell(new TeacherListCellShort());
        filterTeacher1ComboBox.setCellFactory(teacher -> new TeacherListCellShort());
        filterTeacher1ComboBox.setConverter(teacherStringConverterShort);
        filterTeacher1ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher1.setDisable(newValue == null));

        filterTeacher2ComboBox.setItems(getTeacherListFromDB());
        filterTeacher2ComboBox.setButtonCell(new TeacherListCellShort());
        filterTeacher2ComboBox.setCellFactory(teacher -> new TeacherListCellShort());
        filterTeacher2ComboBox.setConverter(teacherStringConverterShort);
        filterTeacher2ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher2.setDisable(newValue == null));

        filterTeacher3ComboBox.setItems(getTeacherListFromDB());
        filterTeacher3ComboBox.setButtonCell(new TeacherListCellShort());
        filterTeacher3ComboBox.setCellFactory(teacher -> new TeacherListCellShort());
        filterTeacher3ComboBox.setConverter(teacherStringConverterShort);
        filterTeacher3ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher3.setDisable(newValue == null));

        filterInstrument1ComboBox.setItems(SCHOOL_INSTRUMENTS);
        filterInstrument1ComboBox.setButtonCell(new StringListCell());
        filterInstrument1ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterInstrument1.setDisable(newValue == null));

        filterInstrument2ComboBox.setItems(SCHOOL_INSTRUMENTS);
        filterInstrument2ComboBox.setButtonCell(new StringListCell());
        filterInstrument2ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterInstrument2.setDisable(newValue == null));

        filterInstrument3ComboBox.setItems(SCHOOL_INSTRUMENTS);
        filterInstrument3ComboBox.setButtonCell(new StringListCell());
        filterInstrument3ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterInstrument3.setDisable(newValue == null));

        filterInstrument1.setDisable(filterInstrument1ComboBox.getSelectionModel().isEmpty());
        filterInstrument2.setDisable(filterInstrument2ComboBox.getSelectionModel().isEmpty());
        filterInstrument3.setDisable(filterInstrument3ComboBox.getSelectionModel().isEmpty());

        // Initialize SelectAllCheckBox
        selectAllCheckBox.setOnAction(event -> {
            for (Lesson lesson : selectedTableView.getItems()) {
                lesson.setSelected(selectAllCheckBox.isSelected());
            }
        });

        // Initialize SearchBar
        lessonSearchBar.textProperty().addListener(lessonSearchListener);

        // Initialize TableView: All lessons
        selectColumn.setCellValueFactory(cd -> cd.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLessonName()));
        nameColumn.prefWidthProperty().bind(lessonTableView.widthProperty().multiply(0.30));

        instrumentColumn.setCellValueFactory(new PropertyValueFactory<>("instrument"));
        instrumentColumn.prefWidthProperty().bind(lessonTableView.widthProperty().multiply(0.15));

        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().category()));
        categoryColumn.prefWidthProperty().bind(lessonTableView.widthProperty().multiply(0.15));

        teacherColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().teacher().shortName()));
        teacherColumn.prefWidthProperty().bind(lessonTableView.widthProperty().multiply(0.15));

        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().location().getName()));
        locationColumn.prefWidthProperty().bind(lessonTableView.widthProperty().multiply(0.15));

        lessonTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lessonTableView.setItems(sortableLessons);
        sortableLessons.comparatorProperty().bind(lessonTableView.comparatorProperty());
        enableLessonSelection(lessonTableView);
        lessonTableView.getStylesheets().add(getClass().getResource("/css/tableView.css").toExternalForm());

        // Initialize displayed table view
        lessonTableView.getSortOrder().setAll(nameColumn);
        showTableView(lessonTableView);

        // Initialize lesson info

        // Initialize appointment TableView
        aptColumnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        aptColumnNote.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        aptColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().status()));
        aptColumnTime.setCellValueFactory(cellData -> new SimpleStringProperty(asString(cellData.getValue().getTime())));
        aptLabel.setText("Termine im 1. Halbjahr 2024:");


        aptTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (aptTableView.getSelectionModel().isEmpty()) return;
                selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();
//                showLessonInfo(selectedLesson);
//                lessonDetails.setVisible(true);
            }
        });
    }
}
