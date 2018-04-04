package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowAppointmentListEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Person;

//@@author muruges95
/**
 * Lists all appointments within two dates in a calendar.
 */
public class ListAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "listAppointment";
    public static final String COMMAND_ALIAS = "la";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all appointments in a celebrity calendar within a certain date range. ";

    public static final String MESSAGE_SUCCESS = "Listed appointments successfully.";

    public static final String MESSAGE_NO_APPTS_ERROR = "No appointments to list!";

    private static LocalDate startDate;
    private static LocalDate endDate;

    public ListAppointmentCommand() {

    }

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
        startDate = model.getStorageCalendar().getEarliestDate();
        endDate = model.getStorageCalendar().getLatestDate();

        List<Person> personList = model.getFilteredPersonList();

        model.setIsListingAppointments(true);
        List<Appointment> newAppointmentList =
                model.getStorageCalendar().getAppointmentsWithinDate(startDate, endDate);
        model.setAppointmentList(newAppointmentList);
        EventsCenter.getInstance().post(new ShowAppointmentListEvent(newAppointmentList));

        return new CommandResult(MESSAGE_SUCCESS);
    }

    public static LocalDate getEndDate() {
        return endDate;
    }

    public static LocalDate getStartDate () {
        return startDate;
    }
}
