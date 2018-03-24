package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;

/**
 * Adds an appointment to a calendar.
 */
public class AddAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "addAppointment";
    public static final String COMMAND_ALIAS = "aa";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to the celebrity calendar. "
            + "Parameters: "
            + PREFIX_NAME + "APPOINTMENT NAME "
            + PREFIX_START_TIME + "START TIME "
            + PREFIX_START_DATE + "START DATE "
            + PREFIX_LOCATION + "LOCATION "
            + PREFIX_END_TIME + "END TIME "
            + PREFIX_END_DATE + "END DATE\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Oscars 2018 "
            + PREFIX_START_TIME + "18:00 "
            + PREFIX_START_DATE + "23-04-2018 "
            + PREFIX_LOCATION + "Hollywood "
            + PREFIX_END_TIME + "20:00 "
            + PREFIX_END_DATE + "23-04-2018";

    public static final String MESSAGE_SUCCESS = "Added appointment successfully";

    private final Appointment appt;

    /**
     * Creates an AddAppointmentCommand with the following parameter
     * @param appt The created appointment
     */
    public AddAppointmentCommand(Appointment appt) {
        requireNonNull(appt);
        this.appt = appt;
    }


    @Override
    public CommandResult execute() throws CommandException {
        StorageCalendar cal = model.getStorageCalendar();
        cal.addEntry(appt);
        // ArrayList<Celebrity> celebrities = model.getCelebrities();
        // appt.updateEntries(celebrities);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddAppointmentCommand
                && appt.equals(((AddAppointmentCommand) other).appt));
    }
}
