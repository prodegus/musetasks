package prodegus.musetasks.contacts;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;

import static prodegus.musetasks.contacts.LessonModel.*;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherFromDB;

public class Lesson {

    private SimpleIntegerProperty id          = new SimpleIntegerProperty();
    private SimpleStringProperty  lessonName  = new SimpleStringProperty();
    private SimpleIntegerProperty category    = new SimpleIntegerProperty();
    private SimpleStringProperty  instrument  = new SimpleStringProperty();
    private SimpleIntegerProperty teacherId   = new SimpleIntegerProperty();
    private SimpleStringProperty  location    = new SimpleStringProperty();
    private SimpleBooleanProperty repeat      = new SimpleBooleanProperty();
    private SimpleIntegerProperty repeatTimes = new SimpleIntegerProperty();
    private SimpleStringProperty  repeatEnd   = new SimpleStringProperty();
    private SimpleStringProperty  weekday     = new SimpleStringProperty();
    private SimpleStringProperty  time        = new SimpleStringProperty();
    private SimpleIntegerProperty duration    = new SimpleIntegerProperty();
    private SimpleStringProperty  startDate   = new SimpleStringProperty();
    private SimpleStringProperty  endDate     = new SimpleStringProperty();
    private SimpleIntegerProperty status      = new SimpleIntegerProperty();
    private SimpleStringProperty  statusFrom  = new SimpleStringProperty();
    private SimpleStringProperty  statusTo    = new SimpleStringProperty();
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

    public String getLessonName() {
        return lessonName.get();
    }

    public SimpleStringProperty lessonNameProperty() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName.set(lessonName);
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

    public String getInstrument() {
        return instrument.get();
    }

    public SimpleStringProperty instrumentProperty() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument.set(instrument);
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

    public boolean isRepeat() {
        return repeat.get();
    }

    public SimpleBooleanProperty repeatProperty() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat.set(repeat);
    }

    public int getRepeatTimes() {
        return repeatTimes.get();
    }

    public SimpleIntegerProperty repeatTimesProperty() {
        return repeatTimes;
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes.set(repeatTimes);
    }

    public String getRepeatEnd() {
        return repeatEnd.get();
    }

    public SimpleStringProperty repeatEndProperty() {
        return repeatEnd;
    }

    public void setRepeatEnd(String repeatEnd) {
        this.repeatEnd.set(repeatEnd);
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

    public int getStatus() {
        return status.get();
    }

    public SimpleIntegerProperty statusProperty() {
        return status;
    }

    public void setStatus(int status) {
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

    public Teacher teacher() {
        return getTeacherFromDB(getTeacherId());
    }

    public String category() {
        return switch (this.getCategory()) {
            case CATEGORY_SINGLE -> "Einzelunterricht";
            case CATEGORY_GROUP -> "Gruppenunterricht";
            case CATEGORY_COURSE -> "Kurs/Workshop";
            case CATEGORY_WORKGROUP -> "AG";
            default -> null;
        };
    }

    public String teacherName() {
        if (this.getTeacherId() == 0) return "";
        return this.teacher().getFirstName() + " " + this.teacher().getLastName().charAt(0) + ".";
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setLessonName(rs.getString("lessonname"));
        this.setCategory(rs.getInt("category"));
        this.setInstrument(rs.getString("instrument"));
        this.setTeacherId(rs.getInt("teacherid"));
        this.setLocation(rs.getString("location"));
        this.setRepeat(rs.getInt("repeat") == 1);
        this.setRepeatTimes(rs.getInt("repeattimes"));
        this.setRepeatEnd(rs.getString("repeatend  "));
        this.setWeekday(rs.getString("weekday"));
        this.setTime(rs.getString("time"));
        this.setDuration(rs.getInt("duration"));
        this.setStartDate(rs.getString("startdate"));
        this.setEndDate(rs.getString("enddate"));
        this.setStatus(rs.getInt("status"));
        this.setStatusFrom(rs.getString("statusfrom"));
        this.setStatusTo(rs.getString("statusto"));
        this.setStudentId1(rs.getInt("studentid1"));
        this.setStudentId2(rs.getInt("studentid2"));
        this.setStudentId3(rs.getInt("studentid3"));
        this.setStudentId4(rs.getInt("studentid4"));
        this.setStudentId5(rs.getInt("studentid5"));
        this.setStudentId6(rs.getInt("studentid6"));
        this.setStudentId7(rs.getInt("studentid7"));
        this.setStudentId8(rs.getInt("studentid8"));
        this.setStudentId9(rs.getInt("studentid9"));
        this.setStudentId10(rs.getInt("studentid10"));
        this.setSelected(false);
    }

    public String valuesToSQLString() {
        StringBuilder sb = new StringBuilder("null, '");
        sb.append(this.getLessonName()).append("', '");
        sb.append(this.getCategory()).append("', '");
        sb.append(this.getInstrument()).append("', '");
        sb.append(this.getTeacherId()).append("', '");
        sb.append(this.getLocation()).append("', '");
        sb.append(this.isRepeat() ? "1" : "0").append("', '");
        sb.append(this.getRepeatTimes()).append("', '");
        sb.append(this.getRepeatEnd()).append("', '");
        sb.append(this.getWeekday()).append("', '");
        sb.append(this.getTime()).append("', '");
        sb.append(this.getDuration()).append("', '");
        sb.append(this.getStartDate()).append("', '");
        sb.append(this.getEndDate()).append("', '");
        sb.append(this.getStatus()).append("', '");
        sb.append(this.getStatusFrom()).append("', '");
        sb.append(this.getStatusTo()).append("', '");
        sb.append(this.getStudentId1()).append("', '");
        sb.append(this.getStudentId2()).append("', '");
        sb.append(this.getStudentId3()).append("', '");
        sb.append(this.getStudentId4()).append("', '");
        sb.append(this.getStudentId5()).append("', '");
        sb.append(this.getStudentId6()).append("', '");
        sb.append(this.getStudentId7()).append("', '");
        sb.append(this.getStudentId8()).append("', '");
        sb.append(this.getStudentId9()).append("', '");
        sb.append(this.getStudentId10()).append("'");
        return sb.toString();
    }

}
