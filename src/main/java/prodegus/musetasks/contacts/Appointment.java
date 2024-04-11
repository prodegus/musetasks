package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Appointment {

    private SimpleIntegerProperty id          = new SimpleIntegerProperty();
    private SimpleStringProperty  date        = new SimpleStringProperty();
    private SimpleStringProperty  time        = new SimpleStringProperty();
    private SimpleIntegerProperty lessonId    = new SimpleIntegerProperty();
    private SimpleStringProperty  description = new SimpleStringProperty();

    private SimpleBooleanProperty selected    = new SimpleBooleanProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int appointmentId) {
        this.id.set(appointmentId);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
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

    public String id() {
        return String.valueOf(this.getId());
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt(1));
        this.setDate(rs.getString(2));
        this.setTime(rs.getString(3));
        this.setLessonId(rs.getInt(4));
        this.setDescription(rs.getString(5));
        this.setSelected(false);
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("'NULL', '");
        sb.append(this.getDate());
        sb.append("', '");
        sb.append(this.getTime());
        sb.append("', '");
        sb.append(this.getLessonId());
        sb.append("', '");
        sb.append(this.getDescription());
        sb.append("'");
        return sb.toString();
    }

}
