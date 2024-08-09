package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import prodegus.musetasks.workspace.cells.ContactListCell;
import prodegus.musetasks.workspace.cells.TeacherListCellFormal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.database.Database.*;

public class TeacherModel {

    public static void enableTeacherSearch(ComboBox<Teacher> comboBox) {
        FilteredList<Teacher> filteredTeachers = new FilteredList<>(comboBox.getItems());
        comboBox.setItems(filteredTeachers);
        comboBox.getEditor().textProperty().addListener(observable -> {
            comboBox.hide();
            if (comboBox.getSelectionModel().getSelectedItem() != null) return;
            String filter = comboBox.getEditor().getText();
            if (filter == null || filter.isBlank()) {
                filteredTeachers.setPredicate(contact -> true);
            } else {
                filteredTeachers.setPredicate(contact ->
                        containsIgnoreCase(contact.getLastName(), filter) ||
                                containsIgnoreCase(contact.getFirstName(), filter));
                if (!filteredTeachers.isEmpty()) comboBox.show();
            }
        });
    }

    public static void initializeForTeachers(ComboBox<Teacher> comboBox) {
        comboBox.setItems(getTeacherListFromDB());
        comboBox.setButtonCell(new TeacherListCellFormal());
        comboBox.setCellFactory(teacher -> new TeacherListCellFormal());
        comboBox.setConverter(teacherStringConverterFormal);
        enableTeacherSearch(comboBox);
    }

    public static StringConverter<Teacher> teacherStringConverterFormal = new StringConverter<Teacher>() {
        @Override
        public String toString(Teacher teacher) {
            if (teacher == null) return "";
            return teacher.formalName();
        }

        @Override
        public Teacher fromString(String string) {
            for (Teacher teacher : getTeacherListFromDB()) {
                if (teacher.formalName().equals(string)) return teacher;
            }
            return null;
        }
    };

    public static StringConverter<Teacher> teacherStringConverterShort = new StringConverter<Teacher>() {
        @Override
        public String toString(Teacher teacher) {
            return teacher.getFirstName() + " " + teacher.getLastName().charAt(0);
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
