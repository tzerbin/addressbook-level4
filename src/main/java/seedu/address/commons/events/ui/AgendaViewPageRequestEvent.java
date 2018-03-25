package seedu.address.commons.events.ui;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import seedu.address.commons.events.BaseEvent;

/**
 * Event for changing the DayPage of the CalendarView to display an AgendaView
 */
public class AgendaViewPageRequestEvent extends BaseEvent {

    private final LocalDate startDate;
    private final LocalDate endDate;

    public AgendaViewPageRequestEvent(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getStartEndDateDifference() {
        return (int) startDate.until(endDate, ChronoUnit.DAYS);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
