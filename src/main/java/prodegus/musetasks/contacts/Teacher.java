package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleStringProperty;
import prodegus.musetasks.database.Filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringJoiner;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.school.LocationModel.getLocationFromDB;
import static prodegus.musetasks.utils.Strings.string;

public class Teacher extends Contact {

    private SimpleStringProperty instruments = new SimpleStringProperty();
    private SimpleStringProperty activeDays = new SimpleStringProperty();
    private SimpleStringProperty status = new SimpleStringProperty();
    private SimpleStringProperty statusFrom = new SimpleStringProperty();
    private SimpleStringProperty statusTo = new SimpleStringProperty();

    public String getInstruments() {
        return instruments.get();
    }

    public void setInstruments(String instruments) {
        this.instruments.set(instruments);
    }

    public SimpleStringProperty instrumentsProperty() {
        return instruments;
    }

    public String getActiveDays() {
        return activeDays.get();
    }

    public void setActiveDays(String activeDays) {
        this.activeDays.set(activeDays);
    }

    public SimpleStringProperty activeDaysProperty() {
        return activeDays;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public String getStatusFrom() {
        return statusFrom.get();
    }

    public void setStatusFrom(String statusFrom) {
        this.statusFrom.set(statusFrom);
    }

    public SimpleStringProperty statusFromProperty() {
        return statusFrom;
    }

    public String getStatusTo() {
        return statusTo.get();
    }

    public void setStatusTo(String statusTo) {
        this.statusTo.set(statusTo);
    }

    public SimpleStringProperty statusToProperty() {
        return statusTo;
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setInstruments(string(rs.getString("instruments")));
        this.setActiveDays(string(rs.getString("activedays")));
        this.setStatus(string(rs.getString("status")));
        this.setStatusFrom(string(rs.getString("statusfrom")));
        this.setStatusTo(string(rs.getString("statusto")));
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder(super.valuesToSQLUpdateString()).append(", ");
        sb.append("instruments = '").append(this.getInstruments()).append("', ");
        sb.append("activedays  = '").append(this.getActiveDays()).append("', ");
        sb.append("status      = '").append(this.getStatus()).append("', ");
        sb.append("statusfrom  = '").append(this.getStatusFrom()).append("', ");
        sb.append("statusto    = '").append(this.getStatusTo()).append("'");
        return sb.toString();
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");
        columns.add(super.sqlColumns());
        if (!this.getInstruments().isBlank()) columns.add("instruments");
        if (!this.getActiveDays().isBlank()) columns.add("activedays");
        if (!this.getStatus().isBlank()) columns.add("status");
        if (!this.getStatusFrom().isBlank()) columns.add("statusfrom");
        if (!this.getStatusTo().isBlank()) columns.add("statusto");
        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'").setEmptyValue("");
        StringJoiner result = new StringJoiner(", ").add(super.sqlValues());
        if (!this.getInstruments().isBlank()) values.add(this.getInstruments());
        if (!this.getActiveDays().isBlank()) values.add(this.getActiveDays());
        if (!this.getStatus().isBlank()) values.add(this.getStatus());
        if (!this.getStatusFrom().isBlank()) values.add(this.getStatusFrom());
        if (!this.getStatusTo().isBlank()) values.add(this.getStatusTo());
        if (!values.toString().isEmpty()) result.add(values.toString());
        return result.toString();
    }

    public String status() {
        StringBuilder sb = new StringBuilder();
        if (!this.getStatus().isEmpty()) sb.append(this.getStatus());
        if (!this.getStatusFrom().isEmpty()) sb.append(" ab ").append(this.getStatusFrom());
        if (!this.getStatusTo().isEmpty()) sb.append(" bis ").append(this.getStatusTo());
        return sb.toString();
    }

    public String courses() {
        StringBuilder courses = new StringBuilder();
        Filter courseFilter = new Filter("category", "'Kurs'");
        Filter teacherFilter = new Filter("teacherid", this.id());
        List<String> results = queryString("lessonname", LESSON_TABLE, courseFilter, teacherFilter);
        int i = 1;
        for (String string : results) {
            courses.append(string);
            if (i < results.size()) courses.append(", ");
            i++;
        }
        return courses.toString();
    }

    public String weekdayLocation(int weekday) {
        StringJoiner locations = new StringJoiner(", ");
        Filter teacherFilter = new Filter("teacherid", this.id());
        Filter weekdayFilter = new Filter("weekday", String.valueOf(weekday));
        HashSet<Integer> results = new LinkedHashSet<>(
                queryInteger("locationid", LESSON_TABLE, teacherFilter, weekdayFilter));
        if (results.size() == 0) return "-";
        for (Integer locationId : results) {
            locations.add(getLocationFromDB(locationId).getName());
        }
        return locations.toString();
    }

    public int numberOfStudents() {
        Filter categoryFilter = new Filter("category", "'Einzelunterricht'");
        Filter teacherFilter = new Filter("teacherid", this.id());
        List<Integer> results = queryInteger("id", LESSON_TABLE, categoryFilter, teacherFilter);
        return results.size();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
