package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.AgendaViewPageRequestEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Lists all appointments within two dates in a calendar.
 */
public class ListAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "listAppointment";
    public static final String COMMAND_ALIAS = "la";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all appointments in a celebrity calendar within a certain date range. ";

    public static final String MESSAGE_SUCCESS = "Listed appointments successfully.";

    private LocalDate startDate;
    private LocalDate endDate;

    public ListAppointmentCommand() {

    }

    public ListAppointmentCommand(LocalDate startDate, LocalDate endDate) {
        requireNonNull(startDate);
        requireNonNull(endDate);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public CommandResult execute() throws CommandException {
        startDate = model.getStorageCalendar().getEarliestDate();
        endDate = model.getStorageCalendar().getLatestDate();

        model.setCelebCalendarViewPage("agenda");
        EventsCenter.getInstance().post(new AgendaViewPageRequestEvent(startDate, endDate));

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
