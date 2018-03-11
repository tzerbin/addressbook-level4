package seedu.address.logic.commands.calendar;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Deletes an appointment in a calendar.
 */
public class DeleteAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "addap";
    public static final String COMMAND_ALIAS = "da";

    public static final String MESSAGE_SUCCESS = "Deleted appointment successfully";

    @Override
    public CommandResult execute() throws CommandException {
        return null;
    }
}
