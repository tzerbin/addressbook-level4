package seedu.address.logic.commands.calendar;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.ModelStub;
import seedu.address.testutil.TypicalCelebrities;
// @@author muruges95

public class AddAppointmentCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Set<Index> emptyCelebrityIndices = new HashSet<>();
    private Set<Index> emptyPointOfContactIndices = new HashSet<>();


    @Test
    public void constructor_nullAppointment_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddAppointmentCommand(null, emptyCelebrityIndices, emptyPointOfContactIndices);
    }

    @Test
    public void execute_appointmentAcceptedByModel_addSuccessful() throws CommandException {
        ModelStubAcceptingAppointmentAdded modelStub = new ModelStubAcceptingAppointmentAdded();
        Appointment validAppointment = new AppointmentBuilder().build();

        CommandResult commandResult = getAddAppointmentCommandForAppointment(validAppointment, modelStub).execute();
        assertEquals(String.format(AddAppointmentCommand.MESSAGE_SUCCESS, validAppointment.getTitle()),
                commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validAppointment), modelStub.appointmentsAdded);
    }

    @Test
    public void execute_addAppointmentWhileNotInCombinedCalendarView_throwsCommandException() throws CommandException {
        ModelStub modelStub = new ModelStubThrowingNotInCombinedCalendarViewException();
        Appointment validAppointment = new AppointmentBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(String.format(AddAppointmentCommand.MESSAGE_NOT_IN_COMBINED_CALENDAR,
                modelStub.getCurrentCelebCalendarOwner().getName().toString()));

        getAddAppointmentCommandForAppointment(validAppointment, modelStub).execute();
    }

    @Test
    public void equals() {
        Appointment dentistAppointment = new AppointmentBuilder().withName("Dentist Appointment").build();
        Appointment doctorAppointment = new AppointmentBuilder().withName("Doctor Appointment").build();
        AddAppointmentCommand addDentistApptCommand = new AddAppointmentCommand(dentistAppointment,
                emptyCelebrityIndices, emptyPointOfContactIndices);
        AddAppointmentCommand addDoctorApptCommand = new AddAppointmentCommand(doctorAppointment,
                emptyCelebrityIndices, emptyPointOfContactIndices);

        // same object -> return true
        assertTrue(addDentistApptCommand.equals(addDentistApptCommand));

        // same values -> return true
        AddAppointmentCommand addDentistApptCommandCopy = new AddAppointmentCommand(dentistAppointment,
                emptyCelebrityIndices, emptyPointOfContactIndices);
        assertTrue(addDentistApptCommand.equals(addDentistApptCommandCopy));

        // different types -> returns false
        assertFalse(addDentistApptCommand.equals(1));

        // null -> returns false
        assertFalse(addDentistApptCommand.equals(null));

        // different appointment -> returns false
        assertFalse(addDentistApptCommand.equals(addDoctorApptCommand));
    }

    /**
     * Generates a new AddAppointmentCommand with the details of the given appointment.
     */
    private AddAppointmentCommand getAddAppointmentCommandForAppointment(Appointment appointment, Model model) {
        AddAppointmentCommand command = new AddAppointmentCommand(appointment, emptyCelebrityIndices,
                emptyPointOfContactIndices);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    private class ModelStubThrowingNotInCombinedCalendarViewException extends ModelStub {

        @Override
        public Celebrity getCurrentCelebCalendarOwner() {
            return TypicalCelebrities.AYANE;
        }
    }

    /**
     * A Model stub that always accept the appointment being added.
     */
    private class ModelStubAcceptingAppointmentAdded extends ModelStub {
        final ArrayList<Appointment> appointmentsAdded = new ArrayList<>();

        @Override
        public void addAppointmentToStorageCalendar(Appointment appt) {
            requireNonNull(appt);
            appointmentsAdded.add(appt);
        }

        @Override
        public Celebrity getCurrentCelebCalendarOwner() {
            return null;
        }

        @Override
        public List<Celebrity> getCelebritiesChosen(Set<Index> celebrityIndices) {
            return new ArrayList<>();
        }

        @Override
        public List<Person> getPointsOfContactChosen(Set<Index> pocIndices) {
            return new ArrayList<>();
        }

        @Override
        public void setBaseDate(LocalDate startDate) {

        }

        @Override
        public void setCelebCalendarViewPage(String page) {

        }

        @Override
        public void setIsListingAppointments(boolean isListing) {

        }

        @Override
        public boolean getIsListingAppointments() {
            return false;
        }
    }
}
