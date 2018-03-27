package seedu.address.logic.map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class GeocodingTest {

    Geocoding test;

    @Test
    public void isValidGeocode() {
        test = new Geocoding();

        //Invalid address to geocode conversion
        Assert.assertThrows(NullPointerException.class, () -> test.checkIfAddressCanBeFound(null));

        // invalid addresses
        assertFalse(test.checkIfAddressCanBeFound("")); // empty string
        assertFalse(test.checkIfAddressCanBeFound(" ")); // spaces only
        assertFalse(test.checkIfAddressCanBeFound("!!!!!!!")); // location not found in google server

        // valid addresses
        assertTrue(test.checkIfAddressCanBeFound("Kent ridge road"));
        assertTrue(test.checkIfAddressCanBeFound("820297")); // postal code
        assertTrue(test.checkIfAddressCanBeFound("National University Of Singapore")); // long address
    }
}
