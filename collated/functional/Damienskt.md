# Damienskt
###### \java\seedu\address\logic\commands\calendar\ViewAppointmentCommand.java
``` java
/**
 * Display the appointment details in the display panel
 * based on the index given
 */
public class ViewAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "viewAppointment";
    public static final String COMMAND_ALIAS = "va";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Show the appointment details based on index "
            + "Parameters: "
            + "Index (Based on most recent appointment list generated). \n"
            + "Example: " + COMMAND_WORD + " "
            + "1";

    public static final String MESSAGE_SUCCESS = "Selected appointment details:\n";

    private Appointment selectedAppointment;
    private int chosenIndex;

    /**
     * Takes in a zero-based index {@code index}
     */
    public ViewAppointmentCommand (int index) {
        chosenIndex = index;
    }

    @Override
    public CommandResult execute() throws CommandException {
        selectedAppointment = model.getChosenAppointment(chosenIndex);
        try {
            ShowLocationCommand showLocation = new ShowLocationCommand(
                    new MapAddress(selectedAppointment.getLocation()));
            showLocation.execute();
            return new CommandResult(MESSAGE_SUCCESS + getAppointmentDetailsResult());
        } catch (NullPointerException npe) {
            return new CommandResult(MESSAGE_SUCCESS + getAppointmentDetailsResult());
        }
    }

    private String getAppointmentDetailsResult () {
        return "Appointment Name: " + selectedAppointment.getTitle() + "\n"
                + "Start Date: " + selectedAppointment.getStartDate() + "\n"
                + "Start Time: " + selectedAppointment.getStartTime() + "\n"
                + "End Date: " + selectedAppointment.getEndDate() + "\n"
                + "End Time: " + selectedAppointment.getEndTime() + "\n"
                + "Location: " + selectedAppointment.getLocation();
    }
}
```
###### \java\seedu\address\logic\commands\map\EstimateRouteCommand.java
``` java
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
        Map.removeExistingMarker();
        Map.clearRoute();
        directionService = Map.getDirectionService();
    }

    @Override
    public CommandResult execute() {
        addRouteToMap();
        setDistAndTimeOfTravel();
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
}
```
###### \java\seedu\address\logic\commands\map\ShowLocationCommand.java
``` java
/**
 * Update the Map by adding a marker to the location of map address
 * and delete existing marker if it exist
 */
public class ShowLocationCommand extends Command {

    public static final String COMMAND_WORD = "showLocation";
    public static final String COMMAND_ALIAS = "sl";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the location of address in the Map.\n"
            + "Parameters: "
            + PREFIX_MAP_ADDRESS + "LOCATION (Name of location or postal code)\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MAP_ADDRESS + "Punggol Central\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MAP_ADDRESS + "820296";

    public static final String MESSAGE_SUCCESS = "Location is being shown in Map (identified by marker)!";

    private final MapAddress address;

    /**
     * Creates an AddAppointmentCommand with the following parameters
     * @param address The created appointment
     */
    public ShowLocationCommand (MapAddress address) {
        requireNonNull(address);
        this.address = address;
        Map.removeExistingMarker();
        Map.clearRoute();
    }

    @Override
    public CommandResult execute() {
        addNewMarkerToMap();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ShowLocationCommand // instanceof handles nulls
                && this.address.equals(((ShowLocationCommand) other).address));
    }

    /**
     * Remove any existing marker and adds new marker {@code location} to Map
     */
    public void addNewMarkerToMap() {

        LatLong center = getLatLong();

        Map.setLocation(getMarkerOptions(center));

        Map.setMarkerOnMap(center);
    }

    private Marker getMarkerOptions(LatLong center) {
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
```
###### \java\seedu\address\logic\map\DistanceEstimate.java
``` java
/**
 * Calculates distance and travel duration between two location.
 */
public class DistanceEstimate {

    /**
     * API Key required for requesting service from google server
     */
    //public static final String API_KEY = "AIzaSyAplrsZatzM_d2ynML097uqXd1-usgscOA";
    public static final String API_KEY = "AIzaSyDdJMB6Jug8D_45K72FpbEL8S5XQF_98Oc";

    private GeoApiContext context;
    private String distOriginDest;
    private String travelTime;

    /**
     * Initialises access to google server
     */
    public DistanceEstimate() {
        context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
    }

    /**
     * Extract time duration details from {@code matrixDetails}
     * @return time needed to travel between two location
     */
    private static Duration extractDurationDetailsToString(DistanceMatrix matrixDetails) {
        DistanceMatrixRow[] dataRow = matrixDetails.rows;
        DistanceMatrixElement[] dataElements = dataRow[0].elements;

        return dataElements[0].duration;
    }

    /**
     * Extract travel distance details from {@code matrixDetails}
     * @return distance between two location
     */
    private static Distance extractDistanceDetailsToString(DistanceMatrix matrixDetails) {
        DistanceMatrixRow[] dataRow = matrixDetails.rows;
        DistanceMatrixElement[] dataElements = dataRow[0].elements;

        return dataElements[0].distance;
    }

    /**
     * Request new approval for each access
     * @return successful approval from google server
     */
    private static DistanceMatrixApiRequest getApprovalForRequest(GeoApiContext context) {
        return DistanceMatrixApi.newRequest(context);
    }

    /**
     * Initialises the calculation of time and distance between two location by sending request with
     * {@code startPostalCode},{@code endPostalCode} and {@code modeOfTravel} to google server, details
     * extracted from result {@code estimate} and stored into {@code distOriginDest} and {@code travelTime}
     */
    public void calculateDistanceMatrix(LatLng startLocation, LatLng endLocation, TravelMode modeOfTravel) {
        DistanceMatrixApiRequest request = getApprovalForRequest(context);
        DistanceMatrix estimate = null;
        try {
            estimate = request.origins(startLocation)
                    .destinations(endLocation)
                    .mode(modeOfTravel)
                    .language("en-EN")
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        distOriginDest = String.valueOf(extractDistanceDetailsToString(estimate));
        travelTime = String.valueOf(extractDurationDetailsToString(estimate));
    }

    public String getTravelTime() {
        return travelTime;
    }

    public String getDistOriginDest() {
        return distOriginDest;
    }
}
```
###### \java\seedu\address\logic\map\Geocoding.java
``` java
/**
 * Converts address to LatLng form.
 */
public class Geocoding {

    /**
     * API Key required for requesting service from google server
     */
    //public static final String API_KEY = "AIzaSyAplrsZatzM_d2ynML097uqXd1-usgscOA";
    public static final String API_KEY = "AIzaSyDdJMB6Jug8D_45K72FpbEL8S5XQF_98Oc";

    private static LatLng location;
    private GeoApiContext context;

    /**
     * Initialises access to google server
     */
    public Geocoding() {
        context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();
    }

    /**
     * Send request to google server to obtain {@code results}
     * Where that {@code location} is extracted in LatLng form.
     * @param address
     */
    public void initialiseLatLngFromAddress(String address) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                    address).await();
            getLocation(results);
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getLocation(GeocodingResult[] results) {
        try {
            location = results[0].geometry.location;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw e;
        }
    }

    public double getLat() {
        return location.lat;
    }

    public double getLong() {
        return location.lng;
    }

    public LatLng getLatLng() {
        return location;
    }

    /**
     * Checks if the {@code address} can be found in google server
     * @param address
     * @return boolean
     */
    public boolean checkIfAddressCanBeFound(String address) {

        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                    address).await();
            getLocation(results);
        } catch (ApiException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}
```
###### \java\seedu\address\logic\parser\calendar\ViewAppointmentCommandParser.java
``` java
/**
 * Parses input arguments and creates a new ViewAppointmentCommand object
 */
public class ViewAppointmentCommandParser implements Parser<ViewAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ViewAppointmentCommand
     * and returns an ViewAppointmentCommand object for execution
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public ViewAppointmentCommand parse(String args) throws ParseException {

        try {
            Index index = ParserUtil.parseIndex(args);
            return new ViewAppointmentCommand(index.getZeroBased());
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
```
###### \java\seedu\address\logic\parser\map\EstimateRouteCommandParser.java
``` java
/**
 * Reads {@code args} and checks if the input has all the necessary values
 */
public class EstimateRouteCommandParser implements Parser<EstimateRouteCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EstimateRouteCommand
     * and returns an EstimateRouteCommand object for execution
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public EstimateRouteCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultiMap =
                ArgumentTokenizer.tokenize(args, PREFIX_START_MAP_ADDRESS, PREFIX_END_MAP_ADDRESS);
        if (!arePrefixesPresent(argMultiMap, PREFIX_START_MAP_ADDRESS, PREFIX_END_MAP_ADDRESS)
                || !argMultiMap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        EstimateRouteCommand.MESSAGE_USAGE));
        }

        try {
            MapAddress start = ParserUtil.parseMapAddress(argMultiMap.getValue(PREFIX_START_MAP_ADDRESS)).get();
            MapAddress end = ParserUtil.parseMapAddress(argMultiMap.getValue(PREFIX_END_MAP_ADDRESS)).get();
            if (!MapAddress.isValidAddressForEstimatingRoute(start.toString(), end.toString())) {
                throw new InvalidAddress("");
            }
            return new EstimateRouteCommand(start, end);
        } catch (IllegalValueException ive) {
            throw new ParseException(MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);
        } catch (InvalidAddress ia) {
            throw new ParseException(MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS_ROUTE_ESTIMATION);
        }
    }
}
```
###### \java\seedu\address\logic\parser\map\ShowLocationCommandParser.java
``` java
/**
 * Reads {@code args} and checks if the input has all the necessary values
 */
public class ShowLocationCommandParser implements Parser<ShowLocationCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ShowLocationCommand
     * and returns an ShowLocationCommand object for execution
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public ShowLocationCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultiMap =
                ArgumentTokenizer.tokenize(args, PREFIX_MAP_ADDRESS);
        if (!arePrefixesPresent(argMultiMap, PREFIX_MAP_ADDRESS)
                || !argMultiMap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ShowLocationCommand.MESSAGE_USAGE));
        }

        try {
            MapAddress address = ParserUtil.parseMapAddress(argMultiMap.getValue(PREFIX_MAP_ADDRESS)).get();
            return new ShowLocationCommand(address);
        } catch (IllegalValueException ive) {
            throw new ParseException(MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);
        }
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code address} is invalid.
     */
    public static MapAddress parseMapAddress(String address) throws IllegalValueException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!MapAddress.isValidAddress(trimmedAddress)) {
            throw new IllegalValueException(MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);
        }
        return new MapAddress(trimmedAddress);
    }
    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<MapAddress> parseMapAddress(Optional<String> address) throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(parseMapAddress(address.get())) : Optional.empty();
    }
```
###### \java\seedu\address\model\map\Map.java
``` java
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
```
###### \java\seedu\address\model\map\MapAddress.java
``` java
/**
 * Represents a map address in the CelebManager.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class MapAddress {
    public static final String MESSAGE_ADDRESS_MAP_CONSTRAINTS =
            "Address should be in location name, road name, block and road name or postal code format.\n"
                    + "Note:(Person address may not be valid as it consist of too many details like unit number)";
    public static final String MESSAGE_ADDRESS_MAP_CONSTRAINTS_ROUTE_ESTIMATION =
            "Both address cannot be reached by Driving!\n"
            + "Tips: \n"
            + "1.Be more specific with location name.\n"
            + "2.Insert Country name in front of location name (e.g Botanic Gardens -> Singapore Botanic Gardens).\n"
            + "3.Use Postal Code instead.";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ADDRESS_MAP_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid MapAddress.
     */
    public MapAddress(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_ADDRESS_MAP_CONSTRAINTS);
        this.value = address;
    }

    /**
     * Returns true if a given string is a valid map address.
     */
    public static boolean isValidAddress(String test) {
        boolean isValid;
        Geocoding testAddress = new Geocoding();
        isValid = testAddress.checkIfAddressCanBeFound(test);
        return test.matches(ADDRESS_MAP_VALIDATION_REGEX) && isValid;
    }
    /**
     * Returns true if a given string is a valid map address.
     */
    public static boolean isValidAddressForEstimatingRoute(String start, String end) {
        boolean isValid = true;
        LatLng startLatLng;
        LatLng endLatLng;
        DistanceEstimate checkValid = new DistanceEstimate();
        Geocoding latLong = new Geocoding();
        latLong.initialiseLatLngFromAddress(start);
        startLatLng = latLong.getLatLng();
        latLong.initialiseLatLngFromAddress(end);
        endLatLng = latLong.getLatLng();
        checkValid.calculateDistanceMatrix(startLatLng, endLatLng, TravelMode.DRIVING);
        if (checkValid.getTravelTime().equals("null")) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return (other == this) // short circuit if same object
                || (other instanceof MapAddress // instanceof handles nulls
                && this.value.equals(((MapAddress) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\address\ui\MapPanel.java
``` java
/**
 * The panel for google maps. Construct the maps view which is return by calling
 * getMapView() to MainWindow which attaches it to mapPanelPlaceHolder. After which it initialises the Map contents
 * mapInitialised()
 */
