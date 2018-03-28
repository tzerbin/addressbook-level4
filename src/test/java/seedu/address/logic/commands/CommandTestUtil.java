package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MAP_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.EditPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 555, Ang Mo Kio, #11-222";
    public static final String VALID_ADDRESS_BOB = "Block 297, Punggol Central, #01-222";
    public static final String VALID_ADDRESS_MAP_AMY = "National University Of Singapore";
    public static final String VALID_ADDRESS_MAP_BOB = "Block 297, Punggol Central";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friends";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_ADDRESS_MAP_DESC1 = " " + PREFIX_MAP_ADDRESS
            + "$$$$"; // random text not allowed for map addresses
    public static final String INVALID_ADDRESS_MAP_DESC2 = " "
            + PREFIX_MAP_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String VALID_APPOINTMENT_NAME_OSCAR = "Oscar Awards";
    public static final String VALID_APPOINTMENT_NAME_GRAMMY = "Grammy Awards";
    public static final String VALID_APPOINTMENT_LOCATION_OSCAR = "Clementi Rd";
    public static final String VALID_APPOINTMENT_LOCATION_GRAMMY = "Commonwealth Ave";
    public static final String VALID_START_TIME_OSCAR = "12:30";
    public static final String VALID_START_TIME_GRAMMY = "18:00";
    public static final String VALID_START_DATE_OSCAR = "12-12-2018";
    public static final String VALID_START_DATE_GRAMMY = "10-10-2019";
    public static final String VALID_END_TIME_OSCAR = "13:30";
    public static final String VALID_END_TIME_GRAMMY = "19:00";
    public static final String VALID_END_DATE_OSCAR = "12-12-2018";
    public static final String VALID_END_DATE_GRAMMY = "10-10-2019";

    public static final String APPT_NAME_DESC_OSCAR = " " + PREFIX_NAME + VALID_APPOINTMENT_NAME_OSCAR;
    public static final String APPT_NAME_DESC_GRAMMY = " " + PREFIX_NAME + VALID_APPOINTMENT_NAME_GRAMMY;
    public static final String APPT_LOCATION_DESC_OSCAR = " " + PREFIX_LOCATION + VALID_APPOINTMENT_LOCATION_OSCAR;
    public static final String APPT_LOCATION_DESC_GRAMMY = " " + PREFIX_LOCATION + VALID_APPOINTMENT_LOCATION_GRAMMY;
    public static final String APPT_START_TIME_DESC_OSCAR = " " + PREFIX_START_TIME + VALID_START_TIME_OSCAR;
    public static final String APPT_START_TIME_DESC_GRAMMY = " " + PREFIX_START_TIME + VALID_START_TIME_GRAMMY;
    public static final String APPT_START_DATE_DESC_OSCAR = " " + PREFIX_START_DATE + VALID_START_DATE_OSCAR;
    public static final String APPT_START_DATE_DESC_GRAMMY = " " + PREFIX_START_DATE + VALID_START_DATE_GRAMMY;
    public static final String APPT_END_TIME_DESC_OSCAR = " " + PREFIX_END_TIME + VALID_END_TIME_OSCAR;
    public static final String APPT_END_TIME_DESC_GRAMMY = " " + PREFIX_END_TIME + VALID_END_TIME_GRAMMY;
    public static final String APPT_END_DATE_DESC_OSCAR = " " + PREFIX_END_DATE + VALID_END_DATE_OSCAR;
    public static final String APPT_END_DATE_DESC_GRAMMY = " " + PREFIX_END_DATE + VALID_END_DATE_GRAMMY;

    public static final String INVALID_APPT_NAME_DESC = " " + PREFIX_NAME + "Dinner & Dance"; // '&' not allowed in name
    public static final String INVALID_APPT_LOCATION_DESC = " " + PREFIX_LOCATION + " "; // cant be just empty space
    public static final String INVALID_START_TIME = " " + PREFIX_START_TIME + "23:61"; // Minute cannot be more than 60
    public static final String INVALID_START_DATE = " " + PREFIX_START_DATE + "31-02-2018"; // There is no Feb 31st

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book and the filtered person list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedAddressBook, actualModel.getAddressBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredPersonList().size());

        Person person = model.getFilteredPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assertEquals(1, model.getFilteredPersonList().size());
    }

    /**
     * Deletes the first person in {@code model}'s filtered list from {@code model}'s address book.
     */
    public static void deleteFirstPerson(Model model) {
        Person firstPerson = model.getFilteredPersonList().get(0);
        try {
            model.deletePerson(firstPerson);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Person in filtered list must exist in model.", pnfe);
        }
    }

    /**
     * Returns an {@code UndoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static UndoCommand prepareUndoCommand(Model model, UndoRedoStack undoRedoStack) {
        UndoCommand undoCommand = new UndoCommand();
        undoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return undoCommand;
    }

    /**
     * Returns a {@code RedoCommand} with the given {@code model} and {@code undoRedoStack} set.
     */
    public static RedoCommand prepareRedoCommand(Model model, UndoRedoStack undoRedoStack) {
        RedoCommand redoCommand = new RedoCommand();
        redoCommand.setData(model, new CommandHistory(), undoRedoStack);
        return redoCommand;
    }
}
