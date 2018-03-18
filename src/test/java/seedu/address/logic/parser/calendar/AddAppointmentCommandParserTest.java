package seedu.address.logic.parser.calendar;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.APPT_LOCATION_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_LOCATION_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_NAME_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_NAME_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_HOUR_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_HOUR_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_MIN_DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.APPT_START_MIN_DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPT_LOCATION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_APPT_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_START_HOUR;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_START_MIN;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_LOCATION_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NAME_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_HOUR_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_MINUTE_OSCAR;
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
                .withLocation(VALID_APPOINTMENT_LOCATION_OSCAR).withStartHour(VALID_START_HOUR_OSCAR)
                .withStartMinute(VALID_START_MINUTE_OSCAR).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple names - last name accepted
        assertParseSuccess(parser, APPT_NAME_DESC_GRAMMY + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                        + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple locations - last location accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_GRAMMY + APPT_LOCATION_DESC_OSCAR
                        + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple start hours - last start hour accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                        + APPT_START_HOUR_DESC_GRAMMY + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));

        // multiple start minutes - last start minute accepted
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR + APPT_START_HOUR_DESC_OSCAR
                        + APPT_START_MIN_DESC_GRAMMY + APPT_START_MIN_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_locationFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withStartHour(VALID_START_HOUR_OSCAR).withStartMinute(VALID_START_MINUTE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_START_HOUR_DESC_OSCAR
                        + APPT_START_MIN_DESC_OSCAR, new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_hourFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withStartMinute(VALID_START_MINUTE_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_minuteFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withStartHour(VALID_START_HOUR_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR + APPT_START_HOUR_DESC_OSCAR,
                new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parseMinuteAndHourFieldMissing_success() {
        Appointment expectedAppointment = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR).build();
        assertParseSuccess(parser, APPT_NAME_DESC_OSCAR, new AddAppointmentCommand(expectedAppointment, 0));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAppointmentCommand.MESSAGE_USAGE);
        // missing name prefix
        assertParseFailure(parser, VALID_APPOINTMENT_NAME_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_APPT_NAME_DESC + APPT_LOCATION_DESC_OSCAR
                + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR, Appointment.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, APPT_NAME_DESC_OSCAR + INVALID_APPT_LOCATION_DESC
                + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR, Appointment.MESSAGE_NAME_CONSTRAINTS);


        // invalid email
        assertParseFailure(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + INVALID_START_HOUR + APPT_START_MIN_DESC_OSCAR, Appointment.MESSAGE_HOUR_CONSTRAINTS);


        // invalid address
        assertParseFailure(parser, APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_HOUR_DESC_OSCAR + INVALID_START_MIN, Appointment.MESSAGE_MINUTE_CONSTRAINTS);


        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_APPT_NAME_DESC + APPT_LOCATION_DESC_OSCAR
                + APPT_START_HOUR_DESC_OSCAR + INVALID_START_MIN, Appointment.MESSAGE_NAME_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_HOUR_DESC_OSCAR + APPT_START_MIN_DESC_OSCAR,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAppointmentCommand.MESSAGE_USAGE));
    }
}
