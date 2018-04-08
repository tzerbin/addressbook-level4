package seedu.address.logic.commands.calendar;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.calendar.ViewDateCommand.MESSAGE_NO_CHANGE_IN_BASE_DATE;
import static seedu.address.model.ModelManager.WEEK_VIEW_PAGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStorageCalendar.EMPTY_CALENDAR;

import java.time.LocalDate;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ViewDateCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), EMPTY_CALENDAR, new UserPrefs());
    private LocalDate mayFirst2018 = LocalDate.of(2018, 5, 1);
    private LocalDate maySecond2018 = LocalDate.of(2018, 5, 2);

    @Test
    public void execute_viewADifferentDay_success() {
        model.setBaseDate(mayFirst2018);
        ViewDateCommand viewDateCommand = prepareCommand(maySecond2018);

        String expectedMessage = String.format(ViewDateCommand.MESSAGE_SUCCESS,
                maySecond2018.format(ViewDateCommand.FORMATTER));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setBaseDate(maySecond2018);

        assertCommandSuccess(viewDateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_changeFromAppointmentListViewSameDay_success() {
        model.setBaseDate(mayFirst2018);
        model.setIsListingAppointments(true);
        ViewDateCommand viewDateCommand = prepareCommand(mayFirst2018);

        String expectedMessage = String.format(ViewDateCommand.MESSAGE_SUCCESS,
                mayFirst2018.format(ViewDateCommand.FORMATTER));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setBaseDate(mayFirst2018);

        assertCommandSuccess(viewDateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_changeFromWeekViewPageViewADifferentDay_success() {
        model.setCelebCalendarViewPage(WEEK_VIEW_PAGE);
        model.setBaseDate(mayFirst2018);
        ViewDateCommand viewDateCommand = prepareCommand(maySecond2018);

        String expectedMessage = String.format(ViewDateCommand.MESSAGE_SUCCESS,
                maySecond2018.format(ViewDateCommand.FORMATTER));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setBaseDate(maySecond2018);

        assertCommandSuccess(viewDateCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_changeFromWeekViewPageViewSameDay_throwsCommandException() {
        model.setCelebCalendarViewPage(WEEK_VIEW_PAGE);
        model.setBaseDate(mayFirst2018);
        ViewDateCommand viewDateCommand = prepareCommand(mayFirst2018);

        assertCommandFailure(viewDateCommand, model, String.format(MESSAGE_NO_CHANGE_IN_BASE_DATE,
                mayFirst2018.format(ViewDateCommand.FORMATTER)));
    }

    @Test
    public void execute_viewSameDay_throwsCommandException() {
        model.setBaseDate(mayFirst2018);
        ViewDateCommand viewDateCommand = prepareCommand(mayFirst2018);

        assertCommandFailure(viewDateCommand, model, String.format(MESSAGE_NO_CHANGE_IN_BASE_DATE,
                mayFirst2018.format(ViewDateCommand.FORMATTER)));
    }

    @Test
    public void equals() {
        ViewDateCommand viewDateMayFirstCommand = prepareCommand(mayFirst2018);
        ViewDateCommand viewDateMaySecondCommand = prepareCommand(maySecond2018);

        // same object -> returns true
        assertTrue(viewDateMayFirstCommand.equals(viewDateMayFirstCommand));

        // same values -> returns true
        ViewDateCommand viewDateMayFirstCommandCopy = prepareCommand(mayFirst2018);
        assertTrue(viewDateMayFirstCommand.equals(viewDateMayFirstCommandCopy));

        // different types -> returns false
        assertFalse(viewDateMayFirstCommand.equals(1));

        // null -> returns false
        assertFalse(viewDateMayFirstCommand.equals(null));

        // different calendar -> returns false
        assertFalse(viewDateMayFirstCommand.equals(viewDateMaySecondCommand));
    }

    /**
     * Returns a {@code ViewDateCommand} with the parameter {@code index}.
     */
    private ViewDateCommand prepareCommand(LocalDate date) {
        ViewDateCommand viewDateCommand = new ViewDateCommand(date);
        viewDateCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return viewDateCommand;
    }
}
