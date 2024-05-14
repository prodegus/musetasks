package prodegus.musetasks.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

public class DateTime {

    public static void main(String[] args) {
        System.out.println(nowAsDate());
    }

    public static Date nowAsDate() {
        TimeZone timeZone = TimeZone.getDefault();
        int offsetMinutes = timeZone.getOffset(new Date().getTime()) / 1000 / 60;
        ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(offsetMinutes / 60, offsetMinutes % 60);
        return Date.from(LocalDateTime.now().toInstant(zoneOffset));
    }

}
