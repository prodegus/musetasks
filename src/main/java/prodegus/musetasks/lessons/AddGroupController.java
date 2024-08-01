package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;
import prodegus.musetasks.workspace.cells.StringListCell;
import prodegus.musetasks.workspace.cells.StudentListCell;
import prodegus.musetasks.workspace.cells.TeacherListCellFormal;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.StudentModel.*;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.teacherStringConverterFormal;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.school.LocationModel.locationStringConverter;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.DateTime.times;
import static prodegus.musetasks.utils.DateTime.toTime;

public class AddGroupController implements Initializable {

    @FXML private GridPane gridPane;
    @FXML private Label titleTextField;
    @FXML private VBox changeDateVBox;
    @FXML private DatePicker changeDatePicker;
    @FXML private ToggleGroup aptStatusGroup;
    @FXML private RadioButton aptDraftRadioButton;
    @FXML private RadioButton aptRequestRadioButton;
    @FXML private RadioButton aptConfirmedRadioButton;
    @FXML private ToggleGroup lessonStatusGroup;
    @FXML private RadioButton lessonStatusMeet;
    @FXML private RadioButton lessonStatusTrial;
    @FXML private RadioButton lessonStatusActive;
    @FXML private TextField lessonNameTextField;

    @FXML private VBox studentBox;

    @FXML private HBox studentHBox1;
    @FXML private HBox studentHBox2;
    @FXML private HBox studentHBox3;
    @FXML private HBox studentHBox4;
    @FXML private HBox studentHBox5;
    @FXML private HBox studentHBox6;
    @FXML private HBox studentHBox7;
    @FXML private HBox studentHBox8;
    @FXML private HBox studentHBox9;
    @FXML private HBox studentHBox10;

    @FXML private ComboBox<Student> studentComboBox1;
    @FXML private ComboBox<Student> studentComboBox2;
    @FXML private ComboBox<Student> studentComboBox3;
    @FXML private ComboBox<Student> studentComboBox4;
    @FXML private ComboBox<Student> studentComboBox5;
    @FXML private ComboBox<Student> studentComboBox6;
    @FXML private ComboBox<Student> studentComboBox7;
    @FXML private ComboBox<Student> studentComboBox8;
    @FXML private ComboBox<Student> studentComboBox9;
    @FXML private ComboBox<Student> studentComboBox10;

    @FXML private Button studentDeleteButton1;
    @FXML private Button studentDeleteButton2;
    @FXML private Button studentDeleteButton3;
    @FXML private Button studentDeleteButton4;
    @FXML private Button studentDeleteButton5;
    @FXML private Button studentDeleteButton6;
    @FXML private Button studentDeleteButton7;
    @FXML private Button studentDeleteButton8;
    @FXML private Button studentDeleteButton9;
    @FXML private Button studentDeleteButton10;

    @FXML private Button addStudentButton;

    @FXML private ComboBox<Teacher> teacherComboBox;
    @FXML private ComboBox<String> instrumentComboBox;
    @FXML private ComboBox<String> durationComboBox;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private HBox hBoxApt;
    @FXML private VBox vBoxRepeat;
    @FXML private ComboBox<String> repeatComboBox;
    @FXML private VBox vBoxWeekday;
    @FXML private ComboBox<String> weekdayComboBox;
    @FXML private VBox vBoxTime;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private HBox hBoxPeriod;
    @FXML private VBox vBoxStartDate;
    @FXML private Label startDateLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private VBox vBoxEndDate;
    @FXML private Label endDateLabel;
    @FXML private DatePicker endDatePicker;

