package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;
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
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import static prodegus.musetasks.appointments.AppointmentModel.getLessonAppointmentsFromDB;
import static prodegus.musetasks.contacts.StudentModel.*;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.teacherStringConverterFormal;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.school.LocationModel.locationStringConverter;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.Nodes.hide;
import static prodegus.musetasks.utils.Nodes.show;

public class AddSingleController implements Initializable {

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
    @FXML private Button editCustomAptsButton;
    @FXML private HBox hBoxPeriod;
    @FXML private VBox vBoxStartDate;
    @FXML private Label startDateLabel;
    @FXML private DatePicker startDatePicker;
    @FXML private VBox vBoxEndDate;
    @FXML private Label endDateLabel;
    @FXML private DatePicker endDatePicker;

    private boolean editMode;
    private int id;
    private final ObservableList<Appointment> customAppointments = FXCollections.observableArrayList();

    @FXML
    void editCustomApts(ActionEvent event) {
        Lesson lesson = fromInputValues();
        if (lesson == null) return;
        List<Appointment> newAppointments = CustomAptWindow.customAppointments(lesson);
        if (newAppointments != null) customAppointments.setAll(newAppointments);
    }

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    Lesson fromInputValues() {
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
        LocalDate endDate = repeat == REPEAT_OFF ? startDate : endDatePicker.getValue();

        if (editMode) lesson.setId(id);

        if (lessonStatus == 0 || aptStatus == 0) {
            invalidData = true;
            errorMessage.append(" - Bitte Termin- und Unterrichts-Status angeben\n");
        }

        lesson.setCategory(category);

        boolean draft = aptStatus == LESSON_APT_STATUS_DRAFT;

        if (student == null) {
            invalidData = true;
            errorMessage.append("- Bitte Schüler auswählen\n");
        } else {
            lesson.setStudentId1(student.getId());
            if (student.getContactEmail().isBlank()) {
                invalidData = true;
                errorMessage.append("- Schüler " + student.name() + "besitzt keine Kontakt-E-Mail-Adresse\n");
            } else {
                lesson.setStudentId1(student.getId());
            }
        }


        if (teacher == null) {
            invalidData = true;
            errorMessage.append("- Bitte Lehrer auswählen\n");
        } else {
            lesson.setTeacherId(teacher.getId());
        }

        if (instrument.equals("Kein Instrument")) {
            invalidData = true;
            errorMessage.append("- Bitte Instrument auswählen\n");
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

        if (startDate == null && repeat != REPEAT_CUSTOM) {
            invalidData = !draft;
            errorMessage.append(draft ? "" : "- Bitte gültiges Start-Datum eingeben\n");
        } else {
            lesson.setStartDate(startDate);
        }

        if (repeat != REPEAT_OFF && repeat != REPEAT_CUSTOM) {
            if (weekday < 1) {
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

            if (endDate == null) {
                invalidData = true;
                String regular = "- Bitte unter \"Termine eintragen bis\" angeben, bis zu welchem Datum die " +
                        "Unterrichtstermine in den Plan eingetragen werden sollen\n";
                String trial = "- Bitte Ende des Probemonats angeben\n";
                errorMessage.append(lessonStatus == LESSON_STATUS_ACTIVE ? regular : trial);
            } else {
                lesson.setEndDate(endDate);
            }
        }

        lesson.setLessonStatus(lessonStatus);
        lesson.setAptStatus(aptStatus);

        lesson.setLessonName(lessonName.isBlank() ? lesson.createLessonName() : lessonName);

        if (invalidData) {
            PopupWindow.displayInformation("Fehlende Angaben:\n\n" + errorMessage);
            return null;
        }

        return lesson;
    }

    @FXML
    void confirm(ActionEvent event) {
        Lesson lesson = fromInputValues();
        if (lesson == null) return;

        LocalDate startDate = startDatePicker.getValue();
        Student student = studentComboBox.getValue();
        LocalDate changeDate = changeDatePicker.getValue();

        if (customAppointments.size() > 0) {
            startDate = customAppointments.sorted().get(0).getDate();
            lesson.setStartDate(startDate);
            lesson.setTime(toTime(2359));
        } else if (lesson.getRepeat() == REPEAT_CUSTOM && lesson.getAptStatus() != LESSON_APT_STATUS_DRAFT) {
            PopupWindow.displayInformation("Unterricht konnte nicht angelegt werden:\n\n" +
                    "- Bitte mindestens einen Termin eintragen");
            return;
        }

        if (collidingLesson(lesson, startDate) != null) {
            PopupWindow.displayInformation("Unterricht konnte nicht angelegt werden: \n\n" +
                    "- Raumkollision mit: " + collidingLesson(lesson, lesson.getStartDate()).getLessonName() + "\n");
            return;
        }

        if (!editMode) {
            insertLesson(lesson);
            if (lesson.getAptStatus() == LESSON_APT_STATUS_DRAFT) {
                stageOf(event).close();
                return;
            }
            lesson.setId(getLastLessonID());
            insertLessonChange(new LessonChange(lesson, startDate, true));
            student.addLessonInDB(getLastLessonID());
            insertLessonAppointments(getLessonFromDB(getLastLessonID()), customAppointments);
        } else {
            lesson.setId(id);
            if (realChanges(lesson, getLatestLessonChange(id, changeDate))) {
                if (lessonChangeExists(id, changeDate)) {
                    if (PopupWindow.displayYesNo("Es existiert bereits eine Änderung zu diesem Datum. Überschreiben?"))
                        updateLessonChange(new LessonChange(lesson, changeDate, false));
                    else return;
                } else {
                    insertLessonChange(new LessonChange(lesson, changeDate, false));
                }
            }
            if (lesson.getAptStatus() == LESSON_APT_STATUS_DRAFT) {
                stageOf(event).close();
                return;
            }

            student.addLessonInDB(id);
            insertLessonAppointments(lesson, customAppointments, changeDate);
        }

        stageOf(event).close();
    }

    private int getLessonStatus() {
        if (lessonStatusGroup.getSelectedToggle() == null) return 0;
        if (lessonStatusGroup.getSelectedToggle().equals(lessonStatusMeet)) return LESSON_STATUS_MEET;
        if (lessonStatusGroup.getSelectedToggle().equals(lessonStatusTrial)) return LESSON_STATUS_TRIAL;
        if (lessonStatusGroup.getSelectedToggle().equals(lessonStatusActive)) return LESSON_STATUS_ACTIVE;
        return 0;
    }

    private int getAppointmentStatus() {
        if (aptStatusGroup.getSelectedToggle() == null) return 0;
        if (aptStatusGroup.getSelectedToggle().equals(aptDraftRadioButton)) return LESSON_APT_STATUS_DRAFT;
        if (aptStatusGroup.getSelectedToggle().equals(aptRequestRadioButton)) return LESSON_APT_STATUS_REQUEST;
        if (aptStatusGroup.getSelectedToggle().equals(aptConfirmedRadioButton)) return LESSON_APT_STATUS_CONFIRMED;
        return 0;
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
        studentComboBox.setValue(getStudentFromDB(lesson.getStudentId1()));
        teacherComboBox.setValue(lesson.teacher());
        instrumentComboBox.setValue(lesson.getInstrument());
        durationComboBox.setValue(lesson.durationString());
        locationComboBox.setValue(lesson.location());
        roomComboBox.setValue(lesson.getRoom());
        repeatComboBox.getSelectionModel().select(lesson.getRepeat());
        if (lesson.getRepeat() != REPEAT_OFF && lesson.getRepeat() != REPEAT_CUSTOM && lesson.getWeekday() != 0)
            weekdayComboBox.setValue(lesson.weekday());
        timeComboBox.setValue(lesson.getTime().toString() + " Uhr");
        startDatePicker.setValue(lesson.getStartDate());
        endDatePicker.setValue(lesson.getEndDate().equals(LocalDate.MAX) ? null : lesson.getEndDate());
    }

    public void initLesson(Lesson lesson) {
        LessonChange latestChange = getLatestLessonChange(lesson.getId());
        Lesson futureLesson = latestChange == null ? lesson : latestChange.lesson();
        show(changeDateVBox);
        changeDatePicker.valueProperty().addListener(e -> {
            LessonChange currentChange = getLatestLessonChange(lesson.getId(), changeDatePicker.getValue());
            if (currentChange != null) initValues(currentChange.lesson());
        });
        if (getLessonChangeListFromDB(lesson.getId()).size() > 1) startDatePicker.setDisable(true);
        gridPane.getRowConstraints().set(1, new RowConstraints(60));
        gridPane.getRowConstraints().set(10, new RowConstraints(10));
        changeDatePicker.setValue(latestChange == null ? lesson.getStartDate() : latestChange.getChangeDate());
        editMode = true;
        id = futureLesson.getId();
        titleTextField.setText("Einzel-Unterricht bearbeiten");

        initValues(futureLesson);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hide(changeDateVBox);
        gridPane.getRowConstraints().set(1, new RowConstraints(10));
        gridPane.getRowConstraints().set(10, new RowConstraints(60));

        lessonStatusGroup.selectedToggleProperty().addListener(e -> {
            Toggle selectedToggle = lessonStatusGroup.getSelectedToggle();
            if (selectedToggle.equals(lessonStatusMeet)) {
                repeatComboBox.getSelectionModel().select(REPEAT_OFF);
                hBoxApt.getChildren().setAll(vBoxStartDate, vBoxTime);
                startDateLabel.setText("Schnupper-Termin");
            }
            if (selectedToggle.equals(lessonStatusTrial)) {
                if (repeatComboBox.getSelectionModel().getSelectedIndex() == REPEAT_OFF) {
                    repeatComboBox.getSelectionModel().select(REPEAT_WEEKLY);
                }
                startDateLabel.setText("Probemonat von");
                endDateLabel.setText("bis");
            }
            if (selectedToggle.equals(lessonStatusActive)) {
                if (repeatComboBox.getSelectionModel().getSelectedIndex() == REPEAT_OFF) {
                    repeatComboBox.getSelectionModel().select(REPEAT_WEEKLY);
                }
                startDateLabel.setText("Beginn");
                endDateLabel.setText("Termine eintragen bis");
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
                "alle 4 Wochen", "alle 5 Wochen", "alle 6 Wochen", "einmaliger Termin", "individuelle Termine"));
        repeatComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (repeatComboBox.getSelectionModel().getSelectedIndex()) {
                case REPEAT_OFF -> {
                    hBoxApt.getChildren().setAll(vBoxRepeat, vBoxStartDate, vBoxTime);
                    weekdayComboBox.getSelectionModel().select(WEEKDAY_NO_SELECTION);
                    hBoxPeriod.getChildren().clear();
                    startDateLabel.setText("Termin");
                }
                case REPEAT_CUSTOM -> {
                    hBoxApt.getChildren().setAll(vBoxRepeat, editCustomAptsButton);
                    hBoxPeriod.getChildren().clear();
                }
                default -> {
                    hBoxApt.getChildren().setAll(vBoxRepeat, vBoxWeekday, vBoxTime);
                    hBoxPeriod.getChildren().setAll(vBoxStartDate, vBoxEndDate);
                }
            }
        });

        weekdayComboBox.setItems(FXCollections.observableArrayList("auswählen", "Montag", "Dienstag", "Mittwoch", "Donnerstag",
                "Freitag", "Samstag", "nicht festgelegt"));

        timeComboBox.setItems(FXCollections.observableArrayList(times(8, 23)));

        repeatComboBox.getSelectionModel().select(1);
    }
}
