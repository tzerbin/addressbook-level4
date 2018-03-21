package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.EditCommand.MESSAGE_DUPLICATE_PERSON;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

/**
 * Removes a tag from every person who has it in the address book
 * and shows the number of people affected by the operation.
 */
public class RemoveTagCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removeTag";
    public static final String COMMAND_ALIAS = "rt";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes a tag from every person who has it in the list and shows the number of people affected "
            + "by this operation.\n"
            + "Parameters: TAG (must be a non-empty string)\n"
            + "Example: " + COMMAND_WORD + " friends";

    public static final String MESSAGE_TAG_NOT_FOUND = "The tag %1$s does not exist and thus cannot be removed.";
    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Removed tag %1$s and %2$s person(s) affected.";

    public final Tag tagToRemove;

    public RemoveTagCommand(Tag tagToRemove) {
        requireNonNull(tagToRemove);
        this.tagToRemove = tagToRemove;
    }


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(tagToRemove);

        int numberOfAffectedPersons = 0;
        try {
            numberOfAffectedPersons = model.removeTag(tagToRemove);
        } catch (TagNotFoundException tnfe) {
            throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND, tagToRemove.toString()));
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        return new CommandResult(String.format(
                MESSAGE_DELETE_TAG_SUCCESS,
                tagToRemove.toString(),
                numberOfAffectedPersons));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RemoveTagCommand // instanceof handles nulls
                && this.tagToRemove.equals(((RemoveTagCommand) other).tagToRemove));
    }
}
