package seedu.address.model.person;

import java.util.Random;

import com.calendarfx.model.Calendar;

import seedu.address.model.calendar.CelebCalendar;

//@@author muruges95
/**
 *  Child class of Person for those who are tagged celebrities
 */
public class Celebrity extends Person {
    // for creation of different style for each celebrity calendar to differentiate
    private static final Random random = new Random();

    private CelebCalendar celebCalendar;

    /**
     * Every field must be present and not null.
     */
    public Celebrity(Person celeb) {
        super(celeb.getName(), celeb.getPhone(), celeb.getEmail(), celeb.getAddress(), celeb.getTags(), celeb.getId());
        celebCalendar = new CelebCalendar(this.getName().fullName);
        celebCalendar.setStyle(Calendar.Style.getStyle(random.nextInt(7)));
    }

    public CelebCalendar getCelebCalendar() {
        return celebCalendar;
    }

    /**
     * Sets the celebCalendar to another one.
     */
    public void setCelebCalendar(CelebCalendar newCelebCalendar) {
        this.celebCalendar = newCelebCalendar;
    }

    /**
     * Returns if input celeb is a copy of this celebrity
     */
    public boolean isCopyOf(Celebrity celeb) {
        return super.equals(celeb);
    }
}
