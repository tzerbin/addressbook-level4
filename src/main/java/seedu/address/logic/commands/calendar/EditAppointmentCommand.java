package seedu.address.logic.commands.calendar;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Edits an appointment in a calendar.
 */
public class EditAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "editap";
    public static final String COMMAND_ALIAS = "ea";

    public static final String MESSAGE_SUCCESS = "Edited appointment successfully";

    @Override
    public CommandResult execute() throws CommandException {
        return null;
    }

}
