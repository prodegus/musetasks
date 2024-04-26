package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import prodegus.musetasks.database.Filter;

import java.sql.*;

import static prodegus.musetasks.database.Database.*;

public class StudentModel {

    public static StringConverter<Student> studentStringConverter = new StringConverter<Student>() {
        @Override
        public String toString(Student student) {
            return student.getLastName() + ", " + student.getFirstName();
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

    public static ObservableList<Student> getFilteredStudentListFromDB(Filter filter1, Filter... filters) {
        ObservableList<Student> students = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + STUDENT_TABLE + " WHERE ");

        sql.append(filter1.toSQLString());
        if (filters.length > 0) sql.append(" AND ");

        int i = 1;
        for (Filter filter : filters) {
            sql.append(filter.toSQLString());
            if (i < filters.length) sql.append(" AND ");
            i++;
        }

        students.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql.toString())) {
            while (rs.next()) {
                Student student = new Student();
                student.setAttributes(rs);
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
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

    public static int findStudentId(String lastName, String firstName, int parentId) {
        int studentId = 0;
        String sql = "SELECT id FROM " + STUDENT_TABLE +
                " WHERE lastname = '" + lastName + "'" +
                " AND firstname = '" + firstName + "'" +
                " AND (parentid1 = " + parentId +
                "      OR parentid2 = " + parentId + ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                studentId = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return studentId;
    }

}
