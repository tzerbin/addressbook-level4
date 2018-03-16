package seedu.address.ui;

import java.util.logging.Logger;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEvent;

/**
 * The panel for google maps. Construct the maps view which is return by calling
 * getMapView() to MainWindow which attaches it to mapPanelPlaceHolder. After which it initialises the map contents
 * mapInitialised()
 */
public class MapPanel extends UiPart<Region> implements MapComponentInitializedListener {

    private static final String FXML = "MapsPanel.fxml";
    private GoogleMap actualMap;
    private GoogleMapView mapView;

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
        setMapOptions();
    }

    /**
     * Set the map options for initialisation of {@code actualMap}
     */
    private void setMapOptions() {
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
                .mapType(MapTypeIdEnum.ROADMAP);

        actualMap = mapView.createMap(options);
    }

    public GoogleMapView getMapView() {
        return mapView;
    }
}
