package seedu.address.testutil;

import java.time.LocalDate;
import java.time.LocalTime;

import seedu.address.model.appointment.Appointment;

/**
 * A utility class to help with building Appointment objects.
 */
public class AppointmentBuilder {

    public static final String DEFAULT_NAME = "Oscars 2018";
    public static final String DEFAULT_LOCATION = null;
    public static final LocalTime DEFAULT_START_TIME = LocalTime.now();
    public static final LocalDate DEFAULT_START_DATE = LocalDate.now();
    public static final LocalTime DEFAULT_END_TIME = LocalTime.now();
    public static final LocalDate DEFAULT_END_DATE = LocalDate.now();

    private String name;
    private String location;
    private LocalTime startTime;
    private LocalDate startDate;
    private LocalTime endTime;
    private LocalDate endDate;

    public AppointmentBuilder() {
        name = DEFAULT_NAME;
        location = DEFAULT_LOCATION;
        startTime = DEFAULT_START_TIME;
        startDate = DEFAULT_START_DATE;
        endDate = DEFAULT_END_DATE;
        endTime = DEFAULT_END_TIME;
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code apptToCopy}
     */
    public AppointmentBuilder(Appointment apptToCopy) {
        name = apptToCopy.getTitle();
        location = apptToCopy.getLocation();
        startTime = apptToCopy.getStartTime();
        startDate = apptToCopy.getStartDate();
        endTime = apptToCopy.getEndTime();
        endDate = apptToCopy.getEndDate();
    }

    /**
     * Sets the {@code name} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the {@code location} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    /**
     * Sets the {@code startTime} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withStartTime(String startTime) {
        this.startTime = LocalTime.parse(startTime, Appointment.TIME_FORMAT);
        return this;
    }

    /**
     * Sets the {@code startDate} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withStartDate(String startDate) {
        this.startDate = LocalDate.parse(startDate, Appointment.DATE_FORMAT);
        return this;
    }

    /**
     * Sets the {@code endTime} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withEndTime(String endTime) {
        this.endTime = LocalTime.parse(endTime, Appointment.TIME_FORMAT);
        return this;
    }

    /**
     * Sets the {@code endDate} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withEndDate(String endDate) {
        this.endDate = LocalDate.parse(endDate, Appointment.DATE_FORMAT);
        return this;
    }

    public Appointment build() {
        return new Appointment(name, startTime, startDate, location, endTime, endDate);
    }
}
