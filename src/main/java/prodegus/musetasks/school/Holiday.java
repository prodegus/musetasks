package prodegus.musetasks.school;

import javafx.beans.property.SimpleStringProperty;
import prodegus.musetasks.appointments.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.StringJoiner;

import static prodegus.musetasks.appointments.Appointment.CATEGORY_HOLIDAY;
import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.DateTime.toInt;

public class Holiday {

    private String description = "";
    private LocalDate start = LocalDate.MIN;
    private LocalDate end = LocalDate.MAX;

    public Holiday() {}

    public Holiday(String description, LocalDate start, LocalDate end) {
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }



    public void setAttributes(ResultSet rs) throws SQLException {
        this.setDescription(rs.getString("description"));
        this.setStart(toDate(rs.getInt("start")));
        this.setEnd(toDate(rs.getInt("end")));
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");

        columns.add("description");
        columns.add("start");
        columns.add("end");

        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'");

        values.add(this.getDescription());
        values.add(String.valueOf(toInt(this.getStart())));
        values.add(String.valueOf(toInt(this.getEnd())));

        return values.toString();
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder();

        sb.append("description = '").append(this.getDescription()).append("', ");
        sb.append("start       = ").append(toInt(this.getStart())).append(", ");
        sb.append("end         = ").append(toInt(this.getEnd()));

        return sb.toString();
    }

}
