package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import prodegus.musetasks.database.Filter;
import prodegus.musetasks.workspace.cells.StudentListCell;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.database.Database.*;

public class StudentModel {

    public static void enableStudentSearch(ComboBox<Student> comboBox) {
        FilteredList<Student> filteredStudents = new FilteredList<>(comboBox.getItems());
        comboBox.setItems(filteredStudents);
        comboBox.getEditor().textProperty().addListener(observable -> {
            comboBox.hide();
            if (comboBox.getSelectionModel().getSelectedItem() != null) return;
            String filter = comboBox.getEditor().getText();
            if (filter == null || filter.isBlank()) {
                filteredStudents.setPredicate(contact -> true);
            } else {
                filteredStudents.setPredicate(contact ->
                        containsIgnoreCase(contact.getLastName(), filter) ||
                                containsIgnoreCase(contact.getFirstName(), filter));
                if (!filteredStudents.isEmpty()) comboBox.show();
            }
        });
    }

    public static void initializeForStudents(ComboBox<Student> comboBox) {
        comboBox.setItems(getStudentListFromDB());
        comboBox.setButtonCell(new StudentListCell());
        comboBox.setCellFactory(student -> new StudentListCell());
        comboBox.setConverter(studentStringConverter);
        enableStudentSearch(comboBox);
    }

    public static StringConverter<Student> studentStringConverter = new StringConverter<Student>() {
        @Override
        public String toString(Student student) {
            if (student == null) return "";
            return student.formalName();
        }

        @Override
        public Student fromString(String string) {
            for (Student student : getStudentListFromDB())
                if (student.formalName().equals(string)) return student;
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

    public static List<Student> studentsAdded(List<Student> oldStudents, List<Student> newStudents) {
        List<Student> studentsAdded = new ArrayList<>();
        List<Integer> oldIds = new ArrayList<>();
        List<Integer> newIds = new ArrayList<>();

        for (Student student : oldStudents) {
            oldIds.add(student.getId());
        }

        for (Student student : newStudents) {
            newIds.add(student.getId());
        }

        newIds.removeAll(oldIds);

        for (Integer id : newIds) {
            studentsAdded.add(getStudentFromDB(id));
        }

        return studentsAdded;
    }

    public static List<Student> studentsRemoved(List<Student> oldStudents, List<Student> newStudents) {
        List<Student> studentsRemoved = new ArrayList<>();
        List<Integer> oldIds = new ArrayList<>();
        List<Integer> newIds = new ArrayList<>();

        for (Student student : oldStudents) {
            oldIds.add(student.getId());
        }

        for (Student student : newStudents) {
            newIds.add(student.getId());
        }

        oldIds.removeAll(newIds);

        for (Integer id : oldIds) {
            studentsRemoved.add(getStudentFromDB(id));
        }

        return studentsRemoved;
    }

}
