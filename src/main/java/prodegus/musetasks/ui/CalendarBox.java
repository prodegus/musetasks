package prodegus.musetasks.ui;

import javafx.geometry.Insets;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.lessons.Lesson;

import static prodegus.musetasks.appointments.EditAppointmentController.*;
import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;
import static prodegus.musetasks.lessons.LessonModel.*;

public class CalendarBox extends VBox {
    private Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public CalendarBox(Appointment appointment) {
        this.setAppointment(appointment);
        this.setPrefWidth(200);
        this.setPadding(new Insets(0, 3, 0, 3));
        this.setPrefHeight((double) appointment.getDuration() / 60 * 100);
        this.setStyle("-fx-background-color: #90ee90;" +
                "-fx-border-color: #5dd55d;" +
                "-fx-background-radius: 5;" +
                "-fx-border-radius: 5;");
        this.getChildren().add(calendarBoxContent(appointment));

        int hours = appointment.getTime().minusHours(8).getHour();
        int minutes = appointment.getTime().getMinute();
        AnchorPane.setTopAnchor(this, (hours + (minutes / 60.0)) * 100.0);
    }
    
    public VBox calendarBoxContent(Appointment appointment) {
        Lesson lesson = appointment.lesson();
        VBox content = new VBox();

        Tooltip.install(content, new Tooltip("Lehrer: " + lesson.teacher().name()));

        if (lesson.getCategory() == CATEGORY_SINGLE) {
            Label label1 = new Label(appointment.getTime() + "   " + getStudentFromDB(lesson.getStudentId1()).name());
            label1.setStyle("-fx-font-weight: bold");
            label1.setWrapText(true);
            Label label2 = new Label(String.join(", ", lesson.getInstrument(), "Einzel", lesson.getDuration() + " min"));
            label2.setWrapText(true);
            content.getChildren().addAll(label1, label2);
        } else {
            Label label3 = new Label(appointment.getTime() + "   " + lesson.getLessonName());
            label3.setStyle("-fx-font-weight: bold");
            label3.setWrapText(true);
            content.getChildren().add(label3);
        }

        return content;
    }
}
