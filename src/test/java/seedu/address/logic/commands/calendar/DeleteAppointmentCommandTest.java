package seedu.address.logic.commands.calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.calendar.DeleteAppointmentCommand.MESSAGE_APPOINTMENT_LIST_BECOMES_EMPTY;
import static seedu.address.logic.commands.calendar.DeleteAppointmentCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.TypicalCelebrities.JAY;
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

//@@author WJY-norainu
public class DeleteAppointmentCommandTest {

    private Model model;

    @Test
    public void execute_validIndexListingAppointmentsWithRemainingAppointments_success()
            throws DuplicateAppointmentException {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.addAppointmentToStorageCalendar(CONCERT);
        model.addAppointmentToStorageCalendar(DENTAL);
        model.setIsListingAppointments(true);
        model.setCurrentlyDisplayedAppointments(model.getStoredAppointmentList());
        DeleteAppointmentCommand deleteAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);

        String expectedMessage = String.format(
                MESSAGE_SUCCESS,
                CONCERT.getTitle());

        Model expectedModel = new ModelManager(model.getAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        expectedModel.addAppointmentToStorageCalendar((new AppointmentBuilder(DENTAL)).build());
        //still have appointments in the list after deletion, should show appointment list
        expectedModel.setIsListingAppointments(true);

        assertCommandSuccess(deleteAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deletesTheOnlyAppointmentWithCombinedCalendar_successAndChangeToCombinedCalendar()
            throws DuplicateAppointmentException {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.addAppointmentToStorageCalendar(DENTAL);
        model.setIsListingAppointments(true);
        model.setCurrentlyDisplayedAppointments(model.getStoredAppointmentList());
        DeleteAppointmentCommand deleteAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);

        String expectedMessage = String.format(MESSAGE_SUCCESS, DENTAL.getTitle())
                + String.format(MESSAGE_APPOINTMENT_LIST_BECOMES_EMPTY, "combined");

        Model expectedModel = new ModelManager(model.getAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        //have no appointments in the list after deletion, should show calendar
        expectedModel.setIsListingAppointments(false);
        expectedModel.setCelebCalendarViewPage(ModelManager.DAY_VIEW_PAGE);

        assertCommandSuccess(deleteAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteTheOnlyAppointmentWithCelebCalendar_successAndShowCelebCalendar()
            throws DuplicateAppointmentException {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.addAppointmentToStorageCalendar(DENTAL);
        model.setCelebCalendarOwner(JAY);
        model.setIsListingAppointments(true);
        model.setCurrentlyDisplayedAppointments(model.getStoredAppointmentList());
        DeleteAppointmentCommand deleteAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);

        String expectedMessage = String.format(MESSAGE_SUCCESS, DENTAL.getTitle())
                + String.format(MESSAGE_APPOINTMENT_LIST_BECOMES_EMPTY, JAY.getName().toString() + "'s");

        Model expectedModel = new ModelManager(model.getAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        //have no appointments in the list after deletion, should show calendar
        expectedModel.setIsListingAppointments(false);
        expectedModel.setCelebCalendarViewPage(ModelManager.DAY_VIEW_PAGE);

        assertCommandSuccess(deleteAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexNotListingAppointments_throwsCommandException()
            throws DuplicateAppointmentException {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.addAppointmentToStorageCalendar(CONCERT);
        model.addAppointmentToStorageCalendar(DENTAL);
        model.setCurrentlyDisplayedAppointments(model.getStoredAppointmentList());
        model.setIsListingAppointments(false);
        DeleteAppointmentCommand deleteAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);

        assertCommandFailure(deleteAppointmentCommand,
                model, Messages.MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS);
    }

    @Test
    public void execute_invalidIndexListingAppointments_throwsCommandException() {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        model.setIsListingAppointments(true);
        model.setCurrentlyDisplayedAppointments(model.getStoredAppointmentList());
        Index outOfBoundIndex = Index.fromOneBased(model.getAppointmentList().size() + 1);
        DeleteAppointmentCommand deleteAppointmentCommand = prepareCommand(outOfBoundIndex);

        assertCommandFailure(deleteAppointmentCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        model = new ModelManager(getTypicalAddressBook(), generateEmptyStorageCalendar(), new UserPrefs());
        DeleteAppointmentCommand deleteFirstAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT);
        DeleteAppointmentCommand deleteSecondAppointmentCommand = prepareCommand(INDEX_SECOND_APPOINTMENT);

        // same object -> returns true
        assertTrue(deleteFirstAppointmentCommand.equals(deleteFirstAppointmentCommand));

        // same values -> returns true
        DeleteAppointmentCommand deleteFirstAppointmentCommandCopy = prepareCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstAppointmentCommand.equals(deleteFirstAppointmentCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstAppointmentCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstAppointmentCommand.equals(null));

        // different appointment -> returns false
        assertFalse(deleteFirstAppointmentCommand.equals(deleteSecondAppointmentCommand));
    }

    /**
     * Returns a {@code DeleteAppointmentCommand} with the parameter {@code index}.
     */
    private DeleteAppointmentCommand prepareCommand(Index index) {
        DeleteAppointmentCommand deleteAppointmentCommand = new DeleteAppointmentCommand(index);
        deleteAppointmentCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteAppointmentCommand;
    }

}
