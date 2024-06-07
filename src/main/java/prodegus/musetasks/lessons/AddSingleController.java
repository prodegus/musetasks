package prodegus.musetasks.lessons;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.StudentModel.getStudentListFromDB;
import static prodegus.musetasks.contacts.StudentModel.studentStringConverter;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.teacherStringConverterFormal;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.school.LocationModel.fromString;
import static prodegus.musetasks.school.LocationModel.locationStringConverter;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.DateTime.times;
import static prodegus.musetasks.utils.DateTime.toTime;

public class AddSingleController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private CheckBox draftCheckBox;
    @FXML private TextField lessonNameTextField;
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Teacher> teacherComboBox;
    @FXML private ComboBox<String> instrumentComboBox;
    @FXML private ComboBox<String> durationComboBox;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private ComboBox<String> repeatComboBox;
    @FXML private ComboBox<String> weekdayComboBox;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private DatePicker startDatePicker;
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

        boolean draft = draftCheckBox.isSelected();
        int category = CATEGORY_SINGLE;
        String lessonName = lessonNameTextField.getText();
        Student student = studentComboBox.getValue();
        Teacher teacher = teacherComboBox.getValue();
        String instrument = instrumentComboBox.getValue() == null ? "Kein Instrument" : instrumentComboBox.getValue();
        String durationString = durationComboBox.getValue();
        Location location = locationComboBox.getValue();
        String room = roomComboBox.getValue();
        int repeat = repeatComboBox.getSelectionModel().getSelectedIndex();
        System.out.println("repeatComboBox.getSelectionModel().getSelectedIndex(): " + repeatComboBox.getSelectionModel().getSelectedIndex());
        System.out.println("repeatStringFromInt(repeat): " + repeatStringFromInt(repeat));
        int weekday = weekdayComboBox.getSelectionModel().getSelectedIndex();
        String timeString = timeComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        int status = statusComboBox.getSelectionModel().getSelectedIndex();
        LocalDate statusFrom = statusFromDatePicker.getValue();
        LocalDate statusTo = statusToDatePicker.getValue();

        lesson.setLessonName(lessonName);
        lesson.setCategory(category);

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

        lesson.setRepeat(repeat);

        if (weekday < 0) {
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

        lesson.setEndDate(endDate == null ? LocalDate.MAX : endDate);
        lesson.setStatus(status);
        lesson.setStatusFrom(statusFrom == null ? LocalDate.MIN : statusFrom);
        lesson.setStatusTo(statusTo == null ? LocalDate.MAX : statusTo);

        if (invalidData) {
            PopupWindow.displayInformation("Unterricht konnte nicht angelegt werden: \n\n" + errorMessage);
            return;
        }

        if (!editMode) {
            insertLesson(lesson);
            student.addLessonInDB(getLastLessonID());
            student.addTeacherInDB(teacher.getId());
        } else {
            updateLesson(lesson, id);
            student.addLessonInDB(id);
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        draftCheckBox.setSelected(false);

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

        weekdayComboBox.setItems(FXCollections.observableArrayList("unbekannt", "Montag", "Dienstag", "Mittwoch", "Donnerstag",
                "Freitag", "Samstag"));

        timeComboBox.setItems(FXCollections.observableArrayList(times(8, 23)));

    }
}
