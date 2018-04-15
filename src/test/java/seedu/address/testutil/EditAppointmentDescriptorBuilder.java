package seedu.address.testutil;

import java.time.LocalDate;
import java.time.LocalTime;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.calendar.EditAppointmentCommand;
import seedu.address.logic.commands.calendar.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.appointment.Appointment;
// @@author muruges95

/**
 * A utility class to help with building EditAppointmentDescriptor objects.
 */
public class EditAppointmentDescriptorBuilder {
    private EditAppointmentCommand.EditAppointmentDescriptor descriptor;

    public EditAppointmentDescriptorBuilder() {
        descriptor = new EditAppointmentDescriptor();
    }

    public EditAppointmentDescriptorBuilder(EditAppointmentDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * Returns an {@code EditAppointmentDescriptor} with fields containing {@code appointment}'s details
     */
    public EditAppointmentDescriptorBuilder(Appointment appointment) {
        descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentName(appointment.getTitle());
        descriptor.setStartDate(appointment.getStartDate());
        descriptor.setEndDate(appointment.getEndDate());
        descriptor.setStartTime(appointment.getStartTime());
        descriptor.setEndTime(appointment.getEndTime());
        descriptor.setLocation(appointment.getMapAddress());
        descriptor.setCelebIds(appointment.getCelebIds());
        descriptor.setPointOfContactIds(appointment.getPointOfContactIds());
    }

    /**
     * Sets the {@code Name} of the {@code EditAppointmentDescriptor} that we are building.
     */
    public EditAppointmentDescriptorBuilder withName(String name) {
        descriptor.setAppointmentName(name);
        return this;
    }

    /**
     * Sets the {@code MapAddress} of the {@code EditAppointmentDescriptor} that we are building.
     */
    public EditAppointmentDescriptorBuilder withLocation(String location) {
        try {
            descriptor.setLocation(ParserUtil.parseMapAddress(location));
        } catch (IllegalValueException e) {
            throw new IllegalArgumentException("map address not valid.");
        }
        return this;
    }

    /**
     * Sets the {@code startTime} of the {@code EditAppointmentDescriptor} that we are building.
     */
    public EditAppointmentDescriptorBuilder withStartTime(String startTime) {
        descriptor.setStartTime(LocalTime.parse(startTime, Appointment.TIME_FORMAT));
        return this;
    }

    /**
     * Sets the {@code endTime} of the {@code EditAppointmentDescriptor} that we are building.
     */
    public EditAppointmentDescriptorBuilder withEndTime(String endTime) {
        descriptor.setEndTime(LocalTime.parse(endTime, Appointment.TIME_FORMAT));
        return this;
    }

    /**
     * Sets the {@code startDate} of the {@code EditAppointmentDescriptor} that we are building.
     */
    public EditAppointmentDescriptorBuilder withStartDate(String startDate) {
        descriptor.setStartDate(LocalDate.parse(startDate, Appointment.DATE_FORMAT));
        return this;
    }

    /**
     * Sets the {@code endDate} of the {@code EditAppointmentDescriptor} that we are building.
     */
    public EditAppointmentDescriptorBuilder withEndDate(String endDate) {
        descriptor.setEndDate(LocalDate.parse(endDate, Appointment.DATE_FORMAT));
        return this;
    }

    public EditAppointmentDescriptor build() {
        return descriptor;
    }
}
