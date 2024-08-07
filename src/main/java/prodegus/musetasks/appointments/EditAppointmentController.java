package prodegus.musetasks.appointments;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.lessons.Lesson;
import prodegus.musetasks.mail.NewMailController;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.ui.popup.PopupWindow;
import prodegus.musetasks.workspace.cells.LocationListCell;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static prodegus.musetasks.appointments.Appointment.*;
import static prodegus.musetasks.appointments.AppointmentModel.*;
import static prodegus.musetasks.contacts.ParentModel.getParentFromDB;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherFromDB;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.mail.EmailUtil.errorMail;
import static prodegus.musetasks.mail.TLSEmail.sendMail;
import static prodegus.musetasks.mail.Templates.*;
import static prodegus.musetasks.school.LocationModel.locationStringConverter;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;
import static prodegus.musetasks.ui.StageFactories.newStage;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.DateTime.*;

public class EditAppointmentController implements Initializable {

    @FXML private Label titleTextField;
    @FXML private Label oldDateTimeLabel;
    @FXML private Label lessonCategoryLabel;
    @FXML private Label studentNameLabel;
    @FXML private Label instrumentLabel;
    @FXML private Label locationRoomLabel;
    @FXML private Label teacherLabel;
    @FXML private Label regularAptLabel;
    @FXML private Label reasonLabel;
    @FXML private ComboBox<String> editReasonComboBox;
    @FXML private CheckBox rescheduleCheckBox;
    @FXML private HBox newAptHBox;
    @FXML private DatePicker newDatePicker;
    @FXML private ComboBox<String> newTimeComboBox;
    @FXML private ComboBox<String> newDurationComboBox;
    @FXML private VBox newRoomVBox;
    @FXML private ComboBox<Location> newLocationComboBox;
    @FXML private ComboBox<String> newRoomComboBox;
    @FXML private CheckBox informNoneCheckBox;
    @FXML private CheckBox informContactCheckBox;
    @FXML private CheckBox informParentsCheckBox;
    @FXML private CheckBox informStudentsCheckBox;
    @FXML private CheckBox informTeacherCheckBox;
    @FXML private CheckBox informAllCheckBox;

    public static final int EDIT_MODE_DROPPED = 1;
    public static final int EDIT_MODE_RESCHEDULE = 2;
    public static final int EDIT_MODE_CHANGE = 3;

    private int editMode;
    private Lesson lesson;
    private Appointment appointmentToEdit;

    public int getEditMode() {
        return editMode;
    }

