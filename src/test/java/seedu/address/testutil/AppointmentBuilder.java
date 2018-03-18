package seedu.address.testutil;

import java.time.LocalTime;

import seedu.address.model.appointment.Appointment;

/**
 * A utility class to help with building Appointment objects.
 */
public class AppointmentBuilder {

    public static final String DEFAULT_NAME = "Oscars 2018";
    public static final String DEFAULT_LOCATION = null;
    public static final int DEFAULT_START_HOUR = LocalTime.now().getHour();
    public static final int DEFAULT_START_MINUTE = LocalTime.now().getMinute();

    private String name;
    private String location;
    private int startHour;
    private int startMinute;

    public AppointmentBuilder() {
        name = DEFAULT_NAME;
        location = DEFAULT_LOCATION;
        startHour = DEFAULT_START_HOUR;
        startMinute = DEFAULT_START_MINUTE;
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code apptToCopy}
     */
    public AppointmentBuilder(Appointment apptToCopy) {
        name = apptToCopy.getTitle();
        location = apptToCopy.getLocation();
        startHour = apptToCopy.getStartTime().getHour();
        startMinute = apptToCopy.getStartTime().getMinute();
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
     * Sets the {@code startHour} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withStartHour(String startHour) {
        this.startHour = Integer.parseInt(startHour);
        return this;
    }

    /**
     * Sets the {@code startMinute} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withStartMinute(String startminute) {
        this.startMinute = Integer.parseInt(startminute);
        return this;
    }

    public Appointment build() {
        return new Appointment(name, startHour, startMinute, location);
    }
}
