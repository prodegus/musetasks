package prodegus.musetasks.appointments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.database.Database;
import prodegus.musetasks.lessons.Lesson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static prodegus.musetasks.appointments.Appointment.CATEGORY_LESSON_REGULAR;
import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.utils.DateTime.toInt;

public class AppointmentModel {

    public static ObservableList<Appointment> getAppointmentListFromDB() {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + APPOINTMENT_TABLE;

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
        return appointments;
    }

    public static ObservableList<Appointment> getLessonAppointmentsFromDB(int lessonId, LocalDate startDate, LocalDate endDate) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql =
                "SELECT * FROM " + APPOINTMENT_TABLE +
                " WHERE lessonid = " + lessonId +
                " AND (date BETWEEN " + toInt(startDate) + " AND " + toInt(endDate) + ")";

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
        return appointments;
    }

    public static ObservableList<Appointment> getLessonAppointmentsFromDB(int lessonId) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String sql =
                "SELECT * FROM " + APPOINTMENT_TABLE +
                        " WHERE lessonid = " + lessonId;

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
        return appointments;
    }
    
    public static Appointment getAppointmentFromDB(int id) {
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

    public static Appointment findRegularAppointment(Lesson lesson, LocalDate date) {
        String sql = "SELECT * FROM " + APPOINTMENT_TABLE +
                " WHERE lessonid = " + lesson.getId() +
                " AND date = " + toInt(date) +
                " AND category = " + CATEGORY_LESSON_REGULAR;
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

    public static void insert(Appointment appointment) {
        Database.insert(APPOINTMENT_TABLE, appointment.sqlColumns(), appointment.sqlValues());
    }

    public static void delete(Appointment appointment) {
        Database.delete(APPOINTMENT_TABLE, appointment.getId());
    }

    public static void update(Appointment appointment, int id) {
        updateMultiple(APPOINTMENT_TABLE, id, appointment.valuesToSQLUpdateString());
    }

    public static boolean isChanged(Lesson lesson, LocalDate date) {
        for (Appointment appointment : getLessonAppointmentsFromDB(lesson.getId())) {
            if (appointment.getDateOld().equals(date)) return true;
        }
        return false;
    }

}
