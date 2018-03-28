package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.map.MapAddress;

/**
 * JAXB-friendly version of an Appointment.
 */
public class XmlAdaptedAppointment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Appointment's %s field is missing!";

    @XmlElement(required = true)
    private String title;
    @XmlElement
    private String location;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String startTime;
    @XmlElement(required = true)
    private String endDate;
    @XmlElement(required = true)
    private String endTime;

    /**
     * Constructs an XmlAdaptedAppointment.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAppointment() {}

    /**
     * Constructs an {@code XmlAdaptedAppointment} with the given appointment details.
     */
    public XmlAdaptedAppointment(String title, LocalTime startTime, LocalDate startDate, MapAddress mapAddress,
                                 LocalTime endTime, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate.toString();
        this.startTime = startTime.toString();
        this.endDate = endDate.toString();
        this.endTime = endTime.toString();

        if (mapAddress != null) {
            location = mapAddress.toString();
        }
    }

    /**
     * Converts a given Appointment into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedAppointment
     */
    public XmlAdaptedAppointment(Appointment source) {
        title = source.getTitle();
        startDate = source.getStartDate().toString();
        startTime = source.getStartTime().toString();
        endDate = source.getEndDate().toString();
        endTime = source.getEndTime().toString();

        if (source.getMapAddress() != null) {
            location = source.getMapAddress().toString();
        }
    }

    /**
     * Converts this jaxb-friendly adapted appointment object into the model's Appointment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Appointment toModelType() throws IllegalValueException {

        if (title == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, String.class.getSimpleName()));
        }
        if (!Appointment.isValidName(title)) {
            throw new IllegalValueException(Appointment.MESSAGE_NAME_CONSTRAINTS);
        }
        final String appointmentName = new String(title);

        if (startTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocalTime.class.getSimpleName()));
        }
        final LocalTime startTimeCreated = LocalTime.parse(startTime);

        if (startDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocalTime.class.getSimpleName()));
        }
        final LocalDate startDateCreated = LocalDate.parse(startDate);

        if (endTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocalTime.class.getSimpleName()));
        }
        final LocalTime endTimeCreated = LocalTime.parse(endTime);

        if (endDate == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocalTime.class.getSimpleName()));
        }
        final LocalDate endDateCreated = LocalDate.parse(endDate);

        MapAddress mapAddressCreated = null;
        if (location != null) {
            if (!MapAddress.isValidAddress(location)) {
                throw new IllegalValueException(String.format(MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS));
            }

            mapAddressCreated = new MapAddress(location);
        }

        return new Appointment(appointmentName,
                               startTimeCreated,
                               startDateCreated,
                               mapAddressCreated,
                               endTimeCreated,
                               endDateCreated);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAppointment)) {
            return false;
        }

        XmlAdaptedAppointment otherAppointment = (XmlAdaptedAppointment) other;
        return Objects.equals(title, otherAppointment.title)
                && Objects.equals(startDate, otherAppointment.startDate)
                && Objects.equals(startTime, otherAppointment.startTime)
                && Objects.equals(endDate, otherAppointment.endDate)
                && Objects.equals(endTime, otherAppointment.endTime)
                && Objects.equals(location, otherAppointment.location);
    }
}
