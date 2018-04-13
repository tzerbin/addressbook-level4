package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.calendarfx.model.Entry;

import seedu.address.model.map.MapAddress;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;

//@@author muruges95
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
    private static final Duration minDuration = Duration.ofMinutes(15);

    private final List<Entry> childEntryList;
    private final List<Celebrity> celebrityList;
    private final List<Long> celebrityIds;
    private final List<Person> pointOfContactList;
    private final List<Long> pointOfContactIds;
    private MapAddress mapAddress;

    public Appointment(String title, LocalTime startTime, LocalDate startDate,
                       MapAddress mapAddress, LocalTime endTime, LocalDate endDate) {
        super(requireNonNull(title));
        requireNonNull(startTime);
        requireNonNull(startDate);
        requireNonNull(endTime);
        requireNonNull(endDate);

        this.setMinimumDuration(minDuration);
        this.changeStartDate(startDate);
        this.changeEndDate(endDate);
        this.changeStartTime(startTime);
        this.changeEndTime(endTime);


        this.mapAddress = mapAddress;
        if (mapAddress == null) {
            this.setLocation(null);
        } else {
            this.setLocation(mapAddress.toString());
        }
        this.childEntryList = new ArrayList<>();
        this.celebrityList = new ArrayList<>();
        this.celebrityIds = new ArrayList<>();
        this.pointOfContactList = new ArrayList<>();
        this.pointOfContactIds = new ArrayList<>();
    }

    public Appointment(Appointment appointment) {
        this.setTitle(appointment.getTitle());
        this.changeStartDate(appointment.getStartDate());
        this.changeEndDate(appointment.getEndDate());
        this.changeStartTime(appointment.getStartTime());
        this.changeEndTime(appointment.getEndTime());
        this.mapAddress = appointment.getMapAddress();

        this.childEntryList = appointment.getChildEntryList();
        this.celebrityList = appointment.getCelebrities();
        this.celebrityIds = appointment.getCelebIds();
        this.pointOfContactList = appointment.getPointOfContactList();
        this.pointOfContactIds = appointment.getPointOfContactIds();
    }

    /**
     * Checks if the start date/time is NOT at least 15 min before end date/time
     */
    public static boolean isDateTimeNotValid(LocalDate startDate, LocalDate endDate, LocalTime startTime,
                                             LocalTime endTime) {
        LocalDateTime sdt = LocalDateTime.of(startDate, startTime);
        LocalDateTime edt = LocalDateTime.of(endDate, endTime);

        return edt.isBefore(sdt.plusMinutes(15));
    }

    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    public void setCelebIds(List<Long> ids) {
        celebrityIds.clear();
        celebrityIds.addAll(ids);
    }

    public List<Long> getCelebIds() {
        return celebrityIds;
    }

    public List<Person> getPointOfContactList() {
        return pointOfContactList;
    }

    public List<Long> getPointOfContactIds() {
        return pointOfContactIds;
    }

    public void setPointOfContactIds(List<Long> ids) {
        pointOfContactIds.clear();
        pointOfContactIds.addAll(ids);
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
                && Objects.equals(otherAppt.getMapAddress(), this.getMapAddress())
                && (otherAppt.getStartTime().getHour() == this.getStartTime().getHour())
                && (otherAppt.getStartTime().getMinute() == this.getStartTime().getMinute())
                && Objects.equals(otherAppt.getStartDate(), this.getStartDate())
                && (otherAppt.getEndTime().getHour() == this.getEndTime().getHour())
                && (otherAppt.getEndTime().getMinute() == this.getEndTime().getMinute())
                && Objects.equals(otherAppt.getEndDate(), this.getEndDate());
    }

    /**
     * Resets the stores celebrities and points of contacts, along with their associated
     * information stored in the Appointment object
     */
    public void updateEntries(List<Celebrity> celebrities, List<Person> pointsOfContact) {

        updateCelebEntries(celebrities);
        updatePointsOfContacts(pointsOfContact);
    }

    /**
     * Removes old child entries and creates a new child entry for every celebrity
     * and then stores it in childEntryList. Also stores the id of each celeb and the
     * celebrities themselves
     */
    private void updateCelebEntries(List<Celebrity> celebrities) {
        clearChildEntries();
        celebrityList.clear();
        childEntryList.clear();
        celebrityIds.clear();
        for (Celebrity celebrity : celebrities) {
            childEntryList.add(createChildEntry(celebrity));
            celebrityIds.add(celebrity.getId());
        }
        celebrityList.addAll(celebrities);
    }

    /**
     * Update points of contact list stored and their ids.
     */
    private void updatePointsOfContacts(List<Person> pointsOfContact) {
        pointOfContactList.clear();
        pointOfContactIds.clear();
        for (Person p : pointsOfContact) {
            pointOfContactIds.add(p.getId());
        }
        pointOfContactList.addAll(pointsOfContact);
    }

    /**
     * Returns the current list of Celebrities attending this appointment
     */
    public List<Celebrity> getCelebrities() {
        return celebrityList;
    }

    /**
     * Sets old child entries to the new entries.
     */
    public void setChildEntries(List<Entry> newChildEntryList) {
        childEntryList.clear();
        childEntryList.addAll(newChildEntryList);
    }

    public List<Entry> getChildEntryList() {
        return childEntryList;
    }

    /**
     * Removes all child entries and then removes the appointment itself from
     * the StorageCalendar.
     */
    public void removeAppointment() {
        clearChildEntries();
        this.setCalendar(null);
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

    /**
     * Returns the MapAddress of the Appointment.
     */
    public MapAddress getMapAddress() {
        return mapAddress;
    }

    //@@author Damienskt
    /**
     * Returns list of celebrities attending the appointment in string format.
     */
    public String getCelebritiesAttending () {
        if (celebrityList.size() == 0) {
            return "No celebrities attending";
        }
        String celebritiesAttending = new String(celebrityList.get(0).getName().toString());
        for (int i = 1; i < celebrityList.size(); i++) {
            celebritiesAttending = celebritiesAttending + ", " + celebrityList.get(i).getName().toString();
        }
        return celebritiesAttending;
    }

    /**
     * Returns list of points of contact for the appointment in string format.
     */
    public String getPointsOfContact () {
        if (pointOfContactList.size() == 0) {
            return "No point of contact";
        }
        String pointOfContacts = new String(pointOfContactList.get(0).getName().toString());
        for (int i = 1; i < pointOfContactList.size(); i++) {
            pointOfContacts = pointOfContacts + ", " + pointOfContactList.get(i).getName().toString();
        }
        return pointOfContacts;
    }
}
