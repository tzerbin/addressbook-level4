package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.calendar.ListAppointmentCommand.FORMATTER;
import static seedu.address.logic.commands.calendar.ListAppointmentCommand.MESSAGE_INVALID_DATE_RANGE;
import static seedu.address.logic.commands.calendar.ViewDateCommand.MESSAGE_INVALID_DATE;

import java.time.LocalDate;
import java.util.Arrays;

import seedu.address.logic.commands.calendar.ListAppointmentCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author WJY-norainu
/**
 * Parses input arguments and creates a new ListAppointmentCommand object
 */
public class ListAppointmentCommandParser implements Parser<ListAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListAppointmentCommand
     * and returns a ListAppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListAppointmentCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            return new ListAppointmentCommand();
        }

        String trimmedArgs = args.trim();
        String[] arguments = trimmedArgs.split(" ");
        //there should be two elements, one for start date, the other for end date.
        if (arguments.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListAppointmentCommand.MESSAGE_USAGE));
        }
        //check if start and end dates follow the format
        try {
            FORMATTER.parse(arguments[0]);
            FORMATTER.parse(arguments[1]);
        } catch (Exception e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListAppointmentCommand.MESSAGE_USAGE));
        }

        LocalDate startDate = convertFormattedStringToLocalDate(arguments[0]);
        LocalDate endDate = convertFormattedStringToLocalDate(arguments[1]);
        if (startDate.isAfter(endDate)) {
            throw new ParseException(MESSAGE_INVALID_DATE_RANGE);
        }

        return new ListAppointmentCommand(startDate, endDate);
    }

    /**
     * Converts a correctly-formatted string to a LocalDate object.
     * The input is assumed to be correctly-formmtted.
     * @param s
     * @return LocalDate
     */
    private LocalDate convertFormattedStringToLocalDate(String s) throws ParseException {
        int[] time = Arrays.stream(s.split("-")).mapToInt(Integer::parseInt).toArray();
        LocalDate date;
        try {
            if (time.length == 2) {
                date = LocalDate.of(LocalDate.now().getYear(), time[1], time[0]);
            } else if (time.length == 3) {
                date = LocalDate.of(time[2], time[1], time[0]);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ListAppointmentCommand.MESSAGE_USAGE));
            }
        } catch (Exception e) {
            throw new ParseException(MESSAGE_INVALID_DATE);
        }
        return date;
    }
}
