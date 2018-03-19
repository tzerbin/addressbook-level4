package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Objects;

import com.calendarfx.model.Entry;

/**
 * Wraps all data required for an appointment, inheriting from a class of our calendar library
 */
public class Appointment extends Entry {

    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Appointment names should only contain alphanumeric characters and spaces, and it should not be blank";

    public static final String MESSAGE_TIME_CONSTRAINTS =
            "Time should be a 2 digit number between 00 to 23 followed by a :"
            + " followed by a 2 digit number beetween 00 to 59. Some examples include "
            + "08:45, 13:45, 00:30";
    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Date should be a 2 digit number between 01 to 31 followed by a -"
            + " followed by a 2 digit number between 01 to 12 followed by a -"
            + " followed by a 4 digit number describing a year. Some months might have less than 31 days."
            + " Some examples include: 13-12-2018, 02-05-2019, 28-02-2018";

    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-uuuu")
            .withResolverStyle(ResolverStyle.STRICT); // prevent incorrect dates

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    private static final String NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";


    // Minimum duration for an appointment is at least 1 minute
    private static final Duration minDuration = Duration.ofMinutes(1);

    public Appointment(String title, LocalTime startTime, LocalDate startDate,
                       String location, LocalTime endTime, LocalDate endDate) {
        super(requireNonNull(title));
        requireNonNull(startTime);
        requireNonNull(startDate);
        requireNonNull(endTime);
        requireNonNull(endDate);

        this.setMinimumDuration(minDuration);
        this.changeStartTime(startTime);
        this.changeStartDate(startDate);
        this.changeEndTime(endTime);
        this.changeEndDate(endDate);
        this.setLocation(location);
    }

    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public boolean equals (Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppt = (Appointment) other;
        return Objects.equals(otherAppt.getTitle(), this.getTitle())
                && Objects.equals(otherAppt.getLocation(), this.getLocation())
                && (otherAppt.getStartTime().getHour() == this.getStartTime().getHour())
                && (otherAppt.getStartTime().getMinute() == this.getStartTime().getMinute());
    }

}
