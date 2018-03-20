package seedu.address.model.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class MapAddressTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new MapAddress(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new MapAddress(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> MapAddress.isValidAddress(null));

        // invalid addresses
        assertFalse(MapAddress.isValidAddress("")); // empty string
        assertFalse(MapAddress.isValidAddress(" ")); // spaces only
        assertFalse(MapAddress.isValidAddress("!!!!!!!")); // location not found in google server

        // valid addresses
        assertTrue(MapAddress.isValidAddress("Kent ridge road"));
        assertTrue(MapAddress.isValidAddress("820297")); // postal code
        assertTrue(MapAddress.isValidAddress("National University Of Singapore")); // long address
    }
}
