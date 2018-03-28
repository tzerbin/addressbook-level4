package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CELEBRITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;

/**
 * Adds an appointment to a calendar.
 */
public class AddAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "addAppointment";
    public static final String COMMAND_ALIAS = "aa";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment. "
            + "Parameters: "
            + PREFIX_NAME + "APPOINTMENT NAME "
            + "[" + PREFIX_START_TIME + "START TIME] "
            + "[" + PREFIX_START_DATE + "START DATE] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_END_TIME + "END TIME] "
            + "[" + PREFIX_END_DATE + "END DATE]"
            + "[" + PREFIX_CELEBRITY + "CELEBRITY_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Oscars 2018 "
            + PREFIX_START_TIME + "18:00 "
            + PREFIX_START_DATE + "23-04-2018 "
            + PREFIX_LOCATION + "Hollywood "
            + PREFIX_END_TIME + "20:00 "
            + PREFIX_END_DATE + "23-04-2018 "
            + PREFIX_CELEBRITY + "1 "
            + PREFIX_CELEBRITY + "3";

    public static final String MESSAGE_SUCCESS = "Added appointment successfully";

    private final Appointment appt;
    private final Set<Index> celebrityIndices;

    /**
     * Creates an AddAppointmentCommand with the following parameter
     * @param appt The created appointment
     * @param celebrityIndices The indices of the celebrities who are part of this appointment
     */
    public AddAppointmentCommand(Appointment appt, Set<Index> celebrityIndices) {
        requireNonNull(appt);
        this.appt = appt;
        this.celebrityIndices = celebrityIndices;
    }


    @Override
    public CommandResult execute() throws CommandException {
        StorageCalendar cal = model.getStorageCalendar();
        cal.addEntry(appt);
        appt.updateEntries(getCelebrities(celebrityIndices, model.getFilteredPersonList()));

        // reset calendar view to dayview
        model.setCelebCalendarViewPage(DAY_VIEW_PAGE);
        EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(DAY_VIEW_PAGE));
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddAppointmentCommand
                && appt.equals(((AddAppointmentCommand) other).appt));
    }

    private static List<Celebrity> getCelebrities(Set<Index> indices, List<Person> personList) throws CommandException {
        List<Celebrity> celebrities = new ArrayList<>();
        for (Index index : indices) {
            celebrities.add(getCelebrity(index, personList));
        }
        return celebrities;
    }

    private static Celebrity getCelebrity(Index index, List<Person> personList) throws CommandException {
        int zeroBasedIndex = index.getZeroBased();
        if (zeroBasedIndex >= personList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } else if (!personList.get(zeroBasedIndex).isCelebrity()) {
            throw new CommandException(Messages.MESSAGE_NOT_CELEBRITY_INDEX);
        } else {
            return (Celebrity) personList.get(zeroBasedIndex);
        }
    }
}
