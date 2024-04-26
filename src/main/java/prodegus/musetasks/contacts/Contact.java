package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.OtherModel.getOtherFromDB;
import static prodegus.musetasks.contacts.ParentModel.getParentFromDB;
import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherFromDB;
import static prodegus.musetasks.database.Database.*;

public class Contact {

    private SimpleIntegerProperty id         = new SimpleIntegerProperty();
    private SimpleStringProperty  lastName   = new SimpleStringProperty();
    private SimpleStringProperty  firstName  = new SimpleStringProperty();
    private SimpleIntegerProperty category   = new SimpleIntegerProperty();
    private SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private SimpleStringProperty  location   = new SimpleStringProperty();
    private SimpleStringProperty  street     = new SimpleStringProperty();
    private SimpleIntegerProperty postalCode = new SimpleIntegerProperty();
    private SimpleStringProperty  city       = new SimpleStringProperty();
    private SimpleStringProperty  phone      = new SimpleStringProperty();
    private SimpleStringProperty  email      = new SimpleStringProperty();
    private SimpleStringProperty  zoom       = new SimpleStringProperty();
    private SimpleStringProperty  skype      = new SimpleStringProperty();
    private SimpleStringProperty  birthDate  = new SimpleStringProperty();
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

    public String getLastName() {
        return lastName.get();
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
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

    public int getCustomerId() {
        return customerId.get();
    }

    public SimpleIntegerProperty customerIdProperty() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
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

    public int getPostalCode() {
        return postalCode.get();
    }

    public SimpleIntegerProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
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

    public String getBirthDate() {
        return birthDate.get();
    }

    public SimpleStringProperty birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
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

    public String category() {
        return switch (this.getCategory()) {
            case CATEGORY_STUDENT -> "Schüler";
            case CATEGORY_PROSPECTIVE_STUDENT -> "Schüler (I)";
            case CATEGORY_PARENT -> "Eltern";
            case CATEGORY_PROSPECTIVE_PARENT -> "Eltern (I)";
            case CATEGORY_TEACHER -> "Lehrer";
            case CATEGORY_OTHER -> "Sonstige";
            default -> null;
        };
    }

    public String table() {
        String tableName = switch (this.getCategory()) {
            case CATEGORY_STUDENT, CATEGORY_PROSPECTIVE_STUDENT -> STUDENT_TABLE;
            case CATEGORY_TEACHER -> TEACHER_TABLE;
            case CATEGORY_PARENT, CATEGORY_PROSPECTIVE_PARENT -> PARENT_TABLE;
            case CATEGORY_OTHER -> OTHER_TABLE;
            default -> null;
        };
        return tableName;
    }

    public int age() {
        LocalDate birthDate = LocalDate.parse(getBirthDate(), DATE_FORMATTER);
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String id() {
        return String.valueOf(this.getId());
    }

    public String name() {
        return this.getFirstName() + " " + this.getLastName();
    }

    public String formalName() { return this.getLastName() + ", " + this.getFirstName(); }

    public String shortName() {
        return this.getFirstName() + " " + this.getLastName().charAt(0);
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setLastName(rs.getString("lastname"));
        this.setFirstName(rs.getString("firstname"));
        this.setCategory(rs.getInt("category"));
        this.setCustomerId(rs.getInt("customerid"));
        this.setLocation(rs.getString("location"));
        this.setStreet(rs.getString("street"));
        this.setPostalCode(rs.getInt("postalcode"));
        this.setCity(rs.getString("city"));
        this.setPhone(rs.getString("phone"));
        this.setEmail(rs.getString("email"));
        this.setZoom(rs.getString("zoom"));
        this.setSkype(rs.getString("skype"));
        this.setBirthDate(rs.getString("birthday"));
        this.setNotes(rs.getString("notes"));
        this.setSelected(false);
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder("null, '");
        sb.append(this.getLastName()).append("', '");
        sb.append(this.getFirstName()).append("', '");
        sb.append(this.getCategory()).append("', '");
        sb.append(this.getCustomerId()).append("', '");
        sb.append(this.getLocation()).append("', '");
        sb.append(this.getStreet()).append("', '");
        sb.append(this.getPostalCode()).append("', '");
        sb.append(this.getCity()).append("', '");
        sb.append(this.getPhone()).append("', '");
        sb.append(this.getEmail()).append("', '");
        sb.append(this.getZoom()).append("', '");
        sb.append(this.getSkype()).append("', '");
        sb.append(this.getBirthDate()).append("', '");
        sb.append(this.getNotes()).append("'");
        return sb.toString();
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("lastname   = '").append(this.getLastName()).append("', ");
        sb.append("firstname  = '").append(this.getFirstName()).append("', ");
        sb.append("category   = ").append(this.getCategory()).append(", ");
        sb.append("customerid = ").append(this.getCustomerId()).append(", ");
        sb.append("location   = '").append(this.getLocation()).append("', ");
        sb.append("street     = '").append(this.getStreet()).append("', ");
        sb.append("postalcode = ").append(this.getPostalCode()).append(", ");
        sb.append("city       = '").append(this.getCity()).append("', ");
        sb.append("phone      = '").append(this.getPhone()).append("', ");
        sb.append("email      = '").append(this.getEmail()).append("', ");
        sb.append("zoom       = '").append(this.getZoom()).append("', ");
        sb.append("skype      = '").append(this.getSkype()).append("', ");
        sb.append("birthday   = '").append(this.getBirthDate()).append("', ");
        sb.append("notes      = '").append(this.getNotes()).append("'");
        return sb.toString();
    }

    public boolean hasInstrument(String instrument) {
        if (this.isStudent()) {
            return this.toStudent().instruments().contains(instrument);
        }
        if (this.isParent()) {
            for (Student student : this.toParent().children()) {
                if (student.instruments().contains(instrument)) return true;
            }
        }
        if (this.isTeacher()) {
            return this.toTeacher().getInstruments().contains(instrument);
        }
        return false;
    }

    public boolean isStudent() {
        return this.getCategory() == CATEGORY_STUDENT || this.getCategory() == CATEGORY_PROSPECTIVE_STUDENT;
    }

    public boolean isParent() {
        return this.getCategory() == CATEGORY_PARENT || this.getCategory() == CATEGORY_PROSPECTIVE_PARENT;
    }

    public boolean isTeacher() {
        return this.getCategory() == CATEGORY_TEACHER;
    }

    public boolean isOther() {
        return this.getCategory() == CATEGORY_OTHER;
    }

    public boolean isProspective() {
        return this.getCategory() == CATEGORY_PROSPECTIVE_STUDENT ||
                this.getCategory() == CATEGORY_PROSPECTIVE_PARENT;
    }

    public boolean isCustomer() {
        if (this.customerIdProperty() == null) return false;
        return this.getCustomerId() > 0;
    }

    public Student toStudent() {
        return this.isStudent() ? getStudentFromDB(this.getId()) : null;
    }

    public Parent toParent() {
        return this.isParent() ? getParentFromDB(this.getId()) : null;
    }

    public Teacher toTeacher() {
        return this.isTeacher() ? getTeacherFromDB(this.getId()) : null;
    }

    public Other toOther() {
        return this.isOther() ? getOtherFromDB(this.getId()) : null;
    }

    public boolean hasTeacher(Teacher teacher) {
        if (this.isStudent())
            return this.toStudent().hasTeacher(teacher);
        if (this.isParent()) {
            for (Student child : this.toParent().children()) {
                if (child.hasTeacher(teacher)) return true;
            }
        }
        return false;
    }
}
