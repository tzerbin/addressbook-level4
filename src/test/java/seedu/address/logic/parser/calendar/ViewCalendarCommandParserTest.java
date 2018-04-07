package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.calendar.ViewCalendarCommand;

//@@author: WJY-norainu
public class ViewCalendarCommandParserTest {
    private ViewCalendarCommandParser parser = new ViewCalendarCommandParser();
    private String failureMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCalendarCommand.MESSAGE_USAGE);

    @Test
    public void parse_noInput_throwsParseException() {
        assertParseFailure(parser, "", failureMessage);
    }

    @Test
    public void parse_letter_throwsParseException() {
        assertParseFailure(parser, "a", failureMessage);
    }

    @Test
    public void parse_negativeInteger_throwsParseException() {
        assertParseFailure(parser, "-1", failureMessage);
    }

    @Test
    public void parse_zero_throwsParseException() {
        assertParseFailure(parser, "0", failureMessage);
    }

    @Test
    public void parse_validIndex_returnsViewCalendarCommand() {
        assertParseSuccess(parser, "1", new ViewCalendarCommand(INDEX_FIRST_PERSON));
    }
}
