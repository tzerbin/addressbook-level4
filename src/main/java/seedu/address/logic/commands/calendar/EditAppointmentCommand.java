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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.commons.events.ui.ShowCalendarEvent;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.map.MapAddress;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;

/**
 * Edits an appointment in a calendar.
 */
public class EditAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "editap";
    public static final String COMMAND_ALIAS = "ea";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an appointment. "
            + "Parameters: APPOINTMENT_INDEX (must be a positive integer)"
            + "[" + PREFIX_NAME + "APPOINTMENT NAME] "
            + "[" + PREFIX_START_TIME + "START TIME] "
            + "[" + PREFIX_START_DATE + "START DATE] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_END_TIME + "END TIME] "
            + "[" + PREFIX_END_DATE + "END DATE]"
            + "[" + PREFIX_CELEBRITY + "CELEBRITY_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Oscars 2018 "
            + PREFIX_START_TIME + "18:00 "
            + PREFIX_START_DATE + "23-04-2018 "
            + PREFIX_LOCATION + "Hollywood "
            + PREFIX_END_TIME + "20:00 "
            + PREFIX_END_DATE + "23-04-2018 "
            + PREFIX_CELEBRITY + "1 "
            + PREFIX_CELEBRITY + "2";

    public static final String MESSAGE_SUCCESS = "Edited appointment successfully";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided";

    private final Index appointmentIndex;
    private final EditAppointmentDescriptor editAppointmentDescriptor;

    public EditAppointmentCommand(Index appointmentIndex, EditAppointmentDescriptor editAppointmentDescriptor) {
        requireNonNull(appointmentIndex);
        requireNonNull(editAppointmentDescriptor);

        this.appointmentIndex = appointmentIndex;
        this.editAppointmentDescriptor = editAppointmentDescriptor;
    }

    @Override
    public CommandResult execute() throws CommandException {
        Appointment appointmentToEdit = model.getChosenAppointment(appointmentIndex.getZeroBased());
        Appointment editedAppointment = createEditedAppointment(appointmentToEdit, editAppointmentDescriptor);
        appointmentToEdit.removeAppointment();
        model.addAppointmentToStorageCalendar(editedAppointment);


        // reset calendar view to day view
        model.setCelebCalendarViewPage(DAY_VIEW_PAGE);
        EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(DAY_VIEW_PAGE));
        if (model.getIsListingAppointments()) {
            model.setIsListingAppointments(false);
            EventsCenter.getInstance().post(new ShowCalendarEvent());
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }

    /**
     * Creates and returns a {@code Appointment} with the details of {@code apptToEdit}
     * edited with {@code ead}.
     */
    public static Appointment createEditedAppointment(Appointment apptToEdit, EditAppointmentDescriptor ead) {
        assert apptToEdit != null;

        String apptName = ead.getAppointmentName().orElse(apptToEdit.getTitle());
        LocalTime startTime = ead.getStartTime().orElse(apptToEdit.getStartTime());
        LocalTime endTime = ead.getEndTime().orElse(apptToEdit.getEndTime());
        LocalDate startDate = ead.getStartDate().orElse(apptToEdit.getStartDate());
        LocalDate endDate = ead.getEndDate().orElse(apptToEdit.getEndDate());
        MapAddress location = ead.getAddress().orElse(apptToEdit.getMapAddress());

        return new Appointment(apptName, startTime, startDate, location, endTime, endDate);
    }

    /**
     * Stores the details to edit an appointment with. Each non-empty field value will replace
     * the corresponding field value of the person.
     */
    public static class EditAppointmentDescriptor {
        private String appointmentName;
        private LocalTime startTime;
        private LocalTime endTime;
        private LocalDate startDate;
        private LocalDate endDate;
        private MapAddress location;

        public EditAppointmentDescriptor() {}

        public EditAppointmentDescriptor(EditAppointmentDescriptor toCopy) {

        }

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.appointmentName, this.startTime, this.startDate,
                    this.endTime, this.endDate, this.location);
        }

        public void setAppointmentName(String appointmentName) {
            this.appointmentName = appointmentName;
        }

        public Optional<String> getAppointmentName() {
            return Optional.ofNullable(appointmentName);
        }

        public void setStartTime(LocalTime startTime) {
            this.startTime = startTime;
        }

        public Optional<LocalTime> getStartTime() {
            return Optional.ofNullable(startTime);
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public Optional<LocalDate> getStartDate() {
            return Optional.ofNullable(startDate);
        }

        public void setEndTime(LocalTime endTime) {
            this.endTime = endTime;
        }

        public Optional<LocalTime> getEndTime() {
            return Optional.ofNullable(endTime);
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public Optional<LocalDate> getEndDate() {
            return Optional.ofNullable(endDate);
        }

        public void setLocation(MapAddress location) {
            this.location = location;
        }

        public Optional<MapAddress> getAddress() {
            return Optional.ofNullable(location);
        }

    }

    private static List<Celebrity> getCelebrities(Set<Index> indices, List<Person> personList) throws CommandException {
        List<Celebrity> celebrities = new ArrayList<>();
        for (Index index : indices) {
            celebrities.add(getCelebrity(index, personList));
        }
        return celebrities;
    }

    private static Celebrity getCelebrity(Index index, List<Person> personList) throws CommandException {
        int zeroBasedIndex = index.getZeroBased();
        if (zeroBasedIndex >= personList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } else if (!personList.get(zeroBasedIndex).isCelebrity()) {
            throw new CommandException(Messages.MESSAGE_NOT_CELEBRITY_INDEX);
        } else {
            return (Celebrity) personList.get(zeroBasedIndex);
        }
    }

}
