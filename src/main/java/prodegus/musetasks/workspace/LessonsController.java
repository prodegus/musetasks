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
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.appointments.EditAppointmentController;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.lessons.*;
import prodegus.musetasks.mail.NewMailController;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.CalendarBox;
import prodegus.musetasks.ui.CalendarColumn;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.utils.HalfYear;
import prodegus.musetasks.workspace.cells.StringListCell;
import prodegus.musetasks.workspace.cells.TeacherListCellFormal;
import prodegus.musetasks.workspace.cells.TeacherListCellShort;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.appointments.Appointment.*;
import static prodegus.musetasks.appointments.AppointmentModel.*;
import static prodegus.musetasks.appointments.EditAppointmentController.*;
import static prodegus.musetasks.contacts.TeacherModel.*;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.school.LocationModel.getLocationListFromDB;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.CalendarColumn.columnSeparator;
import static prodegus.musetasks.ui.popup.PopupWindow.displayInformation;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.GridPaneUtils.removeRow;
import static prodegus.musetasks.utils.Nodes.getAllNodes;
import static prodegus.musetasks.utils.Nodes.hide;

public class LessonsController implements Initializable {

    @FXML private SplitPane lessonListView;
    @FXML private VBox lessonCalendarView;

    @FXML private VBox lessonInfoArea;

    @FXML private Label calendarDateLabel;
    @FXML private DatePicker calendarDatePicker;
    @FXML private ScrollPane columnHeaderScrollPane;
    @FXML private HBox columnHeaders;
    @FXML private Rectangle columnHeadersBackground;
    @FXML private HBox calendarColumns;
    @FXML private ScrollPane calendarScrollPane;
    @FXML private VBox background;
    @FXML private ScrollPane rowHeaderScrollPane;

    @FXML private ToggleGroup lessonFilterToggles;
    @FXML private ToggleButton viewAllToggle;
    @FXML private ToggleButton lessonToggleLocation1;
    @FXML private ToggleButton lessonToggleLocation2;
    @FXML private ToggleButton lessonToggleLocation3;
    @FXML private ToggleButton lessonToggleLocation4;
    @FXML private ToggleButton lessonToggleLocation5;
    private final List<ToggleButton> lessonTogglesLocation = FXCollections.observableArrayList();
    @FXML private MenuButton lessonTeacherMenuButton;
    @FXML private MenuButton lessonInstrumentMenuButton;

    @FXML private TitledPane filterPane;

    @FXML private CheckBox filterLocation1;
    @FXML private CheckBox filterLocation2;
    @FXML private CheckBox filterLocation3;
    @FXML private CheckBox filterLocation4;
    @FXML private CheckBox filterLocation5;
    private final List<CheckBox> filterLocationCheckBoxes = FXCollections.observableArrayList();

    @FXML private CheckBox filterTeacher1;
    @FXML private ComboBox<Teacher> filterTeacher1ComboBox;
    @FXML private CheckBox filterTeacher2;
    @FXML private ComboBox<Teacher> filterTeacher2ComboBox;
    @FXML private CheckBox filterTeacher3;
    @FXML private ComboBox<Teacher> filterTeacher3ComboBox;

    @FXML private CheckBox filterInstrument1;
    @FXML private ComboBox<String> filterInstrument1ComboBox;
    @FXML private CheckBox filterInstrument2;
    @FXML private ComboBox<String> filterInstrument2ComboBox;
    @FXML private CheckBox filterInstrument3;
    @FXML private ComboBox<String> filterInstrument3ComboBox;

    @FXML private CheckBox filterCatSingle;
    @FXML private CheckBox filterCatGroup;
    @FXML private CheckBox filterCatCourse;
    @FXML private CheckBox filterCatWorkGroup;

    @FXML private CheckBox filterTimeToday;
    @FXML private CheckBox filterTimeDate;
    @FXML private DatePicker filterTimeDatePicker;
    @FXML private CheckBox filterTimeSpan;
    @FXML private DatePicker filterTimeSpanFrom;
    @FXML private DatePicker filterTimeSpanTo;

    @FXML private Button resetFilterButton;

