package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import prodegus.musetasks.lessons.Lesson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringJoiner;

import static prodegus.musetasks.contacts.ContactModel.updateContact;
import static prodegus.musetasks.lessons.LessonModel.getLessonFromDB;
import static prodegus.musetasks.contacts.ParentModel.getParentFromDB;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherFromDB;
import static prodegus.musetasks.utils.Strings.string;

public class Student extends Contact {

    private SimpleStringProperty  instrument1 = new SimpleStringProperty();
    private SimpleStringProperty  instrument2 = new SimpleStringProperty();
    private SimpleStringProperty  instrument3 = new SimpleStringProperty();
    private SimpleBooleanProperty prospective = new SimpleBooleanProperty();
    private SimpleStringProperty  status      = new SimpleStringProperty();
    private SimpleStringProperty  statusFrom  = new SimpleStringProperty();
    private SimpleStringProperty  statusTo    = new SimpleStringProperty();
    private SimpleIntegerProperty parentId1   = new SimpleIntegerProperty();
    private SimpleIntegerProperty parentId2   = new SimpleIntegerProperty();
    private SimpleIntegerProperty teacherId1  = new SimpleIntegerProperty();
    private SimpleIntegerProperty teacherId2  = new SimpleIntegerProperty();
    private SimpleIntegerProperty teacherId3  = new SimpleIntegerProperty();
    private SimpleIntegerProperty lessonId1   = new SimpleIntegerProperty();
    private SimpleIntegerProperty lessonId2   = new SimpleIntegerProperty();
    private SimpleIntegerProperty lessonId3   = new SimpleIntegerProperty();

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

    public boolean getProspective() {
        return prospective.get();
    }

    public SimpleBooleanProperty prospectiveProperty() {
        return prospective;
    }

