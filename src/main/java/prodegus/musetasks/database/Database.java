package prodegus.musetasks.database;

import java.sql.*;
import java.util.prefs.Preferences;

import static prodegus.musetasks.database.DBConstants.*;


public class Database {
    public static final Preferences prefs = Preferences.userNodeForPackage(Database.class);
    public static final String USER_TABLE = "mtusers";
    public static final String CONTACT_TABLE = "mtcontacts";
    public static final String LESSON_TABLE = "mtlessons";
    public static final String DB_PATH = prefs.get("DB_PATH", "Path not found");

    public static boolean connected() {
        try (Connection conn = connect()) {
            return conn != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveDbPath(String path) {
        prefs.put("DB_PATH", path);
    }

    public static boolean tableExists(String name) {
        String sql = "SELECT name FROM sqlite_master WHERE type = 'table' AND name = '" + name + "'";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createUser(String user, String pw) {
        String sql = "INSERT INTO " + USER_TABLE + " VALUES ('" + user + "', '" + pw + "')";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean loginValid(String user, String pw) {
        String sql = "SELECT * FROM " + USER_TABLE + " WHERE user = '" + user + "' AND password = '" + pw + "'";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createUserTable(String dbPath) {
        String sql = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (user TEXT, password TEXT)";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createContactTable(String dbPath) {
        String sql =
                "CREATE TABLE IF NOT EXISTS " + CONTACT_TABLE + " (" +
                ContactColumns.ID        + " INTEGER PRIMARY KEY ASC AUTOINCREMENT, " +
                ContactColumns.LASTNAME  + " TEXT, " +
                ContactColumns.FIRSTNAME + " TEXT, " +
                ContactColumns.CATEGORY  + " TEXT, " +
                ContactColumns.LOCATION  + " TEXT, " +
                ContactColumns.PHONE     + " TEXT, " +
                ContactColumns.EMAIL     + " TEXT, " +
                ContactColumns.ZOOM      + " TEXT, " +
                ContactColumns.SKYPE     + " TEXT, " +
                ContactColumns.BIRTHDAY  + " TEXT, " +
                ContactColumns.NOTES     + " TEXT " +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createLessonTable(String dbPath) {
        String sql =
                "CREATE TABLE IF NOT EXISTS " + LESSON_TABLE + " (" +
                "id         INTEGER PRIMARY KEY AUTOINCREMENT," +
                "lessonname TEXT," +
                "teacherid  INTEGER REFERENCES mtcontacts (id)," +
                "location   TEXT," +
                "frequency  TEXT," +
                "weekday    TEXT," +
                "time       TEXT," +
                "duration   INTEGER," +
                "pupilid1   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid2   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid3   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid4   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid5   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid6   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid7   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid8   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid9   INTEGER REFERENCES mtcontacts (id)," +
                "pupilid10  INTEGER REFERENCES mtcontacts (id) " +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEmptyTable(String table) {
        String sql = "SELECT EXISTS (SELECT 1 FROM " + table + ")";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1) == 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
    }

    private static Connection connectTo(String path) throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + path);
    }
}
