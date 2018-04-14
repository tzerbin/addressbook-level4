package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import seedu.address.logic.commands.calendar.AddAppointmentCommand;
import seedu.address.model.appointment.Appointment;

/**
 * A util class for Appointments
 */
public class AppointmentUtil {

    /**
     * Returns an add command string for adding the {@code appointment}.
     */
    public static String getAddAppointmentCommand(Appointment appointment) {
        return AddAppointmentCommand.COMMAND_WORD + " " + getAppointmentDetails(appointment);
    }

    /**
     * Returns the part of command string for the given {@code appointment}'s details.
     */
    public static String getAppointmentDetails(Appointment appointment) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + appointment.getTitle() + " ");
        sb.append(PREFIX_LOCATION + appointment.getMapAddress().value + " ");
        sb.append(PREFIX_START_TIME + appointment.getStartTime().format(Appointment.TIME_FORMAT) + " ");
        sb.append(PREFIX_START_DATE + appointment.getStartDate().format(Appointment.DATE_FORMAT) + " ");
        sb.append(PREFIX_END_TIME + appointment.getEndTime().format(Appointment.TIME_FORMAT) + " ");
        sb.append(PREFIX_END_DATE + appointment.getEndDate().format(Appointment.DATE_FORMAT) + " ");
        return sb.toString();
    }
}
