package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

import static prodegus.musetasks.database.Database.*;

public class StudentModel {

    public static ObservableList<Student> getStudentList() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        refreshStudentList(students);
        return students;
    }

    public static void refreshStudentList(ObservableList<Student> students) {
        String sql = "SELECT * FROM " + STUDENT_TABLE;

        students.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student();
                student.setAttributes(rs);
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Student getStudent(String id) {
        String sql = "SELECT * FROM " + STUDENT_TABLE + " WHERE id = " + id;
        Student student = new Student();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                student.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return student;
    }

}
