package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.database.Filter;
import prodegus.musetasks.database.PeriodFilter;
import prodegus.musetasks.ui.popup.PopupWindow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static prodegus.musetasks.appointments.Appointment.*;
import static prodegus.musetasks.appointments.AppointmentModel.*;
import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.utils.DateTime.*;

public class LessonModel {

    public static final int CATEGORY_SINGLE = 1;
    public static final int CATEGORY_GROUP = 2;
    public static final int CATEGORY_COURSE = 3;
    public static final int CATEGORY_WORKGROUP = 4;

    public static final int REPEAT_WEEKLY = 1;
    public static final int REPEAT_2WEEKS = 2;
    public static final int REPEAT_3WEEKS = 3;
    public static final int REPEAT_4WEEKS = 4;
    public static final int REPEAT_5WEEKS = 5;
    public static final int REPEAT_6WEEKS = 6;
    public static final int REPEAT_OFF = 7;
    public static final int REPEAT_CUSTOM = 8;

    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;
    public static final int WEEKDAY_NO_SELECTION = 7;

    public static final int LESSON_STATUS_MEET = 1;
    public static final int LESSON_STATUS_TRIAL = 2;
    public static final int LESSON_STATUS_ACTIVE = 3;
    public static final int LESSON_STATUS_ILL = 4;
    public static final int LESSON_STATUS_HOLIDAY = 5;
    public static final int LESSON_STATUS_RESIGNED = 6;

    public static final int LESSON_APT_STATUS_DRAFT = 1;
    public static final int LESSON_APT_STATUS_REQUEST = 2;
    public static final int LESSON_APT_STATUS_CONFIRMED = 3;

    public static final ObservableList<String> LESSON_STATUS_LIST = FXCollections.observableArrayList(
            "auswählen",                // 0
            "Schnupper-Unterricht",     // 1
            "Probemonat",               // 2
            "Aktiv",                    // 3
            "Unterbrochen (Krankheit)", // 4
            "Unterbrochen (Urlaub)",    // 5
            "Gekündigt"                 // 6
    );

    public static final ObservableList<String> LESSON_APT_STATUS_LIST = FXCollections.observableArrayList(
            "auswählen",        // 0
            "Entwurf",          // 1
            "Termin angefragt", // 2
            "Termin bestätigt"  // 3
    );

    public static final ObservableList<String> WEEKDAY_LIST = FXCollections.observableArrayList(
            "auswählen",    // 0
            "Montag",       // 1
            "Dienstag",     // 2
            "Mittwoch",     // 3
            "Donnerstag",   // 4
            "Freitag",      // 5
            "Samstag",      // 6
            "keine Auswahl" // 7
    );

    public static ObservableList<Lesson> getLessonListFromDB() {
        ObservableList<Lesson> lessons = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + LESSON_TABLE;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Lesson lesson = new Lesson();
                lesson.setAttributes(rs);
                lessons.add(lesson);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lessons;
    }

    public static boolean lessonChangeExists(int lessonId, LocalDate date) {
        return getLatestLessonChange(lessonId, date, date) != null;
    }

    public static LessonChange getLatestLessonChange(int lessonId) {
        return getLatestLessonChange(lessonId, FAR_PAST, FAR_FUTURE);
    }

    public static LessonChange getLatestLessonChange(int lessonId, LocalDate untilDate) {
        return getLatestLessonChange(lessonId, FAR_PAST, untilDate);
    }

    public static LessonChange getLatestLessonChange(int lessonId, LocalDate start, LocalDate end) {
        ObservableList<LessonChange> changes = getLessonChangeListFromDB(lessonId, start, end);
        if (changes.size() == 0) return null;
        return changes.get(0);
    }

    public static ObservableList<LessonChange> getLessonChangeListFromDB(int lessonId) {
        return getLessonChangeListFromDB(" WHERE id = " + lessonId);
    }

    public static ObservableList<LessonChange> getLessonChangeListFromDB() {
        return getLessonChangeListFromDB("");
    }

