package seedu.address.logic.parser.map;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_HOUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MINUTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.map.ShowLocationCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;

public class ShowLocationCommandParser implements Parser<ShowLocationCommand>{
    /**
     * Parses the given {@code String} of arguments in the context of the ShowLocationCommand
     * and returns an ShowLocationCommand object for execution
     * @throws ParseException if the user input does not comform to the expected format
     */
    @Override
    public ShowLocationCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ShowLocationCommand.MESSAGE_USAGE));
        }

        try {
            Address address = ParserUtil.parseAddress(args);
            return new ShowLocationCommand(address);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowLocationCommand.MESSAGE_USAGE));
        }
    }
}
