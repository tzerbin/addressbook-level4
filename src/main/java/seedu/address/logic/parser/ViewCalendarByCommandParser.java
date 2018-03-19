package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.ViewCalendarByCommand.VALID_ARGUMENT;

import seedu.address.logic.commands.ViewCalendarByCommand;
import seedu.address.logic.parser.exceptions.ParseException;


/**
 * Parses input arguments and creates a new ViewCalendarByCommand object
 */
public class ViewCalendarByCommandParser implements Parser<ViewCalendarByCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCalendarByCommand
     * and returns a ViewCalendarByCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCalendarByCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        String[] arguments = trimmedArgs.split("\\s+");
        if (trimmedArgs.isEmpty() || arguments.length > 1) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCalendarByCommand.MESSAGE_USAGE));
        }

        String argument = arguments[0];
        boolean isValidArgument = false;
        for(String validArgument: VALID_ARGUMENT) {
            if(validArgument.equalsIgnoreCase(argument)) {
                isValidArgument = true;
                break;
            }
        }
        if(! isValidArgument) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCalendarByCommand.MESSAGE_USAGE));
        }

        return new ViewCalendarByCommand(arguments[0].toLowerCase());
    }
}
