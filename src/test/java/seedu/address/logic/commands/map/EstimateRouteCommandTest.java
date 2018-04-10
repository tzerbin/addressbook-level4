package seedu.address.logic.commands.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_MAP_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_MAP_BOB;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import seedu.address.logic.map.DistanceEstimate;
import seedu.address.logic.map.Geocoding;
import seedu.address.model.map.MapAddress;

//@@author Damienskt
/**
 * Contains integration tests and unit tests for
 * {@code EstimateRouteCommand}.
 */
public class EstimateRouteCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullAddress_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new EstimateRouteCommand(null, null);
    }

    @Test
    public void execute_initialisationOfCommand_success() {
        EstimateRouteCommand estimateRouteCommand = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB),
                new MapAddress(VALID_ADDRESS_MAP_AMY));
        DistanceEstimate estimate = new DistanceEstimate();
        Geocoding geocode = new Geocoding();
        geocode.initialiseLatLngFromAddress(VALID_ADDRESS_MAP_BOB);
        LatLng startLatLng = geocode.getLatLng();
        geocode.initialiseLatLngFromAddress(VALID_ADDRESS_MAP_AMY);
        LatLng endLatLng = geocode.getLatLng();
        estimate.calculateDistanceMatrix(startLatLng, endLatLng, TravelMode.DRIVING);

        assertEquals(estimateRouteCommand.getStartLocation(), new MapAddress(VALID_ADDRESS_MAP_BOB));
        assertEquals(estimateRouteCommand.getEndLocation(), new MapAddress(VALID_ADDRESS_MAP_AMY));
        assertEquals(estimateRouteCommand.getDistOfTravel(), estimate.getDistOriginDest());
        assertEquals(estimateRouteCommand.getTimeOfTravel(), estimate.getTravelTime());
    }

    @Test
    public void equals() {

        EstimateRouteCommand estimateRouteFirstCommand = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB),
                new MapAddress(VALID_ADDRESS_MAP_AMY));
        EstimateRouteCommand estimateRouteSecondCommand = prepareCommand(new MapAddress("Bedok"),
                new MapAddress("NUS"));

        // same object -> returns true
        assertTrue(estimateRouteFirstCommand.equals(estimateRouteFirstCommand));

        // same start and end address -> returns true
        EstimateRouteCommand estimateRouteFirstCommandCopy = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB),
                new MapAddress(VALID_ADDRESS_MAP_AMY));
        assertTrue(estimateRouteFirstCommand.equals(estimateRouteFirstCommandCopy));

        // different types -> returns false
        assertFalse(estimateRouteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(estimateRouteFirstCommand.equals(null));

        // different start and end address -> returns false
        assertFalse(estimateRouteFirstCommand.equals(estimateRouteSecondCommand));
    }

    /**
     * Returns a {@code estimateRouteCommand} with the parameter {@code start} and {@code end}.
     */
    private EstimateRouteCommand prepareCommand(MapAddress start, MapAddress end) {
        EstimateRouteCommand estimateRouteCommand = new EstimateRouteCommand(start, end);
        return estimateRouteCommand;
    }
}
