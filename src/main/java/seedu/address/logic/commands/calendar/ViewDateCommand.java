package seedu.address.logic.commands.calendar;

import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;

import java.time.LocalDate;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.commons.events.ui.ShowCalendarBasedOnDateEvent;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;

//@@author WJY-norainu
/**
 * Display the calendar based on the date specified by the user.
 */
public class ViewDateCommand extends Command {

    public static final String COMMAND_WORD = "viewDate";
    public static final String COMMAND_ALIAS = "vd";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays the calendar which bases on the specified "
            + "date.\n"
            + "Parameter: [DATE] (goes back to current date when no parameter is given.)"
            + "DATE should be in YYYY-MM-DD format\n"
            + "Example: " + COMMAND_WORD + " 2018-04-23";

    public static final String MESSAGE_NO_CHANGE_IN_BASE_DATE = "The current calendar is already based on %1$s";
    public static final String MESSAGE_SUCCESS = "Switched to view calendar based on %1$s";

    private final LocalDate date;

    public ViewDateCommand(LocalDate date) {
        this.date = date;
    }

    @Override
    public CommandResult execute() throws CommandException {
        // view a date that is already the base
        if (model.getBaseDate().equals(date)
                && !model.getIsListingAppointments()) {
            throw new CommandException(String.format(MESSAGE_NO_CHANGE_IN_BASE_DATE, date.toString()));
        }

        model.setBaseDate(date);
        EventsCenter.getInstance().post(new ShowCalendarBasedOnDateEvent(date));
        model.setCelebCalendarViewPage(DAY_VIEW_PAGE);
        EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(DAY_VIEW_PAGE));

        // if it's switching from appointment list view to calendar
        if (model.getIsListingAppointments()) {
            EventsCenter.getInstance().post(new ShowCalendarEvent());
            model.setIsListingAppointments(false);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, date.toString()));
    }
}
