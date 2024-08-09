package prodegus.musetasks.school;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import prodegus.musetasks.workspace.cells.StringListCell;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.school.School.SCHOOL_INSTRUMENTS;

public class SchoolModel {

    public static void initializeForInstruments(ComboBox<String> comboBox) {
        comboBox.setItems(SCHOOL_INSTRUMENTS);
        comboBox.setCellFactory(string -> new StringListCell());
    }

    public static ObservableList<String> getInstrumentListFromDB() {
        ObservableList<String> instruments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + INSTRUMENT_TABLE;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                instruments.add(rs.getString("instrument"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return instruments;
    }

}
