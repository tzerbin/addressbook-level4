package seedu.address.logic.commands.map;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LOCATION;
import static seedu.address.ui.MapPanel.actualMap;

import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.Animation;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.maps.Geocoding;
import seedu.address.model.person.Address;

public class ShowLocationCommand extends Command implements MapComponentInitializedListener{

    public static final String COMMAND_WORD = "showLocation";
    public static final String COMMAND_ALIAS = "sl";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the location of address in the map.\n"
            + "Parameters: "
            + PREFIX_LOCATION + "LOCATION (Name of location or postal code)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_LOCATION + "Punggol Central\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_LOCATION + "820296";

    public static final String MESSAGE_SUCCESS = "Location is being shown in map(identified by marker)!";

    private final Address address;
    private static Marker location;

    /**
     * Creates an AddAppointmentCommand with the following parameters
     * @param address The created appointment
     */
    public ShowLocationCommand (Address address) {
        requireNonNull(address);
        this.address = address;
    }

    @Override
    public CommandResult execute() throws CommandException {
        mapInitialized();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public void mapInitialized() {

        removeExistingMarker();
        LatLong center = getLatLong();

        location = getMarker(center);

        setMarkerOnMap(center, location);
    }

    private void removeExistingMarker() {
        if(location!=null) {
            actualMap.removeMarker(location);
        }
    }

    private void setMarkerOnMap(LatLong center, Marker location) {
        actualMap.addMarker(location);
        actualMap.setCenter(center);
        actualMap.setZoom(15);
    }

    private Marker getMarker(LatLong center) {
        MarkerOptions markOptions = new MarkerOptions();
        markOptions.animation(Animation.DROP)
                .position(center)
                .visible(true);
        return new Marker(markOptions);
    }

    private LatLong getLatLong() {
        Geocoding convertToLatLng = new Geocoding();
        convertToLatLng.initialiseLatLngFromAddress(address.toString());
        return new LatLong(convertToLatLng.getLat(), convertToLatLng.getLong());
    }

}