package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

import static prodegus.musetasks.database.Database.*;

public class Contact {

    private SimpleIntegerProperty id         = new SimpleIntegerProperty();
    private SimpleStringProperty  lastname   = new SimpleStringProperty();
    private SimpleStringProperty  firstname  = new SimpleStringProperty();
    private SimpleStringProperty  category   = new SimpleStringProperty();
    private SimpleStringProperty  location   = new SimpleStringProperty();
    private SimpleStringProperty  street     = new SimpleStringProperty();
    private SimpleIntegerProperty postalcode = new SimpleIntegerProperty();
    private SimpleStringProperty  city       = new SimpleStringProperty();
    private SimpleStringProperty  phone      = new SimpleStringProperty();
    private SimpleStringProperty  email      = new SimpleStringProperty();
    private SimpleStringProperty  zoom       = new SimpleStringProperty();
    private SimpleStringProperty  skype      = new SimpleStringProperty();
    private SimpleStringProperty  birthday   = new SimpleStringProperty();
    private SimpleStringProperty  notes      = new SimpleStringProperty();
    private SimpleBooleanProperty selected   = new SimpleBooleanProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getLastname() {
        return lastname.get();
    }

    public SimpleStringProperty lastnameProperty() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname.set(lastname);
    }

    public String getFirstname() {
        return firstname.get();
    }

    public SimpleStringProperty firstnameProperty() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname.set(firstname);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public String getStreet() {
        return street.get();
    }

    public SimpleStringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public int getPostalcode() {
        return postalcode.get();
    }

    public SimpleIntegerProperty postalcodeProperty() {
        return postalcode;
    }

    public void setPostalcode(int postalcode) {
        this.postalcode.set(postalcode);
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getZoom() {
        return zoom.get();
    }

    public SimpleStringProperty zoomProperty() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom.set(zoom);
    }

    public String getSkype() {
        return skype.get();
    }

    public SimpleStringProperty skypeProperty() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype.set(skype);
    }

    public String getBirthday() {
        return birthday.get();
    }

    public SimpleStringProperty birthdayProperty() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday.set(birthday);
    }

    public String getNotes() {
        return notes.get();
    }

    public SimpleStringProperty notesProperty() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes.set(notes);
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

    public String table() {
        String tableName = switch (this.getCategory()) {
            case "Schüler" -> STUDENT_TABLE;
            case "Lehrer" -> TEACHER_TABLE;
            case "Eltern" -> PARENT_TABLE;
            case "Sonstige" -> OTHER_TABLE;
            default -> "";
        };
        return tableName;
    }

    public String id() {
        return String.valueOf(this.getId());
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt(1));
        this.setLastname(rs.getString(2));
        this.setFirstname(rs.getString(3));
        this.setCategory(rs.getString(4));
        this.setLocation(rs.getString(5));
        this.setStreet(rs.getString(6));
        this.setPostalcode(rs.getInt(7));
        this.setCity(rs.getString(8));
        this.setPhone(rs.getString(9));
        this.setEmail(rs.getString(10));
        this.setZoom(rs.getString(11));
        this.setSkype(rs.getString(12));
        this.setBirthday(rs.getString(13));
        this.setNotes(rs.getString(14));
        this.setSelected(false);
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("'NULL', '");
        sb.append(this.getLastname());
        sb.append("', '");
        sb.append(this.getFirstname());
        sb.append("', '");
        sb.append(this.getCategory());
        sb.append("', '");
        sb.append(this.getLocation());
        sb.append("', '");
        sb.append(this.getStreet());
        sb.append("', '");
        sb.append(this.getPostalcode());
        sb.append("', '");
        sb.append(this.getCity());
        sb.append("', '");
        sb.append(this.getPhone());
        sb.append("', '");
        sb.append(this.getEmail());
        sb.append("', '");
        sb.append(this.getZoom());
        sb.append("', '");
        sb.append(this.getSkype());
        sb.append("', '");
        sb.append(this.getBirthday());
        sb.append("', '");
        sb.append(this.getNotes());
        sb.append("'");
        return sb.toString();
    }
}
