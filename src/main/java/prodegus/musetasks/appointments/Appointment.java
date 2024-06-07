package prodegus.musetasks.appointments;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import prodegus.musetasks.school.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;

import static prodegus.musetasks.school.LocationModel.getLocationFromDB;
import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.DateTime.toInt;

public class Appointment implements Comparable<Appointment> {

    public static final int CATEGORY_LESSON_REGULAR = 1;
    public static final int CATEGORY_LESSON_RESCHEDULED = 2;
    public static final int CATEGORY_MEET = 3;
    public static final int CATEGORY_HOLIDAY = 4;
    public static final int CATEGORY_CUSTOM = 5;

    public static final int STATUS_OK = 1;
    public static final int STATUS_CANCELLED_STUDENT = 2;
    public static final int STATUS_CANCELLED_TEACHER = 3;
    public static final int STATUS_DROPPED = 4;
    public static final int STATUS_RESCHEDULED = 5;
    public static final int STATUS_COMPENSATED = 6;
    public static final int STATUS_CHANGED = 7;

    private SimpleIntegerProperty id          = new SimpleIntegerProperty();
    private LocalDate             date        = LocalDate.MIN;
    private LocalTime             time        = LocalTime.MAX;
    private SimpleIntegerProperty locationId  = new SimpleIntegerProperty();
    private SimpleStringProperty  room        = new SimpleStringProperty();
    private SimpleIntegerProperty duration    = new SimpleIntegerProperty();
    private SimpleIntegerProperty lessonId    = new SimpleIntegerProperty();
    private LocalDate             dateOld     = LocalDate.MIN;
    private SimpleIntegerProperty category    = new SimpleIntegerProperty();
    private SimpleIntegerProperty status      = new SimpleIntegerProperty();
    private SimpleStringProperty  description = new SimpleStringProperty();
    private SimpleBooleanProperty selected    = new SimpleBooleanProperty(false);

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getLocationId() {
        return locationId.get();
    }

    public SimpleIntegerProperty locationIdProperty() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId.set(locationId);
    }

    public String getRoom() {
        return room.get();
    }

    public SimpleStringProperty roomProperty() {
        return room;
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public int getDuration() {
        return duration.get();
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public int getLessonId() {
        return lessonId.get();
    }

    public SimpleIntegerProperty lessonIdProperty() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId.set(lessonId);
    }

    public LocalDate getDateOld() {
        return dateOld;
    }

    public void setDateOld(LocalDate dateOld) {
        this.dateOld = dateOld;
    }

    public int getCategory() {
        return category.get();
    }

    public SimpleIntegerProperty categoryProperty() {
        return category;
    }

    public void setCategory(int category) {
        this.category.set(category);
    }

    public int getStatus() {
        return status.get();
    }

    public SimpleIntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
        this.status.set(status);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public Location location() {
        return getLocationFromDB(this.getLocationId());
    }

    public String category() {
        return switch(this.getCategory()) {
            case 1 -> "Regulärer Termin";
            case 2 -> "Ersatzermin";
            case 3 -> "Kennenlerntermin";
            case 4 -> "Individueller Termin";
            default -> "";
        };
    }

    public String status() {
        if (this.getCategory() == CATEGORY_HOLIDAY) return "";
        return switch(this.getStatus()) {
            case STATUS_OK -> "Planmäßig";
            case STATUS_CANCELLED_STUDENT, STATUS_CANCELLED_TEACHER -> "Nachholtermin offen";
            case STATUS_DROPPED -> "Abgesagt";
            case STATUS_RESCHEDULED -> "Nachholtermin geplant";
            case STATUS_COMPENSATED -> "Nachgeholt";
            case STATUS_CHANGED -> "Verschoben";
            default -> "";
        };
    }

    public String dateInfo() {
        if (this.getCategory() == CATEGORY_HOLIDAY) {
            LocalDate start = this.getDate();
            LocalDate end = this.getDateOld();
            return (start.isEqual(end) ? weekdayDateString(start) : String.join(" - ", asString(start), asString(end)));
        }
        return weekdayDateString(this.date);
    }

    public String timeInfo() {
        if (this.getCategory() == CATEGORY_HOLIDAY) return "";
        return String.join(" - ", asString(this.getTime()), asString(this.getTime().plusMinutes(this.getDuration())));
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setDate(toDate(rs.getInt("date")));
        this.setTime(toTime(rs.getInt("time")));
        this.setLocationId(rs.getInt("locationid"));
        this.setRoom(rs.getString("room"));
        this.setDuration(rs.getInt("duration"));
        this.setLessonId(rs.getInt("lessonid"));
        this.setDateOld(toDate(rs.getInt("dateold")));
        this.setCategory(rs.getInt("category"));
        this.setStatus(rs.getInt("status"));
        this.setDescription(rs.getString("description"));
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");
        
        if (this.getDate() != LocalDate.MIN) columns.add("date");
        if (this.getTime() != LocalTime.MAX) columns.add("time");
        if (this.getLocationId() != 0) columns.add("locationid");
        if (!this.getRoom().isBlank()) columns.add("room");
        if (this.getDuration() != 0) columns.add("duration");
        if (this.getLessonId() != 0) columns.add("lessonid");
        if (this.getDateOld() != LocalDate.MIN) columns.add("dateold");
        columns.add("category");
        columns.add("status");
        if (!this.getDescription().isBlank()) columns.add("description");
        
        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'");

        if (this.getDate() != LocalDate.MIN) values.add(String.valueOf(toInt(this.getDate())));
        if (this.getTime() != LocalTime.MAX) values.add(String.valueOf(toInt(this.getTime())));
        if (this.getLocationId() != 0) values.add(String.valueOf(this.getLocationId()));
        if (!this.getRoom().isBlank()) values.add(this.getRoom());
        if (this.getDuration() != 0) values.add(String.valueOf(this.getDuration()));
        if (this.getLessonId() != 0) values.add(String.valueOf(this.getLessonId()));
        if (this.getDateOld() != LocalDate.MIN) values.add(String.valueOf(toInt(this.getDateOld())));
        values.add(String.valueOf(this.getCategory()));
        values.add(String.valueOf(this.getStatus()));
        if (!this.getDescription().isBlank()) values.add(this.getDescription());

        return values.toString();
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder();

        sb.append("date        = ").append(this.getDate() == LocalDate.MIN ? "null" : toInt(this.getDate())).append(", ");
        sb.append("time        = ").append(this.getTime() == LocalTime.MAX ? "null" : toInt(this.getTime())).append(", ");
        sb.append("duration    = ").append(this.getDuration()).append(", ");
        sb.append("locationid  = ").append(this.getLocationId() == 0 ? "null" : this.getLocationId()).append(", ");
        sb.append("room        = ").append(this.getRoom().isBlank() ? "null" : this.getRoom()).append(", ");
        sb.append("lessonid    = ").append(this.getLessonId() == 0 ? "null" : this.getLessonId()).append(", ");
        sb.append("dateold     = ").append(this.getDateOld() == LocalDate.MIN ? "null" : toInt(this.getDateOld())).append(", ");
        sb.append("category    = ").append(this.getCategory()).append(", ");
        sb.append("status      = ").append(this.getStatus()).append(", ");
        sb.append("description = '").append(this.getDescription()).append("'");

        return sb.toString();
    }

    @Override
    public int compareTo(Appointment o) {
        if (this.getDate() == null || o.getDate() == null) return 0;
        return getDate().compareTo(o.getDate());
    }
}
