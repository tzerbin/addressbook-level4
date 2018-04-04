package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.RemoveTagCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.commands.calendar.AddAppointmentCommand;
import seedu.address.logic.commands.calendar.DeleteAppointmentCommand;
import seedu.address.logic.commands.calendar.EditAppointmentCommand;
import seedu.address.logic.commands.calendar.ListAppointmentCommand;
import seedu.address.logic.commands.calendar.ViewAppointmentCommand;
import seedu.address.logic.commands.calendar.ViewCalendarByCommand;
import seedu.address.logic.commands.calendar.ViewCalendarCommand;
import seedu.address.logic.commands.calendar.ViewCombinedCalendarCommand;
import seedu.address.logic.commands.calendar.ViewDateCommand;
import seedu.address.logic.commands.map.EstimateRouteCommand;
import seedu.address.logic.commands.map.ShowLocationCommand;
import seedu.address.logic.parser.calendar.AddAppointmentCommandParser;
import seedu.address.logic.parser.calendar.DeleteAppointmentCommandParser;
import seedu.address.logic.parser.calendar.EditAppointmentCommandParser;
import seedu.address.logic.parser.calendar.ViewAppointmentCommandParser;
import seedu.address.logic.parser.calendar.ViewCalendarByCommandParser;
import seedu.address.logic.parser.calendar.ViewCalendarCommandParser;
import seedu.address.logic.parser.calendar.ViewDateCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.map.EstimateRouteCommandParser;
import seedu.address.logic.parser.map.ShowLocationCommandParser;

/**
 * Parses user input.
 */
public class AddressBookParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
        case AddCommand.COMMAND_ALIAS:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
        case EditCommand.COMMAND_ALIAS:
            return new EditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_WORD:
        case SelectCommand.COMMAND_ALIAS:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
        case DeleteCommand.COMMAND_ALIAS:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
        case ClearCommand.COMMAND_ALIAS:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
        case FindCommand.COMMAND_ALIAS:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
        case ListCommand.COMMAND_ALIAS:
            return new ListCommand();

        case HistoryCommand.COMMAND_WORD:
        case HistoryCommand.COMMAND_ALIAS:
            return new HistoryCommand();

        case ExitCommand.COMMAND_WORD:
        case ExitCommand.COMMAND_ALIAS:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_WORD:
        case UndoCommand.COMMAND_ALIAS:
            return new UndoCommand();

        case RedoCommand.COMMAND_WORD:
        case RedoCommand.COMMAND_ALIAS:
            return new RedoCommand();

        case AddAppointmentCommand.COMMAND_WORD:
        case AddAppointmentCommand.COMMAND_ALIAS:
            return new AddAppointmentCommandParser().parse(arguments);

        case EditAppointmentCommand.COMMAND_WORD:
        case EditAppointmentCommand.COMMAND_ALIAS:
            return new EditAppointmentCommandParser().parse(arguments);

        case DeleteAppointmentCommand.COMMAND_WORD:
        case DeleteAppointmentCommand.COMMAND_ALIAS:
            return new DeleteAppointmentCommandParser().parse(arguments);

        case RemoveTagCommand.COMMAND_WORD:
        case RemoveTagCommand.COMMAND_ALIAS:
            return new RemoveTagCommandParser().parse(arguments);

        case ViewCalendarByCommand.COMMAND_WORD:
        case ViewCalendarByCommand.COMMAND_ALIAS:
            return new ViewCalendarByCommandParser().parse(arguments);

        case ViewCalendarCommand.COMMAND_WORD:
        case ViewCalendarCommand.COMMAND_ALIAS:
            return new ViewCalendarCommandParser().parse(arguments);

        case ViewCombinedCalendarCommand.COMMAND_WORD:
        case ViewCombinedCalendarCommand.COMMAND_ALIAS:
            return new ViewCombinedCalendarCommand();

        case ShowLocationCommand.COMMAND_WORD:
        case ShowLocationCommand.COMMAND_ALIAS:
            return new ShowLocationCommandParser().parse(arguments);

        case EstimateRouteCommand.COMMAND_WORD:
        case EstimateRouteCommand.COMMAND_ALIAS:
            return new EstimateRouteCommandParser().parse(arguments);

        case ListAppointmentCommand.COMMAND_WORD:
        case ListAppointmentCommand.COMMAND_ALIAS:
            return new ListAppointmentCommand();

        case ViewAppointmentCommand.COMMAND_WORD:
        case ViewAppointmentCommand.COMMAND_ALIAS:
            return new ViewAppointmentCommandParser().parse(arguments);

        case ViewDateCommand.COMMAND_WORD:
        case ViewDateCommand.COMMAND_ALIAS:
            return new ViewDateCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
