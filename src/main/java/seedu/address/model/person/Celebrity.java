package seedu.address.model.person;

import seedu.address.model.calendar.CelebCalendar;

/**
 *  Child class of Person for those who are tagged celebrities
 */
public class Celebrity extends Person {

    private CelebCalendar celebCalendar;

    /**
     * Every field must be present and not null.
     */
    public Celebrity(Person celeb) {
        super(celeb.getName(), celeb.getPhone(), celeb.getEmail(), celeb.getAddress(), celeb.getTags());
        this.celebCalendar = new CelebCalendar(this.getName().fullName);
    }

    public CelebCalendar getCelebCalendar() {
        return celebCalendar;
    }
}
