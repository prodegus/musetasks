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
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.appointments.EditAppointmentController;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.lessons.AddGroupController;
import prodegus.musetasks.lessons.AddSingleController;
import prodegus.musetasks.lessons.Lesson;
import prodegus.musetasks.lessons.LessonChange;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.utils.HalfYear;
import prodegus.musetasks.workspace.cells.StringListCell;
import prodegus.musetasks.workspace.cells.TeacherListCellShort;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.appointments.Appointment.CATEGORY_HOLIDAY;
import static prodegus.musetasks.appointments.AppointmentModel.deleteAppointmentFromDB;
import static prodegus.musetasks.appointments.AppointmentModel.getLessonAppointmentsFromDB;
import static prodegus.musetasks.appointments.EditAppointmentController.*;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.teacherStringConverterShort;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.popup.PopupWindow.displayInformation;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.GridPaneUtils.removeRow;

public class LessonsController implements Initializable {

    @FXML private SplitPane lessonListView;
    @FXML private HBox lessonCalendarView;

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

    @FXML private Label lessonInfoTitle;
    @FXML private Label lessonInfoSubtitle;
    @FXML private Label lessonInfoTeacherName;

    @FXML private Label singleInfoDuration;
    @FXML private GridPane singleInfoGridPane;
    @FXML private Label singleInfoInstrument;
    @FXML private Label singleInfoLocationRoom;
    @FXML private Label singleInfoStudentName;
    @FXML private Label singleInfoRegularAppointment;
    @FXML private Label singleInfoStatus;
    @FXML private GridPane lessonChangesGridPane;
    @FXML private ScrollPane lessonChangesScrollPane;

    @FXML private TableColumn<Appointment, String> aptColumnDate;
    @FXML private TableColumn<Appointment, String> aptColumnNote;
    @FXML private TableColumn<Appointment, String> aptColumnStatus;
    @FXML private TableColumn<Appointment, String> aptColumnTime;
    @FXML private Label aptLabel;
    @FXML private TableView<Appointment> aptTableView;
    @FXML private VBox noAptInfoBox;
    @FXML private Label noAptLabel;
    @FXML private Button addAptButton;
    @FXML private MenuButton aptEditButton;
    @FXML private Button aptForwardButton;

    private final ObservableList<Lesson> lessons = FXCollections.observableArrayList();
    private final FilteredList<Lesson> filteredLessons = new FilteredList<>(lessons, lesson -> true);
    private final SortedList<Lesson> sortableLessons = new SortedList<>(filteredLessons);
    private Lesson selectedLesson;
    private Appointment selectedAppointment;
    private TableView<? extends Lesson> selectedTableView;
    private HalfYear selectedHalfYear;
    private ContextMenu aptContextMenu;

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

    @FXML void switchToListView(ActionEvent event) {
        lessonCalendarView.setVisible(false);
        lessonListView.setVisible(true);
    }

    @FXML void switchToCalendarView(ActionEvent event) {
        lessonListView.setVisible(false);
        lessonCalendarView.setVisible(true);
    }

    @FXML void addAptButtonClicked(ActionEvent event) {
        if (selectedLesson.getStartDate().isAfter(selectedHalfYear.getEnd())) {
            editLesson(getEarliestLessonChange(selectedLesson.getId()));
        }
        if (selectedLesson.getEndDate().isBefore(selectedHalfYear.getStart())) {
            editLesson(getLatestLessonChange(selectedLesson.getId()));
        }
    }

    @FXML
    void aptEditChange(ActionEvent event) {
        if (selectedAppointment == null) return;
        openEditAppointmentWindow(selectedAppointment, EDIT_MODE_CHANGE);
    }

    @FXML
    void aptEditDrop(ActionEvent event) {
        if (selectedAppointment == null) return;
        openEditAppointmentWindow(selectedAppointment, EDIT_MODE_DROPPED);
    }

    @FXML
    void aptEditReschedule(ActionEvent event) {
        if (selectedAppointment == null) return;
        openEditAppointmentWindow(selectedAppointment, EDIT_MODE_RESCHEDULE);
    }

