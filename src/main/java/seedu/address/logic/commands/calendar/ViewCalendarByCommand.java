package seedu.address.logic.commands.calendar;

import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;
import static seedu.address.model.ModelManager.MONTH_VIEW_PAGE;
import static seedu.address.model.ModelManager.WEEK_VIEW_PAGE;
import static seedu.address.model.ModelManager.YEAR_VIEW_PAGE;

import java.util.Arrays;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Switches the calendar view to another view specified by the user.
 */
public class ViewCalendarByCommand extends Command {

    public static final String COMMAND_WORD = "viewCalendarBy";
    public static final String COMMAND_ALIAS = "vcb";
    public static final String[] VALID_ARGUMENT = {DAY_VIEW_PAGE, WEEK_VIEW_PAGE, MONTH_VIEW_PAGE, YEAR_VIEW_PAGE};

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switches to the calendar view specified.\n"
            + "Parameter: VIEW (must be one of "
            + Arrays.toString(VALID_ARGUMENT)
            + ". Parameter is not case-sensitive)\n"
            + "Example: " + COMMAND_WORD + " week";

    public static final String MESSAGE_NO_CHANGE_IN_CALENDARVIEW = "Calender is already in %1$s view";
    public static final String MESSAGE_SUCCESS = "Switched to view calendar by %1$s";

    private final String calendarViewPage;

    public ViewCalendarByCommand(String calendarViewPage) {
        this.calendarViewPage = calendarViewPage;
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (calendarViewPage.contentEquals(model.getCurrentCelebCalendarViewPage())) {
            throw new CommandException(String.format(MESSAGE_NO_CHANGE_IN_CALENDARVIEW, calendarViewPage));
        }
        model.setCelebCalendarViewPage(calendarViewPage);
        EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(calendarViewPage));
        return new CommandResult(String.format(MESSAGE_SUCCESS, calendarViewPage));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewCalendarByCommand // instanceof handles nulls
                && this.calendarViewPage.equals(((ViewCalendarByCommand) other).calendarViewPage)); // state check
    }
}
