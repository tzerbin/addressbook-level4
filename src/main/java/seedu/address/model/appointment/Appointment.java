package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.calendarfx.model.Entry;

import seedu.address.model.person.Celebrity;

/**
 * Wraps all data required for an appointment, inheriting from a class of our calendar library
 * Each appointment also creates child entries for every celebrity associated with the appointment
 * and then attaches the entries to their respective calendars while keeping a reference to them
 * in an ArrayList of Entries. Appointments are stored in our StorageCalendar.
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

    private List<Entry> childEntryList;

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
        this.childEntryList = new ArrayList<>();
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

    /**
     * Removes old child entries and creates a new child entry for every celebrity
     * and then stores it in childEntryList.
     */
    public void updateEntries(List<Celebrity> celebrities) {
        clearChildEntries();
        childEntryList.clear();

        for (Celebrity celebrity : celebrities) {
            childEntryList.add(createChildEntry(celebrity));
        }
    }

    /**
     * Creates new childEntry for a given celebrity and sets the entry to point
     * to the celebrity's calendar.
     */
    private Entry createChildEntry(Celebrity celebrity) {
        Entry childEntry = new Entry(this.getTitle());

        childEntry.setMinimumDuration(minDuration);
        childEntry.changeStartTime(this.getStartTime());
        childEntry.changeEndTime(this.getEndTime());
        childEntry.changeStartDate(this.getStartDate());
        childEntry.changeEndDate(this.getEndDate());
        childEntry.setLocation(this.getLocation());
        childEntry.setCalendar(celebrity.getCelebCalendar());

        return childEntry;
    }

    /**
     * Remove all existing child entries
     */
    private void clearChildEntries() {
        for (Entry e : childEntryList) {
            // removed entries from the calendars
            e.setCalendar(null);
        }
    }

}
