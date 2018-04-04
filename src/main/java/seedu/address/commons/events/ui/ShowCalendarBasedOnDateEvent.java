package seedu.address.commons.events.ui;

import java.time.LocalDate;

import seedu.address.commons.events.BaseEvent;

//@@author WJY-norainu
/**
 * Event to be raised when switched back from appointment list view to calendar.
 */
public class ShowCalendarBasedOnDateEvent extends BaseEvent {
    private LocalDate date;

    public ShowCalendarBasedOnDateEvent(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
