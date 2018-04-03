package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author WJY-norainu
/**
 * Indicates a request to change to another calendar view page
 */
public class ShowCombinedCalendarViewRequestEvent extends BaseEvent {

    public ShowCombinedCalendarViewRequestEvent() {}

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