    public static ObservableList<LessonChange> getLessonChangeListFromDB(int lessonId, LocalDate start, LocalDate end) {
        return getLessonChangeListFromDB(" WHERE id = " + lessonId + " AND changedate BETWEEN " + toInt(start) + " AND " + toInt(end) +
                " ORDER BY changedate DESC");
    }

    public static LessonChange getEarliestLessonChange(int lessonId) {
        return getLessonChangeListFromDB(" WHERE id = " + lessonId + " ORDER BY changedate ASC").get(0);
    }

    public static LessonChange getEarliestLessonChange(int lessonId, LocalDate start) {
        return getLessonChangeListFromDB(" WHERE id = " + lessonId + " AND changedate > " + toInt(start) +
                " ORDER BY changedate ASC").get(0);
    }

    public static ObservableList<LessonChange> getLessonChangeListFromDB(String filters) {
        ObservableList<LessonChange> lessonChanges = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + LESSON_CHANGE_TABLE + filters;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                LessonChange lessonChange = new LessonChange();
                lessonChange.setAttributes(rs);
                lessonChanges.add(lessonChange);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lessonChanges;
    }

    public static Lesson getLessonFromDB(int id) {
        String sql = "SELECT * FROM " + LESSON_TABLE + " WHERE id = " + id;
        Lesson lesson = new Lesson();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (!rs.next()) return null;
            lesson.setAttributes(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lesson;
    }

    public static void insertLessonAppointments(Lesson lesson, ObservableList<Appointment> appointments, LocalDate changeDate) {
        if (appointments == null) {
            insertLessonAppointments(lesson);
            return;
        }

        List<Appointment> oldLessonAppointments = getLessonAppointmentsFromDB(lesson.getId());
        List<Integer> newIds = new ArrayList<>();
        for (Appointment newAppointment : appointments) newIds.add(newAppointment.getId());

        if (oldLessonAppointments.size() > 0) {
            for (Appointment oldAppointment : oldLessonAppointments) {
                if (oldAppointment.getCategory() == CATEGORY_LESSON_RESCHEDULED && oldAppointment.getDateOld().isBefore(changeDate)) continue;
                if (!newIds.contains(oldAppointment.getId())) deleteAppointmentFromDB(oldAppointment);
            }
        }

        for (Appointment appointment : appointments) {
            Lesson currentLesson = getLatestLessonChange(lesson.getId(), appointment.getDate());
            appointment.setAttributesFromLesson(currentLesson);
            insertOrUpdate(appointment);
        }
    }

    public static void insertLessonAppointments(Lesson lesson, ObservableList<Appointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            insertLessonAppointments(lesson);
            return;
        }

        for (Appointment appointment : appointments) {
            Lesson currentLesson = getLatestLessonChange(lesson.getId(), appointment.getDate());
            appointment.setAttributesFromLesson(currentLesson);
            insertOrUpdate(appointment);
        }
    }

