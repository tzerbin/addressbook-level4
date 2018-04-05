package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.INVALID_INDEX_CHOSEN;
import static seedu.address.commons.core.Messages.MESSAGE_NOT_LISTING_APPOINTMENTS;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.ComponentManager;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.model.AddressBookChangedEvent;
import seedu.address.commons.events.model.StorageCalendarChangedEvent;
import seedu.address.logic.commands.calendar.ListAppointmentCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.CelebCalendar;
import seedu.address.model.calendar.StorageCalendar;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

//@@author muruges95
/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    public static final String DAY_VIEW_PAGE = "day";
    public static final String WEEK_VIEW_PAGE = "week";
    public static final String MONTH_VIEW_PAGE = "month";

    public static final Tag CELEBRITY_TAG = new Tag("celebrity");

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private static final String CELEB_CALENDAR_SOURCE_NAME  = "Celeb Calendar Source";

    private final AddressBook addressBook;
    private final FilteredList<Person> filteredPersons;
    private final CalendarSource celebCalendarSource;
    private final StorageCalendar storageCalendar;

    // attributes related to calendarPanel status
    private String currentCelebCalendarViewPage;
    private Celebrity currentCelebCalendarOwner;
    private List<Appointment> appointments;
    private boolean isListingAppointments;
    private LocalDate baseDate;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, StorageCalendar storageCalendar, UserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());

        celebCalendarSource = new CalendarSource(CELEB_CALENDAR_SOURCE_NAME);
        resetCelebCalendars();

        this.storageCalendar = storageCalendar;
        appointments = storageCalendar.getAllAppointments();
        associateAppointmentsWithCelebritiesAndPointsOfContact();

        currentCelebCalendarViewPage = DAY_VIEW_PAGE;
        currentCelebCalendarOwner = null;
        isListingAppointments = false;
        baseDate = LocalDate.now();
    }

    public ModelManager() {
        this(new AddressBook(), new StorageCalendar("Storage Calendar"), new UserPrefs());
    }


    //@@author WJY-norainu
    @Override
    public void resetData(ReadOnlyAddressBook newData) {
        addressBook.resetData(newData);
        resetCelebCalendars();
        indicateAddressBookChanged();
    }

    //@@author
    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    /**
     * Raises an event to indicate the addressbook has changed
     * and reassoicates appointments with relevant celebrities and points of contact
     **/
    private void indicateAddressBookChanged() {
        resetCelebCalendars();
        associateAppointmentsWithCelebritiesAndPointsOfContact();
        raise(new AddressBookChangedEvent(addressBook));
    }

    /** Raises an event to indicate the appointment list has changed */
    private void indicateAppointmentListChanged() {
        raise(new StorageCalendarChangedEvent(getStorageCalendar()));
    }

    @Override
    public synchronized void deletePerson(Person target) throws PersonNotFoundException {
        addressBook.removePerson(target);
        if (target.isCelebrity()) {
            Celebrity targetCelebrity = (Celebrity) target;
            deleteCelebrity(targetCelebrity);
        }
        //removePersonFromAppointments(target);

        indicateAddressBookChanged();
    }

    //@@author WJY-norainu
    /**
     * Removes the person from appointments
     */
    private void removePersonFromAppointments(Person person) {
        List<Appointment> allAppointments = getStorageCalendar().getAllAppointments();
        // to be implemented after knowing how normal person is added to an appointment
    }

    //@@author WJY-norainu
    /**
     * Removes celebrity from appointments and celebrity's celeb calendar
     */
    private synchronized void deleteCelebrity(Celebrity targetCelebrity) {
        CelebCalendar targetCelebCalendar = targetCelebrity.getCelebCalendar();
        StorageCalendar storageCalendar = this.getStorageCalendar();
        List<Appointment> allAppointments = storageCalendar.getAllAppointments();

        // find appointment that has entry(s) belonging to target's celeb calendar
        // remove these entry(s) from the appointment's childEntryList
        List<Appointment> appointmentsToRemove =
                removeEntriesOfTargetCelebCalendarFrom(targetCelebCalendar, allAppointments);

        // remove appointment that only involves the deleted celebrity
        // from appointment list in storageCalendar and in storageCalendar itself
        allAppointments.removeAll(appointmentsToRemove);
        for (Appointment appointment: appointmentsToRemove) {
            storageCalendar.removeEntry(appointment);
        }

        // remove the celebrity's calendar
        celebCalendarSource.getCalendars().removeAll(targetCelebCalendar);
    }

    /**
     * Removes child entries that belong to target celeb calendar from all appointments
     * Finds appointments that only have child entries belonging to target celeb calendar
     * @return a list of appointments that only has child entry pointing to the target celeb calendar
     */
    private List<Appointment> removeEntriesOfTargetCelebCalendarFrom(
            CelebCalendar targetCelebCalendar, List<Appointment> allAppointments) {

        List<Appointment> appointmentsOnlyInvolveTargetCelebCalendar = new ArrayList<>();

        for (Appointment appointment: allAppointments) {
            List<Entry> oldChildEntries = appointment.getChildEntryList();
            List<Entry> newChildEntries = new ArrayList<>();
            for (Entry childEntry: oldChildEntries) {
                if (childEntry.getCalendar() == targetCelebCalendar) {
                    continue;
                }
                newChildEntries.add(childEntry);
            }
            // appointment only involves the target celeb calendar, add to to the list
            if (newChildEntries.size() < 1) {
                appointmentsOnlyInvolveTargetCelebCalendar.add(appointment);
            } else {
                appointment.setChildEntries(newChildEntries);
            }
        }

        return appointmentsOnlyInvolveTargetCelebCalendar;
    }

    //@@author muruges95
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
    public void associateAppointmentsWithCelebritiesAndPointsOfContact() {
        List<Celebrity> celebrityList;
        List<Person> pointOfContactList;
        for (Appointment a : appointments) {
            celebrityList = getCelebritiesFromId(a.getCelebIds());
            pointOfContactList = getPointsOfContactFromId(a.getPointOfContactIds());
            a.updateEntries(celebrityList, pointOfContactList);
        }
    }

    //@@author WJY-norainu
    @Override
    public int countPersonsWithTag(Tag tag) {
        return addressBook.countPersonsWithTag(tag);
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
    public void setCelebCalendarOwner(Celebrity celerity) {
        this.currentCelebCalendarOwner = celerity;
    }

    @Override
    public boolean getIsListingAppointments() {
        return this.isListingAppointments;
    }

    @Override
    public void setIsListingAppointments(boolean isListingAppointments) {
        this.isListingAppointments = isListingAppointments;
    }

    //=========== Celeb Calendar Accessors ===================================================================

    //@@author muruges95
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
        return storageCalendar;
    }

    @Override
    public String getCurrentCelebCalendarViewPage() {
        return currentCelebCalendarViewPage;
    }

    @Override
    public Celebrity getCurrentCelebCalendarOwner() {
        return currentCelebCalendarOwner;
    }

    @Override
    public List<Appointment> getAppointmentList() {
        return this.appointments;
    }

    @Override
    public void setAppointmentList(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public LocalDate getBaseDate() {
        return this.baseDate;
    }

    @Override
    public  void setBaseDate(LocalDate date) {
        this.baseDate = date;
    }

    @Override
    public Appointment getChosenAppointment(int chosenIndex) throws CommandException {
        if (!isListingAppointments) {
            throw new CommandException(MESSAGE_NOT_LISTING_APPOINTMENTS);
        }
        LocalDate startDate = ListAppointmentCommand.getStartDate();
        LocalDate endDate = ListAppointmentCommand.getEndDate();
        StorageCalendar storageCalendar = getStorageCalendar();
        List<Appointment> listOfAppointment = storageCalendar.getAppointmentsWithinDate(startDate, endDate);
        if (chosenIndex < 0 || chosenIndex >= listOfAppointment.size()) {
            throw new CommandException(INVALID_INDEX_CHOSEN);
        }
        return listOfAppointment.get(chosenIndex);
    }

    @Override
    public void addAppointmentToStorageCalendar(Appointment appt) {
        getStorageCalendar().addEntry(appt);
        indicateAppointmentListChanged();
    }

    @Override
    public Appointment deleteAppointment(int index) throws CommandException {
        Appointment apptToDelete = getChosenAppointment(index);
        apptToDelete.removeAppointment();
        indicateAppointmentListChanged();

        apptToDelete = removeAppointmentFromInternalList(index);

        if (getAppointmentList().size() < 1) {
            setIsListingAppointments(false);
            setCelebCalendarViewPage(DAY_VIEW_PAGE);
        }
        return apptToDelete;
    }

    /** Makes changes to model's internal appointment list */
    private Appointment removeAppointmentFromInternalList(int index) {
        return getAppointmentList().remove(index);

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
    public List<Celebrity> getCelebritiesChosen(Set<Index> indices) throws CommandException {
        List<Celebrity> celebrities = new ArrayList<>();
        for (Index index : indices) {
            celebrities.add(getCelebrityChosen(index));
        }
        return celebrities;
    }

    @Override
    public Celebrity getCelebrityChosen(Index index) throws CommandException {
        int zeroBasedIndex = index.getZeroBased();
        if (zeroBasedIndex >= filteredPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } else if (!filteredPersons.get(zeroBasedIndex).isCelebrity()) {
            throw new CommandException(Messages.MESSAGE_NOT_CELEBRITY_INDEX);
        } else {
            return (Celebrity) filteredPersons.get(zeroBasedIndex);
        }
    }

    @Override
    public List<Person> getPointsOfContactChosen(Set<Index> indices) throws CommandException {
        List<Person> pointsOfContact = new ArrayList<>();
        for (Index index : indices) {
            pointsOfContact.add(getPointOfContactChosen(index));
        }
        return pointsOfContact;
    }

    @Override
    public Person getPointOfContactChosen(Index index) throws CommandException {
        int zeroBasedIndex = index.getZeroBased();
        if (zeroBasedIndex >= filteredPersons.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } else if (filteredPersons.get(zeroBasedIndex).isCelebrity()) {
            throw new CommandException(Messages.MESSAGE_NOT_POINT_OF_CONTACT_INDEX);
        } else {
            return filteredPersons.get(zeroBasedIndex);
        }
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
    //=========== Private inner methods =============================================================

    /**
     * Populates our CelebCalendar CalendarSource by creating a calendar for every celebrity in our addressbook
     */
    private void resetCelebCalendars() {
        // reset calendars in celebCalendarSource to the restored calendars
        List<Celebrity> celebrities = addressBook.getCelebritiesList();
        List<Calendar> calendars = new ArrayList<>();

        for (Celebrity celebrity: celebrities) {
            calendars.add(celebrity.getCelebCalendar());
        }
        celebCalendarSource.getCalendars().clear();
        celebCalendarSource.getCalendars().addAll(calendars);
    }

    private List<Person> getPointsOfContactFromId(List<Long> pointOfContactIds) {
        List<Person> pointsOfContact = new ArrayList<>();
        for (long pointOfContactId : pointOfContactIds) {
            for (Person p : addressBook.getPersonList()) {
                if (!p.isCelebrity() && (p.getId() == pointOfContactId)) {
                    pointsOfContact.add(p);
                    break;
                }
            }
        }
        return pointsOfContact;
    }

    /**
     * Gets the celebrities based on their ids from our person list
     */
    private List<Celebrity> getCelebritiesFromId(List<Long> celebrityIds) {
        List<Celebrity> celebrities = new ArrayList<>();
        for (long celebId : celebrityIds) {
            for (Person p : addressBook.getPersonList()) {
                if (p.isCelebrity() && (p.getId() == celebId)) {
                    celebrities.add((Celebrity) p);
                    break;
                }
            }
        }
        return celebrities;
    }

}
