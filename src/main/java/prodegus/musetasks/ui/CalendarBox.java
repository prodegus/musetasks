package prodegus.musetasks.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;

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
        Label label = new Label(appointment.lesson().getLessonName());
        label.setStyle("-fx-font-weight: bold");
        label.setWrapText(true);
        this.getChildren().add(label);

        int hours = appointment.getTime().minusHours(8).getHour();
        int minutes = appointment.getTime().getMinute();
        AnchorPane.setTopAnchor(this, (hours + (minutes / 60.0)) * 100.0);
    }
}
