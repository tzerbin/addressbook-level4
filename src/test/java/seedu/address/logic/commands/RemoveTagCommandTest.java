package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.RemoveTagCommand.MESSAGE_CANNOT_REMOVE_CELEBRITY_TAG;
import static seedu.address.logic.commands.RemoveTagCommand.MESSAGE_TAG_NOT_FOUND;
import static seedu.address.model.ModelManager.CELEBRITY_TAG;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStorageCalendar.EMPTY_CALENDAR;
import static seedu.address.testutil.TypicalTags.FRIENDS_TAG;
import static seedu.address.testutil.TypicalTags.HUSBAND_TAG;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

//@@author WJY-norainu
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code RemoveTagCommand}.
 */
public class RemoveTagCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), EMPTY_CALENDAR, new UserPrefs());
    private Tag nonExistingTag = new Tag("thisTagNameIsSuperLongAndThereShouldntBeAnyoneWithSuchATag");

    @Test
    public void constructor_nullTag_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new RemoveTagCommand(null);
    }

    @Test
    public void execute_celebrityTag_throwsCommandException() {
        RemoveTagCommand removeTagCommand = prepareCommand(CELEBRITY_TAG);
        assertCommandFailure(removeTagCommand, model, MESSAGE_CANNOT_REMOVE_CELEBRITY_TAG);
    }

    @Test
    public void execute_nonExistingTag_throwsCommandException() {
        RemoveTagCommand removeTagCommand = prepareCommand(nonExistingTag);
        assertCommandFailure(removeTagCommand, model,
                String.format(MESSAGE_TAG_NOT_FOUND, nonExistingTag.toString()));
    }

    @Test
    public void execute_friendsTag_success() throws Exception {
        RemoveTagCommand removeTagCommand = prepareCommand(FRIENDS_TAG);
        int count = model.countPersonsWithTag(FRIENDS_TAG);

        String expectedMessage = String.format(
                RemoveTagCommand.MESSAGE_DELETE_TAG_SUCCESS,
                FRIENDS_TAG.toString(),
                count);

        Model expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.removeTag(FRIENDS_TAG);

        assertCommandSuccess(removeTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void executeUndoRedo_friendsTagWhichThreePersonsHave_success() throws Exception {
        Model expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());

        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        RemoveTagCommand removeTagCommand = prepareCommand(FRIENDS_TAG);

        // removeTag -> friends tag removed
        removeTagCommand.execute();
        undoRedoStack.push(removeTagCommand);

        // undo -> reverts address book back to previous state
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> friends tag deleted again
        expectedModel.removeTag(FRIENDS_TAG);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        RemoveTagCommand removeTagFirstCommand = prepareCommand(FRIENDS_TAG);
        RemoveTagCommand removeTagSecondCommand = prepareCommand(HUSBAND_TAG);

        // same object -> returns true
        assertTrue(removeTagFirstCommand.equals(removeTagFirstCommand));

        // same tag -> returns true
        RemoveTagCommand removeTagFirstCommandCopy = prepareCommand(FRIENDS_TAG);
        assertTrue(removeTagFirstCommand.equals(removeTagFirstCommandCopy));


        // different types -> returns false
        assertFalse(removeTagFirstCommand.equals(1));

        // null -> returns false
        assertFalse(removeTagFirstCommand.equals(null));

        // different tags -> returns false
        assertFalse(removeTagFirstCommand.equals(removeTagSecondCommand));
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private RemoveTagCommand prepareCommand(Tag tag) {
        RemoveTagCommand removeTagCommand = new RemoveTagCommand(tag);
        removeTagCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return removeTagCommand;
    }
}
