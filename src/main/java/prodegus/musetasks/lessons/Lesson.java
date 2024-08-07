package prodegus.musetasks.lessons;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.school.Holiday;
import prodegus.musetasks.school.Location;
import prodegus.musetasks.utils.HalfYear;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static prodegus.musetasks.appointments.Appointment.*;
import static prodegus.musetasks.appointments.AppointmentModel.*;
import static prodegus.musetasks.contacts.StudentModel.getStudentFromDB;
import static prodegus.musetasks.lessons.LessonModel.*;
import static prodegus.musetasks.contacts.TeacherModel.getTeacherFromDB;
import static prodegus.musetasks.school.HolidayModel.createHolidayAppointment;
import static prodegus.musetasks.school.HolidayModel.getHoliday;
import static prodegus.musetasks.school.LocationModel.getLocationFromDB;
import static prodegus.musetasks.utils.DateTime.*;
import static prodegus.musetasks.utils.Strings.string;

public class Lesson {

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty lessonName = new SimpleStringProperty();
    private SimpleIntegerProperty category = new SimpleIntegerProperty();
    private SimpleStringProperty instrument = new SimpleStringProperty();
    private SimpleIntegerProperty teacherId = new SimpleIntegerProperty();
    private SimpleIntegerProperty locationId = new SimpleIntegerProperty();
    private SimpleStringProperty room = new SimpleStringProperty();
    private SimpleIntegerProperty repeat = new SimpleIntegerProperty();
    private SimpleIntegerProperty weekday = new SimpleIntegerProperty();
    private LocalTime time = LocalTime.MAX;
    private SimpleIntegerProperty duration = new SimpleIntegerProperty();
    private LocalDate startDate = LocalDate.MIN;
    private LocalDate endDate = LocalDate.MAX;
    private SimpleIntegerProperty lessonStatus = new SimpleIntegerProperty();
    private SimpleIntegerProperty aptStatus = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId1 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId2 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId3 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId4 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId5 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId6 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId7 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId8 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId9 = new SimpleIntegerProperty();
    private SimpleIntegerProperty studentId10 = new SimpleIntegerProperty();
    private SimpleBooleanProperty selected = new SimpleBooleanProperty();

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getLessonName() {
        return lessonName.get();
    }

    public void setLessonName(String lessonName) {
        this.lessonName.set(lessonName);
    }

    public SimpleStringProperty lessonNameProperty() {
        return lessonName;
    }

    public int getCategory() {
        return category.get();
    }

    public void setCategory(int category) {
        this.category.set(category);
    }

    public SimpleIntegerProperty categoryProperty() {
        return category;
    }

    public String getInstrument() {
        return instrument.get();
    }

    public void setInstrument(String instrument) {
        this.instrument.set(instrument);
    }

    public SimpleStringProperty instrumentProperty() {
        return instrument;
    }

    public int getTeacherId() {
        return teacherId.get();
    }

    public void setTeacherId(int teacherId) {
        this.teacherId.set(teacherId);
    }

    public SimpleIntegerProperty teacherIdProperty() {
        return teacherId;
    }

    public int getLocationId() {
        return locationId.get();
    }

    public void setLocationId(int locationId) {
        this.locationId.set(locationId);
    }

    public SimpleIntegerProperty locationIdProperty() {
        return locationId;
    }

    public String getRoom() {
        return room.get();
    }

    public void setRoom(String room) {
        this.room.set(room);
    }

    public SimpleStringProperty roomProperty() {
        return room;
    }

    public int getRepeat() {
        return repeat.get();
    }

    public void setRepeat(int repeat) {
        this.repeat.set(repeat);
    }

    public SimpleIntegerProperty repeatProperty() {
        return repeat;
    }

    public int getWeekday() {
        return weekday.get();
    }

    public void setWeekday(int weekday) {
        this.weekday.set(weekday);
    }

