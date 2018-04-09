package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowAppointmentListEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;

//@@author muruges95
/**
 * Lists all appointments within two dates in a calendar.
 */
public class ListAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "listAppointment";
    public static final String COMMAND_ALIAS = "la";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM[-yyyy]");

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists appointments of all celebrities within the specified date range."
            + "Parameter: [START_DATE END_DATE] (includes the space in between."
            + " lists all appointments when no range specified.)"
            + "START_DATE and END_DATE should be in DD-MM-YYYY or DD-MM format, including the dash."
            + "When latter is entered, YYYY will take the current year.\n"
            + "Example: " + COMMAND_WORD + " 23-04 01-05";

    public static final String MESSAGE_INVALID_DATE_RANGE = "Start date cannot be after end date";
    public static final String MESSAGE_SUCCESS = "Listed appointments from %s to %s successfully.";

    public static final String MESSAGE_NO_APPTS_ERROR = "No appointments to list";

    private LocalDate startDate;
    private LocalDate endDate;

    public ListAppointmentCommand() {}

    public ListAppointmentCommand(LocalDate startDateInput, LocalDate endDateInput) {
        requireNonNull(startDateInput);
        requireNonNull(endDateInput);

        startDate = startDateInput;
        endDate = endDateInput;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.getStorageCalendar().hasAtLeastOneAppointment()) {
            throw new CommandException(MESSAGE_NO_APPTS_ERROR);
        }

        if (startDate == null && endDate == null) {
            startDate = model.getStorageCalendar().getEarliestDate();
            endDate = model.getStorageCalendar().getLatestDate();
        }

        model.setIsListingAppointments(true);
        List<Appointment> newAppointmentList =
                model.getStorageCalendar().getAppointmentsWithinDate(startDate, endDate);
        model.setCurrentlyDisplayedAppointments(newAppointmentList);
        EventsCenter.getInstance().post(new ShowAppointmentListEvent(newAppointmentList));

        return new CommandResult(
                String.format(MESSAGE_SUCCESS, startDate.format(FORMATTER), endDate.format(FORMATTER)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListAppointmentCommand // instanceof handles nulls
                && Objects.equals(startDate, ((ListAppointmentCommand) other).startDate)
                && Objects.equals(endDate, ((ListAppointmentCommand) other).endDate));
    }
}
