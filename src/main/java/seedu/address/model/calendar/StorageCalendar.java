package seedu.address.model.calendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

import seedu.address.model.appointment.Appointment;

/**
 * Stores the list of all the celebrity appointments
 */
public class StorageCalendar extends Calendar {

    public StorageCalendar(String title) {
        super(title);
    }

    public LocalDate getEarliestDate() {
        return LocalDateTime.ofInstant(this.getEarliestTimeUsed(), ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDate getLatestDate() {
        return LocalDateTime.ofInstant(this.getLatestTimeUsed(), ZoneId.systemDefault()).toLocalDate();
    }

    public List<Appointment> getAllAppointments() {
        LocalDate startDate = getEarliestDate();
        LocalDate endDate = getLatestDate();
        return getAppointmentsWithinDate(startDate, endDate);
    }

    public List<Appointment> getAppointmentsWithinDate(LocalDate startDate, LocalDate endDate) {
        List<Appointment> appointments = new ArrayList<>();
        Map<LocalDate, List<Entry<?>>> dateListMap = this.findEntries(startDate, endDate, ZoneId.systemDefault());
        SortedSet<LocalDate> sortedKeySet = new TreeSet<>(dateListMap.keySet());

        for (LocalDate date : sortedKeySet) {
            for (Entry e : dateListMap.get(date)) {
                appointments.add((Appointment) e);
            }
        }

        return appointments;

    }
}
