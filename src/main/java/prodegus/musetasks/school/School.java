package prodegus.musetasks.school;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static prodegus.musetasks.school.LocationModel.getLocationListFromDB;
import static prodegus.musetasks.school.SchoolModel.getInstrumentListFromDB;

public class School {
    public static ObservableList<String> SCHOOL_INSTRUMENTS = getInstrumentListFromDB();
    public static ObservableList<Location> SCHOOL_LOCATIONS = getLocationListFromDB();
}
