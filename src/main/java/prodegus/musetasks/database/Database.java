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
                "CREATE TABLE mtcontacts (" +
                "    contactid  INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    lastname   TEXT," +
                "    firstname  TEXT," +
                "    category   TEXT," +
                "    location   TEXT," +
                "    street     TEXT," +
                "    postalcode INTEGER," +
                "    city       TEXT," +
                "    phone      TEXT," +
                "    email      TEXT," +
                "    zoom       TEXT," +
                "    skype      TEXT," +
                "    birthday   TEXT," +
                "    notes      TEXT" +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createStudentTable(String dbPath) {
        String sql =
                "CREATE TABLE mtstudents (" +
                "    contactid   INTEGER PRIMARY KEY" +
                "                        REFERENCES mtcontacts (contactid)," +
                "    instrument1 TEXT," +
                "    instrument2 TEXT," +
                "    instrument3 TEXT," +
                "    prospective INTEGER," +
                "    status      TEXT," +
                "    statusfrom  TEXT," +
                "    statusto    TEXT," +
                "    parentid1   INTEGER REFERENCES mtcontacts (contactid)," +
                "    parentid2   INTEGER REFERENCES mtcontacts (contactid)" +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTeacherTable(String dbPath) {
        String sql =
                "CREATE TABLE mtteachers (" +
                "    contactid   INTEGER PRIMARY KEY" +
                "                        REFERENCES mtcontacts (contactid)," +
                "    instrument1 TEXT," +
                "    instrument2 TEXT," +
                "    instrument3 TEXT," +
                "    instrument4 TEXT," +
                "    instrument5 TEXT," +
                "    activesince TEXT" +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createParentTable(String dbPath) {
        String sql =
                "CREATE TABLE mtparents (" +
                "    contactid INTEGER PRIMARY KEY" +
                "                      REFERENCES mtcontacts (contactid)," +
                "    childid1  INTEGER REFERENCES mtcontacts (contactid)," +
                "    childid2  INTEGER REFERENCES mtcontacts (contactid)," +
                "    childid3  INTEGER REFERENCES mtcontacts (contactid)," +
                "    childid4  INTEGER REFERENCES mtcontacts (contactid)," +
                "    childid5  INTEGER REFERENCES mtcontacts (contactid)" +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createOtherTable(String dbPath) {
        String sql =
                "CREATE TABLE mtothers (" +
                "    contactid   INTEGER PRIMARY KEY" +
                "                        REFERENCES mtcontacts (contactid)," +
                "    description TEXT" +
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
                "CREATE TABLE mtlessons (" +
                "    lessonid    INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    instrument  TEXT," +
                "    lessonname  TEXT," +
                "    teacherid   INTEGER REFERENCES mtcontacts (id)," +
                "    location    TEXT," +
                "    frequency   TEXT," +
                "    weekday     TEXT," +
                "    time        TEXT," +
                "    duration    INTEGER," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid1  INTEGER REFERENCES mtcontacts (id)," +
                "    studentid10 INTEGER REFERENCES mtcontacts (id)," +
                "    startdate   TEXT," +
                "    enddate     TEXT" +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createAppointmentTable(String dbPath) {
        String sql =
                "CREATE TABLE mtappointments (" +
                "    appointmentid INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    date          TEXT," +
                "    time          TEXT," +
                "    lessonid      INTEGER REFERENCES mtlessons (lessonid)," +
                "    description   TEXT" +
                ")";

        try (Connection conn = connectTo(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTaskTable(String dbPath) {
        String sql =
                "CREATE TABLE mttasks (" +
                "    taskid      INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    description TEXT," +
                "    deadline    TEXT," +
                "    comment     TEXT," +
                "    status      TEXT" +
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
