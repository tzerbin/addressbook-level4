package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.calendar.ViewDateCommand.FORMATTER;

import java.time.LocalDate;
import java.util.Arrays;

import seedu.address.logic.commands.calendar.ViewDateCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author WJY-norainu
/**
 * Parses input arguments and creates a new ViewDateCommand object
 */
public class ViewDateCommandParser implements Parser<ViewDateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewDateCommand
     * and returns a ViewDateCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewDateCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            return new ViewDateCommand(LocalDate.now());
        }

        String trimmedArgs = args.trim();
        if (FORMATTER.parse(trimmedArgs) == null) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewDateCommand.MESSAGE_USAGE));
        }

        int[] time = Arrays.stream(args.trim().split("-")).mapToInt(Integer::parseInt).toArray();
        LocalDate date;
        try {
            if (time.length == 2) {
                date = LocalDate.of(LocalDate.now().getYear(), time[1], time[0]);
            } else if (time.length == 3) {
                date = LocalDate.of(time[2], time[1], time[0]);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewDateCommand.MESSAGE_USAGE));
            }
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
        return new ViewDateCommand(date);
    }
}
