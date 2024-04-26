package prodegus.musetasks.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.contacts.Contact;
import prodegus.musetasks.database.Filter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static prodegus.musetasks.database.Database.CONTACT_TABLE;
import static prodegus.musetasks.database.Database.connect;

public class Test {

    public static void main(String[] args) {
        Filter filter1 = new Filter("location", "Lohmar");
        Filter filter2 = new Filter("instrument", "Gitarre");
        ObservableList<Contact> contacts = getFilteredContactListFromDB(filter1, filter2);

    }

    public static ObservableList<Contact> getFilteredContactListFromDB(Filter filter1, Filter... filters) {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM " + CONTACT_TABLE + " WHERE ");

        sql.append(filter1.toSQLString());
        if (filters.length > 0) sql.append(" AND ");

        int i = 1;
        for (Filter filter : filters) {
            sql.append(filter.toSQLString());
            if (i < filters.length) sql.append(" AND ");
            i++;
        }
        System.out.println(sql);
        return contacts;
    }
}