    @FXML
    void editLesson(ActionEvent event) {
        editLesson(selectedLesson);
    }

    public void editLesson(Lesson lesson) {
        switch (selectedLesson.getCategory()) {
            case CATEGORY_SINGLE -> showEditSingleWindow(selectedLesson);
            case CATEGORY_GROUP, CATEGORY_COURSE, CATEGORY_WORKGROUP -> showEditGroupWindow(selectedLesson);
        }
    }

    public void showEditSingleWindow(Lesson lesson) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addsingle.fxml"));
        Stage stage = newStage("Einzel-Unterricht bearbeiten", loader);
        AddSingleController controller = loader.getController();
        controller.initLesson(lesson);
        stage.showAndWait();
        refreshLessons();
    }

    public void showEditGroupWindow(Lesson lesson) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addgroup.fxml"));
        Stage stage = newStage(lesson.category() + " bearbeiten", loader);
        AddGroupController controller = loader.getController();
        controller.initLesson(lesson);
        stage.showAndWait();
        refreshLessons();
    }


    @FXML
    void deleteLesson(ActionEvent event) {
        if (PopupWindow.displayYesNo("Der Unterricht \"" + selectedLesson.getLessonName() + "\" sowie alle " +
                "zugehörigen Termine werden gelöscht.\n\nFortfahren?")) {
            for (Appointment appointment : getLessonAppointmentsFromDB(selectedLesson.getId())) {
                deleteAppointmentFromDB(appointment);
            }
            for (LessonChange change : getLessonChangeListFromDB(selectedLesson.getId())) {
                deleteLessonChange(selectedLesson.getId(), change.getChangeDate());
            }
            deleteLessonFromDB(selectedLesson);
        }
        refreshLessons();
    }


    @FXML
    void aptForwardTo(ActionEvent event) {

    }

    @FXML
    void aptShowNextHalfYear(ActionEvent event) {
        int currentIndex = halfYears.indexOf(selectedHalfYear);
        if (currentIndex == halfYears.size() - 1) {
            displayInformation("Ende der Liste erreicht!");
            return;
        }
        selectedHalfYear = halfYears.get(currentIndex + 1);
        aptLabel.setText("Termine im " + selectedHalfYear.title() + ":");
        refreshAppointments();
    }

    @FXML
    void aptShowPreviousHalfYear(ActionEvent event) {
        int currentIndex = halfYears.indexOf(selectedHalfYear);
        if (currentIndex == 0) {
            displayInformation("Anfang der Liste erreicht!");
            return;
        }
        selectedHalfYear = halfYears.get(currentIndex - 1);
        aptLabel.setText("Termine im " + selectedHalfYear.title() + ":");
        refreshAppointments();
    }

    @FXML
    void showAddCourseWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addgroup.fxml"));
        Stage stage = newStage("Kurs/Workshop anlegen", loader);
        AddGroupController controller = loader.getController();
        controller.init(CATEGORY_COURSE);
        stage.showAndWait();
        refreshLessons();
    }

    @FXML
    void showAddGroupWindow(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addgroup.fxml"));
        Stage stage = newStage("Gruppen-Unterricht anlegen", loader);
        AddGroupController controller = loader.getController();
        controller.init(CATEGORY_GROUP);
        stage.showAndWait();
        refreshLessons();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/lesson-addgroup.fxml"));
        Stage stage = newStage("AG anlegen", loader);
        AddGroupController controller = loader.getController();
        controller.init(CATEGORY_WORKGROUP);
        stage.showAndWait();
        refreshLessons();
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
        if (lesson == null) return;
        lessonInfoTitle.setText(lesson.category() + " - Details");
        lessonInfoSubtitle.setText(lesson.getLessonName());
        lessonInfoTeacherName.setText(lesson.teacher().name());
        singleInfoStudentName.setText(lesson.studentsNamesString());
        singleInfoInstrument.setText(lesson.getInstrument());
        singleInfoDuration.setText(lesson.durationString());
        singleInfoLocationRoom.setText(lesson.locationRoom());
        singleInfoRegularAppointment.setText(switch (lesson.getRepeat()) {
            case REPEAT_OFF -> asString(lesson.getStartDate(), lesson.getTime());
            case REPEAT_CUSTOM -> "Individuell";
            case REPEAT_WEEKLY -> lesson.regularAppointment();
            case REPEAT_2WEEKS -> lesson.regularAppointment() + " (2-wöchentl.)";
            case REPEAT_3WEEKS -> lesson.regularAppointment() + " (3-wöchentl.)";
            case REPEAT_4WEEKS -> lesson.regularAppointment() + " (4-wöchentl.)";
            case REPEAT_5WEEKS -> lesson.regularAppointment() + " (5-wöchentl.)";
            case REPEAT_6WEEKS -> lesson.regularAppointment() + " (6-wöchentl.)";
            default -> "nicht vereinbart";
        });
        singleInfoStatus.setText(lesson.lessonStatus());
        aptTableView.setItems(getLessonAppointmentsFromDB(lesson.getId(), selectedHalfYear));
        clearLessonChanges();
        showLessonChanges(lesson.getId());
        refreshAppointments();
    }

    private void showLessonChanges(int lessonId) {
        List<LessonChange> changes = getLessonChangeListFromDB(lessonId);
        if (changes.isEmpty()) return;

        int row = 2;
        for (LessonChange change : changes) {
            Label date = new Label(asString(change.getChangeDate()));
            Label changeNote = new Label(change.getChangeNote());
            changeNote.setWrapText(true);
            Button edit = editChangeButton(change);
            edit.setPrefWidth(25);
            Button delete = deleteChangeButton(change);
            delete.setPrefWidth(25);
            HBox buttonBox = new HBox(3, edit, delete);

            lessonChangesGridPane.getRowConstraints().add(new RowConstraints(22, -1, Integer.MAX_VALUE));
            lessonChangesGridPane.addRow(row, date, changeNote, buttonBox);
            GridPane.setValignment(date, VPos.TOP);
            GridPane.setValignment(changeNote, VPos.TOP);
            GridPane.setValignment(buttonBox, VPos.TOP);
            row++;

            if (row < changes.size() * 2 + 1) {
                lessonChangesGridPane.getRowConstraints().add(new RowConstraints(10));
                lessonChangesGridPane.add(new Separator(), 0, row, 3, 1);
                row++;
            }
        }
    }

    private void clearLessonChanges() {
        while (lessonChangesGridPane.getRowCount() > 2) {
            removeRow(lessonChangesGridPane, 2);
        }
    }

    private void openEditAppointmentWindow(Appointment selectedAppointment, int editMode) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editappointment-view.fxml"));
        Stage stage = newStage("Termin bearbeiten", loader);
        EditAppointmentController controller = loader.getController();
        controller.setEditMode(editMode);
        controller.initAppointment(selectedAppointment);
        stage.showAndWait();
        refreshAppointments();
    }
    
    @FXML void refreshLessons() {
        lessons.setAll(getLessonListFromDB());
        tableViewSelect(selectedTableView);
        showLessonInfo(selectedLesson);
    }

    public void refreshAppointments() {
        if (selectedLesson == null) {
            return;
        }

        LessonChange earliestChange = getEarliestLessonChange(selectedLesson.getId());
        LessonChange latestChange = getLatestLessonChange(selectedLesson.getId());

        if (earliestChange.getStartDate().isAfter(selectedHalfYear.getEnd())) {
            aptTableView.setVisible(false);
            noAptInfoBox.setVisible(true);
            // show info: lesson not yet started
            noAptLabel.setText("Keine Termine in diesem Halbjahr");
            addAptButton.setText("Unterrichtsdaten ändern");
            return;
        }

        if (latestChange.getEndDate().isBefore(selectedHalfYear.getStart())) {
            aptTableView.setVisible(false);
            noAptInfoBox.setVisible(true);
            if (selectedLesson.getLessonStatus() == LESSON_STATUS_ACTIVE) {
                noAptLabel.setText("Unterricht aktiv, aber noch keine Termine für dieses Halbjahr eingetragen");
                addAptButton.setText("Unterricht verlängern");
            } else {
                noAptLabel.setText("Keine Termine in diesem Halbjahr");
                addAptButton.setText("Unterrichtsdaten ändern");
            }
            return;
        }

        aptTableView.setVisible(true);
        noAptInfoBox.setVisible(false);
        aptTableView.setItems(getLessonAppointmentsFromDB(selectedLesson.getId(), selectedHalfYear).sorted());
        aptTableView.refresh();
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
            if (index == tableView.getItems().size()) {
                selectedLesson = null;
                return;
            }
        }
        tableView.getSelectionModel().select(index);
        selectedLesson = tableView.getSelectionModel().getSelectedItem();
    }

    private Button editChangeButton(LessonChange lessonChange) {
        Button button = new Button("B");
        button.setOnAction(e -> {
            editLesson(lessonChange.lesson());
        });
        return button;
    }

    private Button deleteChangeButton(LessonChange lessonChange) {
        Button button = new Button("L");
        button.setOnAction(e -> {
            Node source = (Node) e.getSource();
            int rowIndex = GridPane.getRowIndex(source.getParent());
            if (rowIndex == 2) {
                PopupWindow.displayInformation("Erster Eintrag, löschen nicht möglich. Zum Löschen des Unterrichts " +
                        "bitte die Funktion \"Unterricht löschen\" verwenden.");
                return;
            }
            if (PopupWindow.displayYesNo("Änderung löschen?")) {
                deleteLessonChange(lessonChange.getId(), lessonChange.getChangeDate());
                if(rowIndex < lessonChangesGridPane.getRowCount() - 1) removeRow(lessonChangesGridPane, rowIndex + 1);
                removeRow(lessonChangesGridPane, rowIndex);
            }
        });
        return button;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Lesson List
        lessons.setAll(getLessonListFromDB());
        selectedHalfYear = currentHalfYear();
        aptLabel.setText("Termine im " + selectedHalfYear.title() + ":");

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
        lessonChangesScrollPane.setFitToWidth(true);

        // Initialize appointment TableView
        aptColumnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().dateInfo()));
        aptColumnDate.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.23));

        aptColumnTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().timeInfo()));
        aptColumnTime.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.13));

        aptColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().status()));
        aptColumnStatus.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.23));

        aptColumnNote.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        aptColumnNote.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.4));

        aptLabel.setText("Termine im 1. Halbjahr 2024:");

        MenuItem item1 = new MenuItem("Termin absagen (ohne Ersatz)");
        MenuItem item2 = new MenuItem("Termin absagen (mit Ersatz)");
        MenuItem item3 = new MenuItem("Termin ändern");
        item1.setOnAction(e -> openEditAppointmentWindow(selectedAppointment, EDIT_MODE_DROPPED));
        item2.setOnAction(e -> openEditAppointmentWindow(selectedAppointment, EDIT_MODE_RESCHEDULE));
        item3.setOnAction(e -> openEditAppointmentWindow(selectedAppointment, EDIT_MODE_CHANGE));
        aptContextMenu = new ContextMenu(item1, item2, item3);
        aptContextMenu.setAutoHide(true);

        aptTableView.setOnMouseClicked(event -> {
            aptContextMenu.hide();
            if (aptTableView.getSelectionModel().isEmpty()) return;
            selectedAppointment = aptTableView.getSelectionModel().getSelectedItem();
            aptEditButton.setDisable(selectedAppointment.getCategory() == CATEGORY_HOLIDAY);
        });

        aptTableView.setOnContextMenuRequested(event -> {
            if (aptTableView.getSelectionModel().isEmpty() || selectedAppointment.getCategory() == CATEGORY_HOLIDAY ||
                    event.getY() < 24.0) return;

            aptContextMenu.show(aptTableView, event.getScreenX(), event.getScreenY());
        });
    }

}
