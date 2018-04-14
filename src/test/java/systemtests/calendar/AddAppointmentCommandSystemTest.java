package systemtests.calendar;

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
import static seedu.address.logic.parser.CliSyntax.PREFIX_CELEBRITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINT_OF_CONTACT;
import static seedu.address.testutil.TestUtil.getCelebrityIndices;
import static seedu.address.testutil.TestUtil.getPersonIndices;
import static seedu.address.testutil.TypicalCelebrities.AYANE;
import static seedu.address.testutil.TypicalCelebrities.JAY;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalStorageCalendar.OSCAR;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.calendar.AddAppointmentCommand;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicateAppointmentException;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.AppointmentUtil;
import systemtests.AddressBookSystemTest;

public class AddAppointmentCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void addAppointment() {
        /* ------------------------ Perform add appointment operations on the shown unfiltered list ----------------- */

        /* Case: add an appointment, command with leading spaces and trailing spaces
         * -> added
         */
        Appointment toAdd = OSCAR;
        List<Celebrity> celebrityList = new ArrayList<>();
        List<Person> pointOfContactList = new ArrayList<>();
        List<Index> celebrityIndices = new ArrayList<>();
        List<Index> pointOfContactIndices = new ArrayList<>();

        String command = "   " + AddAppointmentCommand.COMMAND_WORD + "  " + APPT_NAME_DESC_OSCAR + "  "
                + APPT_LOCATION_DESC_OSCAR + "  " + APPT_START_DATE_DESC_OSCAR + "  " + APPT_END_DATE_DESC_OSCAR + "  "
                + APPT_START_TIME_DESC_OSCAR + "  " + APPT_END_TIME_DESC_OSCAR + " ";
        assertCommandSuccess(command, toAdd);


        /* Case: add an appointment with all fields and a celebrity -> added */

