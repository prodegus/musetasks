package prodegus.musetasks.appointments;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;

import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.DateTime.toInt;

public class Appointment {

    private SimpleIntegerProperty id          = new SimpleIntegerProperty();
    private LocalDate             date        = LocalDate.MIN;
    private LocalTime             time        = LocalTime.MAX;
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
        return switch(this.getStatus()) {
            case 1 -> "Planmäßig";
            case 2 -> "Abgesagt (Schüler)";
            case 3 -> "Abgesagt (Lehrer)";
            case 4 -> "Abgesagt";
            case 5 -> "Nachholtermin geplant";
            case 6 -> "Nachgeholt";
            case 7 -> "Verschoben";
            default -> "";
        };
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setDate(toDate(rs.getInt("date")));
        this.setTime(toTime(rs.getInt("time")));
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
        if (this.getDuration() != 0) values.add("duration");
        if (this.getLessonId() != 0) values.add("lessonid");
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
        sb.append("lessonid    = ").append(this.getLessonId() == 0 ? "null" : this.getLessonId()).append(", ");
        sb.append("dateold     = ").append(this.getDateOld() == LocalDate.MIN ? "null" : toInt(this.getDateOld())).append(", ");
        sb.append("category    = ").append(this.getCategory()).append(", ");
        sb.append("status      = ").append(this.getStatus()).append(", ");
        sb.append("description = '").append(this.getDescription()).append("'");

        return sb.toString();
    }

}
