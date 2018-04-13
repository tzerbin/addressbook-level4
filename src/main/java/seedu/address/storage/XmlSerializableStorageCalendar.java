//@@author tzerbin
package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;

/**
 * An Immutable StorageCalendar that is serializable to XML format
 */
@XmlRootElement(name = "storagecalendar")
public class XmlSerializableStorageCalendar {

    @XmlElement
    private List<XmlAdaptedAppointment> appointments;

    /**
     * Creates an empty XMLSerializableStorageCalendar.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableStorageCalendar() {
        appointments = new ArrayList<>();
    }

    public XmlSerializableStorageCalendar(StorageCalendar storageCalendar) {
        this();
        if (storageCalendar != null) {
            List<Appointment> appointmentList = storageCalendar.getAllAppointments();
            for (Appointment appt : appointmentList) {
                appointments.add(new XmlAdaptedAppointment(appt));
            }
        }
    }

    /**
     * Converts XMLAdapterAppointments into a {@code StorageCalendar} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedAppointments}.
     */
    public StorageCalendar toModelType() throws IllegalValueException {
        StorageCalendar calendar = new StorageCalendar();
        for (XmlAdaptedAppointment a : appointments) {
            calendar.addAppointment(a.toModelType());
        }
        return calendar;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableStorageCalendar)) {
            return false;
        }

        XmlSerializableStorageCalendar otherAl = (XmlSerializableStorageCalendar) other;
        return appointments.equals(otherAl.appointments);
    }
}
