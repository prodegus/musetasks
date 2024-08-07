package prodegus.musetasks.lessons;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.ui.AppointmentHBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static prodegus.musetasks.appointments.Appointment.*;
import static prodegus.musetasks.appointments.AppointmentModel.getLessonAppointmentsFromDB;
import static prodegus.musetasks.lessons.LessonModel.LESSON_APT_STATUS_REQUEST;
import static prodegus.musetasks.ui.StageFactories.stageOf;
import static prodegus.musetasks.utils.DateTime.toTime;

public class CustomAptController implements Initializable {

    @FXML private Label lessonTitle;
    @FXML private Label titleTextField;
    @FXML private ScrollPane aptScrollPane;
    @FXML public VBox aptVBox;

    public Lesson lesson;

    private final SimpleBooleanProperty cancelled = new SimpleBooleanProperty();

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

    void addRow(Appointment appointment) {
        aptVBox.getChildren().add(new AppointmentHBox(getRowCount(), appointment));
    }


    int getRowCount() {
        return getRows().size();
    }

    List<Appointment> appointments() {
        if (cancelled.get()) return null;
        List<Appointment> appointments = new ArrayList<>();

        for (AppointmentHBox appointmentHBox : getRows()) {
            appointments.add(appointmentHBox.getAppointment());
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
        cancelled.set(false);
        aptVBox.getChildren().clear();
        addRow();
    }

    public void init(Lesson lesson) {
        this.lesson = lesson;
        lessonTitle.setText(lesson.getLessonName());
        List<Appointment> appointments = getLessonAppointmentsFromDB(lesson.getId()).sorted();
        if (appointments.size() == 0) return;
        aptVBox.getChildren().clear();
        for (Appointment appointment : appointments) {
            addRow(appointment);
        }
    }
}

