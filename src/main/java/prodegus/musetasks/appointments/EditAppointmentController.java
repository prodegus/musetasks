package prodegus.musetasks.appointments;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.lessons.Lesson;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static prodegus.musetasks.appointments.Appointment.*;
import static prodegus.musetasks.appointments.AppointmentModel.*;
import static prodegus.musetasks.lessons.LessonModel.REPEAT_OFF;
import static prodegus.musetasks.lessons.LessonModel.getLessonFromDB;
import static prodegus.musetasks.school.LocationModel.locationStringConverter;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.DateTime.*;

public class EditAppointmentController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private Label oldDateTimeLabel;
    @FXML private Label studentNameLabel;
    @FXML private Label instrumentLabel;
    @FXML private Label locationRoomLabel;
    @FXML private Label teacherLabel;
    @FXML private Label regularAptLabel;
    @FXML private ToggleGroup cancelChangeSwitch;
    @FXML private RadioButton cancelAptRadioButton;
    @FXML private RadioButton changeAptRadioButton;
    @FXML private Label reasonLabel;
    @FXML private ComboBox<String> editReasonComboBox;
    @FXML private HBox newAptHBox;
    @FXML private DatePicker newDatePicker;
    @FXML private TextField newTimeHoursTextField;
    @FXML private TextField newTimeMinTextField;
    @FXML private ComboBox<String> newDurationComboBox;
    @FXML private VBox newRoomVBox;
    @FXML private ComboBox<Location> newLocationComboBox;
    @FXML private ComboBox<String> newRoomComboBox;
    @FXML private CheckBox informNoneCheckBox;
    @FXML private CheckBox informParentsCheckBox;
    @FXML private CheckBox informStudentsCheckBox;
    @FXML private CheckBox informTeacherCheckBox;
    @FXML private CheckBox informAllCheckBox;

    public Lesson lesson;
    public Appointment appointmentToEdit;

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void cancelAptChecked(ActionEvent event) {
        newAptHBox.setDisable(true);
        newRoomVBox.setDisable(true);
        reasonLabel.setText("Grund der Absage:");
        editReasonComboBox.setItems(FXCollections.observableArrayList("Kurzfristige Absage (Schüler)",
                "Krankheit (Schüler)", "Krankheit (Lehrer)", "Sonstige Gründe (Schüler)", "Sonstige Gründe (Lehrer)"));
    }

    @FXML
    void changeAptChecked(ActionEvent event) {
        newAptHBox.setDisable(false);
        newRoomVBox.setDisable(false);
        reasonLabel.setText("Grund der Änderung:");
        editReasonComboBox.setItems(FXCollections.observableArrayList("Kurzfristige Änderung", "Krankheit (Schüler)",
                "Krankheit (Lehrer)", "Sonstige Gründe (Schüler)", "Sonstige Gründe (Lehrer)", "Raumwechsel"));
    }

    @FXML
    void confirm(ActionEvent event) {
        Appointment oldAppointment = appointmentToEdit;
        Appointment newAppointment = new Appointment();
        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();

        boolean cancelled = cancelAptRadioButton.isSelected();
        String reason = editReasonComboBox.getValue();
        LocalDate date = newDatePicker.getValue();
        LocalTime time = LocalTime.of(Integer.parseInt(newTimeHoursTextField.getText()),
                Integer.parseInt(newTimeMinTextField.getText()));
        String durationString = newDurationComboBox.getValue();
        Location location = newLocationComboBox.getValue();
        String room = newRoomComboBox.getValue();
        boolean informParents = informParentsCheckBox.isSelected();
        boolean informStudents = informStudentsCheckBox.isSelected();
        boolean informTeacher = informTeacherCheckBox.isSelected();
        boolean changeOnly = false;

        newAppointment.setCategory(CATEGORY_LESSON_RESCHEDULED);
        newAppointment.setStatus(STATUS_OK);

        switch (reason) {
            case "Kurzfristige Absage (Schüler)" -> oldAppointment.setStatus(STATUS_DROPPED);
            case "Kurzfristige Änderung", "Raumwechsel" -> {
                newAppointment.setCategory(CATEGORY_LESSON_REGULAR);
                newAppointment.setStatus(STATUS_CHANGED);
                changeOnly = true;
            }
            case "Krankheit (Schüler)", "Sonstige Gründe (Schüler)" ->
                    oldAppointment.setStatus(cancelled ? STATUS_CANCELLED_STUDENT : STATUS_RESCHEDULED);
            case "Krankheit (Lehrer)", "Sonstige Gründe (Lehrer)" ->
                    oldAppointment.setStatus(cancelled ? STATUS_CANCELLED_TEACHER : STATUS_RESCHEDULED);
        }

        if (invalidDateInput(newDatePicker)) {
            invalidData = true;
            errorMessage.append("- Bitte gültiges Datum eingeben (z.B. 01.01.2000)\n");
        } else {
            newAppointment.setDate(date);
        }

        newAppointment.setTime(time);
        newAppointment.setLocationId(location == null ? 0 : location.getId());
        newAppointment.setRoom(room == null ? "" : room);
        newAppointment.setDuration(Integer.parseInt(durationString.replaceAll("[^0-9]", "")));
        newAppointment.setLessonId(appointmentToEdit.getLessonId());
        newAppointment.setDateOld(appointmentToEdit.getDate());
        System.out.println("appointmentToEdit.getDate(): " + appointmentToEdit.getDate());

        if (invalidData) {
            PopupWindow.displayInformation("Änderung konnte nicht durchgeführt werden:\n\n" + errorMessage);
            return;
        }

        if (!changeOnly) {
            StringBuilder descriptionOld = new StringBuilder(reason);
            oldAppointment.setDateOld(oldAppointment.getDate());
            if (oldAppointment.getStatus() == STATUS_RESCHEDULED)
                descriptionOld.append(", Nachholtermin: ").append(asString(date)).append(", ").append(asString(time));
            if (oldAppointment.getStatus() == STATUS_DROPPED)
                descriptionOld.append(", wird nicht nachgeholt");
            oldAppointment.setDescription(descriptionOld.toString());

            if (oldAppointment.getId() != 0) {
                update(oldAppointment, oldAppointment.getId());
                System.out.println("update oldAppointment");
            } else {
                insert(oldAppointment);
                System.out.println("insert oldAppointment");
            }
        }
        if (!cancelled) {
            newAppointment.setDescription(newAppointment.getCategory() == CATEGORY_LESSON_REGULAR ? reason :
                    "Nachholtermin für " + asString(newAppointment.getDateOld()));
            System.out.println("insert newAppointment");
            insert(newAppointment);
        }
        stageOf(event).close();

    }

    public void initAppointment(Appointment appointment) {
        appointmentToEdit = appointment;
        lesson = getLessonFromDB(appointment.getLessonId());
        LocalDate oldDate = appointment.getDate();
        LocalTime oldTime = appointment.getTime();
        oldDateTimeLabel.setText(fullDateTimeString(oldDate, oldTime));
        studentNameLabel.setText(lesson.studentsNamesString());
        instrumentLabel.setText(lesson.getInstrument());
        locationRoomLabel.setText(lesson.locationRoom());
        teacherLabel.setText(lesson.teacher().name());
        regularAptLabel.setText(lesson.getRepeat() == REPEAT_OFF ? "nicht festgelegt" : lesson.regularAppointment());
        newDatePicker.setValue(appointment.getDate());
        newTimeHoursTextField.setText(String.valueOf(oldTime.getHour()));
        newTimeMinTextField.setText((oldTime.getMinute() < 10 ? "0" : "") + oldTime.getMinute());
        newDurationComboBox.setValue(switch(appointment.getDuration()) {
            case 30 -> "30 Minuten";
            case 45 -> "45 Minuten";
            case 60 -> "60 Minuten";
            case 90 -> "90 Minuten";
            case 120 -> "120 Minuten";
            default -> "auswählen";
        });
        if (appointment.getLocationId() != 0) {
            newLocationComboBox.setValue(appointment.location());
            newRoomComboBox.setItems(FXCollections.observableArrayList(appointment.location().rooms()));
        }
        if (!appointment.getRoom().isBlank()) newRoomComboBox.setValue(appointment.getRoom());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeAptRadioButton.setSelected(true);
        newAptHBox.setVisible(true);
        editReasonComboBox.setItems(FXCollections.observableArrayList("Kurzfristige Änderung", "Krankheit (Schüler)",
                "Krankheit (Lehrer)", "Sonstige Gründe (Schüler)", "Sonstige Gründe (Lehrer)"));

        newTimeHoursTextField.focusedProperty().addListener((obs, oldValue, newValue) -> Platform.runLater(() -> {
            if (newTimeHoursTextField.isFocused() && !newTimeHoursTextField.getText().isEmpty())
                newTimeHoursTextField.selectAll();
            if (!newTimeHoursTextField.isFocused() && newTimeHoursTextField.getText().isBlank())
                newTimeHoursTextField.setText("0");
        }));

        newTimeHoursTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isBlank() && Integer.parseInt(newValue) > 23) newTimeHoursTextField.setText(String.valueOf(23));
            if (newValue.length() > 2) newTimeHoursTextField.setText(newValue.substring(0, 2));
        });
        
        newTimeMinTextField.focusedProperty().addListener((obs, oldValue, newValue) -> Platform.runLater(() -> {
            if (newTimeMinTextField.isFocused() && !newTimeMinTextField.getText().isEmpty())
                newTimeMinTextField.selectAll();
            switch (newTimeMinTextField.getText().length()) {
                case 0 -> newTimeMinTextField.setText("00");
                case 1 -> newTimeMinTextField.setText("0" + newTimeMinTextField.getText());
            }
        }));

        newTimeMinTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue.isBlank() && Integer.parseInt(newValue) > 59) newTimeMinTextField.setText(String.valueOf(59));
            if (newValue.length() > 2) newTimeMinTextField.setText(newValue.substring(0, 2));
        });

        newDurationComboBox.setItems(FXCollections.observableArrayList("30 Minuten", "45 Minuten", "60 Minuten",
                "90 Minuten", "120 Minuten", "auswählen"));

        newLocationComboBox.setItems(SCHOOL_LOCATIONS);
        newLocationComboBox.setCellFactory(string -> new LocationListCell());
        newLocationComboBox.setConverter(locationStringConverter);
        newLocationComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> newRoomComboBox.setItems(FXCollections.observableArrayList(newValue.rooms())));

        informNoneCheckBox.setOnAction(event -> {
            if (informNoneCheckBox.isSelected()) {
                informParentsCheckBox.setSelected(false);
                informStudentsCheckBox.setSelected(false);
                informTeacherCheckBox.setSelected(false);
                informAllCheckBox.setSelected(false);
            }
        });
        informAllCheckBox.setOnAction(event -> {
            if (informAllCheckBox.isSelected()) {
                informNoneCheckBox.setSelected(false);
                informParentsCheckBox.setSelected(true);
                informStudentsCheckBox.setSelected(true);
                informTeacherCheckBox.setSelected(true);
            }
        });
        informParentsCheckBox.setOnAction(event -> informNoneCheckBox.setSelected(false));
        informStudentsCheckBox.setOnAction(event -> informNoneCheckBox.setSelected(false));
        informTeacherCheckBox.setOnAction(event -> informNoneCheckBox.setSelected(false));
    }
}
