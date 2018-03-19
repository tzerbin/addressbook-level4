package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.APPT_END_DATE_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_END_DATE_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_END_TIME_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_END_TIME_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_LOCATION_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_LOCATION_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_NAME_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_NAME_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_DATE_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_DATE_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_TIME_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_TIME_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPT_LOCATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPT_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_START_DATE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_START_TIME;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_LOCATION_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NAME_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_DATE_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_TIME_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_DATE_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_TIME_OSCAR;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.calendar.AddAppointmentCommand;
import seedu.address.model.appointment.Appointment;
import seedu.address.testutil.AppointmentBuilder;

public class AddAppointmentCommandParserTest {
    private AddAppointmentCommandParser parser = new AddAppointmentCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withLocation(VALID_APPOINTMENT_LOCATION_OSCAR).withStartTime(VALID_START_TIME_OSCAR)
                .withStartDate(VALID_START_DATE_OSCAR).withEndTime(VALID_END_TIME_OSCAR)
                .withEndDate(VALID_END_DATE_OSCAR).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                        + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR
                        + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple names - last name accepted
        assertParseSuccess(parser, APPT_NAME_DESC_GRAMMY + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                        + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR
                        + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple locations - last location accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_GRAMMY + APPT_LOCATION_DESC_OSCAR
                        + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR
                        + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple start times - last start times accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                        + APPT_START_TIME_DESC_GRAMMY + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR
                        + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple start dates - last start date accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR
                        + APPT_START_DATE_DESC_GRAMMY + APPT_START_DATE_DESC_OSCAR
                        + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple end times - last end time accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                        + APPT_END_TIME_DESC_GRAMMY + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR
                        + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple end dates - last end date accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR
                        + APPT_END_DATE_DESC_GRAMMY + APPT_START_DATE_DESC_OSCAR
                        + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_locationFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withStartTime(VALID_START_TIME_OSCAR).withStartDate(VALID_START_DATE_OSCAR)
                .withEndTime(VALID_END_TIME_OSCAR).withEndDate(VALID_END_DATE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR
                        + APPT_START_DATE_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_startTimeFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withStartDate(VALID_START_DATE_OSCAR).withEndTime(VALID_END_TIME_OSCAR)
                .withEndDate(VALID_END_DATE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR
                        + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_startDateFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withStartTime(VALID_START_TIME_OSCAR).withEndTime(VALID_END_TIME_OSCAR)
                .withEndDate(VALID_END_DATE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR
                        + APPT_END_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parseStartTimeAndStartDateFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withEndTime(VALID_END_TIME_OSCAR).withEndDate(VALID_END_DATE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR
                + APPT_END_DATE_DESC_OSCAR, new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_endTimeFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withEndDate(VALID_END_DATE_OSCAR).withStartTime(VALID_START_TIME_OSCAR)
                .withStartDate(VALID_START_DATE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR
                        + APPT_START_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_endDateFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withEndTime(VALID_END_TIME_OSCAR).withStartTime(VALID_START_TIME_OSCAR)
                .withStartDate(VALID_START_DATE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR
                        + APPT_START_DATE_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parseEndTimeAndEndDateFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withStartTime(VALID_START_TIME_OSCAR).withStartDate(VALID_START_DATE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR
                + APPT_START_DATE_DESC_OSCAR, new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parseAllTimeAndDateFieldsMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR, new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAppointmentCommand.MESSAGE_USAGE);
        // missing name prefix
        assertParseFailure(parser, VALID_APPOINTMENT_NAME_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_APPT_NAME_DESC + APPT_LOCATION_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR, Appointment.MESSAGE_NAME_CONSTRAINTS);

        // invalid location
        assertParseFailure(parser, APPT_NAME_DESC_OSCAR + INVALID_APPT_LOCATION_DESC
                + APPT_START_DATE_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR, Appointment.MESSAGE_NAME_CONSTRAINTS);


        // invalid start time
        assertParseFailure(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + INVALID_START_TIME + APPT_START_DATE_DESC_OSCAR, Appointment.MESSAGE_TIME_CONSTRAINTS);

        // invalid start date
        assertParseFailure(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + INVALID_START_DATE, Appointment.MESSAGE_DATE_CONSTRAINTS);


        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_APPT_NAME_DESC + APPT_LOCATION_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + INVALID_START_DATE, Appointment.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAppointmentCommand.MESSAGE_USAGE));
    }
}