    @FXML private TableView<Lesson> lessonTableView;
    @FXML private TableColumn<Lesson, Boolean> selectColumn;
    @FXML private TableColumn<Lesson, String> nameColumn;
    @FXML private TableColumn<Lesson, String> instrumentColumn;
    @FXML private TableColumn<Lesson, String> categoryColumn;
    @FXML private TableColumn<Lesson, String> teacherColumn;
    @FXML private TableColumn<Lesson, String> locationColumn;

    @FXML private CheckBox selectAllCheckBox;

    @FXML private TextField lessonSearchBar;

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

    private final String STYLE_UNSELECTED =
            "-fx-background-color: #90ee90;" +
            "-fx-border-color: #5dd55d;" +
            "-fx-background-radius: 5;" +
            "-fx-border-radius: 5;";

    private final String STYLE_SELECTED =
            "-fx-background-color: #5dd55d;" +
            "-fx-border-color: black;" +
            "-fx-background-radius: 5;" +
            "-fx-border-radius: 5;";

    private final ObservableList<Lesson> lessons = FXCollections.observableArrayList();
    private final FilteredList<Lesson> filteredLessons = new FilteredList<>(lessons, lesson -> true);
    private final SortedList<Lesson> sortableLessons = new SortedList<>(filteredLessons);
    private Lesson selectedLesson;
    private Appointment selectedAppointment;
    private TableView<? extends Lesson> selectedTableView;
    private HalfYear selectedHalfYear;
    private ContextMenu aptContextMenu;
    
    private LocalDate calendarStartDate;
    private LocalDate calendarEndDate;

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

    @FXML void calendarBack() {
        calendarStartDate = calendarStartDate.minusDays(1);
        refreshCalendar();
    }

    @FXML void calendarForward() {
        calendarStartDate = calendarStartDate.plusDays(1);
        refreshCalendar();
    }

    private void refreshCalendar() {
        calendarDateLabel.setText(weekdayDateString(calendarStartDate));
        for (Node node : getAllNodes(calendarColumns)) {
            if (node instanceof CalendarColumn calendarColumn) {
                calendarColumn.setDate(calendarStartDate);
            }
        }
        enableCalendarBoxSelection();
    }

    @FXML void confirmDate(ActionEvent event) {
        if (isInvalidDate(calendarDatePicker.getEditor().getText())) {
            PopupWindow.displayInformation("Ungültiges Datum!");
            calendarDatePicker.getEditor().clear();
            return;
        }
        calendarStartDate = calendarDatePicker.getValue();
        calendarDatePicker.getEditor().clear();
        refreshCalendar();
    }

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

    private boolean isInvalidDate(String birthDate) {
        if (birthDate.isBlank()) return false;
        try {
            calendarDatePicker.getConverter().fromString(birthDate);
        } catch (DateTimeParseException e) {
            return true;
        }
        return false;
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
        if (selectedLesson == null) return;
        String message = "Der Unterricht \"" + selectedLesson.getLessonName() + "\" sowie alle zugehörigen Termine " +
                "werden gelöscht.\n\nFortfahren?";
        if (PopupWindow.displayYesNo(message)) deleteFullLesson(selectedLesson);
        refreshLessons();
    }

