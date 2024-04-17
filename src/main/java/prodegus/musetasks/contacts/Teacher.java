package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        this.setInstruments(rs.getString(15));
        this.setActiveDays(rs.getString(16));
        this.setStatus(rs.getString(17));
        this.setStatusFrom(rs.getString(18));
        this.setStatusTo(rs.getString(19));
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

}
