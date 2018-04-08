package seedu.address.logic.commands.calendar;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.calendar.ViewCalendarByCommand.MESSAGE_NO_CHANGE_IN_CALENDARVIEW;
import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;
import static seedu.address.model.ModelManager.MONTH_VIEW_PAGE;
import static seedu.address.model.ModelManager.WEEK_VIEW_PAGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStorageCalendar.EMPTY_CALENDAR;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

//@@author WJY-norainu
public class ViewCalendarByCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), EMPTY_CALENDAR, new UserPrefs());

    @Test
    public void execute_calendarViewByDay_success() {
        ViewCalendarByCommand viewCalendarByCommand = prepareCommand("day");

        String expectedMessage = String.format(ViewCalendarByCommand.MESSAGE_SUCCESS, DAY_VIEW_PAGE);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setCelebCalendarViewPage(DAY_VIEW_PAGE);

        model.setCelebCalendarViewPage(WEEK_VIEW_PAGE);
        assertCommandSuccess(viewCalendarByCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_calendarViewByWeek_success() {
        ViewCalendarByCommand viewCalendarByCommand = prepareCommand("week");

        String expectedMessage = String.format(ViewCalendarByCommand.MESSAGE_SUCCESS, WEEK_VIEW_PAGE);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setCelebCalendarViewPage(WEEK_VIEW_PAGE);

        assertCommandSuccess(viewCalendarByCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_calendarViewByMonth_success() {
        ViewCalendarByCommand viewCalendarByCommand = prepareCommand("month");

        String expectedMessage = String.format(ViewCalendarByCommand.MESSAGE_SUCCESS, MONTH_VIEW_PAGE);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setCelebCalendarViewPage(MONTH_VIEW_PAGE);

        assertCommandSuccess(viewCalendarByCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_changeFromAppointmentListWithNoChangeInView_success() {
        model.setIsListingAppointments(true);
        ViewCalendarByCommand viewCalendarByCommand = prepareCommand(model.getCurrentCelebCalendarViewPage());

        String expectedMessage = String.format(ViewCalendarByCommand.MESSAGE_SUCCESS, DAY_VIEW_PAGE);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setCelebCalendarViewPage(DAY_VIEW_PAGE);

        assertCommandSuccess(viewCalendarByCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_changeFromAppointmentListWithChangeInView_success() {
        model.setIsListingAppointments(true);
        ViewCalendarByCommand viewCalendarByCommand = prepareCommand(WEEK_VIEW_PAGE);

        String expectedMessage = String.format(ViewCalendarByCommand.MESSAGE_SUCCESS, WEEK_VIEW_PAGE);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.setCelebCalendarViewPage(WEEK_VIEW_PAGE);

        assertCommandSuccess(viewCalendarByCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noChangeInView_throwsCommandException() {
        ViewCalendarByCommand viewCalendarByCommand = prepareCommand("day");
        assertCommandFailure(viewCalendarByCommand, model, String.format(MESSAGE_NO_CHANGE_IN_CALENDARVIEW,
                DAY_VIEW_PAGE));
    }

    @Test
    public void equals() {
        ViewCalendarByCommand viewCalendarByDayCommand = prepareCommand("day");
        ViewCalendarByCommand viewCalendarByWeekCommand = prepareCommand("week");

        // same object -> returns true
        assertTrue(viewCalendarByDayCommand.equals(viewCalendarByDayCommand));

        // same values -> returns true
        ViewCalendarByCommand viewCalendarByDayCommandCopy = prepareCommand("day");
        assertTrue(viewCalendarByDayCommand.equals(viewCalendarByDayCommandCopy));

        // different types -> returns false
        assertFalse(viewCalendarByDayCommand.equals(1));

        // null -> returns false
        assertFalse(viewCalendarByDayCommand.equals(null));

        // different appointment -> returns false
        assertFalse(viewCalendarByDayCommand.equals(viewCalendarByWeekCommand));
    }

    /**
     * Returns a {@code ViewCalendarCommand} with the parameter {@code index}.
     */
    private ViewCalendarByCommand prepareCommand(String calendarViewPage) {
        ViewCalendarByCommand viewCalendarByCommand = new ViewCalendarByCommand(calendarViewPage);
        viewCalendarByCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return viewCalendarByCommand;
    }
}
