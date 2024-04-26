package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleStringProperty;
import prodegus.musetasks.database.Filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import static prodegus.musetasks.database.Database.*;

public class Teacher extends Contact {

    private SimpleStringProperty instruments = new SimpleStringProperty();
    private SimpleStringProperty activeDays  = new SimpleStringProperty();
    private SimpleStringProperty status      = new SimpleStringProperty();
    private SimpleStringProperty statusFrom  = new SimpleStringProperty();
    private SimpleStringProperty statusTo    = new SimpleStringProperty();

    public String getInstruments() {
        return instruments.get();
    }

    public SimpleStringProperty instrumentsProperty() {
        return instruments;
    }

    public void setInstruments(String instruments) {
        this.instruments.set(instruments);
    }

    public String getActiveDays() {
        return activeDays.get();
    }

    public SimpleStringProperty activeDaysProperty() {
        return activeDays;
    }

    public void setActiveDays(String activeDays) {
        this.activeDays.set(activeDays);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getStatusFrom() {
        return statusFrom.get();
    }

    public SimpleStringProperty statusFromProperty() {
        return statusFrom;
    }

    public void setStatusFrom(String statusFrom) {
        this.statusFrom.set(statusFrom);
    }

    public String getStatusTo() {
        return statusTo.get();
    }

    public SimpleStringProperty statusToProperty() {
        return statusTo;
    }

    public void setStatusTo(String statusTo) {
        this.statusTo.set(statusTo);
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setInstruments(rs.getString("instruments"));
        this.setActiveDays(rs.getString("activedays"));
        this.setStatus(rs.getString("status"));
        this.setStatusFrom(rs.getString("statusfrom"));
        this.setStatusTo(rs.getString("statusto"));
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.valuesToSQLString());
        sb.append(", '");
        sb.append(this.getInstruments());
        sb.append("', '");
        sb.append(this.getActiveDays());
        sb.append("', '");
        sb.append(this.getStatus());
        sb.append("', '");
        sb.append(this.getStatusFrom());
        sb.append("', '");
        sb.append(this.getStatusTo());
        sb.append("'");
        return sb.toString();
    }

    public String status() {
        StringBuilder status = new StringBuilder();
        if (this.getStatus().isEmpty()) return "";
        status.append(this.getStatus());
        if (!this.getStatusFrom().isEmpty()) {
            status.append(" ab ");
            status.append(this.getStatusFrom());
        }
        if (!this.getStatusTo().isEmpty()) {
            status.append(" bis ");
            status.append(this.getStatusTo());
        }

        return status.toString();
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

    public String weekdayLocation(String weekday) {
        StringBuilder locations = new StringBuilder();
        Filter teacherFilter = new Filter("teacherid", this.id());
        Filter weekdayFilter = new Filter("weekday", "'" + weekday +"'");
        HashSet<String> results = new LinkedHashSet<>(
                queryString("location", LESSON_TABLE, teacherFilter, weekdayFilter));
        if (results.size() == 0) return "-";
        int i = 1;
        for (String string : results) {
            locations.append(string);
            if (i < results.size()) locations.append(", ");
            i++;
        }
        return locations.toString();
    }

    public int numberOfStudents() {
        Filter categoryFilter = new Filter("category", "'Einzelunterricht'");
        Filter teacherFilter = new Filter("teacherid", this.id());
        List<Integer> results = queryInteger("id", LESSON_TABLE, categoryFilter, teacherFilter);
        return results.size();
    }
}
