package seedu.address.commons.events.model;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.calendar.StorageCalendar;

/** Indicates the StorageCalendar in the model has changed*/
public class StorageCalendarChangedEvent extends BaseEvent {
    public final StorageCalendar data;

    public StorageCalendarChangedEvent(StorageCalendar data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
