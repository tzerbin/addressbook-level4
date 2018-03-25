package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.EditCommand.createEditedPerson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.EditCommand;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    private final ArrayList<Celebrity> celebrities;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tags = new UniqueTagList();
        celebrities = new ArrayList<>();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons and Tags in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<Person> persons) throws DuplicatePersonException {
        this.persons.setPersons(persons);
    }

    public void setCelebrities(List<Celebrity> celebrities) throws DuplicatePersonException {
        this.celebrities.addAll(celebrities);
    }


    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Person> syncedPersonList = newData.getPersonList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());
        List<Celebrity> syncedCelebrityList = filterCelebrities(syncedPersonList);

        try {
            setPersons(syncedPersonList);
            setCelebrities(syncedCelebrityList);
        } catch (DuplicatePersonException e) {
            throw new AssertionError("AddressBooks should not have duplicate persons");
        }
    }


    /**
     * Removes {@code tag} from all persons in this {@code AddressBook}.
     * @returns the number of {@code person}s with this {@code tag} removed.
     */
    public int removeTag(Tag tag) throws PersonNotFoundException, DuplicatePersonException, TagNotFoundException {
        boolean tagExists = false;
        for (Tag existingTag: tags) {
            if (existingTag.equals(tag)) {
                tagExists = true;
            }
        }
        if (!tagExists) {
            throw new TagNotFoundException();
        }

        int count = 0;
        //is it possible to have two people with everything the same except for tag?
        for (Person person: persons) {
            if (person.hasTag(tag)) {
                //get the new tag set with the specified tag removed
                Set<Tag> oldTags = person.getTags();
                Set<Tag> newTags = new HashSet<>();
                for (Tag tagToKeep: oldTags) {
                    if (tagToKeep.equals(tag)) {
                        continue;
                    }
                    newTags.add(tagToKeep);
                }

                //create a new person with the specified tag removed to replace the person
                EditCommand.EditPersonDescriptor editPersonDescriptor = new EditCommand.EditPersonDescriptor();
                editPersonDescriptor.setTags(newTags);
                Person editedPerson = createEditedPerson(person, editPersonDescriptor);
                Person syncedEditedPerson = syncWithMasterTagList(editedPerson);
                persons.setPerson(person, syncedEditedPerson);
                removeUnusedTags();

                count++;
            }
        }
        return count;
    }

    //// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person p) throws DuplicatePersonException {
        Person person = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.add(person);
    }

    /**
     * Adds a copy of the given celebrity and returns it.
     */
    public Celebrity addCelebrity(Person person) throws DuplicatePersonException {
        Celebrity celebrity = (Celebrity) syncWithMasterTagList(person);
        persons.add(celebrity);
        celebrities.add(celebrity);
        return celebrity;
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *      another existing person in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Person)
     */
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        Person syncedEditedPerson = syncWithMasterTagList(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.setPerson(target, syncedEditedPerson);
        removeUnusedTags();
    }

    /**
     * Filters through a list of persons and returns those with a celebrity tag
     */
    private ArrayList<Celebrity> filterCelebrities(List<Person> persons) {
        ArrayList<Celebrity> celebrities = new ArrayList<>();
        for (Person p : persons) {
            if (p.isCelebrity()) {
                celebrities.add((Celebrity) p);
            }
        }
        return celebrities;
    }

    /**
     * Removes all {@code tag}s that are not used by any {@code person} in this {@code AddressBook}.
     */
    private void removeUnusedTags() {
        Set<Tag> usedTags = getUsedTags();
        tags.setTags(usedTags);
    }

    /**
     * @return a set of {@code tag}s that are used at least by one {@code person} in the person list.
     */
    private Set<Tag> getUsedTags() {
        Set<Tag> usedTags = new HashSet<>();
        for (Person person: persons) {
            Set<Tag> tagsOfThisPerson = person.getTags();
            for (Tag tag: tagsOfThisPerson) {
                if (!usedTags.contains(tag)) {
                    usedTags.add(tag);
                }
            }
        }
        return usedTags;
    }


    /**
     *  Updates the master tag list to include tags in {@code person} that are not in the list.
     *  @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     *  list.
     */
    private Person syncWithMasterTagList(Person person) {
        final UniqueTagList personTags = new UniqueTagList(person.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        Person updatedPerson = new Person(
                person.getName(), person.getPhone(), person.getEmail(), person.getAddress(), correctTagReferences);

        if (updatedPerson.isCelebrity()) {
            updatedPerson = new Celebrity(updatedPerson);
        }
        return updatedPerson;
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(Person key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            removeUnusedTags();
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public ArrayList<Celebrity> getCelebritiesList() {
        return celebrities;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags))
                && this.celebrities.equals(((AddressBook) other).celebrities);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags, celebrities);
    }
}
