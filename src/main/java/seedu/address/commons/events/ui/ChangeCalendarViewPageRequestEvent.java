package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author WJY-norainu
/**
 * Indicates a request to change to another calendar view page
 */
public class ChangeCalendarViewPageRequestEvent extends BaseEvent {

    public final String calendarViewPage;

    public ChangeCalendarViewPageRequestEvent(String calendarViewPage) {
        this.calendarViewPage = calendarViewPage;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
