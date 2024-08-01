package prodegus.musetasks.database;

import java.time.LocalDate;
import static prodegus.musetasks.utils.DateTime.*;

public class PeriodFilter extends Filter {
    private final String key;
    private final LocalDate from;
    private final LocalDate to;

    public PeriodFilter(String key, LocalDate from, LocalDate to) {
        this.key = key;
        this.from = from;
        this.to = to;
    }

    public String toSQLString() {
        return key +  " BETWEEN " + toInt(from) + " AND " + toInt(to);
    }
}
