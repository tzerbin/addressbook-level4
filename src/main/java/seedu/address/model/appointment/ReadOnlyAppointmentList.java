package seedu.address.model.appointment;

import javafx.collections.ObservableList;

/**
 * Unmodifiable view of an appointment list.
 */
public interface ReadOnlyAppointmentList {
    
    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Appointment> getAppointmentList();
}
