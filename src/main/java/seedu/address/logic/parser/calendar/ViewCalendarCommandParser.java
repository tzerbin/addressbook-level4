package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.person.Name.isValidName;

import seedu.address.logic.commands.calendar.ViewCalendarCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;

/**
 * Parses input arguments and creates a new ViewCalendarOfCommand object
 */
public class ViewCalendarCommandParser implements Parser<ViewCalendarCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCalendarByCommand
     * and returns a ViewCalendarByCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCalendarCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] arguments = trimmedArgs.split("\\s+");
        if (arguments.length == 0) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCalendarCommand.MESSAGE_USAGE));
        }
        String name = "";
        for (String argument: arguments) {
            name += argument;
            name += " ";
        }
        name = name.substring(0, name.length() - 1);
        if (!isValidName(name)) {
            throw new ParseException(Name.MESSAGE_NAME_CONSTRAINTS);
        }

        return new ViewCalendarCommand(name);
    }
}
