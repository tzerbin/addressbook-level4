package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_MAP_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalTags.FRIENDS_TAG;

import org.junit.Test;

import seedu.address.logic.commands.RemoveTagCommand;
import seedu.address.logic.commands.map.ShowLocationCommand;
import seedu.address.logic.parser.map.ShowLocationCommandParser;
import seedu.address.model.map.MapAddress;
import seedu.address.model.person.Address;

public class ShowLocationCommandParserTest {

    private ShowLocationCommandParser parser = new ShowLocationCommandParser();

    @Test
    public void parse_validArgs_returnsRemoveTagCommand() {
        MapAddress address = new MapAddress(VALID_ADDRESS_MAP_BOB);
        assertParseSuccess(parser, " " + PREFIX_ADDRESS + VALID_ADDRESS_MAP_BOB, new ShowLocationCommand(address));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowLocationCommand.MESSAGE_USAGE));
    }
}