    public SimpleIntegerProperty weekdayProperty() {
        return weekday;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getLessonStatus() {
        return lessonStatus.get();
    }

    public void setLessonStatus(int lessonStatus) {
        this.lessonStatus.set(lessonStatus);
    }

    public SimpleIntegerProperty lessonStatusProperty() {
        return lessonStatus;
    }

    public int getAptStatus() {
        return aptStatus.get();
    }

    public SimpleIntegerProperty aptStatusProperty() {
        return aptStatus;
    }

    public void setAptStatus(int aptStatus) {
        this.aptStatus.set(aptStatus);
    }

    public int getStudentId1() {
        return studentId1.get();
    }

    public void setStudentId1(int studentId1) {
        this.studentId1.set(studentId1);
    }

    public SimpleIntegerProperty studentId1Property() {
        return studentId1;
    }

    public int getStudentId2() {
        return studentId2.get();
    }

    public void setStudentId2(int studentId2) {
        this.studentId2.set(studentId2);
    }

    public SimpleIntegerProperty studentId2Property() {
        return studentId2;
    }

    public int getStudentId3() {
        return studentId3.get();
    }

    public void setStudentId3(int studentId3) {
        this.studentId3.set(studentId3);
    }

    public SimpleIntegerProperty studentId3Property() {
        return studentId3;
    }

    public int getStudentId4() {
        return studentId4.get();
    }

    public void setStudentId4(int studentId4) {
        this.studentId4.set(studentId4);
    }

    public SimpleIntegerProperty studentId4Property() {
        return studentId4;
    }

    public int getStudentId5() {
        return studentId5.get();
    }

    public void setStudentId5(int studentId5) {
        this.studentId5.set(studentId5);
    }

    public SimpleIntegerProperty studentId5Property() {
        return studentId5;
    }

    public int getStudentId6() {
        return studentId6.get();
    }

    public void setStudentId6(int studentId6) {
        this.studentId6.set(studentId6);
    }

    public SimpleIntegerProperty studentId6Property() {
        return studentId6;
    }

    public int getStudentId7() {
        return studentId7.get();
    }

    public void setStudentId7(int studentId7) {
        this.studentId7.set(studentId7);
    }

    public SimpleIntegerProperty studentId7Property() {
        return studentId7;
    }

    public int getStudentId8() {
        return studentId8.get();
    }

    public void setStudentId8(int studentId8) {
        this.studentId8.set(studentId8);
    }

    public SimpleIntegerProperty studentId8Property() {
        return studentId8;
    }

    public int getStudentId9() {
        return studentId9.get();
    }

    public void setStudentId9(int studentId9) {
        this.studentId9.set(studentId9);
    }

    public SimpleIntegerProperty studentId9Property() {
        return studentId9;
    }

    public int getStudentId10() {
        return studentId10.get();
    }

    public void setStudentId10(int studentId10) {
        this.studentId10.set(studentId10);
    }

    public SimpleIntegerProperty studentId10Property() {
        return studentId10;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public String id() {
        return String.valueOf(this.getId());
    }

    public String durationString() {
        return this.getDuration() + " Minuten";
    }

    public Teacher teacher() {
        return getTeacherFromDB(getTeacherId());
    }

    public String createLessonName() {
        StringBuilder lessonName = new StringBuilder();
        if (this.getAptStatus() == LESSON_APT_STATUS_DRAFT) lessonName.append("[Entwurf] ");
        if (this.getAptStatus() == LESSON_APT_STATUS_REQUEST) lessonName.append("[Vorschlag] ");
        if (this.getCategory() == CATEGORY_SINGLE) {
            lessonName.append(getStudentFromDB(this.getStudentId1()).name());
            lessonName.append(" (");
            lessonName.append(String.join(", ", this.getInstrument(), "Einzel", this.getDuration() + " min)"));
        } else {
            String category = switch(this.getCategory()) {
                case CATEGORY_GROUP -> "Gruppe";
                case CATEGORY_COURSE -> "Kurs";
                case CATEGORY_WORKGROUP -> "AG";
                default -> "";
            };
            int i = 1;
            for (Lesson lesson : getLessonListFromDB()) {
                if (lesson.getCategory() == this.getCategory() && lesson.getInstrument().equals(this.getInstrument())) i++;
            }
            lessonName.append(this.getInstrument()).append(" - ").append(category).append(" ").append(i);
            lessonName.append(" (").append(this.getDuration()).append(" min)");
        }

        return lessonName.toString();
    }

    public ObservableList<Appointment> appointments() {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        if (this.getRepeat() == REPEAT_CUSTOM) return list;
        if (this.getRepeat() == REPEAT_OFF) {
            this.addAppointment(list, startDate);
            return list;
        }
        LocalDate realStartDate = this.getStartDate();
        LocalDate realEndDate = this.getEndDate();
        Holiday currentHoliday = null;

        while (realStartDate.getDayOfWeek().getValue() != this.getWeekday())
            realStartDate = realStartDate.plusDays(1);

        for (LocalDate date = realStartDate; !date.isAfter(realEndDate); date = date.plusWeeks(this.getRepeat())) {
            Holiday holiday = getHoliday(date);

            if (holiday != null) {
                if (currentHoliday == null || !holiday.getDescription().equals(currentHoliday.getDescription())) {
                    currentHoliday = holiday;
                    Appointment holidayAppointment = createHolidayAppointment(currentHoliday);
                    holidayAppointment.setLessonId(this.getId());
                    list.add(holidayAppointment);
                }
                continue;
            }

            this.addAppointment(list, date);
        }
        Collections.sort(list);
        return list;
    }

    public void addAppointment(ObservableList<Appointment> list, LocalDate date) {
        LessonChange latestChange = getLatestLessonChange(this.getId(), date);
        Lesson futureLesson = latestChange == null ? this : latestChange.lesson();

        if (futureLesson.getLessonStatus() != LESSON_STATUS_ACTIVE) {
            return;
        }
        list.add(futureLesson.createAppointment(date));
    }

    public Appointment createAppointment(LocalDate date) {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setTime(this.getTime());
        appointment.setLocationId(this.getLocationId());
        appointment.setRoom(this.getRoom());
        appointment.setDuration(this.getDuration());
        appointment.setLessonId(this.getId());
        appointment.setCategory(CATEGORY_LESSON_REGULAR);
        appointment.setStatus(this.getAptStatus() == LESSON_APT_STATUS_REQUEST ? STATUS_REQUEST : STATUS_OK);
        return appointment;
    }

    public List<Student> students() {
        List<Student> students = new ArrayList<>();

        if (getStudentId1() != 0) students.add(getStudentFromDB(getStudentId1()));
        if (getStudentId2() != 0) students.add(getStudentFromDB(getStudentId2()));
        if (getStudentId3() != 0) students.add(getStudentFromDB(getStudentId3()));
        if (getStudentId4() != 0) students.add(getStudentFromDB(getStudentId4()));
        if (getStudentId5() != 0) students.add(getStudentFromDB(getStudentId5()));
        if (getStudentId6() != 0) students.add(getStudentFromDB(getStudentId6()));
        if (getStudentId7() != 0) students.add(getStudentFromDB(getStudentId7()));
        if (getStudentId8() != 0) students.add(getStudentFromDB(getStudentId8()));
        if (getStudentId9() != 0) students.add(getStudentFromDB(getStudentId9()));
        if (getStudentId10() != 0) students.add(getStudentFromDB(getStudentId10()));

        return students;
    }

    public Location location() {
        if (this.getLocationId() == 0) return null;
        return getLocationFromDB(this.getLocationId());
    }

    public String locationRoom() {
        return this.location().getName() + " (" + this.getRoom() + ")";
    }

    public String regularAppointment() {
        if (this.getRepeat() == REPEAT_OFF || this.getRepeat() == REPEAT_CUSTOM) return "individuell";
        return String.join(" ", this.weekday(), this.getTime().toString());
    }

    public List<String> studentsNames() {
        List<String> names = new ArrayList<String>();

        for (Student student : this.students()) {
            names.add(student.name());
        }

        return names;
    }

    public String studentsNamesString() {
        return String.join("\n", studentsNames());
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

    public String weekday() {
        if (this.getWeekday() < 1 || this.getWeekday() > 6) return "";
        return WEEKDAY_LIST.get(this.getWeekday());
    }

    public String lessonStatus() {
        return LESSON_STATUS_LIST.get(this.getLessonStatus());
    }

    public String appointmentStatus() {
        return LESSON_APT_STATUS_LIST.get(this.getAptStatus());
    }

    public void setAttributes(ResultSet rs) throws SQLException {

        this.setId(rs.getInt("id"));
        this.setLessonName(rs.getString("lessonname"));
        this.setCategory(rs.getInt("category"));
        this.setInstrument(rs.getString("instrument"));
        this.setTeacherId(rs.getInt("teacherid"));
        this.setLocationId(rs.getInt("locationid"));
        this.setRoom(rs.getString("room"));
        this.setRepeat(rs.getInt("repeat"));
        this.setWeekday(rs.getInt("weekday"));
        this.setTime(toTime(rs.getInt("time")));
        this.setDuration(rs.getInt("duration"));
        this.setStartDate(toDate(rs.getInt("startdate")));
        this.setEndDate(rs.getInt("enddate") == 0 ? LocalDate.MAX : toDate(rs.getInt("enddate")));
        this.setLessonStatus(rs.getInt("lessonstatus"));
        this.setAptStatus(rs.getInt("aptstatus"));
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

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");

        if (this.getLessonName() != null) columns.add("lessonname");
        columns.add("category");
        columns.add("instrument");
        if (this.getTeacherId() != 0) columns.add("teacherid");
        if (this.getLocationId() != 0) columns.add("locationid");
        if (this.getRoom() != null) columns.add("room");
        columns.add("repeat");
        columns.add("weekday");
        columns.add("time");
        columns.add("duration");
        if (this.getStartDate() != LocalDate.MIN) columns.add("startdate");
        if (this.getEndDate() != LocalDate.MAX) columns.add("enddate");
        columns.add("lessonstatus");
        columns.add("aptstatus");
        if (this.getStudentId1() != 0) columns.add("studentid1");
        if (this.getStudentId2() != 0) columns.add("studentid2");
        if (this.getStudentId3() != 0) columns.add("studentid3");
        if (this.getStudentId4() != 0) columns.add("studentid4");
        if (this.getStudentId5() != 0) columns.add("studentid5");
        if (this.getStudentId6() != 0) columns.add("studentid6");
        if (this.getStudentId7() != 0) columns.add("studentid7");
        if (this.getStudentId8() != 0) columns.add("studentid8");
        if (this.getStudentId9() != 0) columns.add("studentid9");
        if (this.getStudentId10() != 0) columns.add("studentid10");

        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'");

        if (this.getLessonName() != null) values.add(this.getLessonName());
        values.add(String.valueOf(this.getCategory()));
        values.add(this.getInstrument());
        if (this.getTeacherId() != 0) values.add(String.valueOf(this.getTeacherId()));
        if (this.getLocationId() != 0) values.add(String.valueOf(this.getLocationId()));
        if (this.getRoom() != null) values.add(this.getRoom());
        values.add(String.valueOf(this.getRepeat()));
        values.add(String.valueOf(this.getWeekday()));
        values.add(String.valueOf(toInt(this.getTime())));
        values.add(String.valueOf(this.getDuration()));
        if (getStartDate() != LocalDate.MIN) values.add(String.valueOf(toInt(this.getStartDate())));
        if (getEndDate() != LocalDate.MAX) values.add(String.valueOf(toInt(this.getEndDate())));
        values.add(String.valueOf(this.getLessonStatus()));
        values.add(String.valueOf(this.getAptStatus()));
        if (this.getStudentId1() != 0) values.add(String.valueOf(this.getStudentId1()));
        if (this.getStudentId2() != 0) values.add(String.valueOf(this.getStudentId2()));
        if (this.getStudentId3() != 0) values.add(String.valueOf(this.getStudentId3()));
        if (this.getStudentId4() != 0) values.add(String.valueOf(this.getStudentId4()));
        if (this.getStudentId5() != 0) values.add(String.valueOf(this.getStudentId5()));
        if (this.getStudentId6() != 0) values.add(String.valueOf(this.getStudentId6()));
        if (this.getStudentId7() != 0) values.add(String.valueOf(this.getStudentId7()));
        if (this.getStudentId8() != 0) values.add(String.valueOf(this.getStudentId8()));
        if (this.getStudentId9() != 0) values.add(String.valueOf(this.getStudentId9()));
        if (this.getStudentId10() != 0) values.add(String.valueOf(this.getStudentId10()));

        return values.toString();
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder();

        sb.append("lessonname   = '").append(this.getLessonName()).append("', ");
        sb.append("category     = ").append(this.getCategory()).append(", ");
        sb.append("instrument   = '").append(this.getInstrument()).append("', ");
        sb.append("teacherid    = ").append(this.getTeacherId() == 0 ? "null" : this.getTeacherId()).append(", ");
        sb.append("locationid   = ").append(this.getLocationId() == 0 ? "null" : this.getLocationId()).append(", ");
        sb.append("room         = '").append(this.getRoom()).append("', ");
        sb.append("repeat       = ").append(this.getRepeat()).append(", ");
        sb.append("weekday      = ").append(this.getWeekday()).append(", ");
        sb.append("time         = ").append(this.getTime() == LocalTime.MAX ? "null" : toInt(this.getTime())).append(", ");
        sb.append("duration     = ").append(this.getDuration()).append(", ");
        sb.append("startdate    = ").append(this.getStartDate() == LocalDate.MIN ? "null" : toInt(this.getStartDate())).append(", ");
        sb.append("enddate      = ").append(this.getEndDate() == LocalDate.MAX ? "null" : toInt(this.getEndDate())).append(", ");
        sb.append("lessonstatus = ").append(this.getLessonStatus()).append(", ");
        sb.append("aptstatus    = ").append(this.getAptStatus()).append(", ");
        sb.append("studentid1   = ").append(this.getStudentId1() == 0 ? "null" : this.getStudentId1()).append(", ");
        sb.append("studentid2   = ").append(this.getStudentId2() == 0 ? "null" : this.getStudentId2()).append(", ");
        sb.append("studentid3   = ").append(this.getStudentId3() == 0 ? "null" : this.getStudentId3()).append(", ");
        sb.append("studentid4   = ").append(this.getStudentId4() == 0 ? "null" : this.getStudentId4()).append(", ");
        sb.append("studentid5   = ").append(this.getStudentId5() == 0 ? "null" : this.getStudentId5()).append(", ");
        sb.append("studentid6   = ").append(this.getStudentId6() == 0 ? "null" : this.getStudentId6()).append(", ");
        sb.append("studentid7   = ").append(this.getStudentId7() == 0 ? "null" : this.getStudentId7()).append(", ");
        sb.append("studentid8   = ").append(this.getStudentId8() == 0 ? "null" : this.getStudentId8()).append(", ");
        sb.append("studentid9   = ").append(this.getStudentId9() == 0 ? "null" : this.getStudentId9()).append(", ");
        sb.append("studentid10  = ").append(this.getStudentId10() == 0 ? "null" : this.getStudentId10());

        return sb.toString();
    }

    public String shortName() {
        if (this.getCategory() == CATEGORY_SINGLE) return this.getInstrument() + " (Einzel, " + this.getDuration() + " min)";
        return this.getLessonName();
    }
}
