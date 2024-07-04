package prodegus.musetasks.utils;

import java.time.LocalDate;

public class HalfYear {
    private LocalDate start;
    private LocalDate end;

    public HalfYear(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String title() {
        int number = this.getStart().getMonthValue() < 6 ? 1 : 2;
        int year = this.getStart().getYear();
        return number + ". Halbjahr " + year;
    }
}
