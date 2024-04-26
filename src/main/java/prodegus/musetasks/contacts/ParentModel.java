package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.PARENT_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class ParentModel {

    public static StringConverter<Parent> parentStringConverterFormal = new StringConverter<Parent>() {
        @Override
        public String toString(Parent parent) {
            return parent.getLastName() + ", " + parent.getFirstName();
        }

        @Override
        public Parent fromString(String string) {
            return null;
        }
    };

    public static StringConverter<Parent> parentStringConverterShort = new StringConverter<Parent>() {
        @Override
        public String toString(Parent parent) {
            return parent.getFirstName() + " " + parent.getLastName().charAt(0);
        }

        @Override
        public Parent fromString(String string) {
            return null;
        }
    };

    public static ObservableList<Parent> getParentListFromDB() {
        ObservableList<Parent> parents = FXCollections.observableArrayList();
        refreshParentList(parents);
        return parents;
    }

    public static void refreshParentList(ObservableList<Parent> parents) {
        String sql = "SELECT * FROM " + PARENT_TABLE;

        parents.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Parent parent = new Parent();
                parent.setAttributes(rs);
                parents.add(parent);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Parent getParentFromDB(int id) {
        String sql = "SELECT * FROM " + PARENT_TABLE + " WHERE id = " + id;
        Parent parent = new Parent();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                parent.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return parent;
    }

    public static int findParentId(String lastName, String firstName, int childId) {
        int parentId = 0;
        String sql = "SELECT id FROM " + PARENT_TABLE +
                " WHERE lastname = '" + lastName + "'" +
                " AND firstname = '" + firstName + "'" +
                " AND (childid1 = " + childId +
                "      OR childid2 = " + childId +
                "      OR childid3 = " + childId +
                "      OR childid4 = " + childId +
                "      OR childid5 = " + childId + ")";

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                parentId = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return parentId;
    }

}
