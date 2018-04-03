package seedu.address.model.map;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsRequest;
import com.lynden.gmapsfx.service.directions.DirectionsService;

import seedu.address.ui.MapPanel;
//@@author Damienskt
/**
 * Map model which allows updating of map state
 */
public class Map extends MapPanel {

    private static Marker location;

    public static GoogleMap getMap() {
        return actualMap;
    }

    public static DirectionsRequest getDirectionRequest() {
        return directionRequest;
    }

    public static DirectionsService getDirectionService() {
        return directionService;
    }

    public static DirectionsRenderer getDirectionRenderer() {
        if (renderer == null) {
            renderer = new DirectionsRenderer(true, actualMap, directions);
        }
        return renderer;
    }

    /**
     * Clear any existing route in Map by clearing {@code renderer}
     */
    public static void clearRoute() {
        if (renderer != null) {
            renderer.clearDirections();
            renderer = new DirectionsRenderer(true, actualMap, directions);
        }
    }

    /**
     * Remove any existing marker {@code location} to Map
     */
    public static void removeExistingMarker() {
        if (location != null) {
            actualMap.removeMarker(location);
        }
    }

    public static void setMarkerOnMap(LatLong center) {
        actualMap.addMarker(Map.location);
        actualMap.setCenter(center);
        actualMap.setZoom(15);
    }

    public static void setLocation(Marker location) {
        Map.location = location;
    }
}
