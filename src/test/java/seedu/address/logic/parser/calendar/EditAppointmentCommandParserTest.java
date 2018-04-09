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
import static seedu.address.logic.commands.CommandTestUtil.INVALID_END_DATE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_END_TIME;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_START_DATE;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_START_TIME;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_LOCATION_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_LOCATION_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NAME_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_APPOINTMENT_NAME_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_DATE_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_DATE_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_TIME_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_END_TIME_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_DATE_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_DATE_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_TIME_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_START_TIME_OSCAR;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPOINTMENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPOINTMENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_APPOINTMENT;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.calendar.EditAppointmentCommand;
import seedu.address.logic.commands.calendar.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.map.MapAddress;
import seedu.address.testutil.EditAppointmentDescriptorBuilder;

public class EditAppointmentCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            EditAppointmentCommand.MESSAGE_USAGE);

    private EditAppointmentCommandParser parser = new EditAppointmentCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_APPOINTMENT_NAME_OSCAR, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", EditAppointmentCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + APPT_NAME_DESC_OSCAR, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + APPT_NAME_DESC_OSCAR, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, "1" + INVALID_APPT_NAME_DESC, Appointment.MESSAGE_NAME_CONSTRAINTS);

        // invalid start time
        assertParseFailure(parser, "1" + INVALID_START_TIME, Appointment.MESSAGE_TIME_CONSTRAINTS);

        // invalid start date
        assertParseFailure(parser, "1" + INVALID_START_DATE, Appointment.MESSAGE_DATE_CONSTRAINTS);

        // invalid end time
        assertParseFailure(parser, "1" + INVALID_END_TIME, Appointment.MESSAGE_TIME_CONSTRAINTS);

        // invalid end date
        assertParseFailure(parser, "1" + INVALID_END_DATE, Appointment.MESSAGE_DATE_CONSTRAINTS);

        // invalid location
        assertParseFailure(parser, "1" + INVALID_APPT_LOCATION_DESC,
                MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);

        // invalid start date followed by valid end time
        assertParseFailure(parser, "1" + INVALID_START_DATE + APPT_END_TIME_DESC_OSCAR,
                Appointment.MESSAGE_DATE_CONSTRAINTS);

        // valid end date followed by invalid end date. The test case for invalid end date followed by valid end date
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + APPT_END_DATE_DESC_OSCAR + INVALID_END_DATE,
                Appointment.MESSAGE_DATE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser, "1" + INVALID_APPT_NAME_DESC + INVALID_APPT_LOCATION_DESC
                + VALID_START_TIME_OSCAR, Appointment.MESSAGE_NAME_CONSTRAINTS);
    }


    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_APPOINTMENT;
        String userInput = targetIndex.getOneBased() + APPT_LOCATION_DESC_GRAMMY + APPT_START_DATE_DESC_GRAMMY
                + APPT_END_TIME_DESC_OSCAR + APPT_END_DATE_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR
                + APPT_NAME_DESC_OSCAR;

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder()
                .withName(VALID_APPOINTMENT_NAME_OSCAR).withLocation(VALID_APPOINTMENT_LOCATION_GRAMMY)
                .withStartTime(VALID_START_TIME_OSCAR).withEndTime(VALID_END_TIME_OSCAR)
                .withStartDate(VALID_START_DATE_GRAMMY).withEndDate(VALID_END_DATE_OSCAR).build();
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_APPOINTMENT;
        String userInput = targetIndex.getOneBased() + APPT_LOCATION_DESC_OSCAR + APPT_START_DATE_DESC_GRAMMY;

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder()
                .withLocation(VALID_APPOINTMENT_LOCATION_OSCAR).withStartDate(VALID_START_DATE_GRAMMY).build();
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_APPOINTMENT;
        String userInput = targetIndex.getOneBased() + APPT_NAME_DESC_OSCAR;
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder()
                .withName(VALID_APPOINTMENT_NAME_OSCAR).build();
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // location
        userInput = targetIndex.getOneBased() + APPT_LOCATION_DESC_OSCAR;
        descriptor = new EditAppointmentDescriptorBuilder().withLocation(VALID_APPOINTMENT_LOCATION_OSCAR).build();
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // start date
        userInput = targetIndex.getOneBased() + APPT_START_DATE_DESC_GRAMMY;
        descriptor = new EditAppointmentDescriptorBuilder().withStartDate(VALID_START_DATE_GRAMMY).build();
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // start time
        userInput = targetIndex.getOneBased() + APPT_START_TIME_DESC_OSCAR;
        descriptor = new EditAppointmentDescriptorBuilder().withStartTime(VALID_START_TIME_OSCAR).build();
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // end date
        userInput = targetIndex.getOneBased() + APPT_END_DATE_DESC_GRAMMY;
        descriptor = new EditAppointmentDescriptorBuilder().withEndDate(VALID_END_DATE_GRAMMY).build();
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // start time
        userInput = targetIndex.getOneBased() + APPT_END_TIME_DESC_OSCAR;
        descriptor = new EditAppointmentDescriptorBuilder().withEndTime(VALID_END_TIME_OSCAR).build();
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_APPOINTMENT;
        String userInput = targetIndex.getOneBased()  + APPT_END_TIME_DESC_OSCAR + APPT_LOCATION_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + APPT_NAME_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR
                + APPT_LOCATION_DESC_OSCAR + APPT_START_TIME_DESC_OSCAR + APPT_NAME_DESC_OSCAR
                + APPT_END_TIME_DESC_GRAMMY + APPT_LOCATION_DESC_GRAMMY + APPT_START_TIME_DESC_GRAMMY
                + APPT_NAME_DESC_GRAMMY;

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder()
                .withName(VALID_APPOINTMENT_NAME_GRAMMY).withStartTime(VALID_START_TIME_GRAMMY)
                .withEndTime(VALID_END_TIME_GRAMMY).withLocation(VALID_APPOINTMENT_LOCATION_GRAMMY)
                .build();
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() {
        // no other valid values specified
        Index targetIndex = INDEX_FIRST_APPOINTMENT;
        String userInput = targetIndex.getOneBased() + INVALID_START_DATE + APPT_START_DATE_DESC_OSCAR;
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder()
                .withStartDate(VALID_START_DATE_OSCAR).build();
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // other valid values specified
        userInput = targetIndex.getOneBased() + APPT_LOCATION_DESC_OSCAR + INVALID_END_DATE + APPT_NAME_DESC_OSCAR
                + APPT_END_DATE_DESC_OSCAR;
        descriptor = new EditAppointmentDescriptorBuilder().withLocation(VALID_APPOINTMENT_LOCATION_OSCAR)
                .withName(VALID_APPOINTMENT_NAME_OSCAR).withEndDate(VALID_END_DATE_OSCAR).build();
        expectedCommand = new EditAppointmentCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
