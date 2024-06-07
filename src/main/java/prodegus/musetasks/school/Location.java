package prodegus.musetasks.school;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Location {
    public static void main(String[] args) {
        Location location = new Location();
        System.out.println("location.getId(): " + location.getId());
    }

    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty room1 = new SimpleStringProperty();
    private SimpleStringProperty room2 = new SimpleStringProperty();
    private SimpleStringProperty room3 = new SimpleStringProperty();
    private SimpleStringProperty room4 = new SimpleStringProperty();
    private SimpleStringProperty room5 = new SimpleStringProperty();
    private SimpleStringProperty room6 = new SimpleStringProperty();
    private SimpleStringProperty room7 = new SimpleStringProperty();
    private SimpleStringProperty room8 = new SimpleStringProperty();
    private SimpleStringProperty room9 = new SimpleStringProperty();
    private SimpleStringProperty room10 = new SimpleStringProperty();

    public Location() {}

    public Location(SimpleStringProperty name, SimpleStringProperty room1, SimpleStringProperty room2) {
        this.name = name;
        this.room1 = room1;
        this.room2 = room2;
    }

    public Location(SimpleStringProperty name, SimpleStringProperty room1, SimpleStringProperty room2, SimpleStringProperty room3) {
        this.name = name;
        this.room1 = room1;
        this.room2 = room2;
        this.room3 = room3;
    }

    public Location(SimpleStringProperty name, SimpleStringProperty room1, SimpleStringProperty room2, SimpleStringProperty room3, SimpleStringProperty room4, SimpleStringProperty room5) {
        this.name = name;
        this.room1 = room1;
        this.room2 = room2;
        this.room3 = room3;
        this.room4 = room4;
        this.room5 = room5;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRoom1() {
        return room1.get();
    }

    public SimpleStringProperty room1Property() {
        return room1;
    }

    public void setRoom1(String room1) {
        this.room1.set(room1);
    }

    public String getRoom2() {
        return room2.get();
    }

    public SimpleStringProperty room2Property() {
        return room2;
    }

    public void setRoom2(String room2) {
        this.room2.set(room2);
    }

    public String getRoom3() {
        return room3.get();
    }

    public SimpleStringProperty room3Property() {
        return room3;
    }

    public void setRoom3(String room3) {
        this.room3.set(room3);
    }

    public String getRoom4() {
        return room4.get();
    }

    public SimpleStringProperty room4Property() {
        return room4;
    }

    public void setRoom4(String room4) {
        this.room4.set(room4);
    }

    public String getRoom5() {
        return room5.get();
    }

    public SimpleStringProperty room5Property() {
        return room5;
    }

    public void setRoom5(String room5) {
        this.room5.set(room5);
    }

    public String getRoom6() {
        return room6.get();
    }

    public SimpleStringProperty room6Property() {
        return room6;
    }

    public void setRoom6(String room6) {
        this.room6.set(room6);
    }

    public String getRoom7() {
        return room7.get();
    }

    public SimpleStringProperty room7Property() {
        return room7;
    }

    public void setRoom7(String room7) {
        this.room7.set(room7);
    }

    public String getRoom8() {
        return room8.get();
    }

    public SimpleStringProperty room8Property() {
        return room8;
    }

    public void setRoom8(String room8) {
        this.room8.set(room8);
    }

    public String getRoom9() {
        return room9.get();
    }

    public SimpleStringProperty room9Property() {
        return room9;
    }

    public void setRoom9(String room9) {
        this.room9.set(room9);
    }

    public String getRoom10() {
        return room10.get();
    }

    public SimpleStringProperty room10Property() {
        return room10;
    }

    public void setRoom10(String room10) {
        this.room10.set(room10);
    }

    public List<String> rooms() {
        List<String> rooms = new ArrayList<>();
        if (this.getRoom1() != null) rooms.add(this.getRoom1());
        if (this.getRoom2() != null) rooms.add(this.getRoom2());
        if (this.getRoom3() != null) rooms.add(this.getRoom3());
        if (this.getRoom4() != null) rooms.add(this.getRoom4());
        if (this.getRoom5() != null) rooms.add(this.getRoom5());
        if (this.getRoom6() != null) rooms.add(this.getRoom6());
        if (this.getRoom7() != null) rooms.add(this.getRoom7());
        if (this.getRoom8() != null) rooms.add(this.getRoom8());
        if (this.getRoom9() != null) rooms.add(this.getRoom9());
        if (this.getRoom10() != null) rooms.add(this.getRoom10());
        return rooms;
    }

    public void setAttributes(ResultSet rs) throws SQLException {
        this.setId(rs.getInt("id"));
        this.setName(rs.getString("name"));
        this.setRoom1(rs.getString("room1"));
        this.setRoom2(rs.getString("room2"));
        this.setRoom3(rs.getString("room3"));
        this.setRoom4(rs.getString("room4"));
        this.setRoom5(rs.getString("room5"));
        this.setRoom6(rs.getString("room6"));
        this.setRoom7(rs.getString("room7"));
        this.setRoom8(rs.getString("room8"));
        this.setRoom9(rs.getString("room9"));
        this.setRoom10(rs.getString("room10"));
    }

    public String sqlColumns() {
        StringJoiner columns = new StringJoiner(", ");

        columns.add("name");
        if (this.getRoom1() != null) columns.add("room1");
        if (this.getRoom2() != null) columns.add("room2");
        if (this.getRoom3() != null) columns.add("room3");
        if (this.getRoom4() != null) columns.add("room4");
        if (this.getRoom5() != null) columns.add("room5");
        if (this.getRoom6() != null) columns.add("room6");
        if (this.getRoom7() != null) columns.add("room7");
        if (this.getRoom8() != null) columns.add("room8");
        if (this.getRoom9() != null) columns.add("room9");
        if (this.getRoom10() != null) columns.add("room10");

        return columns.toString();
    }

    public String sqlValues() {
        StringJoiner values = new StringJoiner("', '", "'", "'");

        values.add(this.getName());
        if (this.getRoom1() != null) values.add(getRoom1());
        if (this.getRoom2() != null) values.add(getRoom2());
        if (this.getRoom3() != null) values.add(getRoom3());
        if (this.getRoom4() != null) values.add(getRoom4());
        if (this.getRoom5() != null) values.add(getRoom5());
        if (this.getRoom6() != null) values.add(getRoom6());
        if (this.getRoom7() != null) values.add(getRoom7());
        if (this.getRoom8() != null) values.add(getRoom8());
        if (this.getRoom9() != null) values.add(getRoom9());
        if (this.getRoom10() != null) values.add(getRoom10());

        return values.toString();
    }

    public String valuesToSQLUpdateString() {
        StringBuilder sb = new StringBuilder();

        sb.append("name  = '").append(this.getName()).append("', ");
        sb.append("room1  = ").append(this.getRoom1() == null ? "null" : "'" + this.getRoom1() + "'").append(", ");
        sb.append("room2  = ").append(this.getRoom2() == null ? "null" : "'" + this.getRoom2() + "'").append(", ");
        sb.append("room3  = ").append(this.getRoom3() == null ? "null" : "'" + this.getRoom3() + "'").append(", ");
        sb.append("room4  = ").append(this.getRoom4() == null ? "null" : "'" + this.getRoom4() + "'").append(", ");
        sb.append("room5  = ").append(this.getRoom5() == null ? "null" : "'" + this.getRoom5() + "'").append(", ");
        sb.append("room6  = ").append(this.getRoom6() == null ? "null" : "'" + this.getRoom6() + "'").append(", ");
        sb.append("room7  = ").append(this.getRoom7() == null ? "null" : "'" + this.getRoom7() + "'").append(", ");
        sb.append("room8  = ").append(this.getRoom8() == null ? "null" : "'" + this.getRoom8() + "'").append(", ");
        sb.append("room9  = ").append(this.getRoom9() == null ? "null" : "'" + this.getRoom9() + "'").append(", ");
        sb.append("room10 = ").append(this.getRoom10() == null ? "null" : "'" + this.getRoom10() + "'");

        return sb.toString();
    }
}
