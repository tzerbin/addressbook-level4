package seedu.address.logic.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class DistanceEstimateTest {

    DistanceEstimate test;
    LatLng start;
    LatLng end;

    @Test
    public void isValidStartAndEndAddress() {
        test = new DistanceEstimate();
        Geocoding convertToLatLng = new Geocoding();
        convertToLatLng.initialiseLatLngFromAddress("Hollywood");
        start = convertToLatLng.getLatLng();
        convertToLatLng.initialiseLatLngFromAddress("NUS");
        end = convertToLatLng.getLatLng();
        // null start, end addresses and mode of travel
        Assert.assertThrows(NullPointerException.class, () -> test.calculateDistanceMatrix(null, null, null));

        // Start and End cannot be reached by driving
        test.calculateDistanceMatrix(start, end, TravelMode.DRIVING);
        assertEquals(test.getTravelTime(), "null");

        // valid start and end addresses
        convertToLatLng.initialiseLatLngFromAddress("820297");
        start = convertToLatLng.getLatLng();
        test.calculateDistanceMatrix(start, end, TravelMode.DRIVING);
        assertNotEquals(test.getTravelTime(), "null"); // long address
    }
}
