package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.ReadOnlyAppointmentList;
import seedu.address.model.calendar.StorageCalendar;

/**
 * An Immutable AppointmentList that is serializable to XML format
 */
@XmlRootElement(name = "appointmentlist")
public class XmlSerializableAppointmentList {

    @XmlElement
    private List<XmlAdaptedAppointment> appointments;

    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableAppointmentList() {
        appointments = new ArrayList<>();
    }

    public XmlSerializableAppointmentList(ReadOnlyAppointmentList src) {
        this();
        //TODO: Provide the list of appointments
    }

    /**
     * Converts appointments into the model's {@code StorageCalendar} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedAppointments}.
     */
    public StorageCalendar toModelType() throws IllegalValueException {
        StorageCalendar calendar = new StorageCalendar("Stored Calendar");
        for (XmlAdaptedAppointment a : appointments) {
            calendar.addEntry(a.toModelType());
        }
        return calendar;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableAppointmentList)) {
            return false;
        }

        XmlSerializableAppointmentList otherAl = (XmlSerializableAppointmentList) other;
        return appointments.equals(otherAl.appointments);
    }
}
