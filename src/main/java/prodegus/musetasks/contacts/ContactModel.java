package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.time.format.DateTimeFormatter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;
import prodegus.musetasks.database.Database;
import prodegus.musetasks.database.Filter;

import static prodegus.musetasks.database.Database.*;

public class ContactModel {

    public static final int CATEGORY_STUDENT = 1;
    public static final int CATEGORY_PROSPECTIVE_STUDENT = 11;
    public static final int CATEGORY_PARENT = 2;
    public static final int CATEGORY_PROSPECTIVE_PARENT = 22;
    public static final int CATEGORY_TEACHER = 3;
    public static final int CATEGORY_OTHER = 4;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static ObservableList<Contact> getContactListFromDB() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        addContactsFromTable(STUDENT_TABLE, contacts);
        addContactsFromTable(TEACHER_TABLE, contacts);
        addContactsFromTable(PARENT_TABLE, contacts);
        addContactsFromTable(OTHER_TABLE, contacts);
        return contacts;
    }

    public static void addContactsFromTable(String table, ObservableList<Contact> contacts) {
        String sql = "SELECT * FROM " + table;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setAttributes(rs);
                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

        contacts.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql.toString())) {
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setAttributes(rs);
                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contacts;
    }

    public static void insertContact(Contact contact) {
        insert(contact.table(), contact.sqlColumns(), contact.sqlValues());
    }

    public static void deleteContact(Contact contact) {
        delete(contact.table(), contact.id());
    }

    public static void addContactsFromXLSToDB(File file, String tableName) {
        try (POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
             HSSFWorkbook wb = new HSSFWorkbook(fs);
             Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            HSSFSheet sheet = wb.getSheetAt(0);
            HSSFRow row;
            HSSFCell cell;
            DataFormatter dataFormatter = new DataFormatter();

            int rows;
            rows = sheet.getPhysicalNumberOfRows();

            int cols = 0;
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;
                }
            }

            for(int r = 0; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {
                    StringBuilder sql = new StringBuilder();
                    sql.append("INSERT INTO " + tableName + " VALUES (null, ");
                    for (int c = 1; c < cols - 1; c++) {
                        cell = row.getCell(c);
                        if (cell != null) {
                            sql.append("'").append(dataFormatter.formatCellValue(cell)).append("', ");
                        }
                    }
                    cell = row.getCell(cols - 1);
                    if (cell != null) {
                        sql.append("'").append(dataFormatter.formatCellValue(cell)).append("')");
                    }
                    stmt.execute(sql.toString());
                }
            }
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }

    public static void addNoteInDB(Contact contact, String newNote) {
        String sql = "UPDATE " + contact.table() + " SET notes = notes || '" + newNote + "' WHERE id = " + contact.id();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static void updateContact(Contact contact, int id) {
        updateMultiple(contact.table(), id, contact.valuesToSQLUpdateString());
    }

    public static void updateContact(Contact contact, String column, String newValue) {
        update(contact.table(), contact.id(), column, newValue);
    }

    public static void updateContact(Contact contact, String column, int newValue) {
        update(contact.table(), contact.id(), column, newValue);
    }

    public static int findContactID(Contact contact) {
        int id = 0;
        String sql = "SELECT MAX (id) FROM " + contact.table();
        System.out.println(sql);

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return id;
    }
}
