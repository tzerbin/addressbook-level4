package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.calendar.ListAppointmentCommand.MESSAGE_INVALID_DATE_RANGE;
import static seedu.address.logic.commands.calendar.ViewDateCommand.MESSAGE_INVALID_DATE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;

import org.junit.Test;
import seedu.address.logic.commands.calendar.ListAppointmentCommand;

//@@author: WJY-norainu
public class ListAppointmentCommandParserTest {
    private ListAppointmentCommandParser parser = new ListAppointmentCommandParser();

    private LocalDate aprilSecondCurrentYear = LocalDate.of(LocalDate.now().getYear(), 4, 2);
    private LocalDate mayFirstCurrentYear = LocalDate.of(LocalDate.now().getYear(), 5, 1);
    private LocalDate aprilSecond2018 = LocalDate.of(2018, 4, 2);
    private LocalDate mayFirst2018 = LocalDate.of(2018, 5, 1);

    @Test
    public void parse_noInput_returnsListAppointmentCommand() {
        assertParseSuccess(parser, "", new ListAppointmentCommand());
    }

    @Test
    public void parse_dateMonthInCorrectFormat_returnsListAppointmentCommand() {
        assertParseSuccess(parser,
                "02-04 01-05",
                new ListAppointmentCommand(aprilSecondCurrentYear, mayFirstCurrentYear));
    }

    @Test
    public void parse_dateMonthYearInCorrectFormat_returnsListAppointmentCommand() {
        assertParseSuccess(parser,
                "02-04-2018 01-05-2018",
                new ListAppointmentCommand(aprilSecond2018, mayFirst2018));
    }

    @Test
    //this method assumes test is done in the year of 2018 or after 2018
    public void parse_dateMonthAndDateMonthYearInCorrectFormat_returnsListAppointmentCommand() {
        assertParseSuccess(parser,
                "02-04-2018 01-05",
                new ListAppointmentCommand(aprilSecond2018, mayFirstCurrentYear));
    }

    @Test
    public void parse_missingDate_throwsParseException() {
        assertParseFailure(parser,
                "04-2018 05-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingMonth_throwsParseException() {
        assertParseFailure(parser,
                "31-2018 30-2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_dateMonthYearInWrongFormat_throwsParseException() {
        assertParseFailure(parser,
                "02/04/2018 01/05/2018",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        assertParseFailure(parser, "31-02 01-03", MESSAGE_INVALID_DATE);
        assertParseFailure(parser, "01-02 32-03", MESSAGE_INVALID_DATE);
    }

    @Test
    public void parse_startDateAfterEndDate_throwsParseException() {
        assertParseFailure(parser,
                "02-04-2019 01-05-2018",
                MESSAGE_INVALID_DATE_RANGE);
    }
}
