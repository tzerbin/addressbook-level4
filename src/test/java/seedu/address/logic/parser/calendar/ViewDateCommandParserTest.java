package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.calendar.ViewDateCommand.MESSAGE_INVALID_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.Test;

import seedu.address.logic.commands.calendar.ViewDateCommand;

//@@author: WJY-norainu
public class ViewDateCommandParserTest {
    private ViewDateCommandParser parser = new ViewDateCommandParser();
    private LocalDate date = LocalDate.of(2018, 5, 1);

    @Test
    public void parse_noInput_returnsViewDateCommand() {
        assertParseSuccess(parser, "", new ViewDateCommand(LocalDate.now()));
    }

    @Test
    public void parse_dateMonthInCorrectFormat_returnsViewDateCommand() {
        assertParseSuccess(parser, "01-05", new ViewDateCommand(date));
    }

    @Test
    public void parse_dateMonthYearInCorrectFormat_returnsViewDateCommand() {
        assertParseSuccess(parser, "01-05-2018", new ViewDateCommand(date));
    }

    @Test
    public void parse_missingDate_throwsParseException() {
        assertParseFailure(parser, "05-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewDateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingMonth_throwsParseException() {
        assertParseFailure(parser, "01-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewDateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_dateMonthYearInWrongFormat_throwsParseException() {
        assertParseFailure(parser, "01 05 2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewDateCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertParseFailure(parser, "31-02", MESSAGE_INVALID_DATE);
    }
}