    private boolean editMode;
    private int id;
    private final ObservableList<ComboBox<Student>> studentComboBoxes = FXCollections.observableArrayList();
    private final ObservableList<HBox> studentHBoxes = FXCollections.observableArrayList();

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void confirm(ActionEvent event) {
        Lesson lesson = new Lesson();
        LocalDate changeDate = changeDatePicker.getValue();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();
        int lessonStatus = getLessonStatus();
        int aptStatus = getAppointmentStatus();
        int category = CATEGORY_GROUP;
        String lessonName = lessonNameTextField.getText();
        Student student1 = studentComboBox1.getValue();
        Student student2 = studentComboBox2.getValue();
        Student student3 = studentComboBox3.getValue();
        Student student4 = studentComboBox4.getValue();
        Student student5 = studentComboBox5.getValue();
        Student student6 = studentComboBox6.getValue();
        Student student7 = studentComboBox7.getValue();
        Student student8 = studentComboBox8.getValue();
        Student student9 = studentComboBox9.getValue();
        Student student10 = studentComboBox10.getValue();

        Teacher teacher = teacherComboBox.getValue();
        String instrument = instrumentComboBox.getValue() == null ? "Kein Instrument" : instrumentComboBox.getValue();
        String durationString = durationComboBox.getValue();
        Location location = locationComboBox.getValue();
        String room = roomComboBox.getValue();
        int repeat = repeatComboBox.getSelectionModel().getSelectedIndex();
        int weekday = weekdayComboBox.getSelectionModel().getSelectedIndex();
        String timeString = timeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (editMode) lesson.setId(id);

        if (lessonStatus == 0 || aptStatus == 0) {
            invalidData = true;
            errorMessage.append(" - Bitte Termin- und Unterrichts-Status angeben\n");
        }

        lesson.setCategory(category);

        boolean draft = aptStatus == LESSON_APT_STATUS_DRAFT;

        if (student1 == null || student2 == null) {
            invalidData = true;
            errorMessage.append("- Bitte mindestens zwei Schüler auswählen\n");
        } else {
            lesson.setStudentId1(student1.getId());
            lesson.setStudentId2(student2.getId());
        }

        if (student3 != null) lesson.setStudentId3(student3.getId());
        if (student4 != null) lesson.setStudentId4(student4.getId());
        if (student5 != null) lesson.setStudentId5(student5.getId());
        if (student6 != null) lesson.setStudentId6(student6.getId());
        if (student7 != null) lesson.setStudentId7(student7.getId());
        if (student8 != null) lesson.setStudentId8(student8.getId());
        if (student9 != null) lesson.setStudentId9(student9.getId());
        if (student10 != null) lesson.setStudentId1(student10.getId());

        if (teacher == null) {
            invalidData = !draft;
            errorMessage.append(draft ? "" : "- Bitte Lehrer auswählen\n");
        } else {
            lesson.setTeacherId(teacher.getId());
        }

        if (instrument.equals("Kein Instrument")) {
            invalidData = !draft;
            errorMessage.append(draft ? "" : "- Bitte Instrument auswählen\n");
        } else {
            lesson.setInstrument(instrument);
        }

        lesson.setDuration(Integer.parseInt(durationString.replaceAll("[^0-9]", "")));

        if (location == null) {
            invalidData = !draft;
            errorMessage.append(draft ? "" : "- Bitte Standort auswählen\n");
        } else {
            lesson.setLocationId(location.getId());
        }

        if (room == null) {
            invalidData = !draft;
            errorMessage.append(draft ? "" : "- Bitte Raum auswählen\n");
        } else {
            lesson.setRoom(room);
        }

        if (repeat < 1) {
            invalidData = true;
            errorMessage.append("- Bitte Unterrichts-Rhythmus auswählen\n");
        } else {
            lesson.setRepeat(repeat);
        }

        if (weekday < 1 && repeat != REPEAT_OFF) {
            invalidData = true;
            errorMessage.append("- Bitte Wochentag auswählen\n");
        } else {
            lesson.setWeekday(weekday);
        }

        if (timeComboBox.getSelectionModel().isEmpty()) {
            invalidData = true;
            errorMessage.append("- Bitte Uhrzeit angeben\n");
        } else {
            lesson.setTime(toTime(timeString));
        }

        if (startDate == null) {
            invalidData = !draft;
            errorMessage.append(draft ? "" : "- Bitte gültiges Start-Datum eingeben\n");
        } else {
            lesson.setStartDate(startDate);
        }

        if (endDate == null && lessonStatus != LESSON_STATUS_MEET) {
            invalidData = true;
            String regular = "- Bitte unter \"Termine eintragen bis\" angeben, bis zu welchem Datum die " +
                    "Unterrichtstermine in den Plan eingetragen werden sollen\n";
            String trial = "- Bitte Ende des Probemonats angeben\n";
            errorMessage.append(lessonStatus == LESSON_STATUS_ACTIVE ? regular : trial);
        } else {
            lesson.setEndDate(endDate);
        }

        lesson.setLessonStatus(lessonStatus);
        lesson.setAptStatus(aptStatus);

        lesson.setLessonName(lessonName.isBlank() ? lesson.createLessonName() : lessonName);

        if (collidingLesson(lesson, startDate) != null) {
            invalidData = true;
            errorMessage.append("- Raumkollision mit " + collidingLesson(lesson, startDate).getLessonName() + "\n");
        }

        if (invalidData) {
            PopupWindow.displayInformation("Unterricht konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }

        if (!editMode) {
            insertLesson(lesson);
            lesson.setId(getLastLessonID());
            insertLessonChange(new LessonChange(lesson, startDate, true));
            for (Student student : lesson.students()) {
                student.addLessonInDB(getLastLessonID());
            }
            insertLessonAppointments(getLessonFromDB(getLastLessonID()));
        } else {
            lesson.setId(id);
            LessonChange change = new LessonChange(lesson, changeDate, false);
            if (lessonChangeExists(id, changeDate)) {
                if (PopupWindow.displayYesNo("Es existiert bereits eine Änderung zu diesem Datum. Überschreiben?")) {
                    updateLessonChange(change);
                }
                else return;
            } else {
                insertLessonChange(change);
            }
            for (Student student : lesson.students()) {
                student.addLessonInDB(id);
            }
            insertLessonAppointments(lesson);
        }
        stageOf(event).close();
    }

    @FXML private void showNextStudentHBox() {
        ObservableList<HBox> activeBoxes = FXCollections.observableArrayList(studentHBoxes);
        activeBoxes.removeIf(e -> !e.isVisible());
        switch (activeBoxes.size()) {
            case 2 -> {
                studentHBox3.setManaged(true);
                studentHBox3.setVisible(true);
            }
            case 3 -> {
                studentHBox4.setManaged(true);
                studentHBox4.setVisible(true);
            }
            case 4 -> {
                studentHBox5.setManaged(true);
                studentHBox5.setVisible(true);
            }
            case 5 -> {
                studentHBox6.setManaged(true);
                studentHBox6.setVisible(true);
            }
            case 6 -> {
                studentHBox7.setManaged(true);
                studentHBox7.setVisible(true);
            }
            case 7 -> {
                studentHBox8.setManaged(true);
                studentHBox8.setVisible(true);
            }
            case 8 -> {
                studentHBox9.setManaged(true);
                studentHBox9.setVisible(true);
            }
            case 9 -> {
                studentHBox10.setManaged(true);
                studentHBox10.setVisible(true);
                addStudentButton.setDisable(true);
            }
        }
    }

    @FXML private void removeStudent(ActionEvent event) {
//        switch (event.getSource())
    }

    private int getLessonStatus() {
        if (lessonStatusGroup.getSelectedToggle().equals(lessonStatusMeet)) return LESSON_STATUS_MEET;
        if (lessonStatusGroup.getSelectedToggle().equals(lessonStatusTrial)) return LESSON_STATUS_TRIAL;
        if (lessonStatusGroup.getSelectedToggle().equals(lessonStatusActive)) return LESSON_STATUS_ACTIVE;
        return 0;
    }

    private int getAppointmentStatus() {
        if (aptStatusGroup.getSelectedToggle().equals(aptDraftRadioButton)) return LESSON_APT_STATUS_DRAFT;
        if (aptStatusGroup.getSelectedToggle().equals(aptRequestRadioButton)) return LESSON_APT_STATUS_REQUEST;
        if (aptStatusGroup.getSelectedToggle().equals(aptConfirmedRadioButton)) return LESSON_APT_STATUS_CONFIRMED;
        return 0;
    }

    private List<Button> deleteButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(studentDeleteButton1);
        buttons.add(studentDeleteButton2);
        buttons.add(studentDeleteButton3);
        buttons.add(studentDeleteButton4);
        buttons.add(studentDeleteButton5);
        buttons.add(studentDeleteButton6);
        buttons.add(studentDeleteButton7);
        buttons.add(studentDeleteButton8);
        buttons.add(studentDeleteButton9);
        buttons.add(studentDeleteButton10);
        return buttons;
    }

    private void initValues(Lesson lesson) {
        switch (lesson.getAptStatus()) {
            case LESSON_APT_STATUS_DRAFT -> aptDraftRadioButton.setSelected(true);
            case LESSON_APT_STATUS_REQUEST -> aptRequestRadioButton.setSelected(true);
            case LESSON_APT_STATUS_CONFIRMED -> aptConfirmedRadioButton.setSelected(true);
        }

        switch (lesson.getLessonStatus()) {
            case LESSON_STATUS_MEET -> lessonStatusMeet.setSelected(true);
            case LESSON_STATUS_TRIAL -> lessonStatusTrial.setSelected(true);
            case LESSON_STATUS_ACTIVE -> lessonStatusActive.setSelected(true);
        }

        lessonNameTextField.setText(lesson.getLessonName());
        studentComboBox1.setValue(getStudentFromDB(lesson.getStudentId1()));
        studentComboBox2.setValue(getStudentFromDB(lesson.getStudentId2()));
        if (lesson.getStudentId3() != 0) studentComboBox3.setValue(getStudentFromDB(lesson.getStudentId3()));
        if (lesson.getStudentId4() != 0) studentComboBox4.setValue(getStudentFromDB(lesson.getStudentId4()));
        if (lesson.getStudentId5() != 0) studentComboBox5.setValue(getStudentFromDB(lesson.getStudentId5()));
        if (lesson.getStudentId6() != 0) studentComboBox6.setValue(getStudentFromDB(lesson.getStudentId6()));
        if (lesson.getStudentId7() != 0) studentComboBox7.setValue(getStudentFromDB(lesson.getStudentId7()));
        if (lesson.getStudentId8() != 0) studentComboBox8.setValue(getStudentFromDB(lesson.getStudentId8()));
        if (lesson.getStudentId9() != 0) studentComboBox9.setValue(getStudentFromDB(lesson.getStudentId9()));
        if (lesson.getStudentId10() != 0) studentComboBox1.setValue(getStudentFromDB(lesson.getStudentId10()));
        teacherComboBox.setValue(lesson.teacher());
        instrumentComboBox.setValue(lesson.getInstrument());
        durationComboBox.setValue(lesson.durationString());
        locationComboBox.setValue(lesson.location());
        roomComboBox.setValue(lesson.getRoom());
        if (lesson.getRepeat() != REPEAT_OFF && lesson.getRepeat() != REPEAT_CUSTOM && lesson.getWeekday() != 0)
            weekdayComboBox.setValue(lesson.weekday());
        timeComboBox.setValue(lesson.getTime().toString() + " Uhr");
        startDatePicker.setValue(lesson.getStartDate());
        endDatePicker.setValue(lesson.getEndDate().equals(LocalDate.MAX) ? null : lesson.getEndDate());
    }

    public void initLesson(Lesson lesson) {
        LessonChange latestChange = getLatestLessonChange(lesson.getId(), LocalDate.now());
        Lesson futureLesson = latestChange == null ? lesson : latestChange.lesson();
        changeDateVBox.setVisible(true);
        changeDatePicker.valueProperty().addListener(e -> {
            LessonChange currentChange = getLatestLessonChange(lesson.getId(), changeDatePicker.getValue());
            if (currentChange != null) initValues(currentChange.lesson());
            startDatePicker.setValue(changeDatePicker.getValue());
        });
        startDatePicker.setDisable(true);
        changeDatePicker.setValue(LocalDate.now());
        editMode = true;
        id = futureLesson.getId();
        titleTextField.setText("Einzel-Unterricht bearbeiten");

        initValues(futureLesson);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        studentComboBoxes.addAll(studentComboBox1, studentComboBox2, studentComboBox3, studentComboBox4,
                studentComboBox5, studentComboBox6, studentComboBox7, studentComboBox8, studentComboBox9,
                studentComboBox10);
        studentHBoxes.addAll(studentHBox1, studentHBox2, studentHBox3, studentHBox4, studentHBox5, studentHBox6,
                studentHBox7, studentHBox8, studentHBox9, studentHBox10);
        changeDateVBox.setVisible(false);
        for (Button button : deleteButtons()) {
            button.setTooltip(new Tooltip("Schüler entfernen"));
        }

        lessonStatusGroup.selectedToggleProperty().addListener(e -> {
            Toggle selectedToggle = lessonStatusGroup.getSelectedToggle();
            if (selectedToggle.equals(lessonStatusMeet)) {
                repeatComboBox.getSelectionModel().select(REPEAT_OFF);
                weekdayComboBox.getSelectionModel().select(WEEKDAY_NO_SELECTION);
                startDateLabel.setText("Schnupper-Termin");
                hBoxApt.getChildren().setAll(vBoxStartDate, vBoxTime);
                hBoxPeriod.getChildren().clear();
            }
            if (selectedToggle.equals(lessonStatusTrial)) {
                repeatComboBox.getSelectionModel().select(REPEAT_WEEKLY);
                weekdayComboBox.getSelectionModel().select(editMode ? getLessonFromDB(id).getWeekday() : 0);
                startDateLabel.setText("Probemonat von");
                endDateLabel.setText("bis");
                hBoxApt.getChildren().setAll(vBoxRepeat, vBoxWeekday, vBoxTime);
                hBoxPeriod.getChildren().setAll(vBoxStartDate, vBoxEndDate);
            }
            if (selectedToggle.equals(lessonStatusActive)) {
                repeatComboBox.getSelectionModel().select(REPEAT_WEEKLY);
                weekdayComboBox.getSelectionModel().select(0);
                startDateLabel.setText("Beginn");
                endDateLabel.setText("Termine eintragen bis");
                hBoxApt.getChildren().setAll(vBoxRepeat, vBoxWeekday, vBoxTime);
                hBoxPeriod.getChildren().setAll(vBoxStartDate, vBoxEndDate);
            }
        });

        for (ComboBox<Student> comboBox : studentComboBoxes) {
            comboBox.setItems(getStudentListFromDB());
            comboBox.setButtonCell(new StudentListCell());
            comboBox.setCellFactory(student -> new StudentListCell());
            comboBox.setConverter(studentStringConverter);
        }

        studentHBox3.setVisible(false);
        studentHBox3.setManaged(false);
        studentHBox4.setVisible(false);
        studentHBox4.setManaged(false);
        studentHBox5.setVisible(false);
        studentHBox5.setManaged(false);
        studentHBox6.setVisible(false);
        studentHBox6.setManaged(false);
        studentHBox7.setVisible(false);
        studentHBox7.setManaged(false);
        studentHBox8.setVisible(false);
        studentHBox8.setManaged(false);
        studentHBox9.setVisible(false);
        studentHBox9.setManaged(false);
        studentHBox10.setVisible(false);
        studentHBox10.setManaged(false);

        teacherComboBox.setItems(getTeacherListFromDB());
        teacherComboBox.setButtonCell(new TeacherListCellFormal());
        teacherComboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        teacherComboBox.setConverter(teacherStringConverterFormal);

        instrumentComboBox.setItems(SCHOOL_INSTRUMENTS);
        instrumentComboBox.setButtonCell(new StringListCell());
        instrumentComboBox.setCellFactory(string -> new StringListCell());

        durationComboBox.setItems(FXCollections.observableArrayList("30 Minuten", "45 Minuten", "60 Minuten", "90 Minuten", "120 Minuten", "eingeben..."));
        durationComboBox.getSelectionModel().select(0);

        locationComboBox.setItems(SCHOOL_LOCATIONS);
        locationComboBox.setCellFactory(string -> new LocationListCell());
        locationComboBox.setConverter(locationStringConverter);
        locationComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> roomComboBox.setItems(FXCollections.observableArrayList(newValue.rooms())));

        repeatComboBox.setItems(FXCollections.observableArrayList("auswählen", "jede Woche", "alle 2 Wochen", "alle 3 Wochen",
                "alle 4 Wochen", "alle 5 Wochen", "alle 6 Wochen", "einmaliger Termin"));
        repeatComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            boolean repeatOff = repeatComboBox.getValue().equals("einmaliger Termin");
            weekdayComboBox.setDisable(repeatOff);
            startDateLabel.setText(repeatOff ? "Datum" : "Beginn");
            endDatePicker.setDisable(repeatOff);
        });

        weekdayComboBox.setItems(FXCollections.observableArrayList("auswählen", "Montag", "Dienstag", "Mittwoch", "Donnerstag",
                "Freitag", "Samstag", "nicht festgelegt"));

        timeComboBox.setItems(FXCollections.observableArrayList(times(8, 23)));

        repeatComboBox.getSelectionModel().select(1);
        aptDraftRadioButton.setSelected(true);
        lessonStatusMeet.setSelected(true);
    }
}