public class MapPanel extends UiPart<Region> implements MapComponentInitializedListener {

    public static final double LATITUDE_SG = 1.3607962;
    public static final double LONGITUDE_SG = 103.8109208;
    public static final int DEFAULT_ZOOM_LEVEL = 10;
    protected static DirectionsPane directions;
    protected static DirectionsRenderer renderer;
    protected static DirectionsService directionService;
    protected static DirectionsRequest directionRequest;
    protected static GoogleMap actualMap;
    private static final String FXML = "MapsPanel.fxml";
    protected GoogleMapView mapView;

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
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> mapView.getMap().hideDirectionsPane());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        t.start();
        directionService = new DirectionsService();
        actualMap = setMapOptions();
        directions = mapView.getDirec();
    }

    /**
     * Set the Map options for initialisation of {@code actualMap}
     */
    private GoogleMap setMapOptions() {
        LatLong center = new LatLong(LATITUDE_SG, LONGITUDE_SG);
        MapOptions options = new MapOptions();
        options.center(center)
                .zoom(DEFAULT_ZOOM_LEVEL)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(true)
                .mapType(MapTypeIdEnum.ROADMAP);
        return mapView.createMap(options);
    }

    public GoogleMapView getMapView() {
        return mapView;
    }
}
```
###### \resources\view\DarkTheme.css
``` css
.background {
    -fx-background-color: derive(#e8e8e8, 20%);
    background-color: #e8e8e8; /* Used in the default.html file */
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #555555;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: white;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #e8e8e8;
    -fx-control-inner-background: #e8e8e8;
    -fx-background-color: #e8e8e8;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#e8e8e8, 20%);
    -fx-border-color: transparent transparent transparent #4d4d4d;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: derive(#e8e8e8, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: derive(#e8e8e8, 20%);
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-cell:filled:even {
    -fx-background-color: #bababa;
}

.list-cell:filled:odd {
    -fx-background-color: #e8e8e8;
}

.list-cell:filled:selected {
    -fx-background-color: #ffefd5;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: black;
    -fx-border-width: 3;
}

.list-cell .label {
    -fx-text-fill: black;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 16px;
    -fx-text-fill: black;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 13px;
    -fx-text-fill: black;
}

.anchor-pane {
     -fx-background-color: derive(#fcfcfc, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#e8e8e8, 20%);
     -fx-border-color: derive(black, 10%);
     -fx-border-top-width: 1px;
}

.pane-with-thick-border {
     -fx-background-color: derive(#e8e8e8, 20%);
     -fx-border-color: derive(#4f4f4f, 10%);
     -fx-border-top-width: 20px;
}

.status-bar {
    -fx-background-color: derive(#1d1d1d, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: #fcfcfc;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
}

.status-bar-with-border {
    -fx-background-color: derive(#1d1d1d, 30%);
    -fx-border-color: derive(#1d1d1d, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: black;
}

.grid-pane {
    -fx-background-color: derive(#e8e8e8, 30%);
    -fx-border-color: derive(#1d1d1d, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#e8e8e8, 30%);
}

.context-menu {
    -fx-background-color: derive(#e8e8e8, 50%);
}

.context-menu .label {
    -fx-text-fill: black;
}

.menu-bar {
    -fx-background-color: derive(#e8e8e8, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 0.9;
}

.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #e2e2e2;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #1d1d1d;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #1d1d1d;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #3a3a3a;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: white;
  -fx-text-fill: #1d1d1d;
}

.button:focused {
    -fx-border-color: white, white;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #1d1d1d;
    -fx-text-fill: white;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #1d1d1d;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: white;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#1d1d1d, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: white;
    -fx-text-fill: white;
}

.scroll-bar {
    -fx-background-color: derive(#e8e8e8, 20%);
}

.scroll-bar .thumb {
    -fx-background-color: derive(#bababa, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: transparent #383838 transparent #383838;
    -fx-background-insets: 0;
    -fx-border-color: #383838 #383838 #ffffff #383838;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, black, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, #fcfcfc, transparent, #fcfcfc;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}

#tags .teal {
    -fx-text-fill: white;
    -fx-background-color: #3e7b91;
}

#tags .red {
    -fx-text-fill: black;
    -fx-background-color: red;
}

#tags .green {
    -fx-text-fill: black;
    -fx-background-color: green;
}

#tags .pink {
    -fx-text-fill: black;
    -fx-background-color: pink;
}

#tags .black {
    -fx-text-fill: white;
    -fx-background-color: black;
}

#tags .purple {
    -fx-text-fill: white;
    -fx-background-color: purple;
}

#tags .brown {
    -fx-text-fill: white;
    -fx-background-color: brown;
}

#tags .grey {
    -fx-text-fill: black;
    -fx-background-color: grey;
}

#tags .blue {
    -fx-text-fill: white;
    -fx-background-color: blue;
}

#tags .orange {
    -fx-text-fill: black;
    -fx-background-color: orange;

}
```
###### \resources\view\MainWindow.fxml
``` fxml
        <SplitPane id="splitPane" fx:id="splitPane" dividerPositions="0.4" VBox.vgrow="ALWAYS">
          <VBox fx:id="personList" minWidth="340" prefWidth="340" SplitPane.resizableWithParent="false">
            <padding>
              <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
            <StackPane fx:id="personListPanelPlaceholder" prefHeight="270.0" VBox.vgrow="ALWAYS"
                         styleClass="pane-with-thick-border"/>
            <padding>
              <Insets top="10" right="10" bottom="10" left="10" />
            </padding>
            <StackPane fx:id="mapPanelPlaceholder" prefHeight="270.0" VBox.vgrow="ALWAYS"
                         styleClass="pane-with-thick-border">
              <padding>
                <Insets top="10" right="10" bottom="10" left="10" />
              </padding>
            </StackPane>
          </VBox>
```
###### \resources\view\MapsPanel.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8.0.141" xmlns:fx="http://javafx.com/fxml/1">
  <Pane fx:id="mapView" prefWidth="248.0" VBox.vgrow="ALWAYS" />
</VBox>
```
