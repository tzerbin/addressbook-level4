package seedu.address.model.person;

import seedu.address.model.calendar.CelebCalendar;

//@@author muruges95
/**
 *  Child class of Person for those who are tagged celebrities
 */
public class Celebrity extends Person {

    private CelebCalendar celebCalendar;

    /**
     * Every field must be present and not null.
     */
    public Celebrity(Person celeb) {
        super(celeb.getName(), celeb.getPhone(), celeb.getEmail(), celeb.getAddress(), celeb.getTags(), celeb.getId());
        this.celebCalendar = new CelebCalendar(this.getName().fullName);
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
