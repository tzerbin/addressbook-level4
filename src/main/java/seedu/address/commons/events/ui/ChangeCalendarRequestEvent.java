package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.Celebrity;

/**
 * Indicates a request to change to another calendar view page
 */
public class ChangeCalendarRequestEvent extends BaseEvent {

    public final Celebrity celebrity;

    public ChangeCalendarRequestEvent(Celebrity celebrity) {
        this.celebrity = celebrity;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
