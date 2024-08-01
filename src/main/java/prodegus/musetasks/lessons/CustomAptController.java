package prodegus.musetasks.lessons;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.school.Holiday;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static prodegus.musetasks.appointments.Appointment.*;
import static prodegus.musetasks.lessons.LessonModel.LESSON_APT_STATUS_REQUEST;
import static prodegus.musetasks.school.HolidayModel.getHoliday;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.DateTime.toTime;
import static prodegus.musetasks.utils.Nodes.timeComboBox;

public class CustomAptController implements Initializable {

    @FXML private Label lessonTitle;
    @FXML private Label titleTextField;
    @FXML private ScrollPane aptScrollPane;
    @FXML public VBox aptVBox;

    public Lesson lesson;

    List<AppointmentHBox> getRows() {
        List<AppointmentHBox> rows = new ArrayList<>();
        for (Node node : aptVBox.getChildren()) {
            if (node.getClass().getSimpleName().equals("AppointmentHBox")) {
                AppointmentHBox appointmentHBox = (AppointmentHBox) node;
                rows.add(appointmentHBox);
            }
        }
        return rows;
    }

    @FXML void addRow() {
        aptVBox.getChildren().add(new AppointmentHBox(getRowCount()));
    }


    int getRowCount() {
        return getRows().size();
    }

    List<Appointment> appointments() {
        List<Appointment> appointments = new ArrayList<>();

        for (AppointmentHBox appointmentHBox : getRows()) {
            Appointment appointment = new Appointment();
            appointment.setDate(appointmentHBox.datePicker.getValue());
            appointment.setTime(toTime(appointmentHBox.timeComboBox.getValue()));
            appointment.setLocationId(lesson.getLocationId());
            appointment.setRoom(lesson.getRoom());
            appointment.setDuration(lesson.getDuration());
            appointment.setCategory(CATEGORY_LESSON_REGULAR);
            appointment.setStatus(lesson.getAptStatus() == LESSON_APT_STATUS_REQUEST ? STATUS_REQUEST : STATUS_OK);
            appointments.add(appointment);
        }

        return appointments;
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void confirmAppointments(ActionEvent event) {
        stageOf(event).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aptVBox.getChildren().clear();
        addRow();
    }

    public void init(Lesson lesson) {
        this.lesson = lesson;
    }
}

