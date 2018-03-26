package seedu.address.commons.events.ui;

import java.util.List;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.appointment.Appointment;

public class ShowAppointmentListEvent extends BaseEvent {

    private final List<Appointment> appointments;

    public ShowAppointmentListEvent(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
