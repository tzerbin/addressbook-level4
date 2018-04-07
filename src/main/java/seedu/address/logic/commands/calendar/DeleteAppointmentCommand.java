package seedu.address.logic.commands.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX;
import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.commons.events.ui.ShowAppointmentListEvent;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Celebrity;

//@@author WJY-norainu
/**
 * Deletes an appointment identified using its last displayed index from the calendar.
 */
public class DeleteAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "deleteAppointment";
    public static final String COMMAND_ALIAS = "da";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes an appointment. "
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS = "List of appointments must be shown "
            + "before deleting an appointment";
    public static final String MESSAGE_SUCCESS = "Deleted Appointment: %1$s";
    public static final String MESSAGE_APPOINTMENT_LIST_BECOMES_EMPTY = "\nAppointment list becomes empty, "
            + "Switching back to calendar view by day\n"
            + "Currently showing %1$s calendar";

    private final Index targetIndex;

    private Appointment apptToDelete;

    public DeleteAppointmentCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }


    @Override
    public CommandResult execute() throws CommandException {
        if (!model.getIsListingAppointments()) {
            throw new CommandException(MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS);
        }

        try {
            apptToDelete = model.deleteAppointment(targetIndex.getZeroBased());
        } catch (IndexOutOfBoundsException iobe) {
            throw new CommandException(MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        }

        List<Appointment> currentAppointmentList = model.getCurrentlyDisplayedAppointments();
        // if the list becomes empty, switch back to combined calendar day view
        if (currentAppointmentList.isEmpty()) {
            return changeToCalendarWithDayView();
        }

        EventsCenter.getInstance().post(new ShowAppointmentListEvent(currentAppointmentList));
        return new CommandResult(String.format(MESSAGE_SUCCESS, apptToDelete.getTitle()));
    }

    /**
     * sets the calendar panel to calendar
     * @return CommandResult with the corresponding message
     */
    private CommandResult changeToCalendarWithDayView() {
        model.setCelebCalendarViewPage(DAY_VIEW_PAGE);
        EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(DAY_VIEW_PAGE));
        EventsCenter.getInstance().post(new ShowCalendarEvent());

        Celebrity currentCalendarOwner = model.getCurrentCelebCalendarOwner();
        if (currentCalendarOwner == null) {
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS, apptToDelete.getTitle())
                            + String.format(MESSAGE_APPOINTMENT_LIST_BECOMES_EMPTY,
                            "combined"));
        } else {
            return new CommandResult(
                    String.format(MESSAGE_SUCCESS, apptToDelete.getTitle())
                            + String.format(MESSAGE_APPOINTMENT_LIST_BECOMES_EMPTY,
                            currentCalendarOwner.getName().toString() + "'s"));
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeleteAppointmentCommand
                && Objects.equals(this.apptToDelete, ((DeleteAppointmentCommand) other).apptToDelete)
                && this.targetIndex.equals(((DeleteAppointmentCommand) other).targetIndex));
    }
}
