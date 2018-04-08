package seedu.address.logic.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import seedu.address.testutil.Assert;
//@@author Damienskt
public class DistanceEstimateTest {

    private DistanceEstimate test;
    private LatLng start;
    private LatLng end;

    @Test
    public void isValidStartAndEndAddress() {

        test = new DistanceEstimate();

        Geocoding convertToLatLng = new Geocoding();

        //Initialise start location
        convertToLatLng.initialiseLatLngFromAddress("Hollywood");
        start = convertToLatLng.getLatLng();

        //Initialise end location
        convertToLatLng.initialiseLatLngFromAddress("NUS");
        end = convertToLatLng.getLatLng();

        // null start, end addresses and mode of travel
        Assert.assertThrows(NullPointerException.class, () -> test.calculateDistanceMatrix
                (null, null, null));

        // Start and End cannot be reached by driving
        test.calculateDistanceMatrix(start, end, TravelMode.DRIVING);
        assertEquals(test.getTravelTime(), "null");

        // valid start and end addresses
        convertToLatLng.initialiseLatLngFromAddress("820297");
        start = convertToLatLng.getLatLng();
        test.calculateDistanceMatrix(start, end, TravelMode.DRIVING);
        assertNotEquals(test.getTravelTime(), "null"); // long address

        // Invalid start LatLng and valid end LatLng
        start = new LatLng(-1, -1);
        test.calculateDistanceMatrix(start, end, TravelMode.DRIVING);
        assertEquals(test.getTravelTime(), "null"); // long address

        // Valid start LatLng and Invalid end LatLng
        end = new LatLng(-1, -1);
        test.calculateDistanceMatrix(start, end, TravelMode.DRIVING);
        assertEquals(test.getTravelTime(), "null"); // long address

        // Invalid start LatLng and invalid end LatLng
        start = new LatLng(-10, -10);
        end = new LatLng(-1, -1);
        test.calculateDistanceMatrix(start, end, TravelMode.DRIVING);
        assertEquals(test.getTravelTime(), "null"); // long address
    }
}
