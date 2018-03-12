package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalTags.FRIENDS_TAG;
import static seedu.address.testutil.TypicalTags.HUSBAND_TAG;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code RemoveTagCommand}.
 */
public class RemoveTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

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
