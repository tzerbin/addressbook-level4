package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MINUTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import java.time.LocalTime;
import java.util.Optional;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.calendar.AddAppointmentCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;

/**
 * Parses input arguments and creates a new AddAppointmentCommand object
 */
public class AddAppointmentCommandParser implements Parser<AddAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAppointmentCommand
     * and returns an AddAppointmentCommand object for execution
     * @throws ParseException if the user input does not comform to the expected format
     */
    @Override
    public AddAppointmentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultiMap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_HOUR, PREFIX_MINUTE, PREFIX_DAY, PREFIX_LOCATION);
        if (!arePrefixesPresent(argMultiMap, PREFIX_NAME)
                || !argMultiMap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE));
        }

        try {
            String appointmentName = ParserUtil.parseGeneralName(argMultiMap.getValue(PREFIX_NAME)).get();
            Optional<Integer> hourInput = ParserUtil.parseHour(argMultiMap.getValue(PREFIX_HOUR));
            Optional<Integer> minuteInput = ParserUtil.parseMinute(argMultiMap.getValue(PREFIX_MINUTE));
            Optional<String> locationInput = ParserUtil.parseGeneralName(argMultiMap.getValue(PREFIX_LOCATION));

            String location = null;
            int hour = LocalTime.now().getHour();
            int minute = LocalTime.now().getMinute();

            if (hourInput.isPresent()) {
                hour = hourInput.get();
            }

            if (minuteInput.isPresent()) {
                minute = minuteInput.get();
            }

            if (locationInput.isPresent()) {
                location = locationInput.get();
            }

            Appointment appt = new Appointment(appointmentName, hour, minute, location);
            return new AddAppointmentCommand(appt, 0); // Let index be 0 for now since we only have one cal
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
