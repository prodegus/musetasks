package prodegus.musetasks.utils;

import javafx.collections.FXCollections;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.*;

public class DateTime {

    public static final HalfYear H_2024_1 = new HalfYear(LocalDate.of(2024, 1, 6), LocalDate.of(2024, 8, 20));
    public static final HalfYear H_2024_2 = new HalfYear(LocalDate.of(2024, 8, 21), LocalDate.of(2025, 1, 6));
    public static final HalfYear H_2025_1 = new HalfYear(LocalDate.of(2025, 1, 7), LocalDate.of(2025, 8, 26));
    public static final HalfYear H_2025_2 = new HalfYear(LocalDate.of(2025, 8, 27), LocalDate.of(2026, 1, 6));
    public static final HalfYear H_2026_1 = new HalfYear(LocalDate.of(2026, 1, 7), LocalDate.of(2026, 9, 1));
    public static final HalfYear H_2026_2 = new HalfYear(LocalDate.of(2026, 9, 2), LocalDate.of(2027, 1, 6));
    public static final HalfYear H_2027_1 = new HalfYear(LocalDate.of(2027, 1, 7), LocalDate.of(2027, 8, 31));
    public static final HalfYear H_2027_2 = new HalfYear(LocalDate.of(2027, 9, 1), LocalDate.of(2028, 1, 8));
    public static final List<HalfYear> halfYears = FXCollections.observableArrayList(H_2024_1, H_2024_2, H_2025_1,
            H_2025_2, H_2026_1, H_2026_2, H_2027_1, H_2027_2);

    public static final LocalDate FAR_PAST = LocalDate.of(1900, 1, 1);

    public static HalfYear currentHalfYear() {
        LocalDate now = LocalDate.now();
        for (HalfYear halfYear : halfYears) {
            if (!now.isBefore(halfYear.getStart()) && !now.isAfter(halfYear.getEnd())) return halfYear;
        }
        return null;
    }

    public static String fullDateTimeString(LocalDate date, LocalTime time) {
        return weekdayDateString(date) + " - " + time.toString() + " Uhr";
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
        String[] minutes = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
        for(int i = startHour; i < endHour; i++) {
            for(int j = 0; j < 12; j++) {
                String time = i + ":" + minutes[j];
                if(i < 10) {
                    time = "0" + time;
                }
                times.add(time + " Uhr");
            }
        }
        return times;
    }

    public static boolean invalidDateInput(DatePicker datePicker) {
        String dateString = datePicker.getEditor().getText();
        if (dateString.isBlank()) return false;
        try {
            datePicker.getConverter().fromString(dateString);
        } catch (DateTimeParseException e) {
            return true;
        }
        return false;
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

    public static String asString(LocalDate date, LocalTime time) {
        return asString(date) + ", " + asString(time);
    }

    public static String weekdayDateString(LocalDate date) {
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMANY);
        return String.join(", ", dayOfWeek, asString(date));
    }

}
