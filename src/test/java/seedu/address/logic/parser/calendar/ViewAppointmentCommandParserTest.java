package seedu.address.logic.parser.calendar;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPOINTMENT;

import org.junit.Test;

import seedu.address.logic.commands.calendar.ViewAppointmentCommand;
import seedu.address.logic.parser.ParserUtil;
//@@author Damienskt
public class ViewAppointmentCommandParserTest {

    private ViewAppointmentCommandParser parser = new ViewAppointmentCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteAppointmentCommand() {
        assertParseSuccess(parser, "1", new ViewAppointmentCommand(INDEX_FIRST_APPOINTMENT.getZeroBased()));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "b",
                String.format(ParserUtil.MESSAGE_INVALID_INDEX));
    }
}