    public void setEditMode(int editMode) {
        this.editMode = editMode;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Appointment getAppointmentToEdit() {
        return appointmentToEdit;
    }

    public void setAppointmentToEdit(Appointment appointmentToEdit) {
        this.appointmentToEdit = appointmentToEdit;
    }

    @FXML
    void cancel(ActionEvent event) {
        stageOf(event).close();
    }

    @FXML
    void cancelAptChecked(ActionEvent event) {
        newAptHBox.setDisable(true);
        newRoomVBox.setDisable(true);
        reasonLabel.setText("Grund der Absage:");
        editReasonComboBox.setItems(FXCollections.observableArrayList(
                "Kurzfristige Absage (Schüler) - wird nicht nachgeholt", "Krankheit (Schüler)", "Krankheit (Lehrer)",
                "Sonstige Gründe (Schüler)", "Sonstige Gründe (Lehrer)"));
    }

    @FXML
    void changeAptChecked(ActionEvent event) {
        newAptHBox.setDisable(false);
        newRoomVBox.setDisable(false);
        reasonLabel.setText("Grund der Änderung:");
        editReasonComboBox.setItems(FXCollections.observableArrayList("Kurzfristige Terminänderung", "Raumwechsel"));
    }

    @FXML
    void rescheduleAptChecked(ActionEvent event) {
        newAptHBox.setDisable(false);
        newRoomVBox.setDisable(false);
        reasonLabel.setText("Grund der Änderung:");
        editReasonComboBox.setItems(FXCollections.observableArrayList("Krankheit (Schüler)", "Krankheit (Lehrer)",
                "Sonstige Gründe (Schüler)", "Sonstige Gründe (Lehrer)"));
    }

    @FXML
    void confirm(ActionEvent event) {
        String mailTo = "";
        String mailSubject = "";
        String mailMessage = "";

        boolean invalidData = false;
        StringBuilder errorMessage = new StringBuilder();

        String reason = editReasonComboBox.getValue();
        LocalDate newDate = newDatePicker.getValue();
        String timeString = newTimeComboBox.getValue();
        LocalTime newTime;
        int newDuration = Integer.parseInt(newDurationComboBox.getValue().replaceAll("[^0-9]", ""));
        Location newLocation = newLocationComboBox.getValue();
        String newRoom = newRoomComboBox.getValue();
        boolean informParents = informParentsCheckBox.isSelected();
        boolean informStudents = informStudentsCheckBox.isSelected();
        boolean informTeacher = informTeacherCheckBox.isSelected();
        boolean reschedule = rescheduleCheckBox.isSelected();

        Appointment oldAppointment = appointmentToEdit;

        if (newTimeComboBox.getSelectionModel().isEmpty()) {
            PopupWindow.displayInformation("Änderung konnte nicht durchgeführt werden:\n\n- Bitte gültige Uhrzeit angeben!");
            return;
        } else {
            newTime = toTime(timeString);
            lesson.setTime(newTime);
        }

        if (editMode == EDIT_MODE_DROPPED) {
            oldAppointment.setStatus(STATUS_DROPPED);
            oldAppointment.setDescription(reason + ", wird nicht nachgeholt");
            oldAppointment.setDateOld(oldAppointment.getDate());
            update(oldAppointment, oldAppointment.getId());
            mailSubject = "Unterricht abgesagt: " + fullDateTimeString(oldAppointment.getDate(), oldAppointment.getTime());
            mailMessage = lessonDropped(oldAppointment, reason);
        }

        if (editMode == EDIT_MODE_RESCHEDULE) {
            oldAppointment.setStatus(reschedule ? STATUS_RESCHEDULED : STATUS_CANCELLED);
            oldAppointment.setDescription(reason + (reschedule ? ", Nachholtermin: " + asString(newDate, newTime) : ""));
            oldAppointment.setDateOld(oldAppointment.getDate());
            update(oldAppointment, oldAppointment.getId());
            mailSubject = "Unterricht abgesagt: " + fullDateTimeString(oldAppointment.getDate(), oldAppointment.getTime());
            mailMessage = lessonToReschedule(oldAppointment, reason);

            if (reschedule) {
                Appointment newAppointment = new Appointment();
                newAppointment.setCategory(CATEGORY_LESSON_RESCHEDULED);
                newAppointment.setStatus(STATUS_OK);
                if (invalidDateInput(newDatePicker)) {
                    invalidData = true;
                    errorMessage.append("- Bitte gültiges Datum eingeben (z.B. 01.01.2000)\n");
                } else {
                    newAppointment.setDate(newDate);
                }
                newAppointment.setTime(newTime);
                newAppointment.setLocationId(newLocation == null ? 0 : newLocation.getId());
                newAppointment.setRoom(newRoom == null ? "" : newRoom);
                newAppointment.setDuration(newDuration);
                newAppointment.setLessonId(appointmentToEdit.getLessonId());
                newAppointment.setDateOld(appointmentToEdit.getDate());
                newAppointment.setDescription("Nachholtermin für " + asString(newAppointment.getDateOld()));

                if (invalidData) {
                    PopupWindow.displayInformation("Änderung konnte nicht durchgeführt werden:\n\n" + errorMessage);
                    return;
                }

                insert(newAppointment);
                mailMessage = lessonRescheduled(oldAppointment, newAppointment, reason);
            }

        }

        if (editMode == EDIT_MODE_CHANGE) {
            oldAppointment.setStatus(STATUS_CHANGED);
            if (invalidDateInput(newDatePicker)) {
                invalidData = true;
                errorMessage.append("- Bitte gültiges Datum eingeben (z.B. 01.01.2000)\n");
            } else {
                oldAppointment.setDate(newDate);
            }
            oldAppointment.setTime(newTime);
            oldAppointment.setLocationId(newLocation == null ? 0 : newLocation.getId());
            oldAppointment.setRoom(newRoom == null ? "" : newRoom);
            oldAppointment.setDuration(newDuration);
            if (!oldAppointment.isReplacement()) oldAppointment.setDateOld(appointmentToEdit.getDate());
            String oldNote = oldAppointment.getDescription();
            oldAppointment.setDescription(reason + (oldNote.isBlank() ? "" : ", " + oldNote));

            if (invalidData) {
                PopupWindow.displayInformation("Änderung konnte nicht durchgeführt werden:\n\n" + errorMessage);
                return;
            }

            fullDateTimeString(oldAppointment.getDate(), oldAppointment.getTime());
            update(oldAppointment, oldAppointment.getId());
            mailSubject = "Änderung zum Termin: " + fullDateTimeString(oldAppointment.getDate(), oldAppointment.getTime());
            mailMessage = lessonChanged(appointmentToEdit, oldAppointment);

            if (oldAppointment.isReplacement()) {
                Appointment originalApt = findRegularAppointment(lesson, oldAppointment.getDateOld());
                String originalDescription = originalApt.getDescription();
                int length = originalDescription.length();
                originalApt.setDescription(originalDescription.substring(0, length - 17) + asString(newDate, newTime));
                update(originalApt, originalApt.getId());
            }

        }

        if (mailRequired()) {
            StringJoiner recipients = new StringJoiner(", ");

            if (informContactCheckBox.isSelected()) {
                for (Student student : lesson.students()) {
                    if (student.getContactEmail().equals("keine Angabe")) {
                        sendMail(errorMail, "E-Mail an " + student.name() + " konnte nicht gesendet werden", "Inhalt der Mail:\n\n" + mailMessage);
                    } else {
                        recipients.add(student.getContactEmail());
                    }
                }
            }

            if (informParentsCheckBox.isSelected()) {
                for (Student student : lesson.students()) {
                    if (student.getParentId1() != 0) recipients.add(getParentFromDB(student.getParentId1()).getEmail());
                    if (student.getParentId2() != 0) recipients.add(getParentFromDB(student.getParentId2()).getEmail());
                }
            }

            if (informStudentsCheckBox.isSelected()) {
                for (Student student : lesson.students()) {
                    recipients.add(student.getEmail());
                }
            }

            if (informTeacherCheckBox.isSelected()) {
                recipients.add(getTeacherFromDB(lesson.getTeacherId()).getEmail());
            }

            mailTo = recipients.toString();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/newmail-view.fxml"));
            Stage stage = newStage("Neue E-Mail", loader);
            NewMailController controller = loader.getController();
            controller.init(mailTo, mailSubject, mailMessage);
            stage.showAndWait();
        }

        stageOf(event).close();
    }

    private boolean mailRequired() {
        return informContactCheckBox.isSelected() || informParentsCheckBox.isSelected() ||
                informStudentsCheckBox.isSelected() || informTeacherCheckBox.isSelected();
    }

    public void initAppointment(Appointment appointment) {
        switch(editMode) {
            case EDIT_MODE_DROPPED -> {
                titleTextField.setText("Termin absagen");
                reasonLabel.setText("Grund der Absage");
                editReasonComboBox.setItems(FXCollections.observableArrayList("Kurzfristige Absage (Schüler)",
                        "Krankheit (Schüler)", "Krankheit (Lehrer)", "Sonstige Gründe (Schüler)", "Sonstige Gründe (Lehrer)"));
                rescheduleCheckBox.setVisible(false);
            }
            case EDIT_MODE_RESCHEDULE -> {
                titleTextField.setText("Termin absagen");
                reasonLabel.setText("Grund der Absage");
                editReasonComboBox.setItems(FXCollections.observableArrayList("Krankheit (Schüler)",
                        "Krankheit (Lehrer)", "Sonstige Gründe (Schüler)", "Sonstige Gründe (Lehrer)"));
            }
            case EDIT_MODE_CHANGE -> {
                titleTextField.setText("Termin bearbeiten");
                reasonLabel.setText("Grund der Änderung");
                editReasonComboBox.setItems(FXCollections.observableArrayList("Individuelle Terminänderung", "Raumwechsel"));
                rescheduleCheckBox.setVisible(false);
                newAptHBox.setDisable(false);
                newRoomVBox.setDisable(false);
            }
        }

        setAppointmentToEdit(appointment);
        setLesson(getLessonFromDB(appointment.getLessonId()));
        LocalDate oldDate = appointment.getDate();
        LocalTime oldTime = appointment.getTime();
        oldDateTimeLabel.setText(fullDateTimeString(oldDate, oldTime)
                + (appointment.getCategory() == CATEGORY_LESSON_RESCHEDULED ? " (Nachholtermin für " + asString(appointment.getDateOld()) + ")" : ""));
        lessonCategoryLabel.setText(lesson.category());
        studentNameLabel.setText(lesson.studentsNamesString());
        instrumentLabel.setText(lesson.getInstrument());
        locationRoomLabel.setText(appointment.locationRoom());
        teacherLabel.setText(lesson.teacher().name());
        regularAptLabel.setText(lesson.regularAppointment());
        newDatePicker.setValue(appointment.getDate());
        newTimeComboBox.setValue(appointment.getTime().toString() + " Uhr");
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
        rescheduleCheckBox.selectedProperty().addListener(e -> {
            newAptHBox.setDisable(!rescheduleCheckBox.isSelected());
            newRoomVBox.setDisable(!rescheduleCheckBox.isSelected());
        });

        rescheduleCheckBox.setSelected(false);
        newAptHBox.setDisable(true);
        newRoomVBox.setDisable(true);

        newTimeComboBox.setItems(FXCollections.observableArrayList(times(8, 23)));

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
        informContactCheckBox.setOnAction(event -> informNoneCheckBox.setSelected(false));

        informNoneCheckBox.setSelected(true);
    }
}
