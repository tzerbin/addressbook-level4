package seedu.address.logic.commands.calendar;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ChangeCalendarRequestEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Celebrity;

/**
 * Display the calendar of the celebrity specified by the user.
 */
public class ViewCalendarCommand extends Command {

    public static final String COMMAND_WORD = "viewCalendar";
    public static final String COMMAND_ALIAS = "vc";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays the calendar of the celebrity specified."
            + "The parameter is case-sensitive.\n"
            + "Parameter: CELEBRITY_KEYWORD"
            + "Example: " + COMMAND_WORD + " Jay Chou";

    public static final String MESSAGE_NO_CHANGE_IN_CALENDAR = "The calendar shown currently is already %1$s's";
    public static final String MESSAGE_SUCCESS = "Switched to show %1$s's calendar";
    public static final String MESSAGE_NON_UNIQUE_NAME = "There are more than 1 celebrities whose names contain %1$s";
    public static final String MESSAGE_NO_SUCH_CELEBRITY = "There is no celebrity whose name contains %1$s";

    private final String keywords;

    public ViewCalendarCommand(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<Celebrity> matchedCelebrities = model.getCelebrityWithName(keywords);
        if (matchedCelebrities.size() == 0) {
            throw new CommandException(String.format(MESSAGE_NO_SUCH_CELEBRITY, keywords));
        } else if (matchedCelebrities.size() > 1) {
            throw new CommandException(String.format(MESSAGE_NON_UNIQUE_NAME, keywords));
        }

        Celebrity celebrity = matchedCelebrities.get(0);
        if (celebrity.getName().toString().contentEquals(model.getCurrentCelebCalendarOwner())) {
            throw new CommandException((String.format(MESSAGE_NO_CHANGE_IN_CALENDAR, celebrity.getName().toString())));
        }

        model.setCelebCalendarOwner(celebrity.getName().toString());
        EventsCenter.getInstance().post(new ChangeCalendarRequestEvent(celebrity));
        return new CommandResult(String.format(MESSAGE_SUCCESS, celebrity.getName().toString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ViewCalendarCommand // instanceof handles nulls
                && this.keywords.contentEquals(((ViewCalendarCommand) other).keywords)); // state check
    }
}