    @FXML
    void deleteLessons(ActionEvent event) {
        ArrayList<Lesson> selectedLessons = new ArrayList<>();
        StringJoiner lessonNames = new StringJoiner("\n");

        for (Lesson lesson : lessonTableView.getItems()) {
            if (lesson.isSelected()) {
                selectedLessons.add(lesson);
                lessonNames.add(lesson.getLessonName());
            }
        }

        if (selectedLessons.size() == 0) {
            deleteLesson(new ActionEvent());
            return;
        }

        String message = selectedLessons.size() == 1 ? "Unterricht \"" + lessonNames + "\" wird gelöscht. Fortfahren?" :
                "Folgende Unterrichte werden gelöscht:\n\n" + lessonNames + "\n\nFortfahren?";
        if (!PopupWindow.displayYesNo(message)) return;
        for (Lesson lesson : selectedLessons) deleteFullLesson(lesson);
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
    void viewLessonsAll(ActionEvent event) {
        lessons.setAll(getLessonListFromDB());
    }

    @FXML
    void viewLessonsToday(ActionEvent event) {
        List<Integer> lessonIds = new ArrayList<>();
        for (Appointment appointment : getAppointmentListFromDB(LocalDate.now())) {
            if (appointment.getCategory() != CATEGORY_HOLIDAY &&
                    (appointment.getStatus() == STATUS_OK || appointment.getStatus() == STATUS_CHANGED) &&
                    appointment.getLessonId() != 0) {
                lessonIds.add(appointment.getLessonId());
            }
        }
        lessons.removeIf(lesson -> !lessonIds.contains(lesson.getId()));
    }

    @FXML
    void viewLessonsLocation1(ActionEvent event) {
        lessons.setAll(getLessonListFromDB());
        lessons.removeIf(lesson -> lesson.getLocationId() != 1);
    }

    @FXML
    void viewLessonsLocation2(ActionEvent event) {
        lessons.setAll(getLessonListFromDB());
        lessons.removeIf(lesson -> lesson.getLocationId() != 2);
    }

    @FXML
    void viewLessonsLocation3(ActionEvent event) {
        lessons.setAll(getLessonListFromDB());
        lessons.removeIf(lesson -> lesson.getLocationId() != 3);
    }

    @FXML
    void viewLessonsLocation4(ActionEvent event) {
        lessons.setAll(getLessonListFromDB());
        lessons.removeIf(lesson -> lesson.getLocationId() != 4);
    }

    @FXML
    void viewLessonsLocation5(ActionEvent event) {
        lessons.setAll(getLessonListFromDB());
        lessons.removeIf(lesson -> lesson.getLocationId() != 5);
    }

    @FXML
    void viewLessonsFiltered(ActionEvent event) {
        for (Toggle toggle : lessonFilterToggles.getToggles()) toggle.setSelected(false);
        lessons.setAll(getLessonListFromDB());
        lessons.removeIf(lesson -> !fitsLocationFilter(lesson));
        lessons.removeIf(lesson -> !fitsTeacherFilter(lesson));
        lessons.removeIf(lesson -> !fitsInstrumentFilter(lesson));
        lessons.removeIf(lesson -> !fitsCategoryFilter(lesson));
        lessons.removeIf(lesson -> !fitsTimePeriod(lesson));
    }

    private boolean fitsLocationFilter(Lesson lesson) {
        if (!filterLocation1.isSelected() && !filterLocation2.isSelected() && !filterLocation3.isSelected() &&
                !filterLocation4.isSelected() && !filterLocation5.isSelected()) return true;
        boolean filterLoc1Fits = filterLocation1.isSelected() && lesson.getLocationId() == 1;
        boolean filterLoc2Fits = filterLocation2.isSelected() && lesson.getLocationId() == 2;
        boolean filterLoc3Fits = filterLocation3.isSelected() && lesson.getLocationId() == 3;
        boolean filterLoc4Fits = filterLocation4.isSelected() && lesson.getLocationId() == 4;
        boolean filterLoc5Fits = filterLocation5.isSelected() && lesson.getLocationId() == 5;
        return filterLoc1Fits || filterLoc2Fits || filterLoc3Fits || filterLoc4Fits || filterLoc5Fits;
    }

    private boolean fitsTeacherFilter(Lesson lesson) {
        if (!filterTeacher1.isSelected() && !filterTeacher2.isSelected() && !filterTeacher3.isSelected()) return true;
        boolean filterTeacher1Fits = filterTeacher1.isSelected() && lesson.getTeacherId() == filterTeacher1ComboBox.getValue().getId();
        boolean filterTeacher2Fits = filterTeacher2.isSelected() && lesson.getTeacherId() == filterTeacher2ComboBox.getValue().getId();
        boolean filterTeacher3Fits = filterTeacher3.isSelected() && lesson.getTeacherId() == filterTeacher3ComboBox.getValue().getId();
        return filterTeacher1Fits || filterTeacher2Fits || filterTeacher3Fits;
    }

    private boolean fitsInstrumentFilter(Lesson lesson) {
        if (!filterInstrument1.isSelected() && !filterInstrument2.isSelected() && !filterInstrument3.isSelected()) return true;
        boolean filterInstrument1Fits = filterInstrument1.isSelected() && lesson.getInstrument().equals(filterInstrument1ComboBox.getValue());
        boolean filterInstrument2Fits = filterInstrument2.isSelected() && lesson.getInstrument().equals(filterInstrument2ComboBox.getValue());
        boolean filterInstrument3Fits = filterInstrument3.isSelected() && lesson.getInstrument().equals(filterInstrument3ComboBox.getValue());
        return filterInstrument1Fits || filterInstrument2Fits || filterInstrument3Fits;
    }

    private boolean fitsCategoryFilter(Lesson lesson) {
        if (!filterCatSingle.isSelected() && !filterCatGroup.isSelected() && !filterCatCourse.isSelected() && !filterCatWorkGroup.isSelected()) return true;
        boolean filterCatSingleFits = filterCatSingle.isSelected() && lesson.getCategory() == CATEGORY_SINGLE;
        boolean filterCatGroupFits = filterCatGroup.isSelected() && lesson.getCategory() == CATEGORY_GROUP;
        boolean filterCatCourseFits = filterCatCourse.isSelected() && lesson.getCategory() == CATEGORY_COURSE;
        boolean filterCatWorkgroupFits = filterCatWorkGroup.isSelected() && lesson.getCategory() == CATEGORY_WORKGROUP;
        return filterCatSingleFits || filterCatGroupFits || filterCatCourseFits || filterCatWorkgroupFits;
    }

    private boolean fitsTimePeriod(Lesson lesson) {
        if (!filterTimeToday.isSelected() && !filterTimeDate.isSelected() && !filterTimeSpan.isSelected()) return true;
        boolean filterTodayFits = filterTimeToday.isSelected() && lesson.hasAppointment(LocalDate.now());
        boolean filterDateFits = filterTimeDate.isSelected() && lesson.hasAppointment(filterTimeDatePicker.getValue());
        boolean filterSpanFits = filterTimeSpan.isSelected() && lesson.hasAppointment(filterTimeSpanFrom.getValue(), filterTimeSpanTo.getValue());
        return filterTodayFits || filterDateFits || filterSpanFits;
    }

    @FXML
    void resetFilter(ActionEvent event) {
        lessons.setAll(getLessonListFromDB());
        viewAllToggle.setSelected(true);
        filterPane.setExpanded(false);

        for (Node node : getAllNodes(filterPane)) {
            if (node.getClass().getSimpleName().equals("TitledPane")) ((TitledPane)node).setExpanded(false);
            if (node.getClass().getSimpleName().equals("CheckBox")) ((CheckBox)node).setSelected(false);
            if (node.getClass().getSimpleName().equals("ComboBox")) ((ComboBox)node).getSelectionModel().clearSelection();
        }
    }

    @FXML
    void searchBarKeyTyped(KeyEvent event) {

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
        lessonInfoArea.setVisible(true);
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
            ImageView editIcon = editChangeIcon(change);
            ImageView deleteIcon = deleteChangeIcon(change);
            HBox.setMargin(deleteIcon, new Insets(0, 0, 0, 5));
            HBox buttonBox = new HBox(3, editIcon, deleteIcon);

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
        tableViewSelect(lessonTableView);
        showLessonInfo(selectedLesson);
        refreshCalendarColumns();
    }

    @FXML void mailToLessonContacts() {
        if (selectedLesson == null) return;
        StringJoiner recipients = new StringJoiner(", ");
        for (Student student : selectedLesson.students()) {
            recipients.add(student.getContactEmail());
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init(recipients.toString());
        stage.showAndWait();
    }

    @FXML void mailToLessonsContacts() {
        ArrayList<Lesson> selectedLessons = new ArrayList<>();

        for (Lesson lesson : lessonTableView.getItems()) {
            if (lesson.isSelected()) {
                selectedLessons.add(lesson);
            }
        }

        if (selectedLessons.size() == 0) {
            mailToLessonContacts();
            return;
        }

        StringJoiner recipients = new StringJoiner(", ");
        for (Lesson lesson : selectedLessons) {
            for (Student student : lesson.students()) {
                recipients.add(student.getContactEmail());
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
        Stage stage = newStage("Neue E-Mail", loader);
        NewMailController controller = loader.getController();
        controller.init(recipients.toString());
        stage.showAndWait();
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
        showLessonInfo(selectedLesson);
    }

    private ImageView editChangeIcon(LessonChange lessonChange) {
        ImageView icon = new ImageView(new Image(getClass().getResource("/images/edit.png").toExternalForm()));
        icon.setOnMouseClicked(e -> editLesson(lessonChange));
        icon.setOnMouseEntered(e -> {
            icon.setScaleX(1.2);
            icon.setScaleY(1.2);
        });
        icon.setOnMouseExited(e -> {
            icon.setScaleX(1.0);
            icon.setScaleY(1.0);
        });
        return icon;
    }

    private ImageView deleteChangeIcon(LessonChange lessonChange) {
        ImageView icon = new ImageView(new Image(getClass().getResource("/images/delete.png").toExternalForm()));
        icon.setOnMouseClicked(e -> {
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
        icon.setOnMouseEntered(e -> {
            icon.setScaleX(1.2);
            icon.setScaleY(1.2);
        });
        icon.setOnMouseExited(e -> {
            icon.setScaleX(1.0);
            icon.setScaleY(1.0);
        });
        return icon;
    }

    private void addCalendarColumn(Location location, String room) {
        ObservableList<Appointment> appointments = getRoomAppointments(location, room);
        FilteredList<Appointment> filteredAppointments = new FilteredList<>(appointments, appointment -> true);
        CalendarColumn column = new CalendarColumn(location.getName() + " - " + room, filteredAppointments, calendarStartDate);
        columnHeaders.getChildren().addAll(column.headerBox(), columnSeparator());
        calendarColumns.getChildren().addAll(column, columnSeparator());
    }

    private List<CalendarColumn> getCalendarColumns() {
        List<CalendarColumn> columns = new ArrayList<>();
        for (Node node : getAllNodes(calendarColumns)) {
            if (node instanceof CalendarColumn column) columns.add(column);
        }
        return columns;
    }

    private void refreshCalendarColumns() {
        columnHeaders.getChildren().clear();
        calendarColumns.getChildren().clear();
        for (Location lessonLocation : getLocationListFromDB()) {
            for (String room : lessonLocation.rooms()) {
                addCalendarColumn(lessonLocation, room);
            }
        }
        enableCalendarBoxSelection();
    }

    private List<CalendarBox> getCalendarBoxes() {
        List<CalendarBox> boxes = new ArrayList<>();
        for (Node node : getAllNodes(calendarColumns)) {
            if (node instanceof CalendarBox calendarBox) {
                boxes.add(calendarBox);
            }
        }
        return boxes;
    }

    private void enableCalendarBoxSelection() {
        for (CalendarBox calendarBox : getCalendarBoxes()) {
            calendarBox.setOnMouseClicked(event -> {
                unselectAllBoxes();
                calendarBox.setStyle(STYLE_SELECTED);
                selectedLesson = calendarBox.getAppointment().lesson();
                if (event.getClickCount() == 2) {
                    switchToListView(new ActionEvent());
                    tableViewSelect(lessonTableView);
                }
            });

            calendarBox.setOnContextMenuRequested(event -> {
                unselectAllBoxes();
                calendarBox.setStyle(STYLE_SELECTED);
                selectedLesson = calendarBox.getAppointment().lesson();
                selectedAppointment = calendarBox.getAppointment();
                MenuItem item1 = new MenuItem("Termin absagen (ohne Ersatz)");
                MenuItem item2 = new MenuItem("Termin absagen (mit Ersatz)");
                MenuItem item3 = new MenuItem("Termin ändern");
                item1.setOnAction(e -> openEditAppointmentWindow(selectedAppointment, EDIT_MODE_DROPPED));
                item2.setOnAction(e -> openEditAppointmentWindow(selectedAppointment, EDIT_MODE_RESCHEDULE));
                item3.setOnAction(e -> openEditAppointmentWindow(selectedAppointment, EDIT_MODE_CHANGE));
                ContextMenu contextMenu = new ContextMenu(item1, item2, item3);
                contextMenu.setAutoHide(true);
                contextMenu.show(calendarBox, event.getScreenX(), event.getScreenY());
            });
        }
    }

    private void unselectAllBoxes() {
        for (CalendarBox calendarBox : getCalendarBoxes()) {
            calendarBox.setStyle(STYLE_UNSELECTED);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lessonListView.setVisible(false);
        lessonCalendarView.setVisible(true);

        lessonTogglesLocation.addAll(FXCollections.observableArrayList(lessonToggleLocation1, lessonToggleLocation2,
                lessonToggleLocation3, lessonToggleLocation4, lessonToggleLocation5));

        filterLocationCheckBoxes.addAll(FXCollections.observableArrayList(filterLocation1, filterLocation2,
                filterLocation3, filterLocation4, filterLocation5));

        // Initialize Lesson List
        lessons.setAll(getLessonListFromDB());
        selectedHalfYear = currentHalfYear();
        aptLabel.setText("Termine im " + selectedHalfYear.title() + ":");

        // Initialize Lesson Filters
        int i = 0;
        for (ToggleButton toggleButton : lessonTogglesLocation) {
            if (SCHOOL_LOCATIONS.size() > i) toggleButton.setText(SCHOOL_LOCATIONS.get(i).getName());
            else hide(toggleButton);
            i++;
        }

        i = 0;
        for (CheckBox checkBox : filterLocationCheckBoxes) {
            if (SCHOOL_LOCATIONS.size() > i) checkBox.setText(SCHOOL_LOCATIONS.get(i).getName());
            else hide(checkBox);
            i++;
        }

        for (Teacher teacher : getTeacherListFromDB()) {
            MenuItem item = new MenuItem(teacher.name());
            item.setOnAction(e -> {
                lessons.setAll(getLessonListFromDB());
                lessons.removeIf(lesson -> lesson.getTeacherId() != teacher.getId());
            });
            lessonTeacherMenuButton.getItems().add(item);
        }

        for (String instrument : SCHOOL_INSTRUMENTS) {
            MenuItem item = new MenuItem(instrument);
            item.setOnAction(e -> {
                lessons.setAll(getLessonListFromDB());
                lessons.removeIf(lesson -> !lesson.getInstrument().equals(instrument));
            });
            lessonInstrumentMenuButton.getItems().add(item);
        }

        filterTeacher1ComboBox.setItems(getTeacherListFromDB());
        filterTeacher1ComboBox.setButtonCell(new TeacherListCellFormal());
        filterTeacher1ComboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        filterTeacher1ComboBox.setConverter(teacherStringConverterFormal);
        filterTeacher1ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher1.setDisable(newValue == null));

        filterTeacher2ComboBox.setItems(getTeacherListFromDB());
        filterTeacher2ComboBox.setButtonCell(new TeacherListCellFormal());
        filterTeacher2ComboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        filterTeacher2ComboBox.setConverter(teacherStringConverterFormal);
        filterTeacher2ComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> filterTeacher2.setDisable(newValue == null));

        filterTeacher3ComboBox.setItems(getTeacherListFromDB());
        filterTeacher3ComboBox.setButtonCell(new TeacherListCellFormal());
        filterTeacher3ComboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        filterTeacher3ComboBox.setConverter(teacherStringConverterFormal);
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

        // Initialize displayed table view
        lessonTableView.getSortOrder().setAll(nameColumn);
        showTableView(lessonTableView);

        // Initialize lesson info
        lessonInfoArea.setVisible(false);
        lessonChangesScrollPane.setFitToWidth(true);

        // Initialize appointment TableView
        aptColumnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().dateInfo()));
        aptColumnDate.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.26));

        aptColumnTime.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().timeInfo()));
        aptColumnTime.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.16));

        aptColumnStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().status()));
        aptColumnStatus.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.23));

        aptColumnNote.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        aptColumnNote.prefWidthProperty().bind(aptTableView.widthProperty().multiply(0.34));

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

        // Initialize Calendar View
        columnHeaderScrollPane.setStyle("-fx-background-color: lightgrey;");
        rowHeaderScrollPane.setStyle("-fx-background-color: lightgrey;");
        calendarScrollPane.setStyle("-fx-background-color: lightgrey;");

        columnHeaderScrollPane.hvalueProperty().bind(calendarScrollPane.hvalueProperty());
        rowHeaderScrollPane.vvalueProperty().bind(calendarScrollPane.vvalueProperty());

        calendarStartDate = LocalDate.now();
        calendarDateLabel.setText(weekdayDateString(calendarStartDate));
        for (Location lessonLocation : getLocationListFromDB()) {
            for (String room : lessonLocation.rooms()) {
                addCalendarColumn(lessonLocation, room);
            }
        }

        calendarColumns.widthProperty().addListener(e -> {
            for (Node node : background.getChildren()) {
                if (node instanceof Rectangle rectangle) {
                    rectangle.setWidth(calendarColumns.getWidth());
                }
            }
            columnHeadersBackground.setWidth(calendarColumns.getWidth());
        });

        enableCalendarBoxSelection();
    }

}
