package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.appointments.Appointment;
import prodegus.musetasks.database.Filter;
import prodegus.musetasks.ui.PopupWindow;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

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

    public static LessonChange getLatestLessonChange(int lessonId, LocalDate date) {
        return getLatestLessonChange(lessonId, FAR_PAST, date);
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

    public static void insertLessonAppointments(Lesson lesson) {
        if (lesson == null || lesson.getAptStatus() == LESSON_APT_STATUS_DRAFT) return;

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
            appointment.setStatus(STATUS_OK);
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
                appointment.setStatus(STATUS_OK);
                appointment.setDescription("Probemonat");
                insert(appointment);
            }
        }
    }

    public static boolean trialExists(int lessonId) {
        Filter lesson = new Filter("lessonid", String.valueOf(lessonId));
        Filter trial = new Filter("description", "Probemonat");
        return !queryInteger("id", APPOINTMENT_TABLE, lesson, trial).isEmpty();
    }

    public static boolean meetExists(int lessonId) {
        Filter lesson = new Filter("lessonid", String.valueOf(lessonId));
        Filter trial = new Filter("description", "Schnupper-Unterricht");
        return !queryInteger("id", APPOINTMENT_TABLE, lesson, trial).isEmpty();
    }

    public static void insertLesson(Lesson lesson) {
        insert(LESSON_TABLE, lesson.sqlColumns(), lesson.sqlValues());
    }
    
    public static void deleteLesson(Lesson lesson) {
        delete(LESSON_TABLE, lesson.getId());
    }

    public static void updateLesson(Lesson lesson, int id) {
        updateMultiple(LESSON_TABLE, id, lesson.valuesToSQLUpdateString());
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

}
