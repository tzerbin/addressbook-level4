package seedu.address.logic.parser.calendar;

import java.time.LocalDate;

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
        String[] arguments = trimmedArgs.split("\\s+");
        LocalDate date;
        try {
            date = LocalDate.parse(arguments[0]);
        } catch (Exception e) {
            throw new ParseException(e.getMessage());
        }
        return new ViewDateCommand(date);
    }
}
