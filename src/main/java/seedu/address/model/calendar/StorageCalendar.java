package seedu.address.model.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.exceptions.DuplicateAppointmentException;

//@@author muruges95
/**
 * Stores the list of all the celebrity appointments
 */
public class StorageCalendar extends Calendar {

    private static String storageCalendarName = "Storage Calendar";

    public StorageCalendar() {
        super(storageCalendarName);
    }

    public StorageCalendar(StorageCalendar cal) {
        super(storageCalendarName);
        for (Appointment a : cal.getAllAppointments()) {
            this.addEntry(a);
        }
    }

    public boolean hasAtLeastOneAppointment() {
        return this.getEarliestTimeUsed() != null;
    }

    public LocalDate getEarliestDate() {
        return LocalDateTime.ofInstant(this.getEarliestTimeUsed(), ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getLatestDate() {
        return LocalDateTime.ofInstant(this.getLatestTimeUsed(), ZoneId.systemDefault()).toLocalDate();
    }

    public List<Appointment> getAllAppointments() {
        LocalDate startDate;
        LocalDate endDate;
        // handle the case when no entries in calendar
        try {
            startDate = getEarliestDate();
            endDate = getLatestDate();
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
        return getAppointmentsWithinDate(startDate, endDate);
    }

    public List<Appointment> getAppointmentsWithinDate(LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointmentsWithinDate = new ArrayList<>();
        Map<LocalDate, List<Entry<?>>> dateListMap = this.findEntries(startDate, endDate, ZoneId.systemDefault());
        SortedSet<LocalDate> sortedKeySet = new TreeSet<>(dateListMap.keySet());

        Set<Appointment> storedAppointments = new HashSet<>();
        for (LocalDate date : sortedKeySet) {
            for (Entry e : dateListMap.get(date)) {
                Appointment currentAppt = (Appointment) e;
                // because same entry might show up on different dates
                if (!storedAppointments.contains(currentAppt)) {
                    appointmentsWithinDate.add(currentAppt);
                    storedAppointments.add(currentAppt);
                }
            }
        }

        return appointmentsWithinDate;

    }

    /**
     * Checks if appointment already exists in the model, and if it doesnt adds it to the calendar
     */
    public void addAppointment(Appointment appt) throws DuplicateAppointmentException {
        if (getAllAppointments().contains(appt)) {
            throw new DuplicateAppointmentException();
        }
        this.addEntry(appt);
    }
}
