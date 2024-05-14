package prodegus.musetasks.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.contacts.Contact;
import prodegus.musetasks.database.Filter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.StringJoiner;
import java.util.TimeZone;

import static prodegus.musetasks.database.Database.CONTACT_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class Test {

    public static void main(String[] args) {
        System.out.println();
        System.out.println(sqlValues());

    }

    public static String sqlValues() {
        String superValues = "'Duck', 'Daisy', '1', 'Blumenstr. 12', '12384', 'Bockendorf', '01.07.1992', '16.04.24   15:45\n" +
                "Test-Notiz zu Schüler aus der \"Schüler anlegen\"-Maske'";
        StringJoiner values = new StringJoiner("', '", "'", "'").setEmptyValue("");
        StringJoiner result = new StringJoiner(", ").add(superValues);

//            if (!this.getInstrument1().isBlank()) values.add(this.getInstrument1());
//            if (!this.getInstrument2().isBlank()) values.add(this.getInstrument2());
//            if (!this.getInstrument3().isBlank()) values.add(this.getInstrument3());
//            if (this.getProspective()) values.add("1");
//            if (!this.getStatus().isBlank()) values.add(this.getStatus());
//            if (!this.getStatusFrom().isBlank()) values.add(this.getStatusFrom());
//            if (!this.getStatusTo().isBlank()) values.add(this.getStatusTo());
//            if (this.getParentId1() != 0) values.add(String.valueOf(this.getParentId1()));
//            if (this.getParentId2() != 0) values.add(String.valueOf(this.getParentId2()));
//            if (this.getTeacherId1() != 0) values.add(String.valueOf(this.getTeacherId1()));
//            if (this.getTeacherId2() != 0) values.add(String.valueOf(this.getTeacherId2()));
//            if (this.getTeacherId3() != 0) values.add(String.valueOf(this.getTeacherId3()));
//            if (this.getLessonId1() != 0) values.add(String.valueOf(this.getLessonId1()));
//            if (this.getLessonId2() != 0) values.add(String.valueOf(this.getLessonId2()));
//            if (this.getLessonId3() != 0) values.add(String.valueOf(this.getLessonId3()));
        if (!values.toString().isEmpty()) result.add(values.toString());
        return result.toString();
    }

}