    public static void insertLessonAppointments(Lesson lesson) {
        if (lesson == null || lesson.getAptStatus() == LESSON_APT_STATUS_DRAFT) return;
        if (lesson.getRepeat() == REPEAT_CUSTOM) return;

        if (lesson.getLessonStatus() == LESSON_STATUS_MEET) {
            if (meetExists(lesson.getId())) {
                String meetExistsMessage = "Für diesen Unterricht wurde bereits ein Schnupper-Termin festgelegt.\n\n" +
                        "Termin überschreiben?";
                if (!PopupWindow.displayYesNo(meetExistsMessage)) return;
                delete(APPOINTMENT_TABLE, new Filter("lessonid", lesson.getId()), new Filter("description", "Schnupper-Unterricht"));
            }

            Appointment appointment = new Appointment();
            appointment.setDate(lesson.getStartDate());
            appointment.setTime(lesson.getTime());
            appointment.setLocationId(lesson.getLocationId());
            appointment.setRoom(lesson.getRoom());
            appointment.setDuration(lesson.getDuration());
            appointment.setLessonId(lesson.getId());
            appointment.setCategory(CATEGORY_LESSON_MEET);
            appointment.setStatus(lesson.getAptStatus() == LESSON_APT_STATUS_REQUEST ? STATUS_REQUEST : STATUS_OK);
            appointment.setDescription("Schnupper-Unterricht");
            insert(appointment);
        }

        if (lesson.getLessonStatus() == LESSON_STATUS_TRIAL) {
            if (trialExists(lesson.getId())) {
                String trialExistsMessage = "Für diesen Unterricht wurde bereits ein Probemonat festgelegt.\n\n" +
                        "Termine überschreiben?";
                if (!PopupWindow.displayYesNo(trialExistsMessage)) return;
                delete(APPOINTMENT_TABLE, new Filter("lessonid", lesson.getId()), new Filter("description", "Probemonat"));
            }

            LocalDate realStartDate = lesson.getStartDate();
            while (realStartDate.getDayOfWeek().getValue() != lesson.getWeekday())
                realStartDate = realStartDate.plusDays(1);

            for (LocalDate date = realStartDate; !date.isAfter(lesson.getEndDate()); date = date.plusWeeks(lesson.getRepeat())) {
                Appointment appointment = new Appointment();
                appointment.setDate(date);
                appointment.setTime(lesson.getTime());
                appointment.setLocationId(lesson.getLocationId());
                appointment.setRoom(lesson.getRoom());
                appointment.setDuration(lesson.getDuration());
                appointment.setLessonId(lesson.getId());
                appointment.setCategory(CATEGORY_LESSON_TRIAL);
                appointment.setStatus(lesson.getAptStatus() == LESSON_APT_STATUS_REQUEST ? STATUS_REQUEST : STATUS_OK);
                appointment.setDescription("Probemonat");
                insert(appointment);
            }
        }

        if (lesson.getLessonStatus() == LESSON_STATUS_ACTIVE) {
            if (activeLessonsExist(lesson.getId(), lesson.getStartDate(), lesson.getEndDate())) {
                String activeLessonsExistMessage = "Für diesen Zeitraum wurden bereits Termine festgelegt.\n\n" +
                        "Termine überschreiben?";
                if (!PopupWindow.displayYesNo(activeLessonsExistMessage)) return;

                Filter lessonFilter = new Filter("lessonid", String.valueOf(lesson.getId()));
                Filter active = new Filter("category", CATEGORY_LESSON_REGULAR);
                Filter holiday = new Filter("category", CATEGORY_HOLIDAY);
                Filter period = new PeriodFilter("date", lesson.getStartDate(), lesson.getEndDate());

                delete(APPOINTMENT_TABLE, lessonFilter, active, period);
                delete(APPOINTMENT_TABLE, lessonFilter, holiday, period);
            }

            for (Appointment appointment : lesson.appointments()) {
                insert(appointment);
            }

        }
    }

    public static boolean trialExists(int lessonId) {
        Filter lesson = new Filter("lessonid", String.valueOf(lessonId));
        Filter trial = new Filter("category", CATEGORY_LESSON_TRIAL);
        return !queryInteger("id", APPOINTMENT_TABLE, lesson, trial).isEmpty();
    }

    public static boolean meetExists(int lessonId) {
        Filter lesson = new Filter("lessonid", String.valueOf(lessonId));
        Filter trial = new Filter("category", CATEGORY_LESSON_MEET);
        return !queryInteger("id", APPOINTMENT_TABLE, lesson, trial).isEmpty();
    }

    public static boolean activeLessonsExist(int lessonId, LocalDate startDate, LocalDate endDate) {
        Filter lesson = new Filter("lessonid", String.valueOf(lessonId));
        Filter active = new Filter("category", CATEGORY_LESSON_REGULAR);
        Filter period = new PeriodFilter("date", startDate, endDate);
        return !queryInteger("id", APPOINTMENT_TABLE, lesson, active, period).isEmpty();
    }

    public static void insertLesson(Lesson lesson) {
        insert(LESSON_TABLE, lesson.sqlColumns(), lesson.sqlValues());
    }
    
    public static void deleteLessonFromDB(Lesson lesson) {
        delete(LESSON_TABLE, lesson.getId());
    }

