package systemtests.calendar;

import static seedu.address.testutil.TypicalStorageCalendar.DENTAL;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.calendar.ViewAppointmentCommand;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import systemtests.AddressBookSystemTest;
//@@author Damienskt
public class ViewAppointmentCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void viewAppointment() {

        /* ---------------------------- Perform invalid viewAppointment operations --------------------------------- */
        assertCommandFailure(ViewAppointmentCommand.COMMAND_WORD + " " + 0, ParserUtil.MESSAGE_INVALID_INDEX);
        assertCommandFailure(ViewAppointmentCommand.COMMAND_WORD + " ads" , ParserUtil.MESSAGE_INVALID_INDEX);
        assertCommandFailure(ViewAppointmentCommand.COMMAND_WORD + " ", ParserUtil.MESSAGE_INVALID_INDEX);
        assertCommandFailure(ViewAppointmentCommand.COMMAND_WORD + " "
                + 2 , ViewAppointmentCommand.MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS);
        executeCommand("la");
        assertCommandFailure(ViewAppointmentCommand.COMMAND_WORD + " "
                + 4 , Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);

        /* ------------------------------ Perform valid viewAppointment operations --------------------------------- */
        executeCommand("la");
        assertCommandSuccess(1);
    }

    /**
     * Executes the {@code ViewAppointmentCommand} that asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code ViewAppointmentCommand}.<br>
     * 4. Shows the location marker of appointment location in Maps GUI.<br>
     * 5. Calendar panel and selected card remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(int index) {
        assertCommandSuccess(ViewAppointmentCommand.COMMAND_WORD + " " + index, index);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(int)}. Executes {@code command}
     * instead.
     * @see ViewAppointmentCommandSystemTest#assertCommandSuccess(int)
     */
    private void assertCommandSuccess(String command, int index) {
        Model expectedModel = getModel();

        Appointment selected = DENTAL;
        String expectedResultMessage = "Selected appointment details:\n"
                + "Appointment Name: " + selected.getTitle() + "\n"
                + "Start Date: " + selected.getStartDate() + "\n"
                + "Start Time: " + selected.getStartTime() + "\n"
                + "End Date: " + selected.getEndDate() + "\n"
                + "End Time: " + selected.getEndTime() + "\n"
                + "Location: " + selected.getMapAddress().toString() + "\n"
                + "Celebrities attending: " + selected.getCelebritiesAttending() + "\n"
                + "Points of Contact: " + selected.getPointsOfContact();

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String,int)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see ViewAppointmentCommandSystemTest#assertCommandSuccess(String,int)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertResultDisplayAndCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();
        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
