package seedu.address.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;

import javafx.collections.ObservableList;
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

    /** Fetches CalendarSource containing the StorageCalendar */
    CalendarSource getStorageCalendarSource();

    /** Returns a String that represents the current calendar view page. */
    String getCurrentCelebCalendarViewPage();

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns the celebrity whose calendar is currently shown.
     *  If current calendar is a combined view, {@code null} will be returned.
     */
    Celebrity getCurrentCelebCalendarOwner();

    /** Returns the last displayed appointment list */
    List<Appointment> getAppointmentList();

    /** Sets the appointment list to be the last displayed appointment list */
    void setAppointmentList(List<Appointment> appointments);

    /**
     * Returns the chosen appointment from the displayed appointment list
     * based on zeroBasedIndex
     * @throws CommandException if appointments not listed or invalid index
     */
    Appointment getChosenAppointment(int chosenIndex) throws CommandException;

    /** Adds the given appointment to the internal StorageCalendar */
    void addAppointmentToStorageCalendar(Appointment appt);

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

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
}
