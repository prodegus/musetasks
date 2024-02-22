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
import prodegus.musetasks.overview.OverviewController;

import static prodegus.musetasks.database.Database.*;

public class ContactModel {


    public static ObservableList<Contact> getContactList() {
        String sql = "SELECT * FROM " + CONTACT_TABLE;
        ObservableList<Contact> list = FXCollections.observableArrayList();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getString(1));
                contact.setLastname(rs.getString(2));
                contact.setFirstname(rs.getString(3));
                contact.setCategory(rs.getString(4));
                contact.setLocation(rs.getString(5));
                contact.setPhone(rs.getString(6));
                contact.setEmail(rs.getString(7));
                contact.setZoom(rs.getString(8));
                contact.setSkype(rs.getString(9));
                contact.setBirthday(rs.getString(10));
                contact.setNotes(rs.getString(11));
                list.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void refreshContactList(ObservableList<Contact> contacts) {
        String sql = "SELECT * FROM " + CONTACT_TABLE;

        contacts.clear();
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Contact contact = new Contact();
                contact.setId(rs.getString(1));
                contact.setLastname(rs.getString(2));
                contact.setFirstname(rs.getString(3));
                contact.setCategory(rs.getString(4));
                contact.setLocation(rs.getString(5));
                contact.setPhone(rs.getString(6));
                contact.setEmail(rs.getString(7));
                contact.setZoom(rs.getString(8));
                contact.setSkype(rs.getString(9));
                contact.setBirthday(rs.getString(10));
                contact.setNotes(rs.getString(11));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void addContact(Contact contact) {
        String sql =
                "INSERT INTO " + CONTACT_TABLE + " VALUES ('" +
                        "NULL"                 + "', '" + // ID (auto-generated)
                        contact.getLastname()  + "', '" +
                        contact.getFirstname() + "', '" +
                        contact.getCategory()  + "', '" +
                        contact.getLocation()  + "', '" +
                        contact.getPhone()     + "', '" +
                        contact.getEmail()     + "', '" +
                        contact.getZoom()      + "', '" +
                        contact.getSkype()     + "', '" +
                        contact.getBirthday()  + "', '" +
                        contact.getNotes()     + "')";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Contact getContact(String id) {
        String sql = "SELECT * FROM " + CONTACT_TABLE + " WHERE id = " + id;
        Contact contact = new Contact();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                contact.setId(rs.getString(1));
                contact.setLastname(rs.getString(2));
                contact.setFirstname(rs.getString(3));
                contact.setCategory(rs.getString(4));
                contact.setLocation(rs.getString(5));
                contact.setPhone(rs.getString(6));
                contact.setEmail(rs.getString(7));
                contact.setZoom(rs.getString(8));
                contact.setSkype(rs.getString(9));
                contact.setBirthday(rs.getString(10));
                contact.setNotes(rs.getString(11));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contact;
    }

    public static void addContactsFromXLS(File file) {
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
                    sql.append("INSERT INTO " + CONTACT_TABLE + " VALUES (null, ");
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

    public static void addNote(String contactID, String newNote) {
        String sql = "UPDATE " + CONTACT_TABLE + " SET notes = notes || ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newNote);
            ps.setString(2, contactID);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static void saveEditedNotes(String contactID, String newNotes) {
        String sql = "UPDATE " + CONTACT_TABLE + " SET notes = ? WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newNotes);
            ps.setString(2, contactID);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
