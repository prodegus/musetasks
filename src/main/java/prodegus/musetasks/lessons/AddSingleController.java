package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;
import prodegus.musetasks.workspace.cells.StringListCell;
import prodegus.musetasks.workspace.cells.StudentListCell;
import prodegus.musetasks.workspace.cells.TeacherListCellFormal;

import java.net.URL;
import java.time.LocalDate;
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

public class AddSingleController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private ToggleGroup aptStatusGroup;
    @FXML private RadioButton aptDraftRadioButton;
    @FXML private RadioButton aptRequestRadioButton;
    @FXML private RadioButton aptConfirmedRadioButton;
    @FXML private ToggleGroup lessonStatusGroup;
    @FXML private RadioButton lessonStatusMeet;
    @FXML private RadioButton lessonStatusTrial;
    @FXML private RadioButton lessonStatusActive;
    @FXML private TextField lessonNameTextField;
    @FXML private ComboBox<Student> studentComboBox;
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
    @FXML private ComboBox<String> statusComboBox;
    @FXML private DatePicker statusFromDatePicker;
    @FXML private DatePicker statusToDatePicker;

    private boolean editMode;
    private int id;


    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void confirm(ActionEvent event) {
        Lesson lesson = new Lesson();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();
        int lessonStatus = getLessonStatus();
        int aptStatus = getAppointmentStatus();
        int category = CATEGORY_SINGLE;
        String lessonName = lessonNameTextField.getText();
        Student student = studentComboBox.getValue();
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
        System.out.println("endDatePicker.getValue(): " + endDatePicker.getValue());

        lesson.setLessonName(lessonName);
        lesson.setCategory(category);

        boolean draft = aptStatus == LESSON_APT_STATUS_DRAFT;

        if (student == null) {
            invalidData = true;
            errorMessage.append("- Bitte Schüler auswählen\n");
        } else {
            lesson.setStudentId1(student.getId());
        }

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

        if (endDate == null && getLessonStatus() == LESSON_STATUS_TRIAL) {
            invalidData = true;
            errorMessage.append("- Bitte End-Datum für den Probemonat angeben");
        } else {
            lesson.setEndDate(endDate == null ? LocalDate.MAX : endDate);
        }

        lesson.setLessonStatus(lessonStatus);
        lesson.setAptStatus(aptStatus);

        if (invalidData) {
            PopupWindow.displayInformation("Unterricht konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }

        if (!editMode) {
            insertLesson(lesson);
            student.addLessonInDB(getLastLessonID());
            student.addTeacherInDB(teacher.getId());
            insertLessonAppointments(getLastLessonID());

        } else {
            updateLesson(lesson, id);
            student.addLessonInDB(id);
            student.addTeacherInDB(teacher.getId());
            insertLessonAppointments(id);
        }
        stageOf(event).close();
    }

    private String createLessonName(String studentName, String instrument, String durationMinutes, boolean draft) {
        if (!lessonNameTextField.getText().isBlank()) return lessonNameTextField.getText();
        StringBuilder lessonName = new StringBuilder();
        if (draft) lessonName.append("[Entwurf] ");
        lessonName.append(studentName);
        lessonName.append(" (");
        lessonName.append(String.join(", ", instrument, "Einzel", durationMinutes + "min)"));
        return lessonName.toString();
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

    public void initLesson(Lesson lesson) {
        editMode = true;
        id = lesson.getId();
        titleTextField.setText("Einzel-Unterricht bearbeiten");

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
        studentComboBox.setValue(getStudentFromDB(lesson.getStudentId1()));
        teacherComboBox.setValue(lesson.teacher());
        instrumentComboBox.setValue(lesson.getInstrument());
        durationComboBox.setValue(lesson.durationString());
        locationComboBox.setValue(lesson.location());
        roomComboBox.setValue(lesson.getRoom());
        if (lesson.getRepeat() != REPEAT_OFF && lesson.getRepeat() != REPEAT_CUSTOM && lesson.getWeekday() != 0)
            weekdayComboBox.setValue(lesson.weekday());
        timeComboBox.setValue(lesson.getTime().toString() + " Uhr");
        startDatePicker.setValue(lesson.getStartDate());
        if (!lesson.getEndDate().equals(LocalDate.MAX)) endDatePicker.setValue(lesson.getEndDate());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
                startDateLabel.setText("Probemonat von:");
                endDateLabel.setText("bis:");
                hBoxApt.getChildren().setAll(vBoxRepeat, vBoxWeekday, vBoxTime);
                hBoxPeriod.getChildren().setAll(vBoxStartDate, vBoxEndDate);
            }
            if (selectedToggle.equals(lessonStatusActive)) {
                repeatComboBox.getSelectionModel().select(REPEAT_WEEKLY);
                weekdayComboBox.getSelectionModel().select(0);
                startDateLabel.setText("Beginn:");
                endDateLabel.setText("Ende (optional):");
                hBoxApt.getChildren().setAll(vBoxRepeat, vBoxWeekday, vBoxTime);
                hBoxPeriod.getChildren().setAll(vBoxStartDate, vBoxEndDate);
            }
        });

        studentComboBox.setItems(getStudentListFromDB());
        studentComboBox.setButtonCell(new StudentListCell());
        studentComboBox.setCellFactory(student -> new StudentListCell());
        studentComboBox.setConverter(studentStringConverter);

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

        statusComboBox.setItems(FXCollections.observableArrayList(LESSON_STATUS_LIST));

        repeatComboBox.getSelectionModel().select(1);
        aptDraftRadioButton.setSelected(true);
        lessonStatusMeet.setSelected(true);
    }
}
