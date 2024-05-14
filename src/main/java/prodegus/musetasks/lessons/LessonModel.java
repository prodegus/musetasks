package prodegus.musetasks.lessons;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.LESSON_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class LessonModel {

    public static final int CATEGORY_SINGLE = 1;
    public static final int CATEGORY_GROUP = 2;
    public static final int CATEGORY_COURSE = 3;
    public static final int CATEGORY_WORKGROUP = 4;

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

}
