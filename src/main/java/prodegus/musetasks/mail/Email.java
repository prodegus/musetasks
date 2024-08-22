package prodegus.musetasks.mail;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import prodegus.musetasks.appointments.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringJoiner;

import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.DateTime.toInt;

public class Email implements Comparable {
    private SimpleIntegerProperty id          = new SimpleIntegerProperty();
    private LocalDate             date        = LocalDate.EPOCH;
    private LocalTime             time        = LocalTime.MIN;
    private SimpleStringProperty  from        = new SimpleStringProperty();
    private SimpleStringProperty  to          = new SimpleStringProperty();
    private SimpleStringProperty  cc          = new SimpleStringProperty();
    private SimpleStringProperty  subject     = new SimpleStringProperty();
    private SimpleStringProperty  message     = new SimpleStringProperty();
    private SimpleStringProperty  attachments = new SimpleStringProperty();
    private SimpleBooleanProperty draft       = new SimpleBooleanProperty();

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

    public String getFrom() {
        return from.get();
    }

    public SimpleStringProperty fromProperty() {
        return from;
    }

    public void setFrom(String from) {
        this.from.set(from);
    }

    public String getTo() {
        return to.get();
    }

    public SimpleStringProperty toProperty() {
        return to;
    }

    public void setTo(String to) {
        this.to.set(to);
    }

    public String getCc() {
        return cc.get();
    }

    public SimpleStringProperty ccProperty() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc.set(cc);
    }

    public String getSubject() {
        return subject.get();
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public String getMessage() {
        return message.get();
    }

    public SimpleStringProperty messageProperty() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public String getAttachments() {
        return attachments.get();
    }

    public SimpleStringProperty attachmentsProperty() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments.set(attachments);
    }

    public boolean isDraft() {
        return draft.get();
    }

    public SimpleBooleanProperty draftProperty() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft.set(draft);
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

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setDate(toDate(rs.getInt("date")));
        this.setTime(toTime(rs.getInt("time")));
        this.setFrom(rs.getString("sentfrom"));
        this.setTo(rs.getString("sentto"));
        this.setCc(rs.getString("cc"));
        this.setSubject(rs.getString("subject"));
        this.setMessage(rs.getString("message"));
        this.setAttachments(rs.getString("attachments"));
        this.setDraft(rs.getInt("draft") == 1);
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");

        columns.add("date");
        columns.add("time");
        columns.add("sentfrom");
        columns.add("sentto");
        columns.add("cc");
        columns.add("subject");
        columns.add("message");
        columns.add("attachments");
        columns.add("draft");

        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'");

        values.add(String.valueOf(toInt(this.getDate())));
        values.add(String.valueOf(toInt(this.getTime())));
        values.add(this.getFrom());
        values.add(this.getTo());
        values.add(this.getCc());
        values.add(this.getSubject());
        values.add(this.getMessage());
        values.add(this.getAttachments());
        values.add(this.isDraft() ? "1" : "0");

        return values.toString();
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder();

        sb.append("date = ").append(toInt(this.getDate())).append(", ");
        sb.append("time = ").append(toInt(this.getTime())).append(", ");
        sb.append("sentfrom = '").append(this.getFrom()).append("', ");
        sb.append("sentto = '").append(this.getTo()).append("', ");
        sb.append("cc = '").append(this.getCc()).append("', ");
        sb.append("subject = '").append(this.getSubject()).append("', ");
        sb.append("message = '").append(this.getMessage()).append("', ");
        sb.append("attachments = '").append(this.getAttachments()).append("', ");
        sb.append("draft = ").append(this.isDraft() ? "1" : "0");

        return sb.toString();
    }

    @Override
    public int compareTo(Object o) {
        if (this.getDate() == null || ((Email) o).getDate() == null) return 0;
        if (this.getDate().equals(((Email) o).getDate())) return this.getTime().compareTo(((Email) o).getTime());
        return getDate().compareTo(((Email) o).getDate());
    }

    public String dateTime() {
        return asString(this.getDate()) + ", " + this.getTime().toString();
    }
}
