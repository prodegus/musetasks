package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

import static prodegus.musetasks.utils.Strings.string;

public class Other extends Contact {
    private SimpleStringProperty description = new SimpleStringProperty();

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setDescription(string(rs.getString("description")));
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder(super.valuesToSQLUpdateString()).append(", ");
        sb.append("description = '").append(this.getDescription()).append("'");
        return sb.toString();
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");
        columns.add(super.sqlColumns());
        if (!this.getDescription().isBlank()) columns.add("description");
        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'").setEmptyValue("");
        StringJoiner result = new StringJoiner(", ").add(super.sqlValues());
        if (!this.getDescription().isBlank()) values.add(this.getDescription());
        if (!values.toString().isEmpty()) result.add(values.toString());
        return result.toString();
    }

}
