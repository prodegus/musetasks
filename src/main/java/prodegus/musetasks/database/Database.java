package prodegus.musetasks.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static prodegus.musetasks.login.Settings.prefs;


public class Database {
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
    public static final String LOCATION_TABLE = "mtlocations";
    public static final String INSTRUMENT_TABLE = "mtinstruments";
    public static final String HOLIDAY_TABLE = "mtholidays";

    private static final String CONTACT_COLUMNS =
            "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
            "    lastname    TEXT," +
            "    firstname   TEXT," +
            "    category    INTEGER," +
            "    customerid  INTEGER," +
            "    location    TEXT," +
            "    street      TEXT," +
            "    postalcode  INTEGER," +
            "    city        TEXT," +
            "    phone       TEXT," +
            "    email       TEXT," +
            "    zoom        TEXT," +
            "    skype       TEXT," +
            "    birthday    TEXT," +
            "    notes       TEXT";

    public static void checkForeignKey() {
        String sql1 = "PRAGMA foreign_keys";
        String sql2 = "PRAGMA foreign_keys = ON";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            ResultSet rs1 = stmt.executeQuery(sql1);
            rs1.next();
            System.out.println("result of [PRAGMA foreign_keys]: " + rs1.getInt(1));
//            stmt.execute(sql2);
//            System.out.println("Executed [" + sql2 + "]");
//            ResultSet rs2 = stmt.executeQuery(sql1);
//            System.out.println("result of [PRAGMA foreign_keys]: " + rs2.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
                return rs.next();

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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createUserTable(String dbPath) {
        String sql = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (user TEXT, password TEXT)";

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createInstrumentTable(String dbPath) {
        String sql =
                "CREATE TABLE mtinstruments (" +
                "    id          INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    instrument  TEXT" +
                ")";

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createLocationTable(String dbPath) {
        String sql =
                "CREATE TABLE mtlocations (" +
                "    id     INTEGER PRIMARY KEY ASC AUTOINCREMENT," +
                "    name   TEXT," +
                "    room1  TEXT," +
                "    room2  TEXT," +
                "    room3  TEXT," +
                "    room4  TEXT," +
                "    room5  TEXT," +
                "    room6  TEXT," +
                "    room7  TEXT," +
                "    room8  TEXT," +
                "    room9  TEXT," +
                "    room10 TEXT" +
                ")";

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createStudentTable(String dbPath) {
        String sql =
                "CREATE TABLE " + STUDENT_TABLE + " (" +
                CONTACT_COLUMNS + ", " +
                "    instrument1 TEXT," +
                "    instrument2 TEXT," +
                "    instrument3 TEXT," +
                "    prospective INTEGER," +
                "    status      TEXT," +
                "    statusfrom  TEXT," +
                "    statusto    TEXT" +
                ")";

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTeacherTable(String dbPath) {
        String sql =
                "CREATE TABLE mtteachers (" +
                CONTACT_COLUMNS + ", " +
                "    instruments TEXT," +
                "    activedays  TEXT," +
                "    status      TEXT," +
                "    statusfrom  TEXT," +
                "    statusto    TEXT" +
                ")";

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createParentTable(String dbPath) {
        String sql =
                "CREATE TABLE mtparents (" +
                CONTACT_COLUMNS + ", " +
                "    childid1   INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    childid2   INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    childid3   INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    childid4   INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    childid5   INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL" +
                ")";

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createOtherTable(String dbPath) {
        String sql =
                "CREATE TABLE mtothers (" +
                CONTACT_COLUMNS + ", " +
                "    description TEXT" +
                ")";

        try (Connection conn = connect(dbPath);
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
                "    lessonname  TEXT," +
                "    category    INTEGER," +
                "    instrument  TEXT," +
                "    teacherid   INTEGER REFERENCES mtteachers (id) ON DELETE SET NULL," +
                "    locationid  INTEGER REFERENCES mtlocations (id) ON DELETE SET NULL," +
                "    room        TEXT," +
                "    repeat      INTEGER," +
                "    weekday     INTEGER," +
                "    time        INTEGER," +
                "    duration    INTEGER," +
                "    startdate   INTEGER," +
                "    enddate     INTEGER," +
                "    status      INTEGER," +
                "    statusfrom  INTEGER," +
                "    statusto    INTEGER," +
                "    studentid1  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid2  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid3  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid4  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid5  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid6  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid7  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid8  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid9  INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL," +
                "    studentid10 INTEGER REFERENCES mtstudents (id) ON DELETE SET NULL" +
                ")";

        try (Connection conn = connect(dbPath);
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
                "    date        INTEGER," +
                "    time        INTEGER," +
                "    locationid  INTEGER REFERENCES mtlocations (id) ON DELETE SET NULL," +
                "    room        TEXT," +
                "    duration    INTEGER," +
                "    lessonid    INTEGER REFERENCES mtlessons (id) ON DELETE SET NULL," +
                "    dateold     INTEGER," +
                "    category    INTEGER," +
                "    status      INTEGER," +
                "    description TEXT" +
                ")";

        try (Connection conn = connect(dbPath);
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

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createHolidayTable(String dbPath) {
        String sql =
                "CREATE TABLE mtholidays (" +
                        "    description TEXT," +
                        "    start       INTEGER," +
                        "    end         INTEGER" +
                        ")";

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addConstraintsStudentTable(String dbPath) {
        ArrayList<String> sqlStrings = new ArrayList<>();
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN parentid1  INTEGER REFERENCES mtparents(id) ON DELETE SET NULL");
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN parentid2  INTEGER REFERENCES mtparents(id) ON DELETE SET NULL");
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN teacherid1 INTEGER REFERENCES mtteachers(id) ON DELETE SET NULL");
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN teacherid2 INTEGER REFERENCES mtteachers(id) ON DELETE SET NULL");
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN teacherid3 INTEGER REFERENCES mtteachers(id) ON DELETE SET NULL");
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN lessonid1  INTEGER REFERENCES mtlessons(id) ON DELETE SET NULL");
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN lessonid2  INTEGER REFERENCES mtlessons(id) ON DELETE SET NULL");
        sqlStrings.add("ALTER TABLE mtstudents ADD COLUMN lessonid3  INTEGER REFERENCES mtlessons(id) ON DELETE SET NULL");

        try (Connection conn = connect(dbPath);
             Statement stmt = conn.createStatement()) {
            for (String sql: sqlStrings) {
                stmt.execute(sql);
            }
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
        return connect(DB_PATH);
    }

    private static Connection connect(String path) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        Statement stmt = connection.createStatement();
        stmt.execute("PRAGMA foreign_keys = ON");
        stmt.close();
        return connection;
    }

    public static void update(String table, String id, String column, String newValue) {
        String sql = "UPDATE " + table + " SET " + column + " = '" + newValue + "' WHERE id = " + id;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static void update(String table, String id, String column, int newValue) {
        String sql = "UPDATE " + table + " SET " + column + " = " + newValue + " WHERE id = " + id;
        System.out.println(sql);
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static void updateMultiple(String table, int id, String columnsValues) {
        updateMultiple(table, columnsValues, new Filter("id", String.valueOf(id)));
    }

    public static void updateMultiple(String table, String columnsValues, Filter filter) {
        String sql = "UPDATE " + table + " SET " + columnsValues + " WHERE " + filter.toSQLString();
        System.out.println(sql);

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static void delete(String table, int id) {
        delete(table, new Filter("id", String.valueOf(id)));
    }

    public static void delete(String table, Filter filter) {
        String sql = "DELETE FROM " + table + " WHERE " + filter.toSQLString();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insert(String table, String columns, String values) {
        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
        System.out.println(sql);

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    public static List<String> queryString(String column, String table, Filter ... filters) {
        StringBuilder sql = new StringBuilder();
        List<String> result = new ArrayList<>();

        sql.append("SELECT ");
        sql.append(column);
        sql.append(" FROM ");
        sql.append(table);
        sql.append(" WHERE ");

        int i = 1;
        for (Filter filter : filters) {
            sql.append(filter.toSQLString());
            if (i < filters.length) sql.append(" AND ");
            i++;
        }

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static List<Integer> queryInteger(String column, String table, Filter ... filters) {
        StringBuilder sql = new StringBuilder();
        List<Integer> result = new ArrayList<>();

        sql.append("SELECT ");
        sql.append(column);
        sql.append(" FROM ");
        sql.append(table);
        sql.append(" WHERE ");

        int i = 1;
        for (Filter filter : filters) {
            sql.append(filter.toSQLString());
            if (i < filters.length) sql.append(" AND ");
            i++;
        }

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {
            while (rs.next()) {
                result.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