    public void setProspective(boolean prospective) {
        this.prospective.set(prospective);
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

    public int getParentId1() {
        return parentId1.get();
    }

    public SimpleIntegerProperty parentId1Property() {
        return parentId1;
    }

    public void setParentId1(int parentId1) {
        this.parentId1.set(parentId1);
    }

    public int getParentId2() {
        return parentId2.get();
    }

    public SimpleIntegerProperty parentId2Property() {
        return parentId2;
    }

    public void setParentId2(int parentId2) {
        this.parentId2.set(parentId2);
    }

    public int getTeacherId1() {
        return teacherId1.get();
    }

    public SimpleIntegerProperty teacherId1Property() {
        return teacherId1;
    }

    public void setTeacherId1(int teacherId1) {
        this.teacherId1.set(teacherId1);
    }

    public int getTeacherId2() {
        return teacherId2.get();
    }

    public SimpleIntegerProperty teacherId2Property() {
        return teacherId2;
    }

    public void setTeacherId2(int teacherId2) {
        this.teacherId2.set(teacherId2);
    }

    public int getTeacherId3() {
        return teacherId3.get();
    }

    public SimpleIntegerProperty teacherId3Property() {
        return teacherId3;
    }

    public void setTeacherId3(int teacherId3) {
        this.teacherId3.set(teacherId3);
    }

    public int getLessonId1() {
        return lessonId1.get();
    }

    public SimpleIntegerProperty lessonId1Property() {
        return lessonId1;
    }

    public void setLessonId1(int lessonId1) {
        this.lessonId1.set(lessonId1);
    }

    public int getLessonId2() {
        return lessonId2.get();
    }

    public SimpleIntegerProperty lessonId2Property() {
        return lessonId2;
    }

    public void setLessonId2(int lessonId2) {
        this.lessonId2.set(lessonId2);
    }

    public int getLessonId3() {
        return lessonId3.get();
    }

    public SimpleIntegerProperty lessonId3Property() {
        return lessonId3;
    }

    public void setLessonId3(int lessonId3) {
        this.lessonId3.set(lessonId3);
    }

    public Parent parent1() {
        return getParentFromDB(getParentId1());
    }

    public Parent parent2() {
        return getParentFromDB(getParentId2());
    }

    public Teacher teacher1() {
        return this.getTeacherId1() == 0 ? null : getTeacherFromDB(getTeacherId1());
    }

    public Teacher teacher2() {
        return this.getTeacherId2() == 0 ? null : getTeacherFromDB(getTeacherId2());
    }

    public Teacher teacher3() {
        return this.getTeacherId3() == 0 ? null : getTeacherFromDB(getTeacherId3());
    }

    public Lesson lesson1() {
        return getLessonFromDB(getLessonId1());
    }

    public Lesson lesson2() {
        return getLessonFromDB(getLessonId2());
    }

    public Lesson lesson3() {
        return getLessonFromDB(getLessonId3());
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setInstrument1(string(rs.getString("instrument1")));
        this.setInstrument2(string(rs.getString("instrument2")));
        this.setInstrument3(string(rs.getString("instrument3")));
        this.setProspective(rs.getInt("prospective") == 1);
        this.setStatus(string(rs.getString("status")));
        this.setStatusFrom(string(rs.getString("statusfrom")));
        this.setStatusTo(string(rs.getString("statusto")));
        this.setParentId1(rs.getInt("parentid1"));
        this.setParentId2(rs.getInt("parentid2"));
        this.setTeacherId1(rs.getInt("teacherid1"));
        this.setTeacherId2(rs.getInt("teacherid2"));
        this.setTeacherId3(rs.getInt("teacherid3"));
        this.setLessonId1(rs.getInt("lessonid1"));
        this.setLessonId2(rs.getInt("lessonid2"));
        this.setLessonId3(rs.getInt("lessonid3"));
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder(super.valuesToSQLUpdateString()).append(", ");
        sb.append("instrument1 = '").append(this.getInstrument1()).append("', ");
        sb.append("instrument2 = '").append(this.getInstrument2()).append("', ");
        sb.append("instrument3 = '").append(this.getInstrument3()).append("', ");
        sb.append("prospective = ").append(this.getProspective() ? "1" : "0").append(", ");
        sb.append("status      = '").append(this.getStatus()).append("', ");
        sb.append("statusfrom  = '").append(this.getStatusFrom()).append("', ");
        sb.append("statusto    = '").append(this.getStatusTo()).append("', ");
        sb.append("parentid1   = ").append(this.getParentId1() == 0 ? "null" : this.getParentId1()).append(", ");
        sb.append("parentid2   = ").append(this.getParentId2() == 0 ? "null" : this.getParentId2()).append(", ");
        sb.append("teacherid1  = ").append(this.getTeacherId1() == 0 ? "null" : this.getTeacherId1()).append(", ");
        sb.append("teacherid2  = ").append(this.getTeacherId2() == 0 ? "null" : this.getTeacherId2()).append(", ");
        sb.append("teacherid3  = ").append(this.getTeacherId3() == 0 ? "null" : this.getTeacherId3()).append(", ");
        sb.append("lessonid1   = ").append(this.getLessonId1() == 0 ? "null" : this.getLessonId1()).append(", ");
        sb.append("lessonid2   = ").append(this.getLessonId2() == 0 ? "null" : this.getLessonId2()).append(", ");
        sb.append("lessonid3   = ").append(this.getLessonId3() == 0 ? "null" : this.getLessonId3());
        return sb.toString();
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");
        
        columns.add(super.sqlColumns());
        if (!this.getInstrument1().isBlank()) columns.add("instrument1");
        if (!this.getInstrument2().isBlank()) columns.add("instrument2");
        if (!this.getInstrument3().isBlank()) columns.add("instrument3");
        columns.add("prospective");
        if (!this.getStatus().isBlank()) columns.add("status");
        if (!this.getStatusFrom().isBlank()) columns.add("statusfrom");
        if (!this.getStatusTo().isBlank()) columns.add("statusto");
        if (this.getParentId1() != 0) columns.add("parentid1");
        if (this.getParentId2() != 0) columns.add("parentid2");
        if (this.getTeacherId1() != 0) columns.add("teacherid1");
        if (this.getTeacherId2() != 0) columns.add("teacherid2");
        if (this.getTeacherId3() != 0) columns.add("teacherid3");
        if (this.getLessonId1() != 0) columns.add("lessonid1");
        if (this.getLessonId2() != 0) columns.add("lessonid2");
        if (this.getLessonId3() != 0) columns.add("lessonid3");

        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'").setEmptyValue("");
        StringJoiner result = new StringJoiner(", ").add(super.sqlValues());

        if (!this.getInstrument1().isBlank()) values.add(this.getInstrument1());
        if (!this.getInstrument2().isBlank()) values.add(this.getInstrument2());
        if (!this.getInstrument3().isBlank()) values.add(this.getInstrument3());
        values.add((this.getProspective()) ? "1" : "0");
        if (!this.getStatus().isBlank()) values.add(this.getStatus());
        if (!this.getStatusFrom().isBlank()) values.add(this.getStatusFrom());
        if (!this.getStatusTo().isBlank()) values.add(this.getStatusTo());
        if (this.getParentId1() != 0) values.add(String.valueOf(this.getParentId1()));
        if (this.getParentId2() != 0) values.add(String.valueOf(this.getParentId2()));
        if (this.getTeacherId1() != 0) values.add(String.valueOf(this.getTeacherId1()));
        if (this.getTeacherId2() != 0) values.add(String.valueOf(this.getTeacherId2()));
        if (this.getTeacherId3() != 0) values.add(String.valueOf(this.getTeacherId3()));
        if (this.getLessonId1() != 0) values.add(String.valueOf(this.getLessonId1()));
        if (this.getLessonId2() != 0) values.add(String.valueOf(this.getLessonId2()));
        if (this.getLessonId3() != 0) values.add(String.valueOf(this.getLessonId3()));
        if (!values.toString().isEmpty()) result.add(values.toString());
        return result.toString();
    }

    public String ageAndBirthday() {
        if (this.getBirthDate().isEmpty()) return "";
        return this.age() + " (geb. " + this.getBirthDate() + ")";
    }

    public String instruments() {
        StringBuilder instruments = new StringBuilder();
        if (this.getInstrument1().isEmpty()) return "";
        instruments.append(this.getInstrument1());
        if (!this.getInstrument2().isEmpty()) {
            instruments.append(", ").append(this.getInstrument2());
        }
        if (!this.getInstrument3().isEmpty()) {
            instruments.append(", ").append(this.getInstrument3());
        }
        return instruments.toString();
    }

    public String lesson1Name() {
        if (this.getLessonId1() == 0) return "";
        return this.lesson1().getLessonName();
    }

    public String lesson1Time() {
        if (this.getLessonId1() == 0) return "";
        return this.lesson1().getTime();
    }

    public String lesson1Weekday() {
        if (this.getLessonId1() == 0) return "";
        return this.lesson1().weekdayToString();
    }

    public String lesson2Name() {
        if (this.getLessonId2() == 0) return "";
        return this.lesson2().getLessonName();
    }

    public String lesson2Time() {
        if (this.getLessonId2() == 0) return "";
        return this.lesson2().getTime();
    }

    public String lesson2Weekday() {
        if (this.getLessonId2() == 0) return "";
        return this.lesson2().weekdayToString();
    }

    public String lesson3Name() {
        if (this.getLessonId3() == 0) return "";
        return this.lesson3().getLessonName();
    }

    public String lesson3Time() {
        if (this.getLessonId3() == 0) return "";
        return this.lesson3().getTime();
    }

    public String lesson3Weekday() {
        if (this.getLessonId3() == 0) return "";
        return this.lesson3().weekdayToString();
    }

    public String parentsNames() {
        StringJoiner result = new StringJoiner(", ");
        if (this.getParentId1() != 0) result.add(parent1().name());
        if (this.getParentId2() != 0) result.add(parent2().name());
        return result.toString();
    }

    public String status() {
        StringBuilder status = new StringBuilder();
        if (this.getStatus().isEmpty()) return "";
        status.append(this.getStatus());
        if (!this.getStatusFrom().isEmpty()) {
            status.append(" ab ");
            status.append(this.getStatusFrom());
        }
        if (!this.getStatusTo().isEmpty()) {
            status.append(" bis ");
            status.append(this.getStatusTo());
        }

        return status.toString();
    }

    public String teacher1Name() {
        if (this.getTeacherId1() == 0) return "";
        return this.teacher1().name();
    }

    public String teacher2Name() {
        if (this.getTeacherId2() == 0) return "";
        return this.teacher2().name();
    }

    public String teacher3Name() {
        if (this.getTeacherId3() == 0) return "";
        return this.teacher3().name();
    }

    @Override
    public String toString() {
        return "Student [" +
                "id: " + this.getId() + ", " +
                "lastname: " + this.getLastName() + ", " +
                "firstname: " + this.getFirstName() + ", " +
                "category: " + this.getCategory() + ", " +
                "customer: " + this.getCustomerId() + ", " +
                "location: " + this.getLocation() + ", " +
                "street: " + this.getStreet() + ", " +
                "postalCode: " + this.getPostalCode() + ", " +
                "city: " + this.getCity() + ", " +
                "phone: " + this.getPhone() + ", " +
                "email: " + this.getEmail() + ", " +
                "zoom: " + this.getZoom() + ", " +
                "skype: " + this.getSkype() + ", " +
                "birthDate: " + this.getBirthDate() + ", " +
                "notes: " + this.getNotes() + ", " +
                "selected: " + this.isSelected() + ", " +
                "instrument1: " + this.getInstrument1() + ", " +
                "instrument2: " + this.getInstrument2() + ", " +
                "instrument3: " + this.getInstrument3() + ", " +
                "prospective: " + this.getProspective() + ", " +
                "status: " + this.getStatus() + ", " +
                "statusFrom: " + this.getStatusFrom() + ", " +
                "statusTo: " + this.getStatusTo() + ", " +
                "parentId1: " + this.getParentId1() + ", " +
                "parentId2: " + this.getParentId2() + ", " +
                "teacherId1: " + this.getTeacherId1() + ", " +
                "teacherId2: " + this.getTeacherId2() + ", " +
                "teacherId3: " + this.getTeacherId3() + ", " +
                "lessonId1: " + this.getLessonId1() + ", " +
                "lessonId2: " + this.getLessonId2() + ", " +
                "lessonId3: " + this.getLessonId3() + "]";
    }

    public void addParentInDB(int parentId) {
        if (this.hasParent(parentId)) return;

        if (this.getParentId1() == 0) {
            updateContact(this, "parentid1", parentId);
            return;
        }
        if (this.getParentId2() == 0) {
            updateContact(this, "parentid2", parentId);
        }
    }

    private boolean hasParent(int parentId) {
        return this.getParentId1() == parentId || this.getParentId2() == parentId;
    }

    public boolean hasTeacher(Teacher teacher) {
        return this.getTeacherId1() == teacher.getId() ||
                this.getTeacherId2() == teacher.getId() ||
                this.getTeacherId3() == teacher.getId();
    }


}
