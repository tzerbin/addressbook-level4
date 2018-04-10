package seedu.address.logic.commands.map;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_MAP_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_MAP_ADDRESS;

import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsService;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.directions.TravelModes;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.map.DistanceEstimate;
import seedu.address.logic.map.Geocoding;
import seedu.address.model.map.Map;
import seedu.address.model.map.MapAddress;
//@@author Damienskt
/**
 * Estimates the distance and travel time required between two location
 */
public class EstimateRouteCommand extends Command implements DirectionsServiceCallback {

    public static final String COMMAND_WORD = "estimateRoute";
    public static final String COMMAND_ALIAS = "er";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the estimated route in the Map and the distance and time required.\n"
            + "Parameters: "
            + PREFIX_START_MAP_ADDRESS + "START LOCATION (Name of location or postal code) "
            + PREFIX_END_MAP_ADDRESS + "END LOCATION (Name of location or postal code)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_MAP_ADDRESS + "Punggol Central "
            + PREFIX_END_MAP_ADDRESS + "NUS\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_START_MAP_ADDRESS + "820296 "
            + PREFIX_END_MAP_ADDRESS + "118420";

    public static final String MESSAGE_SUCCESS = "Route is being shown in Map, with start point at marker A"
            + " and end point at marker B!\n"
            + "Mode of transport: DRIVING\n";

    private static DirectionsRequest directionRequest;
    private static MapAddress startLocation = null;
    private static MapAddress endLocation = null;
    private static String distOfTravel;
    private static String timeOfTravel;
    private final LatLng endLatLng;
    private final LatLng startLatLng;
    private DirectionsService directionService;

    /**
     * Initialises the different class attributes of EstimateRouteCommand
     * @param start
     * @param end
     */
    public EstimateRouteCommand(MapAddress start, MapAddress end) {
        requireNonNull(start);
        requireNonNull(end);
        this.startLocation = start;
        this.endLocation = end;
        this.startLatLng = getLatLong(startLocation);
        this.endLatLng = getLatLong(endLocation);
        setDistAndTimeOfTravel();
    }

    @Override
    public CommandResult execute() {
        directionService = Map.getDirectionService();
        Map.removeExistingMarker();
        Map.clearRoute();
        addRouteToMap();
        return new CommandResult(MESSAGE_SUCCESS + getStringOfDistanceAndTime());
    }

    /**
     * Retrieves information of {@startLocation}, {@endLocation}, {@distOfTravel} and {@code timeOfTravel}
     * which is then converted to string format to be shown to user
     * @return information of distance and time of travel
     */
    public static String getStringOfDistanceAndTime() {
        return "Start Location: " + startLocation.toString() + "\n"
                + "End Location: " + endLocation.toString() + "\n"
                + "Estimated Distance of travel: " + distOfTravel + "\n"
                + "Estimated Time of travel: " + timeOfTravel;
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
        status.equals(DirectionStatus.OK);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EstimateRouteCommand // instanceof handles nulls
                && this.startLatLng.toString().equals(((EstimateRouteCommand) other).startLatLng.toString())
                && this.endLatLng.toString().equals(((EstimateRouteCommand) other).endLatLng.toString()));
    }

    /**
     * Update {@code MapPanel} to show new route
     */
    private void addRouteToMap() {
        directionRequest = Map.getDirectionRequest();
        directionRequest = new DirectionsRequest(
                startLatLng.toString(),
                endLatLng.toString(),
                TravelModes.DRIVING);
        directionService.getRoute(directionRequest, this, Map.getDirectionRenderer());
    }

    /**
     * Calculates {@code distOfTravel} and {@code timeOfTravel}
     */
    private void setDistAndTimeOfTravel() {
        DistanceEstimate distEstimate = new DistanceEstimate();
        distEstimate.calculateDistanceMatrix(startLatLng, endLatLng, TravelMode.DRIVING);
        distOfTravel = distEstimate.getDistOriginDest();
        timeOfTravel = distEstimate.getTravelTime();
    }

    /**
     * Converts {@code address} into LatLng form
     */
    private LatLng getLatLong(MapAddress address) {
        Geocoding convertToLatLng = new Geocoding();
        convertToLatLng.initialiseLatLngFromAddress(address.toString());
        return convertToLatLng.getLatLng();
    }

    public static MapAddress getStartLocation() {
        return startLocation;
    }

    public static MapAddress getEndLocation() {
        return endLocation;
    }

    public static String getDistOfTravel() {
        return distOfTravel;
    }

    public static String getTimeOfTravel() {
        return timeOfTravel;
    }
}
