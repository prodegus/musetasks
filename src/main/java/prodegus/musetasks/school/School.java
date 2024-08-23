package prodegus.musetasks.school;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import static prodegus.musetasks.school.LocationModel.getLocationListFromDB;
import static prodegus.musetasks.school.SchoolModel.getInstrumentListFromDB;
import static prodegus.musetasks.school.SchoolModel.getSchoolNameFromDB;

public class School {
    public static String getActiveUser() {
        return activeUser.get();
    }

    public static SimpleStringProperty activeUserProperty() {
        return activeUser;
    }

    public static void setActiveUser(String user) {
        activeUser.set(user);
    }

    private static final SimpleStringProperty activeUser = new SimpleStringProperty();
    public static String SCHOOL_NAME = getSchoolNameFromDB();
    public static ObservableList<String> SCHOOL_INSTRUMENTS = getInstrumentListFromDB();
    public static ObservableList<Location> SCHOOL_LOCATIONS = getLocationListFromDB();
}
