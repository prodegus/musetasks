package prodegus.musetasks.utils;

public class Strings {
    public static String string(String s) {
        return s == null ? "" : s;
    }

    public static String string(int i) {
        return i == 0 ? "" : String.valueOf(i);
    }
}
