package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that a new result is available.
 */
public class NewResultAvailableEvent extends BaseEvent {

    public final boolean isSuccessful;
    public final String message;

    public NewResultAvailableEvent(String message,  boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
