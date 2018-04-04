package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CELEBRITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;

import java.util.Set;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;

//@@author muruges95
/**
 * Adds an appointment to a calendar.
 */
public class AddAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "addAppointment";
    public static final String COMMAND_ALIAS = "aa";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment. "
            + "Parameters: "
            + PREFIX_NAME + "APPOINTMENT NAME "
            + "[" + PREFIX_START_TIME + "START TIME] "
            + "[" + PREFIX_START_DATE + "START DATE] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_END_TIME + "END TIME] "
            + "[" + PREFIX_END_DATE + "END DATE]"
            + "[" + PREFIX_CELEBRITY + "CELEBRITY_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Oscars 2018 "
            + PREFIX_START_TIME + "18:00 "
            + PREFIX_START_DATE + "23-04-2018 "
            + PREFIX_LOCATION + "Hollywood "
            + PREFIX_END_TIME + "20:00 "
            + PREFIX_END_DATE + "23-04-2018 "
            + PREFIX_CELEBRITY + "1 "
            + PREFIX_CELEBRITY + "2";

    public static final String MESSAGE_NOT_IN_COMBINED_CALENDAR = "Can only add appointment when "
            + "viewing combined calendar\n"
            + "currently viewing %1$s's calendar";
    public static final String MESSAGE_SUCCESS = "Added appointment successfully";

    private final Appointment appt;
    private final Set<Index> celebrityIndices;
    private final Set<Index> pointOfContactIndices;

    /**
     * Creates an AddAppointmentCommand with the following parameter
     * @param appt The created appointment
     * @param celebrityIndices The indices of the celebrities who are part of this appointment
     */
    public AddAppointmentCommand(Appointment appt, Set<Index> celebrityIndices, Set<Index> pointOfContactIndices) {
        requireNonNull(appt);
        this.appt = appt;
        this.celebrityIndices = celebrityIndices;
        this.pointOfContactIndices = pointOfContactIndices;
    }


    @Override
    public CommandResult execute() throws CommandException {
        if (model.getCurrentCelebCalendarOwner() != null) {
            throw new CommandException(
                    String.format(MESSAGE_NOT_IN_COMBINED_CALENDAR,
                            model.getCurrentCelebCalendarOwner().getName().toString()));
        }

        appt.updateEntries(
                model.getCelebritiesChosen(celebrityIndices),
                model.getPointsOfContactChosen(pointOfContactIndices));
        model.addAppointmentToStorageCalendar(appt);

        // reset calendar view to day view
        model.setCelebCalendarViewPage(DAY_VIEW_PAGE);
        EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(DAY_VIEW_PAGE));
        if (model.getIsListingAppointments()) {
            model.setIsListingAppointments(false);
            EventsCenter.getInstance().post(new ShowCalendarEvent());
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddAppointmentCommand
                && appt.equalsValue(((AddAppointmentCommand) other).appt));
    }


}
