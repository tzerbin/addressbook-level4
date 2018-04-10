package seedu.address.logic.commands.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_MAP_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_MAP_BOB;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.map.DistanceEstimate;
import seedu.address.logic.map.Geocoding;
import seedu.address.model.map.MapAddress;
//@@author Damienskt
/**
 * Contains integration tests and unit tests for
 * {@code ShowLocationCommand}.
 */
public class ShowLocationCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullAddress_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ShowLocationCommand(null);
    }

    @Test
    public void execute_initialisationOfCommand_success() {
        ShowLocationCommand showLocationCommand = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB));
        assertEquals(showLocationCommand.getLocation(), new MapAddress(VALID_ADDRESS_MAP_BOB));
    }

    @Test
    public void equals() {
        ShowLocationCommand showLocationFirstCommand = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB));
        ShowLocationCommand showLocationSecondCommand = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_AMY));

        // same object -> returns true
        assertTrue(showLocationFirstCommand.equals(showLocationFirstCommand));

        // same map address -> returns true
        ShowLocationCommand showLocationFirstCommandCopy = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB));
        assertTrue(showLocationFirstCommand.equals(showLocationFirstCommandCopy));

        // different types -> returns false
        assertFalse(showLocationFirstCommand.equals(1));

        // null -> returns false
        assertFalse(showLocationFirstCommand.equals(null));

        // different map address -> returns false
        assertFalse(showLocationFirstCommand.equals(showLocationSecondCommand));
    }

    /**
     * Returns a {@code showLocationCommand} with the parameter {@code address}.
     */
    private ShowLocationCommand prepareCommand(MapAddress address) {
        ShowLocationCommand showLocationCommand = new ShowLocationCommand(address);
        return showLocationCommand;
    }
}
