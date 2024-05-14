package prodegus.musetasks.login;

import javafx.beans.property.SimpleStringProperty;
import prodegus.musetasks.database.Database;

import java.util.prefs.Preferences;

public class Settings {
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
