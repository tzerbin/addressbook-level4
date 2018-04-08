package seedu.address.logic.commands.calendar;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.calendar.ViewCalendarCommand.MESSAGE_NOT_CELEBRITY;
import static seedu.address.logic.commands.calendar.ViewCalendarCommand.MESSAGE_NO_CHANGE_IN_CALENDAR;
import static seedu.address.testutil.TypicalCelebrities.JAY;
import static seedu.address.testutil.TypicalCelebrities.ROBERT;
import static seedu.address.testutil.TypicalIndexes.INDEX_AYANE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_JAY;
import static seedu.address.testutil.TypicalIndexes.INDEX_ROBERT;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStorageCalendar.EMPTY_CALENDAR;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author WJY-norainu
public class ViewCalendarCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), EMPTY_CALENDAR, new UserPrefs());

    @Test
    public void execute_validCelebrityIndex_success() {
        ViewCalendarCommand viewCalendarCommand = prepareCommand(INDEX_ROBERT);

        String expectedMessage = String.format(ViewCalendarCommand.MESSAGE_SUCCESS, ROBERT.getName().toString());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setCelebCalendarOwner(ROBERT);

        assertCommandSuccess(viewCalendarCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validPersonIndex_throwsCommandException() {
        ViewCalendarCommand viewCalendarCommand = prepareCommand(INDEX_FIRST_PERSON);
        assertCommandFailure(viewCalendarCommand, model, MESSAGE_NOT_CELEBRITY);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index invalidIndex = Index.fromOneBased(model.getAddressBook().getPersonList().size()+1);
        ViewCalendarCommand viewCalendarCommand = prepareCommand(invalidIndex);
        assertCommandFailure(viewCalendarCommand, model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_celebrityCalendarAlreadyShown_throwsCommandException() {
        model.setCelebCalendarOwner(JAY);
        ViewCalendarCommand viewCalendarCommand = prepareCommand(INDEX_JAY);
        assertCommandFailure(viewCalendarCommand,
                model,
                String.format(MESSAGE_NO_CHANGE_IN_CALENDAR, JAY.getName().toString()));
    }

    @Test
    public void execute_fromAppointmentListViewToCalendar_success() {
        model.setCelebCalendarOwner(JAY);
        model.setIsListingAppointments(true);
        ViewCalendarCommand viewCalendarCommand = prepareCommand(INDEX_JAY);

        String expectedMessage = String.format(ViewCalendarCommand.MESSAGE_SUCCESS, JAY.getName().toString());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setCelebCalendarOwner(JAY);

        assertCommandSuccess(viewCalendarCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        ViewCalendarCommand viewJayCalendarCommand = prepareCommand(INDEX_JAY);
        ViewCalendarCommand viewAyaneCalendarCommand = prepareCommand(INDEX_AYANE);

        // same object -> returns true
        assertTrue(viewJayCalendarCommand.equals(viewJayCalendarCommand));

        // same values -> returns true
        ViewCalendarCommand viewJayCalendarCommandCopy = prepareCommand(INDEX_JAY);
        assertTrue(viewJayCalendarCommand.equals(viewJayCalendarCommandCopy));

        // different types -> returns false
        assertFalse(viewJayCalendarCommand.equals(1));

        // null -> returns false
        assertFalse(viewJayCalendarCommand.equals(null));

        // different appointment -> returns false
        assertFalse(viewJayCalendarCommand.equals(viewAyaneCalendarCommand));
    }

    /**
     * Returns a {@code ViewCalendarCommand} with the parameter {@code index}.
     */
    private ViewCalendarCommand prepareCommand(Index index) {
        ViewCalendarCommand viewCalendarCommand = new ViewCalendarCommand(index);
        viewCalendarCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return viewCalendarCommand;
    }
}
