package seedu.address.ui;

import java.util.logging.Logger;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.service.directions.DirectionStatus;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
import com.lynden.gmapsfx.service.directions.DirectionsResult;
import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
import com.lynden.gmapsfx.service.elevation.ElevationResult;
import com.lynden.gmapsfx.service.elevation.ElevationServiceCallback;
import com.lynden.gmapsfx.service.elevation.ElevationStatus;
import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
import com.lynden.gmapsfx.service.geocoding.GeocodingService;
import com.lynden.gmapsfx.service.geocoding.GeocodingServiceCallback;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEvent;
import seedu.address.commons.core.LogsCenter;

public class MapPanel extends UiPart<Region> implements MapComponentInitializedListener {

    private GoogleMapView mapView;
    private GoogleMap actualMap;
    private static final String FXML = "MapsPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(this.getClass());
    private DirectionsRenderer renderer;

    public MapPanel() {
        super(FXML);
        registerAsAnEventHandler(this);
        mapView = new GoogleMapView();
        mapView.setDisableDoubleClick(true);
        mapView.addMapInializedListener(this);
        mapView.getWebview().getEngine().setOnAlert((WebEvent<String> event) -> {
        });
    }
    @Override
    public void mapInitialized() {
        Thread t = new Thread( () -> {
            try {
                Thread.sleep(3000);
                System.out.println("Calling showDirections from Java");
                Platform.runLater(() -> mapView.getMap().hideDirectionsPane());
            } catch( Exception ex ) {
                ex.printStackTrace();
            }
        });
        t.start();
        //Once the map has been loaded by the Webview, initialize the map details.
        LatLong center = new LatLong(1.3607962,103.8109208);

        MapOptions options = new MapOptions();
        options.center(center)
                .mapMarker(true)
                .zoom(10)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .mapType(MapTypeIdEnum.ROADMAP);//map type

        actualMap = mapView.createMap(options);
    }

    public GoogleMapView getMapView() {
        return mapView;
    }
}
