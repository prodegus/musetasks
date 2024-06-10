package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static prodegus.musetasks.database.Database.*;

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

    public static final int UNKNOWN_WEEKDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public static final int STATUS_DRAFT = 0;
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_MEET = 2;
    public static final int STATUS_TRIAL = 3;
    public static final int STATUS_ILL = 4;
    public static final int STATUS_HOLIDAY = 5;
    public static final int STATUS_RESIGNED = 6;

    public static final ObservableList<String> LESSON_STATUS_LIST = FXCollections.observableArrayList(
            "Entwurf",
            "Aktiv",
            "Schnupper-Unterricht",
            "Probemonat",
            "Unterbochen (Krankheit)",
            "Unterbrochen (Urlaub)",
            "Gekündigt"
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

    public static void insertLesson(Lesson lesson) {
        insert(LESSON_TABLE, lesson.sqlColumns(), lesson.sqlValues());
    }

    public static void deleteLesson(Lesson lesson) {
        delete(LESSON_TABLE, lesson.getId());
    }

    public static void updateLesson(Lesson lesson, int id) {
        updateMultiple(LESSON_TABLE, id, lesson.valuesToSQLUpdateString());
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

    public static int repeatModeFromString(String repeatMode) {
        if (repeatMode == null) return REPEAT_OFF;
        return switch(repeatMode) {
            case "jede Woche" -> REPEAT_WEEKLY;
            case "jede 2. Woche" -> REPEAT_2WEEKS;
            case "jede 3. Woche" -> REPEAT_3WEEKS;
            case "jede 4. Woche" -> REPEAT_4WEEKS;
            case "jede 5. Woche" -> REPEAT_5WEEKS;
            case "jede 6. Woche" -> REPEAT_6WEEKS;
            case "individuelle Termine" -> REPEAT_CUSTOM;
            default -> REPEAT_OFF;
        };
    }

    public static String repeatStringFromInt(int repeat) {
        return switch(repeat) {
            case REPEAT_OFF -> "einmaliger Termin";
            case REPEAT_WEEKLY -> "jede Woche";
            case REPEAT_2WEEKS -> "jede 2. Woche";
            case REPEAT_3WEEKS -> "jede 3. Woche";
            case REPEAT_4WEEKS -> "jede 4. Woche";
            case REPEAT_5WEEKS -> "jede 5. Woche";
            case REPEAT_6WEEKS -> "jede 6. Woche";
            case REPEAT_CUSTOM -> "individuelle Termine";
            default -> "-";
        };
    }

    public static int weekdayFromString(String weekday) {
        if (weekday == null) return UNKNOWN_WEEKDAY;
        return switch(weekday) {
            case "Montag" -> MONDAY;
            case "Dienstag" -> TUESDAY;
            case "Mittwoch" -> WEDNESDAY;
            case "Donnerstag" -> THURSDAY;
            case "Freitag" -> FRIDAY;
            case "Samstag" -> SATURDAY;
            default -> UNKNOWN_WEEKDAY;
        };
    }

}
