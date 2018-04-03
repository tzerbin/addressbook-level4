package seedu.address.logic.commands.calendar;

import static seedu.address.model.ModelManager.CELEBRITY_TAG;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ChangeCalendarRequestEvent;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;

//@@author WJY-norainu
/**
 * Display the calendar of the celebrity specified by the user.
 */
public class ViewCalendarCommand extends Command {

    public static final String COMMAND_WORD = "viewCalendar";
    public static final String COMMAND_ALIAS = "vc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays the calendar of the celebrity"
            + " identified by the index number used in the last person listing.\n"
            + "Parameter: INDEX (must be a positive integer and the person at the index must be a celebrity)\n"
            + "Example: " + COMMAND_WORD + " 6";

    public static final String MESSAGE_NO_CHANGE_IN_CALENDAR = "The calendar shown currently is already %1$s's";
    public static final String MESSAGE_SUCCESS = "Switched to show %1$s's calendar";
    public static final String MESSAGE_NOT_CELEBRITY = "The person at the given index is not a celebrity and has "
            + "no calendar to show";

    private final Index targetIndex;

    public ViewCalendarCommand(Index index) {
        this.targetIndex = index;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToShowCalendar = lastShownList.get(targetIndex.getZeroBased());
        // person at the given index is not a celebrity
        if (!personToShowCalendar.hasTag(CELEBRITY_TAG)) {
            throw new CommandException(MESSAGE_NOT_CELEBRITY);
        }

        Celebrity celebrityToShowCalendar = (Celebrity) personToShowCalendar;
        // the celebrity's calendar is currently being shown
        if (celebrityToShowCalendar == model.getCurrentCelebCalendarOwner()
                && !model.getIsListingAppointments()) {
            throw new CommandException((String.format(MESSAGE_NO_CHANGE_IN_CALENDAR,
                    celebrityToShowCalendar.getName().toString())));
        }

        model.setCelebCalendarOwner(celebrityToShowCalendar);
        EventsCenter.getInstance().post(new ChangeCalendarRequestEvent(celebrityToShowCalendar.getCelebCalendar()));
        // if it's switching from appointment list view to calendar
        if (model.getIsListingAppointments()) {
            EventsCenter.getInstance().post(new ShowCalendarEvent());
            model.setIsListingAppointments(false);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, celebrityToShowCalendar.getName().toString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewCalendarCommand // instanceof handles nulls
                && this.targetIndex.equals(((ViewCalendarCommand) other).targetIndex)); // state check
    }
}
