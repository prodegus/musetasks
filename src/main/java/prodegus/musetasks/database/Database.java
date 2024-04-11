package prodegus.musetasks.database;

import prodegus.musetasks.contacts.Contact;

import java.sql.*;
import java.util.prefs.Preferences;

import static prodegus.musetasks.database.DBConstants.*;


public class Database {
    public static final Preferences prefs = Preferences.userNodeForPackage(Database.class);
    public static final String DB_PATH = prefs.get("DB_PATH", "Path not found");
    public static final String USER_TABLE = "mtusers";
    public static final String CONTACT_TABLE = "mtcontacts";
    public static final String STUDENT_TABLE = "mtstudents";
    public static final String TEACHER_TABLE = "mtteachers";
    public static final String PARENT_TABLE = "mtparents";
    public static final String OTHER_TABLE = "mtothers";
    public static final String LESSON_TABLE = "mtlessons";
    public static final String APPOINTMENT_TABLE = "mtappointments";
    public static final String TASK_TABLE = "mttasks";

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
                "    id         INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
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
                "CREATE TABLE " + STUDENT_TABLE + " (" +
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    lastname    TEXT," +
                "    firstname   TEXT," +
                "    category    TEXT," +
                "    location    TEXT," +
                "    street      TEXT," +
                "    postalcode  INTEGER," +
                "    city        TEXT," +
                "    phone       TEXT," +
                "    email       TEXT," +
                "    zoom        TEXT," +
                "    skype       TEXT," +
                "    birthday    TEXT," +
                "    notes       TEXT," +
                "    instrument1 TEXT," +
                "    instrument2 TEXT," +
                "    instrument3 TEXT," +
                "    prospective INTEGER," +
                "    status      TEXT," +
                "    statusfrom  TEXT," +
                "    statusto    TEXT," +
                "    parentid1   INTEGER," +
                "    parentid2   INTEGER," +
                "    teacherid1  INTEGER," +
                "    teacherid2  INTEGER," +
                "    teacherid3  INTEGER," +
                "    lessonid1   INTEGER," +
                "    lessonid2   INTEGER," +
                "    lessonid3   INTEGER" +
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
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    lastname    TEXT," +
                "    firstname   TEXT," +
                "    category    TEXT," +
                "    location    TEXT," +
                "    street      TEXT," +
                "    postalcode  INTEGER," +
                "    city        TEXT," +
                "    phone       TEXT," +
                "    email       TEXT," +
                "    zoom        TEXT," +
                "    skype       TEXT," +
                "    birthday    TEXT," +
                "    notes       TEXT" +
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
                "    id         INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
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
                "    childid1   INTEGER REFERENCES mtstudents (studentid)," +
                "    childid2   INTEGER REFERENCES mtstudents (studentid)," +
                "    childid3   INTEGER REFERENCES mtstudents (studentid)," +
                "    childid4   INTEGER REFERENCES mtstudents (studentid)," +
                "    childid5   INTEGER REFERENCES mtstudents (studentid)" +
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
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    lastname    TEXT," +
                "    firstname   TEXT," +
                "    category    TEXT," +
                "    location    TEXT," +
                "    street      TEXT," +
                "    postalcode  INTEGER," +
                "    city        TEXT," +
                "    phone       TEXT," +
                "    email       TEXT," +
                "    zoom        TEXT," +
                "    skype       TEXT," +
                "    birthday    TEXT," +
                "    notes       TEXT" +
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
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    instrument  TEXT," +
                "    lessonname  TEXT," +
                "    teacherid   INTEGER REFERENCES mtteachers (teacherid)," +
                "    location    TEXT," +
                "    frequency   TEXT," +
                "    weekday     TEXT," +
                "    time        TEXT," +
                "    duration    INTEGER," +
                "    studentid1  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid2  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid3  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid4  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid5  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid6  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid7  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid8  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid9  INTEGER REFERENCES mtstudents (studentid)," +
                "    studentid10 INTEGER REFERENCES mtstudents (studentid)," +
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
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    date        TEXT," +
                "    time        TEXT," +
                "    lessonid    INTEGER REFERENCES mtlessons (lessonid)," +
                "    description TEXT" +
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
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
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

    public static void addConstraintsStudentTable(String dbPath) {
        String sql =
                "PRAGMA foreign_keys = 0;" +
                "CREATE TABLE temp_table AS SELECT * FROM mtstudents;" +
                "DROP TABLE mtstudents;" +
                "CREATE TABLE mtstudents (" +
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    lastname    TEXT," +
                "    firstname   TEXT," +
                "    category    TEXT," +
                "    location    TEXT," +
                "    street      TEXT," +
                "    postalcode  INTEGER," +
                "    city        TEXT," +
                "    phone       TEXT," +
                "    email       TEXT," +
                "    zoom        TEXT," +
                "    skype       TEXT," +
                "    birthday    TEXT," +
                "    notes       TEXT" +
                "    instrument1 TEXT," +
                "    instrument2 TEXT," +
                "    instrument3 TEXT," +
                "    prospective INTEGER," +
                "    status      TEXT," +
                "    statusfrom  TEXT," +
                "    statusto    TEXT," +
                "    parentid1   INTEGER REFERENCES mtparents (id)," +
                "    parentid2   INTEGER REFERENCES mtparents (id)" +
                "    teacherid1  INTEGER REFERENCES mtteachers (id)," +
                "    teacherid2  INTEGER REFERENCES mtteachers (id)," +
                "    teacherid3  INTEGER REFERENCES mtteachers (id)," +
                "    lessonid1   INTEGER REFERENCES mtlessons (id)," +
                "    lessonid2   INTEGER REFERENCES mtlessons (id)," +
                "    lessonid3   INTEGER REFERENCES mtlessons (id)," +
                ")" +
                "INSERT INTO mtstudents (" +
                "    id," +
                "    lastname," +
                "    firstname," +
                "    category," +
                "    location," +
                "    street," +
                "    postalcode," +
                "    city," +
                "    phone," +
                "    email," +
                "    zoom," +
                "    skype," +
                "    birthday," +
                "    notes," +
                "    instrument1," +
                "    instrument2," +
                "    instrument3," +
                "    prospective," +
                "    status," +
                "    statusfrom," +
                "    statusto," +
                "    parentid1," +
                "    parentid2," +
                "    teacherid1," +
                "    teacherid2," +
                "    teacherid3," +
                "    lessonid1," +
                "    lessonid2," +
                "    lessonid3," +
                "    )" +
                "    SELECT id," +
                "           lastname," +
                "           firstname," +
                "           category," +
                "           location," +
                "           street," +
                "           postalcode," +
                "           city," +
                "           phone," +
                "           email," +
                "           zoom," +
                "           skype," +
                "           birthday," +
                "           notes," +
                "           instrument1," +
                "           instrument2," +
                "           instrument3," +
                "           prospective," +
                "           status," +
                "           statusfrom," +
                "           statusto," +
                "           parentid1," +
                "           parentid2," +
                "           teacherid1," +
                "           teacherid2," +
                "           teacherid3," +
                "           lessonid1," +
                "           lessonid2," +
                "           lessonid3" +
                "    FROM temp_table;" +
                "DROP TABLE temp_table;" +
                "PRAGMA foreign_keys = 1;";

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
