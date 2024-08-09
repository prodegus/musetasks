package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import prodegus.musetasks.workspace.cells.ParentListCell;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static prodegus.musetasks.contacts.ParentModel.*;
import static prodegus.musetasks.database.Database.OTHER_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class OtherModel {

    public static ObservableList<Other> getOtherListFromDB() {
        ObservableList<Other> others = FXCollections.observableArrayList();
        refreshOtherListFromDB(others);
        return others;
    }

    public static void refreshOtherListFromDB(ObservableList<Other> others) {
        String sql = "SELECT * FROM " + OTHER_TABLE;

        others.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Other other = new Other();
                other.setAttributes(rs);
                others.add(other);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Other getOtherFromDB(int id) {
        String sql = "SELECT * FROM " + OTHER_TABLE + " WHERE id = " + id;
        Other other = new Other();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                other.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return other;
    }

}
