package seedu.address.testutil;

import static org.junit.Assert.fail;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;

import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Generates a new AddCommand with the details of the given person.
 */
public class ModelStub implements Model {
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
    public List<Appointment> getCurrentlyDisplayedAppointments() {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void setCurrentlyDisplayedAppointments(List<Appointment> appointments) {
        fail("This method should not be called.");
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
    public List<Person> getPointsOfContactChosen(Set<Index> indices) throws CommandException {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public Person getPointOfContactChosen(Index index) throws CommandException {
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
    public Appointment getChosenAppointment(int chosenIndex) throws CommandException {
        fail("This method should not be called.");
        return null;
    }

    @Override
    public void addAppointmentToStorageCalendar(Appointment appt) {
        fail("This method should not be called.");
    }

    @Override
    public Appointment deleteAppointment(int index) throws IndexOutOfBoundsException {
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

    @Override
    public void associateAppointmentsWithCelebritiesAndPointsOfContact() {
        fail("This method should not be called");
    }
}
