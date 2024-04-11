package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.APPOINTMENT_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class AppointmentModel {

    public static ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        refreshAppointmentList(appointments);
        return appointments;
    }

    public static void refreshAppointmentList(ObservableList<Appointment> appointments) {
        String sql = "SELECT * FROM " + APPOINTMENT_TABLE;

        appointments.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAttributes(rs);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Appointment getAppointment(String id) {
        String sql = "SELECT * FROM " + APPOINTMENT_TABLE + " WHERE id = " + id;
        Appointment appointment = new Appointment();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                appointment.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointment;
    }

}
