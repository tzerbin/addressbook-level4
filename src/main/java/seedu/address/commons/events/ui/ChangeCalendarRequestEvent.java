package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.calendar.CelebCalendar;
import seedu.address.model.person.Celebrity;

/**
 * Indicates a request to show another celebrity's calendar
 */
public class ChangeCalendarRequestEvent extends BaseEvent {

    public final CelebCalendar celebCalendar;

    public ChangeCalendarRequestEvent(CelebCalendar celebCalendar) {
        this.celebCalendar = celebCalendar;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
