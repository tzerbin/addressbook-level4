package seedu.address.model.calendar;

import com.calendarfx.model.Calendar;

import seedu.address.model.person.Person;

/**
 * Wraps all data required for our Calendar, inheriting from a Class of our calendar library
 */
public class CelebCalendar extends Calendar {

    private Person celebrity;


    public CelebCalendar(Person celebrity) {
        super(celebrity.getName().fullName);
        this.celebrity = celebrity;
    }
}
