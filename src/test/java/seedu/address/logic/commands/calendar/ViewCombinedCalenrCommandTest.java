package seedu.address.logic.commands.calendar;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.calendar.ViewCombinedCalendarCommand.MESSAGE_ALREADY_IN_COMBINED_VIEW;
import static seedu.address.testutil.TypicalCelebrities.JAY;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStorageCalendar.EMPTY_CALENDAR;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author WJY-norainu
public class ViewCombinedCalenrCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), EMPTY_CALENDAR, new UserPrefs());

    @Test
    public void execute_notAlreadyInCombinedCalendar_success() {
        model.setCelebCalendarOwner(JAY);
        ViewCombinedCalendarCommand viewCombinedCalendarCommand = prepareCommand();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());

        assertCommandSuccess(viewCombinedCalendarCommand, model,
                ViewCombinedCalendarCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_changeFromAppointmentListAlreadyInCombinedCalendar_success() {
        model.setCelebCalendarOwner(null);
        model.setIsListingAppointments(true);
        ViewCombinedCalendarCommand viewCombinedCalendarCommand = prepareCommand();

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());

        assertCommandSuccess(viewCombinedCalendarCommand, model,
                ViewCombinedCalendarCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_alreadyInCombinedCalendar_throwsCommandException() {
        model.setCelebCalendarOwner(null);
        ViewCombinedCalendarCommand viewCombinedCalendarCommand = prepareCommand();

        assertCommandFailure(viewCombinedCalendarCommand, model, MESSAGE_ALREADY_IN_COMBINED_VIEW);
    }

    @Test
    public void equals() {
        ViewCombinedCalendarCommand viewCombinedCalendarCommand = prepareCommand();

        // same object -> returns true
        assertTrue(viewCombinedCalendarCommand.equals(viewCombinedCalendarCommand));

        // same values -> returns true
        ViewCombinedCalendarCommand viewCombinedCalendarCommandCopy = prepareCommand();
        assertTrue(viewCombinedCalendarCommand.equals(viewCombinedCalendarCommandCopy));

        // different types -> returns false
        assertFalse(viewCombinedCalendarCommand.equals(1));

        // null -> returns false
        assertFalse(viewCombinedCalendarCommand.equals(null));
    }

    /**
     * Returns a {@code ViewCombinedCalendarCommand}.
     */
    private ViewCombinedCalendarCommand prepareCommand() {
        ViewCombinedCalendarCommand viewCombinedCalendarCommand = new ViewCombinedCalendarCommand();
        viewCombinedCalendarCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return viewCombinedCalendarCommand;
    }
}
