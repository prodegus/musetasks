package prodegus.musetasks.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateTime {

    public static final HalfYear H_2024_1 = new HalfYear(LocalDate.of(2024, 1, 6), LocalDate.of(2024, 8, 20));
    public static final HalfYear H_2024_2 = new HalfYear(LocalDate.of(2024, 8, 21), LocalDate.of(2025, 1, 6));

    public static void main(String[] args) {
        System.out.println("toTime(0): " + toTime(0));
        System.out.println("toDate(0): " + toDate(0));
    }

    public static Date nowAsDate() {
        TimeZone timeZone = TimeZone.getDefault();
        int offsetMinutes = timeZone.getOffset(new Date().getTime()) / 1000 / 60;
        ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(offsetMinutes / 60, offsetMinutes % 60);
        return Date.from(LocalDateTime.now().toInstant(zoneOffset));
    }

    public static List<String> times(int startHour, int endHour) {
        List<String> times = new ArrayList<>();
        times.add("unbekannt");
        String[] quarterHours = {"00", "15", "30", "45"};
        for(int i = startHour; i < endHour; i++) {
            for(int j = 0; j < 4; j++) {
                String time = i + ":" + quarterHours[j];
                if(i < 10) {
                    time = "0" + time;
                }
                times.add(time + " Uhr");
            }
        }
        return times;
    }

    public static LocalTime toTime(int timeDigits) {
        int hours = timeDigits / 100;
        int minutes = timeDigits % 100;

        return LocalTime.of(hours, minutes);
    }

    public static int toInt(LocalTime time) {
        return Integer.parseInt(time.toString().replaceAll("[^0-9]", ""));
    }

    public static String asString(LocalTime time) {
        return time.toString();
    }

    public static LocalTime toTime(String timeString) {
        if (timeString == null || timeString.replaceAll("[^0-9]", "").isBlank()) return LocalTime.MAX;
        return toTime(Integer.parseInt(timeString.replaceAll("[^0-9]", "")));
    }

    public static LocalDate toDate(int dateDigits) {
        if (dateDigits == 0) return LocalDate.MIN;
        int year = dateDigits / 10000;
        int month = (dateDigits / 100) % 100;
        int day = dateDigits % 100;
        return LocalDate.of(year, month, day);
    }

    public static int toInt(LocalDate date) {
        return Integer.parseInt(date.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    public static String asString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

}
