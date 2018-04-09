package seedu.address.model;

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
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyAddressBook newData);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /** Adds the given celebrity */
    void addCelebrity(Person person) throws DuplicatePersonException;

    /** Gets the list of celebrities */
    ArrayList<Celebrity> getCelebrities();

    /** Deletes the given person. */
    void deletePerson(Person target) throws PersonNotFoundException;

    /** Adds the given person */
    void addPerson(Person person) throws DuplicatePersonException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException;

    /** Fetches the list of CelebCalendars. */
    ObservableList<Calendar> getCelebCalendars();

    /** Fetches the CalendarSource of the CelebCalendars. */
    CalendarSource getCelebCalendarSource();

    /** Fetches the StorageCalendar used to store Appointments */
    StorageCalendar getStorageCalendar();

    /** Returns a String that represents the current calendar view page. */
    String getCurrentCelebCalendarViewPage();

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns the celebrity whose calendar is currently shown.
     *  If current calendar is a combined view, {@code null} will be returned.
     */
    Celebrity getCurrentCelebCalendarOwner();

    /** Returns the current base date of the calendar. */
    LocalDate getBaseDate();

    /** Sets the current base date of the calendar to the specified date. */
    void setBaseDate(LocalDate date);

    /** Returns the full appointment list that contains all appointments */
    List<Appointment> getAppointmentList();

    /** Returns stored appointment list in storage calendar */
    List<Appointment> getStoredAppointmentList();

    /** Returns the currently displayed appointment list */
    List<Appointment> getCurrentlyDisplayedAppointments();

    /** Sets the currently displayed appointment list to be specified list */
    void setCurrentlyDisplayedAppointments(List<Appointment> appointments);

    /**
     * Returns the chosen appointment from the displayed appointment list
     * based on zeroBasedIndex
     * @throws CommandException if appointments not listed or invalid index
     */
    Appointment getChosenAppointment(int chosenIndex) throws CommandException;

    /** Adds the given appointment to the internal StorageCalendar */
    void addAppointmentToStorageCalendar(Appointment appt);

    /** Removes appointment from internal StorageCalendar based on zero-based index
     *  Returns the removed appointment
     */
    Appointment deleteAppointment(int index) throws IndexOutOfBoundsException;

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns the list of celebrities chosen based on their index
     * @throws CommandException if any of the indices given is invalid or the person is not a celebrity
     */
    List<Celebrity> getCelebritiesChosen(Set<Index> indices) throws CommandException;

    /**
     * Returns the Celebrity at a particular index given the current personList
     * @throws CommandException if the given index is invalid or the person is not a celebrity.
     */
    Celebrity getCelebrityChosen(Index index) throws CommandException;

    /**
     * Returns the pointsOfContact list chosen based on their index
     * @throws CommandException if any of the indices given is invalid or the person is a celebrity
     */
    List<Person> getPointsOfContactChosen(Set<Index> indices) throws CommandException;

    /**
     * Returns the Person at a particular index given the current personList
     * @throws CommandException if the given index is invalid or the person is a celebrity.
     */
    Person getPointOfContactChosen(Index index) throws CommandException;

    /** Counts the number of {@code persons} with the given {@code tage}. */
    int countPersonsWithTag(Tag tag);

    /** Removes the given {@code tag} from all {@code person}s. */
    int removeTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException, TagNotFoundException;

    /** Changes the currentCelebCalendarViewPage. */
    void setCelebCalendarViewPage(String newCurrentCelebCalendarViewPage);

    /** Changes the currentCelebCalendarOwner. */
    void setCelebCalendarOwner(Celebrity celerity);

    /** Returns true if calendarPanel is currently displaying appointment list. */
    boolean getIsListingAppointments();

    /** Changes isListAppointments value accordingly. */
    void setIsListingAppointments(boolean isListingAppointments);

    /**
     * Associates each appointment with the relevant celebrities based on the ids they contain
     */
    void associateAppointmentsWithCelebritiesAndPointsOfContact();
}
