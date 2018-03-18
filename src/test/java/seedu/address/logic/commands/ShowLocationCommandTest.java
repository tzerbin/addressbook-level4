package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.map.ShowLocationCommand;
import seedu.address.model.person.Address;

public class ShowLocationCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullAddress_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ShowLocationCommand(null);
    }

    @Test
    public void equals() {
        ShowLocationCommand showLocationFirstCommand = prepareCommand(new Address(VALID_ADDRESS_BOB));
        ShowLocationCommand showLocationSecondCommand = prepareCommand(new Address(VALID_ADDRESS_AMY));

        // same object -> returns true
        assertTrue(showLocationFirstCommand.equals(showLocationFirstCommand));

        // same tag -> returns true
        ShowLocationCommand showLocationFirstCommandCopy = prepareCommand(new Address(VALID_ADDRESS_BOB));
        assertTrue(showLocationFirstCommand.equals(showLocationFirstCommandCopy));

        // different types -> returns false
        assertFalse(showLocationFirstCommand.equals(1));

        // null -> returns false
        assertFalse(showLocationFirstCommand.equals(null));

        // different tags -> returns false
        assertFalse(showLocationFirstCommand.equals(showLocationSecondCommand));
    }

    /**
     * Returns a {@code showLocationCommand} with the parameter {@code address}.
     */
    private ShowLocationCommand prepareCommand(Address address) {
        ShowLocationCommand showLocationCommand = new ShowLocationCommand(address);
        return showLocationCommand;
    }
}
