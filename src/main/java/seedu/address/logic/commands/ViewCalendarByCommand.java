package seedu.address.logic.commands;

import com.calendarfx.view.CalendarView;
import com.calendarfx.view.page.PageBase;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Lists all persons in the address book to the user.
 */
public class ViewCalendarByCommand extends Command {

    public static final String COMMAND_WORD = "viewCalendarBy";
    public static final String COMMAND_ALIAS = "vcb";
    public static final String[] VALID_ARGUMENT = {"day", "week", "month", "year"};

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Opens the calendar view specified.\n"
            + "Parameters: CALENDARVIEWPAGE (must be one of day, week, month, year. Not case-sensitive)\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    public static final String MESSAGE_NO_CHANGE_IN_CALENDARVIEW = "Calender is already in %1$s view";
    public static final String MESSAGE_SUCCESS = "Switched to view calendar by %1$s";

    private final String calendarViewPage;

    public ViewCalendarByCommand(String calendarViewPage) {
        this.calendarViewPage = calendarViewPage;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!isCalendarViewPageDifferent()) {
            throw new CommandException(String.format(MESSAGE_NO_CHANGE_IN_CALENDARVIEW, calendarViewPage));
        }
        EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(calendarViewPage));
        return new CommandResult(String.format(MESSAGE_SUCCESS, calendarViewPage));
    }

    /**
     * @return true if user attempts to change calendar view to a different view
     */
    private boolean isCalendarViewPageDifferent() {
        CalendarView calendarView = model.getCelebCalendarView();
        PageBase currentCalendarViewPage = calendarView.getSelectedPage();
        if (calendarViewPage.contentEquals("day") && currentCalendarViewPage == calendarView.getDayPage()) {
            return false;
        }
        if (calendarViewPage.contentEquals("week") && currentCalendarViewPage == calendarView.getWeekPage()) {
            return false;
        }
        if (calendarViewPage.contentEquals("month") && currentCalendarViewPage == calendarView.getMonthPage()) {
            return false;
        }
        if (calendarViewPage.contentEquals("year") && currentCalendarViewPage == calendarView.getYearPage()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.calendarViewPage.equals(((ViewCalendarByCommand) other).calendarViewPage)); // state check
    }
}
