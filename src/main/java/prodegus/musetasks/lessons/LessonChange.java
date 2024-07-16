package prodegus.musetasks.lessons;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.StringJoiner;

import static prodegus.musetasks.utils.DateTime.toDate;
import static prodegus.musetasks.utils.DateTime.toInt;

public class LessonChange extends Lesson {
    private LocalDate changeDate = LocalDate.MIN;
    private boolean changeDone = false;

    public LessonChange() {}

    public LessonChange(Lesson lesson, LocalDate changeDate, boolean changeDone) {
        this.setId(lesson.getId());
        this.setLessonName(lesson.getLessonName());
        this.setCategory(lesson.getCategory());
        this.setInstrument(lesson.getInstrument());
        this.setTeacherId(lesson.getTeacherId());
        this.setLocationId(lesson.getLocationId());
        this.setRoom(lesson.getRoom());
        this.setRepeat(lesson.getRepeat());
        this.setWeekday(lesson.getWeekday());
        this.setTime(lesson.getTime());
        this.setDuration(lesson.getDuration());
        this.setStartDate(lesson.getStartDate());
        this.setEndDate(lesson.getEndDate());
        this.setLessonStatus(lesson.getLessonStatus());
        this.setAptStatus(lesson.getAptStatus());
        this.setStudentId1(lesson.getStudentId1());
        this.setStudentId2(lesson.getStudentId2());
        this.setStudentId3(lesson.getStudentId3());
        this.setStudentId4(lesson.getStudentId4());
        this.setStudentId5(lesson.getStudentId5());
        this.setStudentId6(lesson.getStudentId6());
        this.setStudentId7(lesson.getStudentId7());
        this.setStudentId8(lesson.getStudentId8());
        this.setStudentId9(lesson.getStudentId9());
        this.setStudentId10(lesson.getStudentId10());
        this.setSelected(lesson.isSelected());
        this.changeDate = changeDate;
        this.changeDone = changeDone;
    }

    public LocalDate getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(LocalDate changeDate) {
        this.changeDate = changeDate;
    }

    public boolean isChangeDone() {
        return changeDone;
    }

    public void setChangeDone(boolean changeDone) {
        this.changeDone = changeDone;
    }

    public Lesson lesson() {
        Lesson lesson = new Lesson();
        lesson.setId(this.getId());
        lesson.setLessonName(this.getLessonName());
        lesson.setCategory(this.getCategory());
        lesson.setInstrument(this.getInstrument());
        lesson.setTeacherId(this.getTeacherId());
        lesson.setLocationId(this.getLocationId());
        lesson.setRoom(this.getRoom());
        lesson.setRepeat(this.getRepeat());
        lesson.setWeekday(this.getWeekday());
        lesson.setTime(this.getTime());
        lesson.setDuration(this.getDuration());
        lesson.setStartDate(this.getStartDate());
        lesson.setEndDate(this.getEndDate());
        lesson.setLessonStatus(this.getLessonStatus());
        lesson.setAptStatus(this.getAptStatus());
        lesson.setStudentId1(this.getStudentId1());
        lesson.setStudentId2(this.getStudentId2());
        lesson.setStudentId3(this.getStudentId3());
        lesson.setStudentId4(this.getStudentId4());
        lesson.setStudentId5(this.getStudentId5());
        lesson.setStudentId6(this.getStudentId6());
        lesson.setStudentId7(this.getStudentId7());
        lesson.setStudentId8(this.getStudentId8());
        lesson.setStudentId9(this.getStudentId9());
        lesson.setStudentId10(this.getStudentId10());
        lesson.setSelected(this.isSelected());
        return lesson;
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        super.setAttributes(rs);
        this.setChangeDate(rs.getInt("changedate") == 0 ? LocalDate.MIN : toDate(rs.getInt("changedate")));
        this.setChangeDone(rs.getInt("changedone") == 1);
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder("id    = ").append(this.getTeacherId() == 0 ? "null" : this.getTeacherId()).append(", ");
        sb.append(super.valuesToSQLUpdateString()).append(", ");
        sb.append("changedate = ").append(toInt(this.getChangeDate())).append(", ");
        sb.append("changedone = ").append(this.isChangeDone() ? "1" : "0");
        return sb.toString();
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");
        columns.add("id");
        columns.add(super.sqlColumns());
        columns.add("changedate");
        columns.add("changedone");
        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'").setEmptyValue("");
        StringJoiner result = new StringJoiner(", ").add(String.valueOf(this.getId())).add(super.sqlValues());
        values.add(String.valueOf(toInt(this.getChangeDate())));
        values.add((this.isChangeDone()) ? "1" : "0");
        result.add(values.toString());
        return result.toString();
    }
}
