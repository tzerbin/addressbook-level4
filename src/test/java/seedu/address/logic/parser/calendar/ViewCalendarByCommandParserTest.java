package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;
import static seedu.address.model.ModelManager.MONTH_VIEW_PAGE;
import static seedu.address.model.ModelManager.WEEK_VIEW_PAGE;

import org.junit.Test;

import seedu.address.logic.commands.calendar.ViewCalendarByCommand;

//@@author WJY-norainu
public class ViewCalendarByCommandParserTest {
    private ViewCalendarByCommandParser parser = new ViewCalendarByCommandParser();

    @Test
    public void parse_day_returnsViewCalendarByCommand() {
        assertParseSuccess(parser, "day", new ViewCalendarByCommand(DAY_VIEW_PAGE));
    }

    @Test
    public void parse_dayWithUpperCaseLetter_returnsViewCalendarByCommand() {
        assertParseSuccess(parser, "Day", new ViewCalendarByCommand(DAY_VIEW_PAGE));
        assertParseSuccess(parser, "DAy", new ViewCalendarByCommand(DAY_VIEW_PAGE));
        assertParseSuccess(parser, "DAY", new ViewCalendarByCommand(DAY_VIEW_PAGE));
    }

    @Test
    public void parse_week_returnsViewCalendarByCommand() {
        assertParseSuccess(parser, "week", new ViewCalendarByCommand(WEEK_VIEW_PAGE));
    }

    @Test
    public void parse_month_returnsViewCalendarByCommand() {
        assertParseSuccess(parser, "month", new ViewCalendarByCommand(MONTH_VIEW_PAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "b",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCalendarByCommand.MESSAGE_USAGE));
    }
}
