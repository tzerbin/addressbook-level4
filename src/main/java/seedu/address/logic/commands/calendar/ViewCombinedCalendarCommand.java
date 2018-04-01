package seedu.address.logic.commands.calendar;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.commons.events.ui.ShowCombinedCalendarViewRequestEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Display the calendar of the celebrity specified by the user.
 */
public class ViewCombinedCalendarCommand extends Command {

    public static final String COMMAND_WORD = "viewCombinedCalendar";
    public static final String COMMAND_ALIAS = "vcc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays a combined view of calendars "
            + "of all celebrities.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_ALREADY_IN_COMBINED_VIEW = "The current calendar is already in combined view";
    public static final String MESSAGE_SUCCESS = "Switched to show combined calendar";

    public ViewCombinedCalendarCommand() {}

    @Override
    public CommandResult execute() throws CommandException {
        // view combined calendar when it's already in combined calendar
        if (model.getCurrentCelebCalendarOwner() == null
                && !model.getIsListingAppointments()) {
            throw new CommandException(MESSAGE_ALREADY_IN_COMBINED_VIEW);
        }

        model.setCelebCalendarOwner(null);
        EventsCenter.getInstance().post(new ShowCombinedCalendarViewRequestEvent());

        // if it's switching from appointment list view to calendar
        if (model.getIsListingAppointments()) {
            EventsCenter.getInstance().post(new ShowCalendarEvent());
            model.setIsListingAppointments(false);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
