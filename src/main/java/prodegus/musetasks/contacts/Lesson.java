package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Lesson {

    private SimpleIntegerProperty id          = new SimpleIntegerProperty();
    private SimpleStringProperty  instrument  = new SimpleStringProperty();
    private SimpleStringProperty  lessonName  = new SimpleStringProperty();
    private SimpleIntegerProperty teacherId   = new SimpleIntegerProperty();
    private SimpleStringProperty  location    = new SimpleStringProperty();
    private SimpleStringProperty  frequency   = new SimpleStringProperty();
    private SimpleStringProperty  weekday     = new SimpleStringProperty();
    private SimpleStringProperty  time        = new SimpleStringProperty();
    private SimpleIntegerProperty duration    = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId1  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId2  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId3  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId4  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId5  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId6  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId7  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId8  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId9  = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId10 = new SimpleIntegerProperty();
    private SimpleStringProperty  startDate   = new SimpleStringProperty();
    private SimpleStringProperty  endDate     = new SimpleStringProperty();

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

    public String getInstrument() {
        return instrument.get();
    }

    public SimpleStringProperty instrumentProperty() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument.set(instrument);
    }

    public String getLessonName() {
        return lessonName.get();
    }

    public SimpleStringProperty lessonNameProperty() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName.set(lessonName);
    }

    public int getTeacherId() {
        return teacherId.get();
    }

    public SimpleIntegerProperty teacherIdProperty() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId.set(teacherId);
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

    public String getFrequency() {
        return frequency.get();
    }

    public SimpleStringProperty frequencyProperty() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency.set(frequency);
    }

    public String getWeekday() {
        return weekday.get();
    }

    public SimpleStringProperty weekdayProperty() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday.set(weekday);
    }

    public String getTime() {
        return time.get();
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public int getDuration() {
        return duration.get();
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public int getStudentId1() {
        return studentId1.get();
    }

    public SimpleIntegerProperty studentId1Property() {
        return studentId1;
    }

    public void setStudentId1(int studentId1) {
        this.studentId1.set(studentId1);
    }

    public int getStudentId2() {
        return studentId2.get();
    }

    public SimpleIntegerProperty studentId2Property() {
        return studentId2;
    }

    public void setStudentId2(int studentId2) {
        this.studentId2.set(studentId2);
    }

    public int getStudentId3() {
        return studentId3.get();
    }

    public SimpleIntegerProperty studentId3Property() {
        return studentId3;
    }

    public void setStudentId3(int studentId3) {
        this.studentId3.set(studentId3);
    }

    public int getStudentId4() {
        return studentId4.get();
    }

    public SimpleIntegerProperty studentId4Property() {
        return studentId4;
    }

    public void setStudentId4(int studentId4) {
        this.studentId4.set(studentId4);
    }

    public int getStudentId5() {
        return studentId5.get();
    }

    public SimpleIntegerProperty studentId5Property() {
        return studentId5;
    }

    public void setStudentId5(int studentId5) {
        this.studentId5.set(studentId5);
    }

    public int getStudentId6() {
        return studentId6.get();
    }

    public SimpleIntegerProperty studentId6Property() {
        return studentId6;
    }

    public void setStudentId6(int studentId6) {
        this.studentId6.set(studentId6);
    }

    public int getStudentId7() {
        return studentId7.get();
    }

    public SimpleIntegerProperty studentId7Property() {
        return studentId7;
    }

    public void setStudentId7(int studentId7) {
        this.studentId7.set(studentId7);
    }

    public int getStudentId8() {
        return studentId8.get();
    }

    public SimpleIntegerProperty studentId8Property() {
        return studentId8;
    }

    public void setStudentId8(int studentId8) {
        this.studentId8.set(studentId8);
    }

    public int getStudentId9() {
        return studentId9.get();
    }

    public SimpleIntegerProperty studentId9Property() {
        return studentId9;
    }

    public void setStudentId9(int studentId9) {
        this.studentId9.set(studentId9);
    }

    public int getStudentId10() {
        return studentId10.get();
    }

    public SimpleIntegerProperty studentId10Property() {
        return studentId10;
    }

    public void setStudentId10(int studentId10) {
        this.studentId10.set(studentId10);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
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
        this.setInstrument(rs.getString(2));
        this.setLessonName(rs.getString(3));
        this.setTeacherId(rs.getInt(4));
        this.setLocation(rs.getString(5));
        this.setFrequency(rs.getString(6));
        this.setWeekday(rs.getString(7));
        this.setTime(rs.getString(8));
        this.setDuration(rs.getInt(9));
        this.setStudentId1(rs.getInt(10));
        this.setStudentId2(rs.getInt(11));
        this.setStudentId3(rs.getInt(12));
        this.setStudentId4(rs.getInt(13));
        this.setStudentId5(rs.getInt(14));
        this.setStudentId6(rs.getInt(15));
        this.setStudentId7(rs.getInt(16));
        this.setStudentId8(rs.getInt(17));
        this.setStudentId9(rs.getInt(18));
        this.setStudentId10(rs.getInt(19));
        this.setStartDate(rs.getString(20));
        this.setEndDate(rs.getString(21));
        this.setSelected(false);
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder();
        sb.append("'NULL', '");
        sb.append(this.getInstrument());
        sb.append("', '");
        sb.append(this.getLessonName());
        sb.append("', '");
        sb.append(this.getTeacherId());
        sb.append("', '");
        sb.append(this.getLocation());
        sb.append("', '");
        sb.append(this.getFrequency());
        sb.append("', '");
        sb.append(this.getWeekday());
        sb.append("', '");
        sb.append(this.getTime());
        sb.append("', '");
        sb.append(this.getDuration());
        sb.append("', '");
        sb.append(this.getStudentId1());
        sb.append("', '");
        sb.append(this.getStudentId2());
        sb.append("', '");
        sb.append(this.getStudentId3());
        sb.append("', '");
        sb.append(this.getStudentId4());
        sb.append("', '");
        sb.append(this.getStudentId5());
        sb.append("', '");
        sb.append(this.getStudentId6());
        sb.append("', '");
        sb.append(this.getStudentId7());
        sb.append("', '");
        sb.append(this.getStudentId8());
        sb.append("', '");
        sb.append(this.getStudentId9());
        sb.append("', '");
        sb.append(this.getStudentId10());
        sb.append("', '");
        sb.append(this.getStartDate());
        sb.append("', '");
        sb.append(this.getEndDate());
        sb.append("'");
        return sb.toString();
    }

}
