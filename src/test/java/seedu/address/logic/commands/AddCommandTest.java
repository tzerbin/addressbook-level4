package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = getAddCommandForPerson(validPerson, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Person validPerson = new PersonBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validPerson, modelStub).execute();
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given person.
     */
    private AddCommand getAddCommandForPerson(Person person, Model model) {
        AddCommand command = new AddCommand(person);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addCelebrity(Person celebrity) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public ArrayList<Celebrity> getCelebrities() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public List<Appointment> getAppointmentList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Calendar> getCelebCalendars() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public CalendarSource getCelebCalendarSource() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public StorageCalendar getStorageCalendar() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public String getCurrentCelebCalendarViewPage() {
            fail("This  method should not be called.");
            return null;
        }

        @Override
        public Celebrity getCurrentCelebCalendarOwner() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public LocalDate getBaseDate() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void setBaseDate(LocalDate date) {
            fail("This method should not be called.");
        }

        @Override
        public boolean getIsListingAppointments() {
            fail("This method should not be called.");
            return false;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public List<Celebrity> getCelebritiesChosen(Set<Index> indices) throws CommandException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public Celebrity getCelebrityChosen(Index index) throws CommandException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public int countPersonsWithTag(Tag tag) {
            fail("This method should not be called.");
            return -1;
        }

        @Override
        public int removeTag(Tag tag) {
            fail("This method should not be called.");
            return -1;
        }

        @Override
        public void setAppointmentList(List<Appointment> appointments) {
            fail("This method should not be called.");
        }

        @Override
        public Appointment getChosenAppointment(int chosenIndex) throws CommandException {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addAppointmentToStorageCalendar(Appointment appt) {
            fail("This method should not be called.");
        }

        @Override
        public void deleteAppointmentFromStorageCalendar(int index) throws CommandException {
            fail("This method should not be called");
        }

        @Override
        public Appointment removeAppointmentFromInternalList(int index) {
            fail("This method should not be called");
            return null;
        }

        @Override
        public void setCelebCalendarViewPage(String newCurrentCelebCalendarViewPage) {
            fail("This method should not be called.");
        }

        @Override
        public void setCelebCalendarOwner(Celebrity newCurrentCelebCalendarOwner) {
            fail("This method should not be called");
        }

        @Override
        public void setIsListingAppointments(boolean isListingAppointments) {
            fail("This method should not be called");
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to add a person.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            throw new DuplicatePersonException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
