package prodegus.musetasks.school;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import prodegus.musetasks.contacts.Student;
import prodegus.musetasks.contacts.Teacher;
import prodegus.musetasks.lessons.Lesson;
import prodegus.musetasks.workspace.cells.LocationListCell;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.school.School.SCHOOL_LOCATIONS;

public class LocationModel {
    public static void initializeForLocations(ComboBox<Location> comboBox) {
        comboBox.setItems(SCHOOL_LOCATIONS);
        comboBox.setCellFactory(string -> new LocationListCell());
        comboBox.setConverter(locationStringConverter);
    }

    public static void initializeForLocations(ComboBox<Location> location, ComboBox<String> room) {
        location.setItems(SCHOOL_LOCATIONS);
        location.setCellFactory(string -> new LocationListCell());
        location.setConverter(locationStringConverter);
        location.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)
                -> room.setItems(FXCollections.observableArrayList(newValue.rooms())));
    }

    public static Location getLocationFromDB(int id) {
        String sql = "SELECT * FROM " + LOCATION_TABLE + " WHERE id = " + id;
        Location location = new Location();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                location.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return location;
    }
    
    public static ObservableList<Location> getLocationListFromDB() {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + LOCATION_TABLE;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Location location = new Location();
                location.setAttributes(rs);
                locations.add(location);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return locations;
    }
    
    public static void insertLocation(Location location) {
        insert(LOCATION_TABLE, location.sqlColumns(), location.sqlValues());
    }
    
    public static void updateLocation(Location location, int id) {
        updateMultiple(LOCATION_TABLE, id, location.valuesToSQLUpdateString());
    }

    public static StringConverter<Location> locationStringConverter = new StringConverter<>() {
        @Override
        public String toString(Location location) {
            return location.getName();
        }

        @Override
        public Location fromString(String string) {
            return LocationModel.fromString(string);
        }
    };

    public static Location fromString(String locationName) {
        for (Location location : getLocationListFromDB()) {
            if (location.getName().equals(locationName)) return location;
        }
        return null;
    }
}
