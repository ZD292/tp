package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Time {

    public static final String MESSAGE_CONSTRAINTS = "The format of a time should be: mon 1200,\n"
        + " the day is the first three "
        + "letters of a day and the time is in 24 hour format";

    private static final String DAY_REGEX = "^(?i)(mon|tue|wed|thu|fri|sat|sun)";
    private static final String TIME_REGEX = "(0[0-9]|1[0-9]|2[0-3])[0-5][0-9]";
    public static final String VALIDATION_REGEX = DAY_REGEX + " " + TIME_REGEX;

    private DayOfWeek day;
    private LocalTime hour;

    /**
     * Time constructor.
     * @param day The day of the week.
     * @param hour The time of the day.
     */
    public Time(DayOfWeek day, LocalTime hour) {
        this.day = day;
        this.hour = hour;
    }

    /**
     * Checks if the time is valid.
     * @param timeString Time in string format.
     * @return true if the sting is valid.
     */
    public static boolean isValidTime(String timeString) {
        return timeString.matches(VALIDATION_REGEX);
    }

    /**
     * Convert either start or end time of timeInterval into an int
     * Use dayOfWeek enum from 1-7 representing Mon-Sun
      * @return Int representing duration of interval in minute
     */
    public int getDurationInMin() {
        int durationInMin = this.day.getValue() * 24 * 60 + this.hour.getHour() * 60 + this.hour.getMinute();
        return durationInMin;
    }

    /**
     * Compares this with another time object.
     * @param otherTime The other time object.
     * @return Returns an int to indicate if it less than/more than/equal.
     */
    public int compareTo(Time otherTime) {
        requireNonNull(otherTime);
        return this.day.compareTo(otherTime.day) == 0 ? this.hour.compareTo(otherTime.hour)
            : this.day.compareTo(otherTime.day);
    }

    @Override
    public String toString() {
        return day.toString().substring(0, 3) + " " + hour.format(DateTimeFormatter.ofPattern("HHmm"));
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Time)) {
            return false;
        } else if (object == this) {
            return true;
        } else {
            Time otherTime = (Time) object;
            return this.day.equals(otherTime.day) && this.hour.equals(otherTime.hour);
        }
    }

    public static DayOfWeek decodeDay(String day) {
        day = day.toLowerCase();

        if (DayOfWeek.MONDAY.toString().toLowerCase().contains(day)) {
            return DayOfWeek.MONDAY;
        } else if (DayOfWeek.TUESDAY.toString().toLowerCase().contains(day)) {
            return DayOfWeek.TUESDAY;
        } else if (DayOfWeek.WEDNESDAY.toString().toLowerCase().contains(day)) {
            return DayOfWeek.WEDNESDAY;
        } else if (DayOfWeek.THURSDAY.toString().toLowerCase().contains(day)) {
            return DayOfWeek.THURSDAY;
        } else if (DayOfWeek.FRIDAY.toString().toLowerCase().contains(day)) {
            return DayOfWeek.FRIDAY;
        } else if (DayOfWeek.SATURDAY.toString().toLowerCase().contains(day)) {
            return DayOfWeek.SATURDAY;
        } else {
            return DayOfWeek.SUNDAY;
        }
    }
}