    public static void updateLesson(Lesson lesson, int id) {
        Lesson oldLesson = getLessonFromDB(id);
        List<Student> removedStudents = oldLesson.students();
        removedStudents.removeAll(lesson.students());
        updateMultiple(LESSON_TABLE, id, lesson.valuesToSQLUpdateString());
        for (Student student : removedStudents) {
            student.removeLessonInDB(id);
        }
    }

    public static void insertLessonChange(LessonChange lessonChange) {
        insert(LESSON_CHANGE_TABLE, lessonChange.sqlColumns(), lessonChange.sqlValues());
    }

    public static void deleteLessonChange(int lessonId, LocalDate changeDate) {
        Filter lesson = new Filter("id", String.valueOf(lessonId));
        Filter date = new Filter("changedate", String.valueOf(toInt(changeDate)));
        delete(LESSON_CHANGE_TABLE, lesson, date);
    }

    public static void updateLessonChange(LessonChange lessonChange) {
        Filter lesson = new Filter("id", String.valueOf(lessonChange.getId()));
        Filter date = new Filter("changedate", String.valueOf(toInt(lessonChange.getChangeDate())));
        updateMultiple(LESSON_CHANGE_TABLE, lessonChange.valuesToSQLUpdateString(), lesson, date);
    }

    public static int getLastLessonID() {
        int id = 0;
        String sql = "SELECT MAX (id) FROM " + LESSON_TABLE;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }

    public static Lesson collidingLesson(Lesson lesson, LocalDate startDate) {
        for (Lesson lesson2 : getLessonListFromDB()) {
            if (lesson2.getId() == lesson.getId()) continue;
            Lesson currentLesson2 = getLatestLessonChange(lesson2.getId(), startDate); // Fehler: currentLesson2 wird null
            if (currentLesson2 == null) currentLesson2 = getEarliestLessonChange(lesson2.getId());
            if (appointmentsOverlap(lesson, currentLesson2)) return currentLesson2;
        }
        return null;
    }

    public static boolean appointmentsOverlap(Lesson lesson1, Lesson lesson2) {
        if (lesson1.getId() == lesson2.getId()) return false;

        String locationRoom1 = lesson1.locationRoom();
        String locationRoom2 = lesson2.locationRoom();

        int weekday1 = lesson1.getWeekday();
        LocalTime time1 = lesson1.getTime();
        int duration1 = lesson1.getDuration();

        int weekday2 = lesson2.getWeekday();
        LocalTime time2 = lesson2.getTime();
        int duration2 = lesson2.getDuration();

        if (!locationRoom1.equals(locationRoom2)) return false;
        if (weekday1 != weekday2) return false;
        return !time1.plusMinutes(duration1 - 1).isBefore(time2) && !time2.plusMinutes(duration2 - 1).isBefore(time1);
    }

    public static boolean equals(Lesson lesson1, Lesson lesson2) {
        if (lesson1.getId() != lesson2.getId()) return false;
        if (!lesson1.getLessonName().equals(lesson2.getLessonName())) return false;
        if (lesson1.getCategory() != lesson2.getCategory()) return false;
        if (!lesson1.getInstrument().equals(lesson2.getInstrument())) return false;
        if (lesson1.getTeacherId() != lesson2.getTeacherId()) return false;
        if (lesson1.getLocationId() != lesson2.getLocationId()) return false;
        if (!lesson1.getRoom().equals(lesson2.getRoom())) return false;
        if (lesson1.getRepeat() != lesson2.getRepeat()) return false;
        if (lesson1.getWeekday() != lesson2.getWeekday()) return false;
        if (!lesson1.getTime().equals(lesson2.getTime())) return false;
        if (lesson1.getDuration() != lesson2.getDuration()) return false;
        if (!lesson1.getStartDate().equals(lesson2.getStartDate())) return false;
        if (!lesson1.getEndDate().equals(lesson2.getEndDate())) return false;
        if (lesson1.getLessonStatus() != lesson2.getLessonStatus()) return false;
        if (lesson1.getAptStatus() != lesson2.getAptStatus()) return false;
        if (lesson1.getStudentId1() != lesson2.getStudentId1()) return false;
        if (lesson1.getStudentId2() != lesson2.getStudentId2()) return false;
        if (lesson1.getStudentId3() != lesson2.getStudentId3()) return false;
        if (lesson1.getStudentId4() != lesson2.getStudentId4()) return false;
        if (lesson1.getStudentId5() != lesson2.getStudentId5()) return false;
        if (lesson1.getStudentId6() != lesson2.getStudentId6()) return false;
        if (lesson1.getStudentId7() != lesson2.getStudentId7()) return false;
        if (lesson1.getStudentId8() != lesson2.getStudentId8()) return false;
        if (lesson1.getStudentId9() != lesson2.getStudentId9()) return false;
        if (lesson1.getStudentId10() != lesson2.getStudentId10()) return false;
        return true;
    }

