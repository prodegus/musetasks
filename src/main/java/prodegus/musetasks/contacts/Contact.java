package prodegus.musetasks.contacts;

import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.lessons.Lesson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

import static prodegus.musetasks.contacts.ContactModel.*;
import static prodegus.musetasks.contacts.OtherModel.getOtherFromDB;
import static prodegus.musetasks.contacts.ParentModel.getParentFromDB;
import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherFromDB;
import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.lessons.LessonModel.LESSON_STATUS_ACTIVE;
import static prodegus.musetasks.utils.Strings.string;

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
        return this.getFirstName() + " " + this.getLastName().charAt(0) + ".";
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setLastName(rs.getString("lastname"));
        this.setFirstName(rs.getString("firstname"));
        this.setCategory(rs.getInt("category"));
        this.setCustomerId(rs.getInt("customerid"));
        this.setLocation(string(rs.getString("location")));
        this.setStreet(string(rs.getString("street")));
        this.setPostalCode(rs.getInt("postalcode"));
        this.setCity(string(rs.getString("city")));
        this.setPhone(string(rs.getString("phone")));
        this.setEmail(string(rs.getString("email")));
        this.setZoom(string(rs.getString("zoom")));
        this.setSkype(string(rs.getString("skype")));
        this.setBirthDate(string(rs.getString("birthday")));
        this.setNotes(string(rs.getString("notes")));
        this.setSelected(false);
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");

        columns.add("lastname");
        columns.add("firstname");
        columns.add("category");
        if (this.getCustomerId() != 0) columns.add("customerid");
        if (this.getLocation() != null) columns.add("location");
        if (!this.getStreet().isBlank()) columns.add("street");
        if (this.getPostalCode() != 0) columns.add("postalcode");
        if (!this.getCity().isBlank()) columns.add("city");
        if (!this.getPhone().isBlank()) columns.add("phone");
        if (!this.getEmail().isBlank()) columns.add("email");
        if (!this.getZoom().isBlank()) columns.add("zoom");
        if (!this.getSkype().isBlank()) columns.add("skype");
        if (!this.getBirthDate().isBlank()) columns.add("birthday");
        if (!this.getNotes().isBlank()) columns.add("notes");
        
        return columns.toString();
    }
    
    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'");

        values.add(this.getLastName());
        values.add(this.getFirstName());
        values.add(String.valueOf(this.getCategory()));
        if (this.getCustomerId() != 0) values.add(String.valueOf(this.getCustomerId()));
        if (this.getLocation() != null) values.add(this.getLocation());
        if (!this.getStreet().isBlank()) values.add(this.getStreet());
        if (this.getPostalCode() != 0) values.add(String.valueOf(this.getPostalCode()));
        if (!this.getCity().isBlank()) values.add(this.getCity());
        if (!this.getPhone().isBlank()) values.add(this.getPhone());
        if (!this.getEmail().isBlank()) values.add(this.getEmail());
        if (!this.getZoom().isBlank()) values.add(this.getZoom());
        if (!this.getSkype().isBlank()) values.add(this.getSkype());
        if (!this.getBirthDate().isBlank()) values.add(this.getBirthDate());
        if (!this.getNotes().isBlank()) values.add(this.getNotes());
        
        return values.toString();
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
        if (this.isStudent()) return !this.toStudent().hasParent() && this.toStudent().hasActiveLesson();
        if (this.isParent()) {
            for (Student student : this.toParent().children()) {
                if (student.hasActiveLesson()) return true;
            }
        }
        return false;
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

    public String getPostalCodeCity() {
        return (getPostalCode() == 0 ? "" : getPostalCode() + " ") + getCity();
    }

    @Override
    public String toString() {
        return this.formalName();
    }

    public ObservableList<String> getAllMail() {
        Set<String> mailList = new HashSet<>();
        if (!this.getEmail().isEmpty()) mailList.add(this.getEmail());
        if (!this.getZoom().isEmpty()) mailList.add(this.getZoom());
        if (!this.getSkype().isEmpty()) mailList.add(this.getSkype());
        if (this.isStudent()) {
            for (Parent parent : this.toStudent().parents()) {
                if (!parent.getEmail().isEmpty()) mailList.add(parent.getEmail());
            }
        }
        return FXCollections.observableArrayList(mailList);
    }
}
