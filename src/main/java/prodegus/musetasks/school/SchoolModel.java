package prodegus.musetasks.school;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.*;

public class SchoolModel {

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