        celebrityList.add(JAY);
        celebrityIndices.addAll(getCelebrityIndices(this.getModel(), celebrityList));
        toAdd = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_GRAMMY)
                .withLocation(VALID_APPOINTMENT_LOCATION_OSCAR).withStartDate(VALID_START_DATE_OSCAR)
                .withStartTime(VALID_START_TIME_OSCAR).withEndDate(VALID_END_DATE_OSCAR)
                .withEndTime(VALID_END_TIME_OSCAR).build();
        toAdd.updateEntries(celebrityList, pointOfContactList);

        command = AddAppointmentCommand.COMMAND_WORD + APPT_NAME_DESC_GRAMMY + APPT_LOCATION_DESC_OSCAR
                + APPT_END_DATE_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + generatePointOfContactandCelebrityFields(celebrityIndices,
                pointOfContactIndices);

        assertCommandSuccess(command, toAdd);


        /* Case: add an appointment with all fields and 2 celebrities -> added */

        celebrityList.add(AYANE);
        celebrityIndices.clear();
        celebrityIndices.addAll(getCelebrityIndices(this.getModel(), celebrityList));
        toAdd = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withLocation(VALID_APPOINTMENT_LOCATION_GRAMMY).withStartDate(VALID_START_DATE_OSCAR)
                .withStartTime(VALID_START_TIME_OSCAR).withEndDate(VALID_END_DATE_OSCAR)
                .withEndTime(VALID_END_TIME_OSCAR).build();
        toAdd.updateEntries(celebrityList, pointOfContactList);

        command = AddAppointmentCommand.COMMAND_WORD + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_GRAMMY
                + APPT_END_DATE_DESC_OSCAR + APPT_END_TIME_DESC_OSCAR + APPT_START_DATE_DESC_OSCAR
                + APPT_START_TIME_DESC_OSCAR + generatePointOfContactandCelebrityFields(celebrityIndices,
                pointOfContactIndices);

        assertCommandSuccess(command, toAdd);

        /* Case: add an appointment with all fields and 2 celebrities and 1 point of contact -> added */

        pointOfContactList.add(BENSON);
        celebrityIndices.clear();
        celebrityIndices.addAll(getCelebrityIndices(this.getModel(), celebrityList));
        pointOfContactIndices.addAll(getPersonIndices(this.getModel(), pointOfContactList));
        toAdd = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_OSCAR)
                .withLocation(VALID_APPOINTMENT_LOCATION_GRAMMY).withStartDate(VALID_START_DATE_OSCAR)
                .withStartTime(VALID_START_TIME_GRAMMY).withEndDate(VALID_END_DATE_OSCAR)
                .withEndTime(VALID_END_TIME_GRAMMY).build();
        toAdd.updateEntries(celebrityList, pointOfContactList);

        command = AddAppointmentCommand.COMMAND_WORD + APPT_NAME_DESC_OSCAR + APPT_LOCATION_DESC_GRAMMY
                + APPT_END_DATE_DESC_OSCAR + APPT_END_TIME_DESC_GRAMMY + APPT_START_DATE_DESC_OSCAR
                + APPT_START_TIME_DESC_GRAMMY + generatePointOfContactandCelebrityFields(celebrityIndices,
                pointOfContactIndices);

        assertCommandSuccess(command, toAdd);

        /* Case: add an appointment with all fields and 2 celebrities and 1 point of contact -> added */

        pointOfContactList.add(CARL);
        celebrityIndices.clear();
        celebrityIndices.addAll(getCelebrityIndices(this.getModel(), celebrityList));
        pointOfContactIndices.clear();
        pointOfContactIndices.addAll(getPersonIndices(this.getModel(), pointOfContactList));
        toAdd = new AppointmentBuilder().withName(VALID_APPOINTMENT_NAME_GRAMMY)
                .withLocation(VALID_APPOINTMENT_LOCATION_GRAMMY).withStartDate(VALID_START_DATE_GRAMMY)
                .withStartTime(VALID_START_TIME_GRAMMY).withEndDate(VALID_END_DATE_GRAMMY)
                .withEndTime(VALID_END_TIME_GRAMMY).build();
        toAdd.updateEntries(celebrityList, pointOfContactList);

        command = AddAppointmentCommand.COMMAND_WORD + APPT_NAME_DESC_GRAMMY + APPT_LOCATION_DESC_GRAMMY
                + APPT_END_DATE_DESC_GRAMMY + APPT_END_TIME_DESC_GRAMMY + APPT_START_DATE_DESC_GRAMMY
                + APPT_START_TIME_DESC_GRAMMY + generatePointOfContactandCelebrityFields(celebrityIndices,
                pointOfContactIndices);

        assertCommandSuccess(command, toAdd);

        /* ----------------------------------- Perform invalid add appointment operations --------------------------- */

        /* Case: add a duplicate appointment -> rejected */
        command = "   " + AddAppointmentCommand.COMMAND_WORD + "  " + APPT_NAME_DESC_OSCAR + "  "
                + APPT_LOCATION_DESC_OSCAR + "  " + APPT_START_DATE_DESC_OSCAR + "  " + APPT_END_DATE_DESC_OSCAR + "  "
                + APPT_START_TIME_DESC_OSCAR + "  " + APPT_END_TIME_DESC_OSCAR + " ";
        assertCommandFailure(command, AddAppointmentCommand.MESSAGE_DUPLICATE_APPOINTMENT);

    }

    /**
     * Executes the {@code AddAppointmentCommand} that adds {@code toAdd} to the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code AddAppointmentCommand} with the name of
     * {@code toAdd}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * the current model added with {@code toAdd}.<br>
     * 5. Selected card remains unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Appointment toAdd) {
        assertCommandSuccess(AppointmentUtil.getAddAppointmentCommand(toAdd), toAdd);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Appointment)}. Executes {@code command}
     * instead.
     * @see AddAppointmentCommandSystemTest#assertCommandSuccess(Appointment)
     */
    private void assertCommandSuccess(String command, Appointment toAdd) {
        Model expectedModel = getModel();
        try {
            expectedModel.addAppointmentToStorageCalendar(toAdd);
        } catch (DuplicateAppointmentException e) {
            throw new IllegalArgumentException("toAdd already exists in th model.");
        }


        String expectedResultMessage = String.format(AddAppointmentCommand.MESSAGE_SUCCESS, toAdd.getTitle());

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Appointment)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see AddAppointmentCommandSystemTest#assertCommandSuccess(String, Appointment)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertResultDisplayAndCommandBoxShowsDefaultStyle();
        assertStatusBarChangedExceptSaveLocation();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertResultDisplayAndCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarChangedExceptSaveLocation();
    }

    /**
     * Generates command string for a list of celebrities and POCs for use with add Appointment command
     */
    private String generatePointOfContactandCelebrityFields(List<Index> celebrityIndices,
                                                            List<Index> pointOfContactIndices) {
        return " " + generateCelebrityFields(celebrityIndices) + " "
                + generatePointOfContactFields(pointOfContactIndices);
    }

    /**
     * Generates a command string for a list of celebrity indices for add Appointment command
     */
    private String generateCelebrityFields(List<Index> celebrityIndices) {
        StringBuilder sb =  new StringBuilder();
        for (Index i : celebrityIndices) {
            sb.append(PREFIX_CELEBRITY).append(i.getOneBased() + " ");
        }
        return sb.toString();
    }

    /**
     * Generates a command string for a list of POC indices for add Appointment command
     */
    private String generatePointOfContactFields(List<Index> pointOfContactIndices) {
        StringBuilder sb =  new StringBuilder();
        for (Index i : pointOfContactIndices) {
            sb.append(PREFIX_POINT_OF_CONTACT).append(i.getOneBased() + " ");
        }
        return sb.toString();
    }
}
