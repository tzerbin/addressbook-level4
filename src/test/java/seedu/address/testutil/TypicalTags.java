package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import seedu.address.model.tag.Tag;

/**
 * A utility class containing a list of {@code Index} objects to be used in tests.
 */
public class TypicalTags {
    public static final Tag FRIENDS_TAG = new Tag(VALID_TAG_FRIEND);
    public static final Tag HUSBAND_TAG = new Tag(VALID_TAG_HUSBAND);
}