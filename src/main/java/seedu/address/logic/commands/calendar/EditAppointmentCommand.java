package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_START_DATE_TIME_NOT_BEFORE_END_DATE_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CELEBRITY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POINT_OF_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.model.ModelManager.DAY_VIEW_PAGE;
import static seedu.address.model.appointment.Appointment.isDateTimeNotValid;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.map.MapAddress;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicateAppointmentException;

//@@author muruges95
/**
 * Edits an appointment in a calendar.
 */
public class EditAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "editAppointment";
    public static final String COMMAND_ALIAS = "ea";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an appointment.\n"
            + "Parameters: APPOINTMENT_INDEX (must be a positive integer)"
            + "[" + PREFIX_NAME + "APPOINTMENT NAME] "
            + "[" + PREFIX_START_TIME + "START TIME] "
            + "[" + PREFIX_START_DATE + "START DATE] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_END_TIME + "END TIME] "
            + "[" + PREFIX_END_DATE + "END DATE] "
            + "[" + PREFIX_CELEBRITY + "CELEBRITY_INDEX]... "
            + "[" + PREFIX_POINT_OF_CONTACT + "POINT_OF_CONTACT_INDEX]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Oscars 2018 "
            + PREFIX_START_TIME + "18:00 "
            + PREFIX_START_DATE + "23-04-2018 "
            + PREFIX_LOCATION + "Hollywood "
            + PREFIX_END_TIME + "20:00 "
            + PREFIX_END_DATE + "23-04-2018 "
            + PREFIX_CELEBRITY + "1 "
            + PREFIX_CELEBRITY + "2 "
            + PREFIX_POINT_OF_CONTACT + "3 "
            + PREFIX_POINT_OF_CONTACT + "4 ";

    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the application,"
            + " or the edited fields are the same as the original.";
    public static final String MESSAGE_SUCCESS = "Edited appointment: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided";

    private final Index appointmentIndex;
    private final EditAppointmentDescriptor editAppointmentDescriptor;

    private Appointment appointmentToEdit;

    public EditAppointmentCommand(Index appointmentIndex, EditAppointmentDescriptor editAppointmentDescriptor) {
        requireNonNull(appointmentIndex);
        requireNonNull(editAppointmentDescriptor);

        this.appointmentIndex = appointmentIndex;
        this.editAppointmentDescriptor = new EditAppointmentDescriptor(editAppointmentDescriptor);
    }

    @Override
    public CommandResult execute() throws CommandException {
        if (!model.getIsListingAppointments()) {
            throw new CommandException(Messages.MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS);
        }
        try {
            appointmentToEdit = model.getChosenAppointment(appointmentIndex.getZeroBased());
            Appointment editedAppointment = createEditedAppointment(appointmentToEdit, editAppointmentDescriptor);

            // either use existing celebrity list or get new one
            List<Celebrity> celebrityList = (editAppointmentDescriptor.getCelebrityIndices().isPresent())
                    ? model.getCelebritiesChosen(editAppointmentDescriptor.getCelebrityIndices().get())
                    : appointmentToEdit.getCelebrities();

            List<Person> pointOfContactList = (editAppointmentDescriptor.getPointOfContactIndices().isPresent())
                    ? model.getPointsOfContactChosen(editAppointmentDescriptor.getPointOfContactIndices().get())
                    : appointmentToEdit.getPointOfContactList();
            model.addAppointmentToStorageCalendar(editedAppointment);
            appointmentToEdit.removeAppointment();
            editedAppointment.updateEntries(celebrityList, pointOfContactList);

            // reset calendar view to day view
            model.setCelebCalendarViewPage(DAY_VIEW_PAGE);
            EventsCenter.getInstance().post(new ChangeCalendarViewPageRequestEvent(DAY_VIEW_PAGE));
            if (model.getIsListingAppointments()) {
                model.setIsListingAppointments(false);
                EventsCenter.getInstance().post(new ShowCalendarEvent());
            }
            return new CommandResult(String.format(MESSAGE_SUCCESS, editedAppointment.getTitle()));
        } catch (IndexOutOfBoundsException iobe) {
            throw new CommandException(MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        } catch (ParseException pe) {
            throw new CommandException(MESSAGE_START_DATE_TIME_NOT_BEFORE_END_DATE_TIME);
        } catch (DuplicateAppointmentException dae) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        }

    }

    /**
     * Creates and returns a {@code Appointment} with the details of {@code apptToEdit}
     * edited with {@code ead}.
     */
    public static Appointment createEditedAppointment(Appointment apptToEdit, EditAppointmentDescriptor ead)
            throws ParseException {
        assert apptToEdit != null;

        String apptName = ead.getAppointmentName().orElse(apptToEdit.getTitle());
        LocalTime startTime = ead.getStartTime().orElse(apptToEdit.getStartTime());
        LocalTime endTime = ead.getEndTime().orElse(apptToEdit.getEndTime());
        LocalDate startDate = ead.getStartDate().orElse(apptToEdit.getStartDate());
        LocalDate endDate = ead.getEndDate().orElse(apptToEdit.getEndDate());
        MapAddress location = ead.getLocation().orElse(apptToEdit.getMapAddress());

        if (isDateTimeNotValid(startDate, endDate, startTime, endTime)) {
            throw new ParseException(MESSAGE_START_DATE_TIME_NOT_BEFORE_END_DATE_TIME);
        }
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
        private Set<Index> celebrityIndices;
        private Set<Index> pointOfContactIndices;

        // for JUnit Tests
        private List<Long> celebIds;
        private List<Long> pointOfContactIds;

        public EditAppointmentDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code celebrityIndices} is used internally.
         */
        public EditAppointmentDescriptor(EditAppointmentDescriptor toCopy) {
            setAppointmentName(toCopy.appointmentName);
            setLocation(toCopy.location);
            setStartTime(toCopy.startTime);
            setStartDate(toCopy.startDate);
            setEndTime(toCopy.endTime);
            setEndDate(toCopy.endDate);
            setCelebrityIndices(toCopy.celebrityIndices);
            setPointOfContactIndices(toCopy.pointOfContactIndices);
            setCelebIds(toCopy.celebIds);
            setPointOfContactIds(toCopy.pointOfContactIds);
        }

        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.appointmentName, this.startTime, this.startDate,
                    this.endTime, this.endDate, this.location, this.celebrityIndices, this.pointOfContactIndices);
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

        public Optional<MapAddress> getLocation() {
            return Optional.ofNullable(location);
        }

        /**
         * Sets {@code celebrityIndices} to this object's {@code celebrityIndices}.
         * A defensive copy of {@code celebrityIndices} is used internally.
         */
        public void setCelebrityIndices(Set<Index> celebrityIndices) {
            this.celebrityIndices = (celebrityIndices != null) ? new HashSet<>(celebrityIndices) : null;
        }

        /**
         * Returns an unmodifiable celebrities set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code celebrityIndices} is null.
         */
        public Optional<Set<Index>> getCelebrityIndices() {
            return (celebrityIndices != null) ? Optional.of(Collections.unmodifiableSet(celebrityIndices))
                                              : Optional.empty();
        }

        /**
         * Sets {@code pointOfContactIndices} to this object's {@code pointOfContactIndices}.
         * A defensive copy of {@code pointOfContactIndices} is used internally.
         */
        public void setPointOfContactIndices(Set<Index> pointOfContactIndices) {
            this.pointOfContactIndices = (pointOfContactIndices != null) ? new HashSet<>(pointOfContactIndices) : null;
        }

        /**
         * Returns an unmodifiable points of contact set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code pointOfContactIndices} is null.
         */
        public Optional<Set<Index>> getPointOfContactIndices() {
            return (pointOfContactIndices != null) ? Optional.of(Collections.unmodifiableSet(pointOfContactIndices))
                    : Optional.empty();
        }

        /**
         * Sets {@code celebIds} to this object's {@code celebIds}.
         * A defensive copy of {@code celebIds} is used internally.
         */
        public void setCelebIds(List<Long> celebIds) {
            this.celebIds = (celebIds != null) ? new ArrayList<>(celebIds) : null;
        }

        /**
         * Returns an unmodifiable celebrities id list, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code celebIds} is null.
         */
        public Optional<List<Long>> getCelebIds() {
            return (this.celebIds != null)
                    ? Optional.of(Collections.unmodifiableList(celebIds))
                    : Optional.empty();
        }

        /**
         * Sets {@code pointOfContactIds} to this object's {@code pointOfContactIds}.
         * A defensive copy of {@code pointOfContactIds} is used internally.
         */
        public void setPointOfContactIds(List<Long> pointOfContactIds) {
            this.pointOfContactIds = (pointOfContactIds != null) ? new ArrayList<>(pointOfContactIds) : null;
        }

        /**
         * Returns an unmodifiable points of contact id list, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code pointOfContactIds} is null.
         */
        public Optional<List<Long>> getPointOfContactIds() {
            return (this.pointOfContactIds != null)
                    ? Optional.of(Collections.unmodifiableList(pointOfContactIds))
                    : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            } else if (!(other instanceof EditAppointmentDescriptor)) {
                return false;
            } else {

                EditAppointmentDescriptor e = (EditAppointmentDescriptor) other;

                return getAppointmentName().equals(e.getAppointmentName())
                        && getStartDate().equals(e.getStartDate())
                        && getEndDate().equals(e.getEndDate())
                        && getStartTime().equals(e.getStartTime())
                        && getEndTime().equals(e.getEndTime())
                        && getLocation().equals(e.getLocation())
                        && getCelebrityIndices().equals(e.getCelebrityIndices())
                        && getPointOfContactIndices().equals(e.getPointOfContactIndices())
                        && getCelebIds().equals(e.getCelebIds())
                        && getPointOfContactIndices().equals(e.getPointOfContactIndices());
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof EditAppointmentCommand)) {
            return false;
        } else {
            EditAppointmentCommand e = (EditAppointmentCommand) other;
            return appointmentIndex.equals(e.appointmentIndex)
                    && editAppointmentDescriptor.equals(e.editAppointmentDescriptor)
                    && Objects.equals(appointmentToEdit, e.appointmentToEdit);
        }
    }

}
