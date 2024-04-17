package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.sql.*;

import static prodegus.musetasks.database.Database.*;

public class StudentModel {

    public static StringConverter<Student> studentStringConverter = new StringConverter<Student>() {
        @Override
        public String toString(Student student) {
            return student.getLastname() + ", " + student.getFirstname();
        }

        @Override
        public Student fromString(String string) {
            return null;
        }
    };

    public static ObservableList<Student> getStudentListFromDB() {
        ObservableList<Student> students = FXCollections.observableArrayList();
        refreshStudentListFromDB(students);
        return students;
    }

    public static void refreshStudentListFromDB(ObservableList<Student> students) {
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

    public static Student getStudentFromDB(int id) {
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
