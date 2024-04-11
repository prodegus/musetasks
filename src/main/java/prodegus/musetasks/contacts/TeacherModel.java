package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.*;

public class TeacherModel {

    public static ObservableList<Teacher> getTeacherList() {
        ObservableList<Teacher> teachers = FXCollections.observableArrayList();
        refreshTeacherList(teachers);
        return teachers;
    }

    public static void refreshTeacherList(ObservableList<Teacher> teachers) {
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

    public static Teacher getTeacher(String id) {
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
