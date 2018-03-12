package seedu.address.model.calendar;

import static java.util.Objects.requireNonNull;

import java.time.LocalTime;

import com.calendarfx.model.Entry;
import com.sun.istack.internal.NotNull;

/**
 * Wraps all data required for an appointment, inheriting from a class of our calendar library
 */
public class Appointment extends Entry {

    public static final String MESSAGE_HOUR_CONSTRAINTS =
            "Hour should be either a 1 digit number or a 2 digit number between 00 and 23";

    public static final String MESSAGE_MINUTE_CONSTRAINTS =
            "Minute should be either a 1 digit number or a 2 digit number between 00 and 59";

    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Appointment names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * Characters must be either 0-9, 00-19 or 20-23
     */
    private static final String HOUR_VALIDATION_REGEX = "^(([0-1]?[0-9])|(2[0-3]))$";

    /*
     * Characters must be either 0-9, or 00-59
     */
    private static final String MINUTE_VALIDATION_REGEX = "^([0-5]?[0-9])$";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    private static final String NAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public Appointment(@NotNull String title, int startHour, int startMinute) {
        super(requireNonNull(title));
        this.changeStartTime(LocalTime.of(startHour, startMinute));
    }

    public static boolean isValidHour(String test) {
        return test.matches(HOUR_VALIDATION_REGEX);
    }
    public static boolean isValidMinute(String test) {
        return test.matches(MINUTE_VALIDATION_REGEX);
    }
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

}
