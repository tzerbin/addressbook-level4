package seedu.address.logic.commands.calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPOINTMENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPOINTMENT;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStorageCalendar.CONCERT;
import static seedu.address.testutil.TypicalStorageCalendar.DENTAL;
import static seedu.address.testutil.TypicalStorageCalendar.generateEmptyStorageCalendar;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.exceptions.DuplicateAppointmentException;
import seedu.address.testutil.AppointmentBuilder;

public class ViewAppointmentCommandTest {

    private Model model;

    @Test
    public void execute_validIndexListingAppointments_success() throws DuplicateAppointmentException {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.addAppointmentToStorageCalendar(CONCERT);
        model.addAppointmentToStorageCalendar(DENTAL);
        model.setIsListingAppointments(true);
        model.setCurrentlyDisplayedAppointments(model.getStorageCalendar().getAllAppointments());
        ViewAppointmentCommand viewAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);

        String displayedLocation = (CONCERT.getLocation() == null)
                ? ViewAppointmentCommand.MESSAGE_NO_LOCATION
                : CONCERT.getLocation();

        String expectedMessage = "Selected appointment details:\n"
                + "Appointment Name: " + CONCERT.getTitle() + "\n"
                + "Start Date: " + CONCERT.getStartDate() + "\n"
                + "Start Time: " + CONCERT.getStartTime() + "\n"
                + "End Date: " + CONCERT.getEndDate() + "\n"
                + "End Time: " + CONCERT.getEndTime() + "\n"
                + "Location: " + displayedLocation + "\n"
                + "Celebrities attending: " + CONCERT.getCelebritiesAttending() + "\n"
                + "Points of Contact: " + CONCERT.getPointsOfContact();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), generateEmptyStorageCalendar(),
                new UserPrefs());
        expectedModel.addAppointmentToStorageCalendar((new AppointmentBuilder(CONCERT)).build());
        expectedModel.addAppointmentToStorageCalendar((new AppointmentBuilder(DENTAL)).build());
        //still have appointments in the list after deletion, should show appointment list
        expectedModel.setIsListingAppointments(true);

        assertCommandSuccess(viewAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexNotListingAppointments_throwsCommandException() throws DuplicateAppointmentException {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.addAppointmentToStorageCalendar(CONCERT);
        model.addAppointmentToStorageCalendar(DENTAL);
        model.setCurrentlyDisplayedAppointments(model.getStorageCalendar().getAllAppointments());
        model.setIsListingAppointments(false);
        ViewAppointmentCommand viewAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);

        assertCommandFailure(viewAppointmentCommand,
                model,
                ViewAppointmentCommand.MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS);
    }

    @Test
    public void execute_invalidOutOfBoundsIndexListingAppointments_throwsCommandException() {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.setIsListingAppointments(true);
        model.setCurrentlyDisplayedAppointments(model.getStorageCalendar().getAllAppointments());
        Index outOfBoundIndex = Index.fromOneBased(model.getAppointmentList().size() + 1);
        ViewAppointmentCommand viewAppointmentCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(viewAppointmentCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        ViewAppointmentCommand viewFirstAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);
        ViewAppointmentCommand viewSecondAppointmentCommand = prepareCommand(INDEX_SECOND_APPOINTMENT);

        // same object -> returns true
        assertTrue(viewFirstAppointmentCommand.equals(viewFirstAppointmentCommand));

        // same values -> returns true
        ViewAppointmentCommand viewFirstAppointmentCommandCopy = prepareCommand(INDEX_FIRST_PERSON);
        assertTrue(viewFirstAppointmentCommand.equals(viewFirstAppointmentCommandCopy));

        // different types -> returns false
        assertFalse(viewFirstAppointmentCommand.equals(1));

        // null -> returns false
        assertFalse(viewFirstAppointmentCommand.equals(null));

        // different appointment -> returns false
        assertFalse(viewFirstAppointmentCommand.equals(viewSecondAppointmentCommand));
    }

    /**
     * Returns a {@code ViewCommand} with the parameter {@code index}.
     */
    private ViewAppointmentCommand prepareCommand(Index index) {
        ViewAppointmentCommand viewAppointmentCommand = new ViewAppointmentCommand(index.getZeroBased());
        viewAppointmentCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return viewAppointmentCommand;
    }
}
