package prodegus.musetasks.appointments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.lessons.Lesson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.utils.DateTime.toInt;

public class AppointmentModel {
    public static int CATEGORY_LESSON_REGULAR = 1;
    public static int CATEGORY_LESSON_RESCHEDULED = 2;
    public static int CATEGORY_MEET = 3;
    public static int CATEGORY_CUSTOM = 4;

    public static int STATUS_OK = 1;
    public static int STATUS_CANCELLED_STUDENT = 2;
    public static int STATUS_CANCELLED_TEACHER = 3;
    public static int STATUS_CANCELLED = 4;
    public static int STATUS_RESCHEDULED = 5;
    public static int STATUS_COMPENSATED = 6;
    public static int STATUS_CHANGED = 7;

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
                " AND (" +
                        "date BETWEEN " + toInt(startDate) + " AND " + toInt(endDate) +
                        " OR " +
                        "dateold BETWEEN " + toInt(startDate) + " AND " + toInt(endDate) +
                ")";

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

    public static void insertAppointment(Appointment appointment) {
        insert(APPOINTMENT_TABLE, appointment.sqlColumns(), appointment.sqlValues());
    }

    public static void deleteAppointment(Appointment appointment) {
        delete(APPOINTMENT_TABLE, appointment.getId());
    }

    public static void updateAppointment(Appointment appointment, int id) {
        updateMultiple(APPOINTMENT_TABLE, id, appointment.valuesToSQLUpdateString());
    }

    public static boolean isChanged(Lesson lesson, LocalDate date) {
        for (Appointment appointment : getLessonAppointmentsFromDB(lesson.getId())) {
            if (appointment.getDateOld().equals(date)) return true;
        }
        return false;
    }

}
