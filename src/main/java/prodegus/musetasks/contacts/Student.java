package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setInstrument1(rs.getString(15));
        this.setInstrument2(rs.getString(16));
        this.setInstrument3(rs.getString(17));
        this.setProspective(rs.getInt(18) == 1);
        this.setStatus(rs.getString(19));
        this.setStatusFrom(rs.getString(20));
        this.setStatusTo(rs.getString(21));
        this.setParentId1(rs.getInt(22));
        this.setParentId2(rs.getInt(23));
        this.setTeacherId1(rs.getInt(24));
        this.setTeacherId2(rs.getInt(25));
        this.setTeacherId3(rs.getInt(26));
        this.setLessonId1(rs.getInt(27));
        this.setLessonId2(rs.getInt(28));
        this.setLessonId3(rs.getInt(29));
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
        sb.append(this.getProspective());
        sb.append("', '");
        sb.append(this.getStatus());
        sb.append("', '");
        sb.append(this.getStatusFrom());
        sb.append("', '");
        sb.append(this.getStatusTo());
        sb.append("', '");
        sb.append(this.getParentId1());
        sb.append("', '");
        sb.append(this.getParentId2());
        sb.append("', '");
        sb.append(this.getTeacherId1());
        sb.append("', '");
        sb.append(this.getTeacherId2());
        sb.append("', '");
        sb.append(this.getTeacherId3());
        sb.append("', '");
        sb.append(this.getLessonId1());
        sb.append("', '");
        sb.append(this.getLessonId3());
        sb.append("'");
        return sb.toString();
    }

}
