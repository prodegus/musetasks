package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Task {

    private SimpleIntegerProperty id          = new SimpleIntegerProperty();
    private SimpleStringProperty  description = new SimpleStringProperty();
    private SimpleStringProperty  deadline    = new SimpleStringProperty();
    private SimpleStringProperty  comment     = new SimpleStringProperty();
    private SimpleStringProperty  status      = new SimpleStringProperty();
    private SimpleBooleanProperty selected    = new SimpleBooleanProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
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

    public String getDeadline() {
        return deadline.get();
    }

    public SimpleStringProperty deadlineProperty() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline.set(deadline);
    }

    public String getComment() {
        return comment.get();
    }

    public SimpleStringProperty commentProperty() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment.set(comment);
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
        this.setDescription(rs.getString(2));
        this.setDeadline(rs.getString(3));
        this.setComment(rs.getString(4));
        this.setStatus(rs.getString(5));
        this.setSelected(false);
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("'NULL', '");
        sb.append(this.getDescription());
        sb.append("', '");
        sb.append(this.getDeadline());
        sb.append("', '");
        sb.append(this.getComment());
        sb.append("', '");
        sb.append(this.getStatus());
        sb.append("'");
        return sb.toString();
    }

}
