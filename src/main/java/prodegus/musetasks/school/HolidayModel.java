package prodegus.musetasks.school;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.database.Filter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static prodegus.musetasks.database.Database.*;

public class HolidayModel {

    public static boolean isHoliday(LocalDate date) {
        for (Holiday holiday : getHolidayListFromDB()) {
            if (!date.isBefore(holiday.getStart()) && !date.isAfter(holiday.getEnd())) return true;
        }
        return false;
    }

    public static ObservableList<Holiday> getHolidayListFromDB() {
        ObservableList<Holiday> holidays = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + HOLIDAY_TABLE;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Holiday holiday = new Holiday();
                holiday.setAttributes(rs);
                holidays.add(holiday);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return holidays;
    }

    public static void insertHoliday(Holiday holiday) {
        insert(HOLIDAY_TABLE, holiday.sqlColumns(), holiday.sqlValues());
    }

    public static void deleteHoliday(Holiday holiday) {
        delete(HOLIDAY_TABLE, new Filter("description", holiday.getDescription()));
    }

    public static void updateHoliday(Holiday holiday) {
        updateMultiple(HOLIDAY_TABLE, holiday.valuesToSQLUpdateString(), new Filter("description", holiday.getDescription()));
    }
}
