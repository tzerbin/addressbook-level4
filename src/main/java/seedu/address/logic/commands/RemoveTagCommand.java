package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.tag.Tag;

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

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Removed Tag %1$s and %2$s person(s) affected.";

    public final Tag tagToRemove;

    public RemoveTagCommand(Tag tagToRemove) {
        requireNonNull(tagToRemove);
        this.tagToRemove = tagToRemove;
    }


    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(tagToRemove);
        int numberOfAffectedPersons = model.removeTag(tagToRemove);
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, tagToRemove, numberOfAffectedPersons));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof RemoveTagCommand // instanceof handles nulls
                && this.tagToRemove.equals(((RemoveTagCommand) other).tagToRemove));
    }
}
