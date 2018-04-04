# WJY-norainu
###### \data\XmlSerializableAddressBookTest\typicalPersonsAddressBook.xml
``` xml
    <persons>
        <name>Jay Chou</name>
        <phone>134520789201</phone>
        <email>jay@gmail.com</email>
        <address>145, Taiwan</address>
        <tagged>friends</tagged>
        <tagged>celebrity</tagged>
    </persons>
    <persons>
        <name>Sakura Ayane</name>
        <phone>5201314</phone>
        <email>ayane@gmail.com</email>
        <address>Tokyo, Japan</address>
        <tagged>celebrity</tagged>
        <tagged>colleagues</tagged>
    </persons>
    <persons>
        <name>Robert Downey</name>
        <phone>19650404</phone>
        <email>ironman@firefox.com</email>
        <address>USA</address>
        <tagged>celebrity</tagged>
        <tagged>owesMoney</tagged>
    </persons>
    <tags>celebrity</tags>
    <tags>colleagues</tags>
    <tags>friends</tags>
    <tags>owesMoney</tags>
</addressbook>
```
###### \java\seedu\address\logic\commands\RemoveTagCommandTest.java
``` java
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
```
###### \java\seedu\address\logic\parser\RemoveTagCommandParserTest.java
``` java
public class RemoveTagCommandParserTest {

    private RemoveTagCommandParser parser = new RemoveTagCommandParser();

    @Test
    public void parse_validArgs_returnsRemoveTagCommand() {
        assertParseSuccess(parser, VALID_TAG_FRIEND, new RemoveTagCommand(FRIENDS_TAG));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser,
                "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgsContainingSpecialCharacters_throwsParseException() {
        assertParseFailure(parser,
                "%#friends",
                Tag.MESSAGE_TAG_CONSTRAINTS);
    }
}
```
###### \java\seedu\address\model\AddressBookTest.java
``` java
    @Test
    public void countPersonsWithTag_countsFriendsTag_returnNumberOfPersonsWithFriendsTag() {
        assertEquals(addressBookWithAlice.countPersonsWithTag(FRIENDS_TAG), 1);
    }

    @Test
    public void countPersonsWithTag_countsHusbandTag_returnNumberOfPersonsWithHusbandTag() {
        assertEquals(addressBookWithAlice.countPersonsWithTag(HUSBAND_TAG), 0);
    }

    @Test
    public void removeTag_existentTag_tagRemoved() throws Exception {
        addressBookWithBobAndAmy.removeTag(new Tag(VALID_TAG_FRIEND));

        Person bobWithoutFriendTag = new PersonBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();
        Person amyWithoutFriendTag = new PersonBuilder(AMY).withTags().build();
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(bobWithoutFriendTag)
                .withPerson(amyWithoutFriendTag).build();

        assertEquals(expectedAddressBook, addressBookWithBobAndAmy);
    }

```
###### \java\seedu\address\model\AddressBookTest.java
``` java
    @Test
    public void removePerson_theOnlyPersonWithHusbandTagRemoved_tagListUpdated() throws Exception {
        addressBookWithBobAndAlice.removePerson(BOB);

        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(ALICE).build();
        assertEquals(expectedAddressBook, addressBookWithBobAndAlice);
    }

    @Test
    public void updatePerson_theOnlyPersonWithFriendTagUpdated_tagListUpdated() throws Exception {
        Person aliceWithoutFriendTag = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        addressBookWithAlice.updatePerson(ALICE, aliceWithoutFriendTag);

        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(aliceWithoutFriendTag).build();
        assertEquals(expectedAddressBook, addressBookWithAlice);
    }

```
###### \java\seedu\address\testutil\TypicalCelebrities.java
``` java
/**
 * A utility class containing a list of {@code Celebrity} objects to be used in tests.
 */
public class TypicalCelebrities {
    public static final Celebrity JAY = new Celebrity(
            new PersonBuilder().withName("Jay Chou")
                    .withAddress("145, Taiwan")
                    .withEmail("jay@gmail.com")
                    .withPhone("134520789201")
                    .withTags("friends", "celebrity").build());

    public static final Celebrity AYANE = new Celebrity(
            new PersonBuilder().withName("Sakura Ayane")
                    .withAddress("Tokyo, Japan")
                    .withEmail("ayane@gmail.com")
                    .withPhone("5201314")
                    .withTags("celebrity", "colleagues").build());

    public static final Celebrity ROBERT = new Celebrity(
            new PersonBuilder().withName("Robert Downey")
                    .withAddress("USA")
                    .withEmail("ironman@firefox.com")
                    .withPhone("19650404")
                    .withTags("celebrity", "owesMoney").build());

    public static List<Celebrity> getTypicalCelebrities() {
        return new ArrayList<>(Arrays.asList(JAY, AYANE, ROBERT));
    }
}
```
###### \java\systemtests\RemoveTagCommandSystemTest.java
``` java
public class RemoveTagCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void removeTag() throws Exception {
        Model model = getModel();

        /* ----------------------------------- Perform valid removeTag operations ----------------------------------- */

        /* Case: remove tag friends from a non-empty address book that has this tag in tag list, command with leading
         * spaces and trailing spaces
         * -> tag removed and shows 7 person affected
         */
        String command = "   " + RemoveTagCommand.COMMAND_WORD + "  " + FRIENDS_TAG.tagName + " ";
        assertCommandSuccess(command, FRIENDS_TAG);

        /* Case: undo removing tag friends from the list -> tag friends restored */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: redo removing tag friends from the list -> tag friends removed again */
        command = RedoCommand.COMMAND_WORD;
        model.removeTag(FRIENDS_TAG);
        expectedResultMessage = RedoCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedResultMessage);

        /* Case: remove tag [owesMoney] from a non-empty address book that has this tag in tag list
         * -> tag removed and shows 1 person affected
         */
        Tag owesMoney = new Tag("owesMoney");
        assertCommandSuccess(owesMoney);

        /* ----------------------------------- Perform invalid removeTag operations --------------------------------- */

        /* Case: missing tag -> rejected */
        command = RemoveTagCommand.COMMAND_WORD + "";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveTagCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "removesTag " + VALID_TAG_FRIEND;
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: invalid tag name -> rejected */
        command = RemoveTagCommand.COMMAND_WORD + INVALID_TAG_DESC;
        assertCommandFailure(command, Tag.MESSAGE_TAG_CONSTRAINTS);

        /* Case: valid tag name but tag does not exist in the address book -> rejected */
        command = RemoveTagCommand.COMMAND_WORD + " " + VALID_TAG_FRIEND;
        assertCommandFailure(command, String.format(RemoveTagCommand.MESSAGE_TAG_NOT_FOUND, FRIENDS_TAG.toString()));
    }

    /**
     * Executes the {@code RemoveTagCommand} that removes {@code toRemove} from the model and asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code RemoveTagCommand} with the details of
     * {@code toRemove}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * the original empty model.<br>
     * 5. Browser url and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(Tag toRemove) throws Exception {
        assertCommandSuccess(RemoveTagCommand.COMMAND_WORD + " " + toRemove.tagName, toRemove);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(Tag)}. Executes {@code command}
     * instead.
     * @see RemoveTagCommandSystemTest#assertCommandSuccess(Tag)
     */
    private void assertCommandSuccess(String command, Tag toRemove) throws Exception {
        Model expectedModel = getModel();
        int numOfPersonsAffected = expectedModel.removeTag(toRemove);
        String expectedResultMessage = String.format(RemoveTagCommand.MESSAGE_DELETE_TAG_SUCCESS,
                                                    toRemove.toString(),
                                                    numOfPersonsAffected);

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String, Tag)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see RemoveTagCommandSystemTest#assertCommandSuccess(String, Tag)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertResultDisplayAndCommandBoxShowsDefaultStyle();
        assertStatusBarUnchangedExceptSyncStatus();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
```
