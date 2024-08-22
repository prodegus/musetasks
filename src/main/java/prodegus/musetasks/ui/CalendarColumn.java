package prodegus.musetasks.ui;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;

import java.time.LocalDate;

import static prodegus.musetasks.ui.Calendar.appointmentBox;

public class CalendarColumn extends AnchorPane {
    private String header;
    private FilteredList<Appointment> appointments;
    private LocalDate date;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public FilteredList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(FilteredList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        this.appointments.setPredicate(appointment -> appointment.getDate().equals(date));

        this.getChildren().clear();
        if (appointments.isEmpty()) this.getChildren().add(placeholder());

        for (Appointment appointment : appointments) {
            this.getChildren().add(appointmentBox(appointment, 0, "#90ee90"));
        }
    }

    public CalendarColumn(String header, FilteredList<Appointment> appointments, LocalDate date) {
        this.header = header;
        this.appointments = appointments;
        this.date = date;

        appointments.setPredicate(appointment -> appointment.getDate().equals(date));

        if (appointments.isEmpty()) this.getChildren().add(placeholder());

        for (Appointment appointment : appointments) {
            this.getChildren().add(appointmentBox(appointment, 0, "#90ee90"));
        }
    }

    public static VBox placeholder() {
        VBox box = new VBox();
        box.setPrefWidth(200);
        return box;
    }

    public Label headerLabel() {
        Label headerLabel = new Label(this.getHeader());
        HBox.setMargin(headerLabel, new Insets(0, 0, 0, 5));
        headerLabel.setMinWidth(195);
        return headerLabel;
    }
}
