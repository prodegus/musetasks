package prodegus.musetasks.workspace;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.school.Location;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static prodegus.musetasks.appointments.AppointmentModel.getRoomAppointmentsToday;
import static prodegus.musetasks.contacts.StudentModel.getProspectiveListFromDB;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherListFromDB;
import static prodegus.musetasks.school.LocationModel.getLocationListFromDB;
import static prodegus.musetasks.school.School.getActiveUser;
import static prodegus.musetasks.utils.DateTime.asString;
import static prodegus.musetasks.utils.DateTime.weekdayDateString;

public class TodayController implements Initializable {

    @FXML private AnchorPane mainAnchorPane;

    @FXML private Label greetingLabel;
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;

    @FXML private VBox lessonsTodayVBox;
    @FXML private VBox teachersVBox;
    @FXML private VBox prospectivesVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        greetingLabel.setText(greeting());
        dateLabel.setText(weekdayDateString(LocalDate.now()));
        getTimer().start();
        initializeLessons();
        initializeTeachers();
        initializeProspectives();
        initializeCalendar();
    }

    private void initializeCalendar() {
        DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
        Node popupContent = datePickerSkin.getPopupContent();
        popupContent.setEffect(null);
        ((VBox) popupContent).setPrefWidth(300);
        AnchorPane.setBottomAnchor(popupContent, 20.0);
        AnchorPane.setRightAnchor(popupContent, 20.0);
        mainAnchorPane.getChildren().add(popupContent);
    }

    private void initializeProspectives() {
        for (Student prospective : getProspectiveListFromDB()) {
            addProspectivePane(prospective);
        }
    }

    private void addProspectivePane(Student prospective) {
        VBox prospectiveVBox = new VBox(new Label("Status: " + prospective.studentStatus()));
        TitledPane prospectivePane = new TitledPane(prospective.name(), prospectiveVBox);
        prospectivePane.setPrefWidth(298);
        prospectivesVBox.getChildren().add(prospectivePane);
    }

    private void initializeTeachers() {
        for (Teacher teacher : getTeacherListFromDB()) {
            addTeacherPane(teacher);
        }
    }

    private void addTeacherPane(Teacher teacher) {
        List<Appointment> pendingAppointments = teacher.getPendingAppointments();
        String header = teacher.name() +
                (pendingAppointments.isEmpty() ? "" : "(" + pendingAppointments.size() + " Nachholtermine offen)");

        VBox teacherVBox = new VBox();
        Label absentDays = new Label("Abwesenheitstage in diesem Halbjahr: " + teacher.getAbsentDays().size());
        Label aptsToCompensate = new Label("Nachzuholende Unterrichte:" + (pendingAppointments.isEmpty() ? " Keine" : ""));
        VBox.setMargin(absentDays, new Insets(0, 0, 10, 0));
        VBox.setMargin(aptsToCompensate, new Insets(0, 0, 3, 0));
        teacherVBox.getChildren().addAll(absentDays, aptsToCompensate);

        for (Appointment appointment : pendingAppointments) {
            Label date = new Label(asString(appointment.getDate()));
            date.setPrefWidth(80);
            Label lessonName = new Label(appointment.lesson().getLessonName());
            teacherVBox.getChildren().add(new HBox(date, lessonName));
        }
        TitledPane teacherPane = new TitledPane(header, teacherVBox);
        teacherPane.setPrefWidth(398);

        teachersVBox.getChildren().add(teacherPane);
    }

    private void initializeLessons() {
        for (Location location : getLocationListFromDB()) {
            for (String room : location.rooms()) {
                addLessonPane(location.getName() + " - " + room, getRoomAppointmentsToday(location, room));
            }
        }
    }

    private void addLessonPane(String header, ObservableList<Appointment> appointments) {
        VBox appointmentsVBox = new VBox();
        if (appointments.isEmpty()) appointmentsVBox.getChildren().add(new Label("Keine Termine"));
        for (Appointment appointment : appointments) {
            appointmentsVBox.getChildren().add(appointmentRow(appointment));
        }
        TitledPane lessonPane = new TitledPane(header, appointmentsVBox);
        lessonPane.setPrefWidth(398);
        lessonsTodayVBox.getChildren().add(lessonPane);
    }

    private HBox appointmentRow(Appointment appointment) {
        Label time = new Label(asString(appointment.getTime()));
        Label timeText = new Label("Uhr");
        Label description = new Label(appointment.lesson().getLessonName());
        time.setPrefWidth(33);
        timeText.setPrefWidth(35);
        return new HBox(time, timeText, description);
    }

    private AnimationTimer getTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        };
    }

    private String greeting() {
        String greetingText = switch (LocalTime.now().getHour()) {
            case 4, 5, 6, 7, 8, 9, 10, 11 -> "Guten Morgen";
            case 18, 19, 20, 21, 22, 23, 0, 1, 2, 3 -> "Guten Abend";
            default -> "Guten Tag";
        };
        return greetingText + ", " + getActiveUser() + "!";
    }
}
