package prodegus.musetasks.lessons;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.StudentModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;

import static prodegus.musetasks.contacts.StudentModel.*;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.utils.DateTime.*;

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

    public String getChangeNote() {
        LessonChange previousChange = getLatestLessonChange(this.getId(), this.getChangeDate().minusDays(1));
        StringJoiner changeNote = new StringJoiner("\n");

        if (previousChange == null) {
            changeNote.add(this.getLessonStatus() == LESSON_STATUS_ACTIVE ? "Unterrichtsbeginn" : this.lessonStatus());
            return changeNote.toString();
        }

        if (this.getLessonStatus() == LESSON_STATUS_TRIAL) {
            changeNote.add("Probemonat bis " + asString(this.getEndDate()));
            return changeNote.toString();
        }

        List<Student> removedStudents = studentsRemoved(previousChange.students(), this.students());
        List<Student> newStudents = studentsAdded(previousChange.students(), this.students());

        if (!previousChange.getLessonName().equals(this.getLessonName())) {
            changeNote.add("Unterrichtsbeschreibung: " + this.getLessonName());
        }
        if (!previousChange.getInstrument().equals(this.getInstrument())) {
            changeNote.add("Instrument: " + this.getInstrument());
        }
        if (previousChange.getTeacherId() != this.getTeacherId()) {
            changeNote.add("Lehrer: " + this.teacher().name());
        }
        if (previousChange.getLocationId() != this.getLocationId()) {
            changeNote.add("Standort: " + this.location().getName());
        }
        if (!previousChange.getRoom().equals(this.getRoom())) {
            changeNote.add("Raum: " + this.getRoom());
        }
        if (previousChange.getRepeat() != this.getRepeat()) {
            changeNote.add("Wiederholungsmodus: " + this.getRepeat());
        }
        if (previousChange.getWeekday() != this.getWeekday()) {
            changeNote.add("Wochentag: " + this.weekday());
        }
        if (!previousChange.getTime().equals(this.getTime())) {
            changeNote.add("Uhrzeit: " + asString(this.getTime()));
        }
        if (previousChange.getDuration() != this.getDuration()) {
            changeNote.add("Dauer: " + this.getDuration() + " Minuten");
        }
        if (previousChange.getLessonStatus() != this.getLessonStatus()) {
            changeNote.add("Status: " + this.lessonStatus());
        }
        if (previousChange.getAptStatus() != this.getAptStatus()) {
            changeNote.add("Terminstatus: " + this.appointmentStatus());
        }
        if (previousChange.getEndDate() != this.getEndDate()) {
            changeNote.add("Verlängert bis " + asString(this.getEndDate()));
        }
        if (removedStudents.size() > 0) {
            StringJoiner oldNames = new StringJoiner(", ");
            for (Student student : removedStudents) {
                oldNames.add(student.name());
            }
            changeNote.add("Schüler entfernt: " + oldNames);
        }
        if (newStudents.size() > 0) {
            StringJoiner newNames = new StringJoiner(", ");
            for (Student student : newStudents) {
                newNames.add(student.name());
            }
            changeNote.add("Schüler hinzugefügt: " + newNames);
        }
        
        return changeNote.toString();
    }
}
