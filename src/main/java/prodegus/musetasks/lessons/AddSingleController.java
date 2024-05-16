package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.workspace.cells.StringListCell;
import prodegus.musetasks.workspace.cells.StudentListCell;
import prodegus.musetasks.workspace.cells.TeacherListCellFormal;

import java.net.URL;
import java.util.ResourceBundle;

import static prodegus.musetasks.contacts.StudentModel.getStudentListFromDB;
import static prodegus.musetasks.contacts.StudentModel.studentStringConverter;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.teacherStringConverterFormal;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;
import static prodegus.musetasks.ui.StageFactories.stageOf;

public class AddSingleController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private CheckBox draftCheckBox;
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private ComboBox<Teacher> teacherComboBox;
    @FXML private ComboBox<String> instrumentComboBox;
    @FXML private ComboBox<String> durationComboBox;
    @FXML private ComboBox<String> locationComboBox;
    @FXML private ComboBox<String> roomComboBox;
    @FXML private CheckBox repeatCheckBox;
    @FXML private ComboBox<String> repeatInterComboBox;
    @FXML private ComboBox<String> repeatPeriodComboBox;
    @FXML private ComboBox<String> weekdayComboBox;
    @FXML private ComboBox<String> timeComboBox;
    @FXML private RadioButton repeatEndRadioButton;
    @FXML private DatePicker repeatEndDatePicker;
    @FXML private RadioButton repeatTimesRadioButton;
    @FXML private ComboBox<String> repeatTimesComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private CheckBox endDateCheckBox;
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
    void submit(ActionEvent event) {
        Lesson lesson = new Lesson();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();

        boolean draft = draftCheckBox.isSelected();
        Student student = studentComboBox.getValue();
        Teacher teacher = teacherComboBox.getValue();
        String instrument = instrumentComboBox.getValue();
        String duration = durationComboBox.getValue();
        String location = locationComboBox.getValue();
        String room = roomComboBox.getValue();
        boolean repeatSelected = repeatCheckBox.isSelected();
        int repeatInter = repeatInterFromString(repeatInterComboBox.getValue());
        int repeatPeriod = repeatPeriodFromString(repeatPeriodComboBox.getValue());
        int weekday = weekdayFromString(weekdayComboBox.getValue());
        String time = timeComboBox.getValue();
        boolean repeatEndSelected = repeatEndRadioButton.isSelected();
        String repeatEnd = repeatEndDatePicker.getValue().toString();
        boolean repeatTimesSelected = repeatTimesRadioButton.isSelected();
        int repeatTimes = Integer.parseInt(String.valueOf(repeatTimesComboBox.getValue().charAt(0)));
        String startDate = startDatePicker.getValue().toString();
        boolean endDateSelected = endDateCheckBox.isSelected();
        String endDate = endDatePicker.getValue().toString();
        String status = statusComboBox.getValue();
        String statusFromDate = statusFromDatePicker.getValue().toString();
        String statusToDate = statusToDatePicker.getValue().toString();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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

        repeatCheckBox.selectedProperty().addListener(selected -> {
            repeatInterComboBox.setDisable(!repeatCheckBox.isSelected());
            repeatPeriodComboBox.setDisable(!repeatCheckBox.isSelected());
            weekdayComboBox.setDisable(!repeatCheckBox.isSelected());
            timeComboBox.setDisable(!repeatCheckBox.isSelected());
            repeatEndRadioButton.setDisable(!repeatCheckBox.isSelected());
            repeatEndDatePicker.setDisable(!repeatCheckBox.isSelected());
            repeatTimesRadioButton.setDisable(!repeatCheckBox.isSelected());
            repeatTimesComboBox.setDisable(!repeatCheckBox.isSelected());
        });

        repeatInterComboBox.setItems(REPEAT_INTERVALS_WEEK);
        repeatInterComboBox.getSelectionModel().select(0);
        repeatInterComboBox.setCellFactory(string -> new StringListCell());

        repeatPeriodComboBox.setItems(FXCollections.observableArrayList("Tag", "Woche", "Monat", "Jahr"));
        repeatPeriodComboBox.getSelectionModel().selectedItemProperty().addListener(item -> {
            switch (repeatPeriodComboBox.getValue()) {
                case "Tag" -> repeatInterComboBox.setItems(REPEAT_INTERVALS_DAY);
                case "Monat" -> repeatInterComboBox.setItems(REPEAT_INTERVALS_MONTH);
                case "Jahr" -> repeatInterComboBox.setItems(REPEAT_INTERVALS_YEAR);
                default -> repeatInterComboBox.setItems(REPEAT_INTERVALS_WEEK);
            }
        });

        weekdayComboBox.setItems(FXCollections.observableArrayList("Montag", "Dienstag", "Mittwoch", "Donnerstag",
                "Freitag", "Samstag"));

        repeatTimesComboBox.setItems(FXCollections.observableArrayList("2 mal", "3 mal", "4 mal", "5 mal", "6 mal",
                "7 mal", "8 mal", "9 mal", "10 mal", "11 mal", "12 mal"));
    }
}
