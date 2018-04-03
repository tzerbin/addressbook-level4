package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.calendar.ViewCalendarCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author WJY-norainu
/**
 * Parses input arguments and creates a new ViewCalendarCommand object
 */
public class ViewCalendarCommandParser implements Parser<ViewCalendarCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewCalendarCommand
     * and returns a ViewCalendarCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCalendarCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewCalendarCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCalendarCommand.MESSAGE_USAGE));
        }
    }
}
