package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.*;

public class LessonModel {

    public static final int CATEGORY_SINGLE = 1;
    public static final int CATEGORY_GROUP = 2;
    public static final int CATEGORY_COURSE = 3;
    public static final int CATEGORY_WORKGROUP = 4;

    public static final int REPEAT_PERIOD_OFF = 0;
    public static final int REPEAT_PERIOD_DAY = 1;
    public static final int REPEAT_PERIOD_WEEK = 2;
    public static final int REPEAT_PERIOD_MONTH = 3;
    public static final int REPEAT_PERIOD_YEAR = 4;
    
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public static final ObservableList<String> REPEAT_INTERVALS_DAY = FXCollections.observableArrayList(
            "jeden", "jeden 2.", "jeden 3.", "jeden 4.", "jeden 5.", "jeden 6.", "jeden 7.", "jeden 8.", "jeden 9.", "jeden 10.");
    
    public static final ObservableList<String> REPEAT_INTERVALS_WEEK = FXCollections.observableArrayList(
            "jede", "jede 2.", "jede 3.", "jede 4.", "jede 5.", "jede 6.", "jede 7.", "jede 8.", "jede 9.", "jede 10.");

    public static final ObservableList<String> REPEAT_INTERVALS_MONTH = REPEAT_INTERVALS_DAY;

    public static final ObservableList<String> REPEAT_INTERVALS_YEAR = FXCollections.observableArrayList(
            "jedes", "jedes 2.", "jedes 3.", "jedes 4.", "jedes 5.", "jedes 6.", "jedes 7.", "jedes 8.", "jedes 9.", "jedes 10.");

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
            while (rs.next()) {
                lesson.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lesson;
    }

    public static void insertLesson(Lesson lesson) {
        insert(LESSON_TABLE, lesson.sqlColumns(), lesson.sqlValues());
    }

    public static void deleteLesson(Lesson lesson) {
        delete(LESSON_TABLE, lesson.id());
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

    public static int repeatInterFromString(String interval) {
        return switch(interval) {
            case "jede", "jeden", "jedes" -> 1;
            case "jede 2.", "jeden 2.", "jedes 2." -> 2;
            case "jede 3.", "jeden 3.", "jedes 3." -> 3;
            case "jede 4.", "jeden 4.", "jedes 4." -> 4;
            case "jede 5.", "jeden 5.", "jedes 5." -> 5;
            case "jede 6.", "jeden 6.", "jedes 6." -> 6;
            case "jede 7.", "jeden 7.", "jedes 7." -> 7;
            case "jede 8.", "jeden 8.", "jedes 8." -> 8;
            case "jede 9.", "jeden 9.", "jedes 9." -> 9;
            case "jede 10.", "jeden 10.", "jedes 10." -> 10;
            default -> 0;
        };
    }

    public static int repeatPeriodFromString(String period) {
        return switch(period) {
            case "Tag" -> REPEAT_PERIOD_DAY;
            case "Woche" -> REPEAT_PERIOD_WEEK;
            case "Monat" -> REPEAT_PERIOD_MONTH;
            case "Jahr" -> REPEAT_PERIOD_YEAR;
            default -> REPEAT_PERIOD_OFF;
        };
    }
    
    public static int weekdayFromString(String weekday) {
        return switch(weekday) {
            case "Montag" -> MONDAY;
            case "Dienstag" -> TUESDAY;
            case "Mittwoch" -> WEDNESDAY;
            case "Donnerstag" -> THURSDAY;
            case "Freitag" -> FRIDAY;
            case "Samstag" -> SATURDAY;
            default -> 0;
        };
    }

}
