package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.PARENT_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class ParentModel {

    public static ObservableList<Parent> getParentList() {
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

    public static Parent getParent(String id) {
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

}