    public static List<String> changes(Lesson lesson1, Lesson lesson2) {
        List<String> changes = new ArrayList<>();
        if (lesson1.getId() != lesson2.getId()) changes.add("id");
        if (!lesson1.getLessonName().equals(lesson2.getLessonName())) changes.add("lessonName");
        if (lesson1.getCategory() != lesson2.getCategory()) changes.add("category");
        if (!lesson1.getInstrument().equals(lesson2.getInstrument())) changes.add("instrument");
        if (lesson1.getTeacherId() != lesson2.getTeacherId()) changes.add("teacherId");
        if (lesson1.getLocationId() != lesson2.getLocationId()) changes.add("locationId");
        if (!lesson1.getRoom().equals(lesson2.getRoom())) changes.add("room");
        if (lesson1.getRepeat() != lesson2.getRepeat()) changes.add("repeat");
        if (lesson1.getWeekday() != lesson2.getWeekday()) changes.add("weekday");
        if (!lesson1.getTime().equals(lesson2.getTime())) changes.add("time");
        if (lesson1.getDuration() != lesson2.getDuration()) changes.add("duration");
        if (!lesson1.getStartDate().equals(lesson2.getStartDate())) changes.add("startDate");
        if (!lesson1.getEndDate().equals(lesson2.getEndDate())) changes.add("endDate");
        if (lesson1.getLessonStatus() != lesson2.getLessonStatus()) changes.add("lessonStatus");
        if (lesson1.getAptStatus() != lesson2.getAptStatus()) changes.add("aptStatus");
        if (lesson1.getStudentId1() != lesson2.getStudentId1()) changes.add("studentId1");
        if (lesson1.getStudentId2() != lesson2.getStudentId2()) changes.add("studentId2");
        if (lesson1.getStudentId3() != lesson2.getStudentId3()) changes.add("studentId3");
        if (lesson1.getStudentId4() != lesson2.getStudentId4()) changes.add("studentId4");
        if (lesson1.getStudentId5() != lesson2.getStudentId5()) changes.add("studentId5");
        if (lesson1.getStudentId6() != lesson2.getStudentId6()) changes.add("studentId6");
        if (lesson1.getStudentId7() != lesson2.getStudentId7()) changes.add("studentId7");
        if (lesson1.getStudentId8() != lesson2.getStudentId8()) changes.add("studentId8");
        if (lesson1.getStudentId9() != lesson2.getStudentId9()) changes.add("studentId9");
        if (lesson1.getStudentId10() != lesson2.getStudentId10()) changes.add("studentId10");
        return changes;
    }

    public static boolean realChanges(Lesson lesson1, Lesson lesson2) {
        List<String> changes = changes(lesson1, lesson2);
        changes.remove("startDate");
        changes.remove("endDate");
        return changes.size() > 0;
    }

    public static void deleteFullLesson(Lesson lesson) {
        for (Appointment appointment : getLessonAppointmentsFromDB(lesson.getId())) {
            deleteAppointmentFromDB(appointment);
        }
        for (LessonChange change : getLessonChangeListFromDB(lesson.getId())) {
            deleteLessonChange(lesson.getId(), change.getChangeDate());
        }
        deleteLessonFromDB(lesson);
    }


}
