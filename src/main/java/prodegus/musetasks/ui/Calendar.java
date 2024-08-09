package prodegus.musetasks.ui;

import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;

import java.time.LocalTime;

public class Calendar {
    public VBox AppointmentBox(Appointment appointment, int configuration) {
        VBox box = new VBox();
        box.setPrefWidth(200);
        box.setPrefHeight((double) appointment.getDuration() / 60 * 100);
        // set AnchorPane constraints, need AnchorPane variable
        return box;
    }
}
