package prodegus.musetasks.database;

public class Filter {
    private final String key;
    private final String value;

    public Filter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public Filter(String key, int value) {
        this.key = key;
        this.value = String.valueOf(value);
    }

    public Filter() {
        this.key = null;
        this.value = null;
    }

    public String toSQLString() {
        return key +  " = '" + value + "'";
    }
}
