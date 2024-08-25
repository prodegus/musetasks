package prodegus.musetasks.ui;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import prodegus.musetasks.appointments.Appointment;

import java.time.LocalDate;

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
        this.appointments.setPredicate(appointment -> appointment.getDate().equals(date));

        if (appointments.isEmpty()) this.getChildren().add(placeholder());

        this.getChildren().clear();

        for (Appointment appointment : appointments) {
            this.getChildren().add(new CalendarBox(appointment));
        }
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
            this.getChildren().add(new CalendarBox(appointment));
        }
    }

    public CalendarColumn(String header, FilteredList<Appointment> appointments, LocalDate date) {
        this.header = header;
        this.appointments = appointments;
        this.date = date;

        appointments.setPredicate(appointment -> appointment.getDate().equals(date));

        if (appointments.isEmpty()) this.getChildren().add(placeholder());

        for (Appointment appointment : appointments) {
            this.getChildren().add(new CalendarBox(appointment));
        }
    }

    public static VBox placeholder() {
        VBox box = new VBox();
        box.setPrefWidth(200);
        return box;
    }

    public static Separator headerSeparator() {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        HBox.setMargin(separator, new Insets(0, 5, 0, 5));
        return separator;
    }

    public static Separator columnSeparator() {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        HBox.setMargin(separator, new Insets(0, 2, 0, 5));
        return separator;
    }

    public Label headerLabel() {
        Label headerLabel = new Label(this.getHeader());
        headerLabel.setMinWidth(197);
        return headerLabel;
    }
}
