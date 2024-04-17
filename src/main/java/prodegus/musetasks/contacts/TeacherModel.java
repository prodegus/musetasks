package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.*;

public class TeacherModel {

    public static StringConverter<Teacher> teacherStringConverter = new StringConverter<Teacher>() {
        @Override
        public String toString(Teacher teacher) {
            return teacher.getLastname() + ", " + teacher.getFirstname();
        }

        @Override
        public Teacher fromString(String string) {
            return null;
        }
    };

    public static ObservableList<Teacher> getTeacherListFromDB() {
        ObservableList<Teacher> teachers = FXCollections.observableArrayList();
        refreshTeacherListFromDB(teachers);
        return teachers;
    }

    public static void refreshTeacherListFromDB(ObservableList<Teacher> teachers) {
        String sql = "SELECT * FROM " + TEACHER_TABLE;

        teachers.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Teacher teacher = new Teacher();
                teacher.setAttributes(rs);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Teacher getTeacherFromDB(int id) {
        String sql = "SELECT * FROM " + TEACHER_TABLE + " WHERE id = " + id;
        Teacher teacher = new Teacher();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                teacher.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teacher;
    }

}
