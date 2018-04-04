//@@author tzerbin
package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalTime;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
    @XmlElement(required = true)
    private List<Long> celebrityIds;

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
        this.celebrityIds = new ArrayList<>();
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
        this.celebrityIds = source.getCelebIds();
    }

    /**
     * Converts this jaxb-friendly adapted appointment object into the model's Appointment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Appointment toModelType() throws IllegalValueException {

        LocalDate startDateCreated;
        LocalTime startTimeCreated;
        LocalDate endDateCreated;
        LocalTime endTimeCreated;

        if (startTime == null) {
            throw new IllegalValueException(Appointment.MESSAGE_TIME_CONSTRAINTS);
        }
        try {
            startTimeCreated = LocalTime.parse(startTime);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_TIME_CONSTRAINTS);
        }

        if (startDate == null) {
            throw new IllegalValueException(Appointment.MESSAGE_DATE_CONSTRAINTS);
        }
        try {
            startDateCreated = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_DATE_CONSTRAINTS);
        }

        if (endTime == null) {
            throw new IllegalValueException(String.format(Appointment.MESSAGE_TIME_CONSTRAINTS,
                    LocalTime.class.getSimpleName()));
        }
        try {
            endTimeCreated = LocalTime.parse(endTime);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_TIME_CONSTRAINTS);
        }

        if (endDate == null) {
            throw new IllegalValueException(String.format(Appointment.MESSAGE_DATE_CONSTRAINTS,
                    LocalTime.class.getSimpleName()));
        }
        try {
            endDateCreated = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_DATE_CONSTRAINTS);
        }

        if (title == null) {
            throw new IllegalValueException(Appointment.MESSAGE_NAME_CONSTRAINTS);
        }
        if (!Appointment.isValidName(title)) {
            throw new IllegalValueException(Appointment.MESSAGE_NAME_CONSTRAINTS);
        }
        final String appointmentName = title;

        MapAddress mapAddressCreated = null;
        if (location != null) {
            if (!MapAddress.isValidAddress(location)) {
                throw new IllegalValueException(String.format(MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS));
            }

            mapAddressCreated = new MapAddress(location);
        }
        if (celebrityIds == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "Celebrity Ids"));
        }
        final List<Long> celebIds = celebrityIds;

        Appointment appt =  new Appointment(appointmentName, startTimeCreated, startDateCreated,
                               mapAddressCreated, endTimeCreated, endDateCreated);
        appt.setCelebIds(celebIds);
        return appt;
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
                && Objects.equals(location, otherAppointment.location)
                && Objects.equals(celebrityIds, otherAppointment.celebrityIds);
    }
}
