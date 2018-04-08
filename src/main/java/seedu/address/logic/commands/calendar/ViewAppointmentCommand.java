package seedu.address.logic.commands.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.map.ShowLocationCommand;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.map.MapAddress;

//@@author Damienskt
/**
 * Display the appointment details in the display panel
 * based on the index given
 */
public class ViewAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "viewAppointment";
    public static final String COMMAND_ALIAS = "va";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Show the appointment details based on index "
            + "Parameters: "
            + "Index (Based on most recent appointment list generated). \n"
            + "Example: " + COMMAND_WORD + " "
            + "1";

    public static final String MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS = "List of appointments must be shown "
            + "before viewing an appointment";
    public static final String MESSAGE_SUCCESS = "Selected appointment details:\n";

    private static Appointment selectedAppointment;
    private int chosenIndex;

    /**
     * Takes in a zero-based index {@code index}
     */
    public ViewAppointmentCommand (int index) {
        chosenIndex = index;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.getIsListingAppointments()) {
            throw new CommandException(MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS);
        }
        try {
            selectedAppointment = model.getChosenAppointment(chosenIndex);
        } catch (IndexOutOfBoundsException iobe) {
            throw new CommandException(MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        }

        try {
            ShowLocationCommand showLocation = new ShowLocationCommand(
                    new MapAddress(selectedAppointment.getLocation()));
            showLocation.execute();
            return new CommandResult(MESSAGE_SUCCESS + getAppointmentDetailsResult());
        } catch (NullPointerException npe) {
            return new CommandResult(MESSAGE_SUCCESS + getAppointmentDetailsResult());
        }
    }

    public static String getAppointmentDetailsResult () {
        return "Appointment Name: " + selectedAppointment.getTitle() + "\n"
                + "Start Date: " + selectedAppointment.getStartDate() + "\n"
                + "Start Time: " + selectedAppointment.getStartTime() + "\n"
                + "End Date: " + selectedAppointment.getEndDate() + "\n"
                + "End Time: " + selectedAppointment.getEndTime() + "\n"
                + "Location: " + selectedAppointment.getLocation() + "\n"
                + "Celebrities attending: " + selectedAppointment.getCelebritiesAttending() + "\n"
                + "Points of Contact: " + selectedAppointment.getPointsOfContact();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ViewAppointmentCommand
                && this.chosenIndex == (((ViewAppointmentCommand) other).chosenIndex));
    }
}
