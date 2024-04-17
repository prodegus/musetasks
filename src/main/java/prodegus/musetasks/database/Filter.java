package prodegus.musetasks.database;

public class Filter {
    private final String key;
    private final String value;

    public Filter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toSQLString() {
        return key +  " = " + value;
    }
}
