package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Teacher extends Contact {

    private SimpleStringProperty instrument1 = new SimpleStringProperty();
    private SimpleStringProperty instrument2 = new SimpleStringProperty();
    private SimpleStringProperty instrument3 = new SimpleStringProperty();
    private SimpleStringProperty instrument4 = new SimpleStringProperty();
    private SimpleStringProperty instrument5 = new SimpleStringProperty();
    private SimpleStringProperty activeSince = new SimpleStringProperty();

    public String getInstrument1() {
        return instrument1.get();
    }

    public SimpleStringProperty instrument1Property() {
        return instrument1;
    }

    public void setInstrument1(String instrument1) {
        this.instrument1.set(instrument1);
    }

    public String getInstrument2() {
        return instrument2.get();
    }

    public SimpleStringProperty instrument2Property() {
        return instrument2;
    }

    public void setInstrument2(String instrument2) {
        this.instrument2.set(instrument2);
    }

    public String getInstrument3() {
        return instrument3.get();
    }

    public SimpleStringProperty instrument3Property() {
        return instrument3;
    }

    public void setInstrument3(String instrument3) {
        this.instrument3.set(instrument3);
    }

    public String getInstrument4() {
        return instrument4.get();
    }

    public SimpleStringProperty instrument4Property() {
        return instrument4;
    }

    public void setInstrument4(String instrument4) {
        this.instrument4.set(instrument4);
    }

    public String getInstrument5() {
        return instrument5.get();
    }

    public SimpleStringProperty instrument5Property() {
        return instrument5;
    }

    public void setInstrument5(String instrument5) {
        this.instrument5.set(instrument5);
    }

    public String getActiveSince() {
        return activeSince.get();
    }

    public SimpleStringProperty activeSinceProperty() {
        return activeSince;
    }

    public void setActiveSince(String activeSince) {
        this.activeSince.set(activeSince);
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setInstrument1(rs.getString(15));
        this.setInstrument2(rs.getString(16));
        this.setInstrument3(rs.getString(17));
        this.setInstrument4(rs.getString(18));
        this.setInstrument5(rs.getString(19));
        this.setActiveSince(rs.getString(20));
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.valuesToSQLString());
        sb.append(", '");
        sb.append(this.getInstrument1());
        sb.append("', '");
        sb.append(this.getInstrument2());
        sb.append("', '");
        sb.append(this.getInstrument3());
        sb.append("', '");
        sb.append(this.getInstrument4());
        sb.append("', '");
        sb.append(this.getInstrument5());
        sb.append("', '");
        sb.append(this.getActiveSince());
        sb.append("'");
        return sb.toString();
    }

}
