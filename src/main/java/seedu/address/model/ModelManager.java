package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.model.calendar.StorageCalendar;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private static final String CELEB_CALENDAR_SOURCE_NAME  = "Celeb Calendar Source";
    private static final String STORAGE_CALENDAR_SOURCE_NAME = "Storage Calendar Source";

    private final AddressBook addressBook;
    private final FilteredList<Person> filteredPersons;
    private final CalendarSource celebCalendarSource;
    private final CalendarSource storageCalendarSource;

    private String currentCelebCalendarViewPage;
    private String currentCelebCalendarOwner;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());

        celebCalendarSource = new CalendarSource(CELEB_CALENDAR_SOURCE_NAME);
        initializeCelebCalendarSource(celebCalendarSource);

        storageCalendarSource = new CalendarSource(STORAGE_CALENDAR_SOURCE_NAME);
        initializeStorageCalendarSource(storageCalendarSource);


        currentCelebCalendarViewPage = "day";
        currentCelebCalendarOwner = "";
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new AddressBookChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(Person target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Person person) throws DuplicatePersonException {
        if (person.isCelebrity()) {
            addCelebrity(person);
        } else {
            addressBook.addPerson(person);
        }
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        indicateAddressBookChanged();
    }

    @Override
    public void addCelebrity(Person person) throws DuplicatePersonException {
        Celebrity celebrity = addressBook.addCelebrity(person);
        celebCalendarSource.getCalendars().add(celebrity.getCelebCalendar());
    }

    @Override
    public ArrayList<Celebrity> getCelebrities() {
        return addressBook.getCelebritiesList();
    }

    @Override
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireAllNonNull(target, editedPerson);

        addressBook.updatePerson(target, editedPerson);
        indicateAddressBookChanged();
    }

    @Override
    public int removeTag(Tag tag) throws DuplicatePersonException, PersonNotFoundException, TagNotFoundException {
        int numPersonsAffected = addressBook.removeTag(tag);
        indicateAddressBookChanged();
        return numPersonsAffected;
    }

    @Override
    public void setCelebCalendarViewPage(String newCurrentCelebCalendarViewPage) {
        currentCelebCalendarViewPage = newCurrentCelebCalendarViewPage;
    }

    @Override
    public List<Celebrity> getCelebrityWithName(String name) {
        List<Celebrity> matchedCelebrities = new ArrayList<>();
        for (Celebrity celebrity: getCelebrities()) {
            if (celebrity.getName().toString().contains(name)) {
                matchedCelebrities.add(celebrity);
            }
        }
        return matchedCelebrities;
    }

    @Override
    public void setCelebCalendarOwner(String celerity) {
        this.currentCelebCalendarOwner = celerity;
    }

    //=========== Celeb Calendar Accessors ===================================================================

    @Override
    public ObservableList<Calendar> getCelebCalendars() {
        return celebCalendarSource.getCalendars();
    }

    @Override
    public CalendarSource getCelebCalendarSource() {
        return celebCalendarSource;
    }

    @Override
    public StorageCalendar getStorageCalendar() {
        return (StorageCalendar) storageCalendarSource.getCalendars().get(0);
    }

    @Override
    public CalendarSource getStorageCalendarSource() {
        return storageCalendarSource;
    }

    @Override
    public String getCurrentCelebCalendarViewPage() {
        return currentCelebCalendarViewPage;
    }

    @Override
    public String getCurrentCelebCalendarOwner() {
        return currentCelebCalendarOwner;
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code addressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return FXCollections.unmodifiableObservableList(filteredPersons);
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
                && filteredPersons.equals(other.filteredPersons);
    }

    /**
     * Populates our CelebCalendar CalendarSource by creating a calendar for every celebrity in our addressbook
     */
    private void initializeCelebCalendarSource(CalendarSource calSource) {
        requireNonNull(addressBook);
        ArrayList<Celebrity> celebrities = addressBook.getCelebritiesList();
        for (Celebrity celebrity : celebrities) {
            calSource.getCalendars().add(celebrity.getCelebCalendar());
        }
    }

    /**
     * Initialize StorageCalendar CalendarSource
     */
    private void initializeStorageCalendarSource(CalendarSource calSource) {
        calSource.getCalendars().add(new StorageCalendar("Storage Calendar"));
    }

}
