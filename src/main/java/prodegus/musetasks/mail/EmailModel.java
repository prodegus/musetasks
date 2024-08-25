package prodegus.musetasks.mail;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import prodegus.musetasks.database.Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;
import java.util.prefs.Preferences;

import static prodegus.musetasks.database.Database.*;
import static prodegus.musetasks.utils.DateTime.toInt;

public class EmailModel {
    public static ObservableList<Email> getEmailSentListFromDB() {
        return getEmailListFromDB(" WHERE draft = 0");
    }

    public static ObservableList<Email> getEmailDraftListFromDB() {
        return getEmailListFromDB(" WHERE draft = 1");
    }

    public static ObservableList<Email> getEmailListFromDB() {
        return getEmailListFromDB("");
    }

    public static ObservableList<Email> getEmailListFromDB(LocalDate date) {
        return getEmailListFromDB(" WHERE date = " + toInt(date));
    }

    public static ObservableList<Email> getEmailListFromDB(LocalDate from, LocalDate to) {
        return getEmailListFromDB(" WHERE date BETWEEN " + toInt(from) + " AND " + toInt(to));
    }

    public static ObservableList<Email> getEmailListFromDB(String filter) {
        ObservableList<Email> emails = FXCollections.observableArrayList();
        String sql = "SELECT * FROM " + EMAIL_TABLE + filter;

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Email email = new Email();
                email.setAttributes(rs);
                emails.add(email);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return emails;
    }

    public static void insert(Email email) {
        Database.insert(EMAIL_TABLE, email.sqlColumns(), email.sqlValues());
    }

    public static void deleteEmailsFromDB(List<Email> emails) {
        for (Email email : emails) {
            Database.delete(EMAIL_TABLE, email.getId());
        }
    }

    public static void deleteEmailFromDB(Email email) {
        Database.delete(EMAIL_TABLE, email.getId());
    }

    public static void update(Email email, int id) {
        updateMultiple(EMAIL_TABLE, id, email.valuesToSQLUpdateString());
    }

    public static final Preferences prefs = Preferences.userNodeForPackage(Database.class);

    public static void setMailCredentials(String username, String password, String sender) {
        prefs.put("mail_user", username);
        prefs.put("mail_password", password);
        prefs.put("mail_sender", sender);
    }

    public static String getMailUser() {
        return prefs.get("mail_user", null);
    }

    public static String getMailPassword() {
        return prefs.get("mail_password", null);
    }

    public static String getMailSender() {
        return prefs.get("mail_sender", null);
    }

}
