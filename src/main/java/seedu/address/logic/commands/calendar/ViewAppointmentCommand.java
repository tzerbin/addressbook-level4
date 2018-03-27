package seedu.address.logic.commands.calendar;

import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;

import java.time.LocalDate;
import java.util.List;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.map.ShowLocationCommand;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;
import seedu.address.model.map.MapAddress;

/**
 * Display the appointment details in the display panel
 * based on the index given
 */
public class ViewAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "viewAppointment";
    public static final String COMMAND_ALIAS = "va";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Show the appointment details based on index "
            + "Parameters: "
            + PREFIX_INDEX + "Index (Based on most recent appointment list generated). \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_INDEX + "1";

    public static final String MESSAGE_SUCCESS = "Selected appointment details (location shown in map):\n";

    private Appointment selectedAppointment;
    private int chosenIndex;
    private List<Appointment> listOfAppointment;
    private LocalDate endDate;
    private LocalDate startDate;

    /**
     * Takes in a zero-based index {@code index}
     * @param index
     */
    public ViewAppointmentCommand (int index) {
        chosenIndex = index;
        startDate = ListAppointmentCommand.getStartDate();
        endDate = ListAppointmentCommand.getEndDate();
    }

    @Override
    public CommandResult execute() throws CommandException {
        StorageCalendar storageCalendar = model.getStorageCalendar();
        listOfAppointment = storageCalendar.getAppointmentsWithinDate(startDate, endDate);
        selectedAppointment = listOfAppointment.get(chosenIndex);
        ShowLocationCommand showLocation = new ShowLocationCommand(
                new MapAddress(listOfAppointment.get(chosenIndex).getLocation()));
        showLocation.execute();
        return new CommandResult(MESSAGE_SUCCESS + getAppointmentDetailsResult());
    }

    private String getAppointmentDetailsResult () {
        return "Appointment Name: " + selectedAppointment.getTitle() + "\n"
                + "Start Date: " + selectedAppointment.getStartDate() + "\n"
                + "Start Time: " + selectedAppointment.getStartTime() + "\n"
                + "End Date: " + selectedAppointment.getEndDate() + "\n"
                + "End Time: " + selectedAppointment.getEndTime() + "\n"
                + "Location: " + selectedAppointment.getLocation();
    }
}
