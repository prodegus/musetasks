package prodegus.musetasks.contacts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DataFormatter;

import static prodegus.musetasks.database.Database.*;

public class ContactModel {

    public static ObservableList<Contact> getContactList() {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        refreshContactList(contacts);
        return contacts;
    }

    public static void refreshContactList(ObservableList<Contact> contacts) {
        contacts.clear();
        addContactsFromTable(STUDENT_TABLE, contacts);
        addContactsFromTable(TEACHER_TABLE, contacts);
        addContactsFromTable(PARENT_TABLE, contacts);
        addContactsFromTable(OTHER_TABLE, contacts);
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

    public static void addContact(Contact contact) {
        String sql = "INSERT INTO ? VALUES (?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contact.table());
            ps.setString(2, contact.valuesToSQLString());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Contact getContact(String table, String id) {
        String sql = "SELECT * FROM " + table + " WHERE id = " + id;
        Contact contact = new Student();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                contact.setAttributes(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contact;
    }

    public static void deleteContact(Contact contact) {
        String sql = "DELETE FROM ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, contact.table());
            ps.setString(2, contact.id());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addContactsFromXLS(File file, String tableName) {
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

    public static void addNote(Contact contact, String newNote) {
        update(contact, "notes", "notes || " + newNote);
    }

    public static void saveEditedNotes(Contact contact, String editedNotes) {
        update(contact, "notes", editedNotes);
    }

    public static void update(Contact contact, String column, String newValue) {
        update(contact.table(), contact.id(), column, newValue);
    }

    public static void update(String table, String id, String column, String newValue) {
        String sql = "UPDATE ? SET ? = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, table);
            ps.setString(2, column);
            ps.setString(3, newValue);
            ps.setString(4, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
