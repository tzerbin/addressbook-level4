package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author WJY-norainu
/**
 * Event to be raised when switched back from appointment list view to calendar.
 */
public class ShowCalendarEvent extends BaseEvent {
    public ShowCalendarEvent() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
