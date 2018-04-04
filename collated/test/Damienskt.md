# Damienskt
###### \java\seedu\address\logic\commands\map\EstimateRouteCommandTest.java
``` java
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
```
###### \java\seedu\address\logic\commands\map\ShowLocationCommandTest.java
``` java
/**
 * Contains integration tests and unit tests for
 * {@code ShowLocationCommand}.
 */
public class ShowLocationCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullAddress_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ShowLocationCommand(null);
    }

    @Test
    public void equals() {
        ShowLocationCommand showLocationFirstCommand = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB));
        ShowLocationCommand showLocationSecondCommand = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_AMY));

        // same object -> returns true
        assertTrue(showLocationFirstCommand.equals(showLocationFirstCommand));

        // same map address -> returns true
        ShowLocationCommand showLocationFirstCommandCopy = prepareCommand(new MapAddress(VALID_ADDRESS_MAP_BOB));
        assertTrue(showLocationFirstCommand.equals(showLocationFirstCommandCopy));

        // different types -> returns false
        assertFalse(showLocationFirstCommand.equals(1));

        // null -> returns false
        assertFalse(showLocationFirstCommand.equals(null));

        // different map address -> returns false
        assertFalse(showLocationFirstCommand.equals(showLocationSecondCommand));
    }

    /**
     * Returns a {@code showLocationCommand} with the parameter {@code address}.
     */
    private ShowLocationCommand prepareCommand(MapAddress address) {
        ShowLocationCommand showLocationCommand = new ShowLocationCommand(address);
        return showLocationCommand;
    }
}
```
###### \java\seedu\address\logic\map\DistanceEstimateTest.java
``` java
public class DistanceEstimateTest {

    private DistanceEstimate test;
    private LatLng start;
    private LatLng end;

    @Test
    public void isValidStartAndEndAddress() {
        test = new DistanceEstimate();
        Geocoding convertToLatLng = new Geocoding();
        convertToLatLng.initialiseLatLngFromAddress("Hollywood");
        start = convertToLatLng.getLatLng();
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
    }
}
```
###### \java\seedu\address\logic\map\GeocodingTest.java
``` java
public class GeocodingTest {

    private Geocoding test;

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
```
###### \java\seedu\address\logic\parser\map\EstimateRouteCommandParserTest.java
``` java
public class EstimateRouteCommandParserTest {
    private EstimateRouteCommandParser parser = new EstimateRouteCommandParser();

    @Test
    public void parse_validArgs_returnsEstimateRouteCommand() {
        MapAddress startAddress = new MapAddress(VALID_ADDRESS_MAP_BOB);
        MapAddress endAddress = new MapAddress(VALID_ADDRESS_MAP_AMY);
        assertParseSuccess(parser, " " + PREFIX_START_MAP_ADDRESS + VALID_ADDRESS_MAP_BOB + " "
                + PREFIX_END_MAP_ADDRESS + VALID_ADDRESS_MAP_AMY, new EstimateRouteCommand(startAddress, endAddress));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EstimateRouteCommand.MESSAGE_USAGE));
    }

}
```
###### \java\seedu\address\logic\parser\map\ShowLocationCommandParserTest.java
``` java
public class ShowLocationCommandParserTest {

    private ShowLocationCommandParser parser = new ShowLocationCommandParser();

    @Test
    public void parse_validArgs_returnsShowLocationCommand() {
        MapAddress address = new MapAddress(VALID_ADDRESS_MAP_BOB);
        assertParseSuccess(parser, " " + PREFIX_MAP_ADDRESS + VALID_ADDRESS_MAP_BOB, new ShowLocationCommand(address));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowLocationCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\model\map\MapAddressTest.java
``` java
public class MapAddressTest {
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new MapAddress(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new MapAddress(invalidAddress));
    }

    @Test
    public void isValidAddress() {

        // null address
        Assert.assertThrows(NullPointerException.class, () -> MapAddress.isValidAddress(null));

        // invalid addresses
        assertFalse(MapAddress.isValidAddress("")); // empty string
        assertFalse(MapAddress.isValidAddress(" ")); // spaces only
        assertFalse(MapAddress.isValidAddress("!!!!!!!")); // location not found in google server

        // valid addresses
        assertTrue(MapAddress.isValidAddress("Kent ridge road"));
        assertTrue(MapAddress.isValidAddress("820297")); // postal code
        assertTrue(MapAddress.isValidAddress("National University Of Singapore")); // long address
    }
}
```
###### \java\systemtests\map\EstimateRouteCommandSystemTest.java
``` java
public class EstimateRouteCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void estimateRoute() {
        Model model = getModel();

        /* ------------------------------ Perform valid estimateRoute operations --------------------------------- */

        /* Case: shows best estimated time and distance of travel between two locations
         *  -> Information shown in result display
         */
        MapAddress startAddress = new MapAddress(VALID_ADDRESS_MAP_BOB);
        MapAddress endAddress = new MapAddress(VALID_ADDRESS_MAP_AMY);
        assertCommandSuccess(startAddress, endAddress);

        /* Case: shows best estimated time and distance of travel between two locations using postal code
         * -> Information shown in result display
         */
        startAddress = new MapAddress("820296");
        endAddress = new MapAddress("119077");
        assertCommandSuccess(startAddress, endAddress);

        /* Case: shows best estimated time and distance of travel between two locations using location name
         * -> Information shown in result display
         */
        startAddress = new MapAddress("National University of Singapore");
        endAddress = new MapAddress("Punggol");
        assertCommandSuccess(startAddress, endAddress);

        /* ------------------------------ Perform invalid estimateRoute operations --------------------------------- */

        /* Case: missing MapAddress and prefix-> rejected */
        String command = EstimateRouteCommand.COMMAND_WORD + "";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EstimateRouteCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "estimateroute " + PREFIX_START_MAP_ADDRESS + VALID_ADDRESS_MAP_BOB
                + PREFIX_START_MAP_ADDRESS + VALID_ADDRESS_MAP_AMY;
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: correct prefix but missing MapAddress -> rejected */
        command = EstimateRouteCommand.COMMAND_WORD + " " + PREFIX_START_MAP_ADDRESS + " " + PREFIX_END_MAP_ADDRESS;
        assertCommandFailure(command, MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);

        /* Case: correct prefix but invalid MapAddress -> rejected */
        command = EstimateRouteCommand.COMMAND_WORD + " " + PREFIX_START_MAP_ADDRESS + "))))))" + " "
                + PREFIX_END_MAP_ADDRESS + "^^^^^^^";
        assertCommandFailure(command, MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);

        /* Case: missing MapAddress prefix -> rejected */
        command = EstimateRouteCommand.COMMAND_WORD + " " + VALID_ADDRESS_MAP_BOB + " " + VALID_ADDRESS_MAP_AMY;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                EstimateRouteCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the {@code EstimateRouteCommand} that asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code EstimateRouteCommand}.<br>
     * 4. Shows the distance and time of travel in result display.<br>
     * 5. Calendar panel and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(MapAddress startAddress, MapAddress endAddress) {
        assertCommandSuccess(EstimateRouteCommand.COMMAND_WORD + " " + PREFIX_START_MAP_ADDRESS
                + startAddress.toString() + " " + PREFIX_END_MAP_ADDRESS + endAddress.toString());
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(MapAddress, MapAddress)}. Executes {@code command}
     * instead.
     * @see EstimateRouteCommandSystemTest#assertCommandSuccess(MapAddress, MapAddress)
     */
    private void assertCommandSuccess(String command) {
        Model expectedModel = getModel();
        String expectedResultMessage = EstimateRouteCommand.MESSAGE_SUCCESS;

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see EstimateRouteCommandSystemTest#assertCommandSuccess(String)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        expectedResultMessage = expectedResultMessage + EstimateRouteCommand.getStringOfDistanceAndTime();
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertResultDisplayAndCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
```
###### \java\systemtests\map\ShowLocationCommandSystemTest.java
``` java
public class ShowLocationCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void showLocation() {

        /* ------------------------------ Perform valid showLocation operations --------------------------------- */

        /* Case: show location using address (block and street name) of a place
         * -> location marker shown in map
         */
        MapAddress newAddress = new MapAddress(VALID_ADDRESS_MAP_BOB);
        assertCommandSuccess(newAddress);

        /* Case: show location using postal code of a place
         * -> location marker shown in map
         */
        newAddress = new MapAddress("820296");
        assertCommandSuccess(newAddress);

        /* Case: show location using name of a place (e.g National University of Singapore)
         * -> location marker shown in map
         */
        newAddress = new MapAddress("National University of Singapore");
        assertCommandSuccess(newAddress);

        /* ------------------------------- Perform invalid showLocation operations --------------------------------- */

        /* Case: missing MapAddress and prefix-> rejected */
        String command = ShowLocationCommand.COMMAND_WORD + "";
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowLocationCommand.MESSAGE_USAGE));

        /* Case: invalid keyword -> rejected */
        command = "showslocation " + VALID_ADDRESS_MAP_BOB;
        assertCommandFailure(command, Messages.MESSAGE_UNKNOWN_COMMAND);

        /* Case: correct prefix but missing MapAddress -> rejected */
        command = ShowLocationCommand.COMMAND_WORD + INVALID_ADDRESS_MAP_DESC2;
        assertCommandFailure(command, MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);

        /* Case: correct prefix but invalid MapAddress -> rejected */
        command = ShowLocationCommand.COMMAND_WORD + INVALID_ADDRESS_MAP_DESC1;
        assertCommandFailure(command, MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS);

        /* Case: missing MapAddress prefix -> rejected */
        command = ShowLocationCommand.COMMAND_WORD + " " + VALID_ADDRESS_MAP_BOB;
        assertCommandFailure(command, String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowLocationCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the {@code ShowLocationCommand} that asserts that the,<br>
     * 1. Command box displays an empty string.<br>
     * 2. Command box has the default style class.<br>
     * 3. Result display box displays the success message of executing {@code ShowLocationCommand}.<br>
     * 4. Shows the location marker of {@code address} in Maps GUI.<br>
     * 5. Calendar panel and selected card remain unchanged.<br>
     * 6. Status bar's sync status changes.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(MapAddress address) {
        assertCommandSuccess(ShowLocationCommand.COMMAND_WORD + " " + PREFIX_MAP_ADDRESS + address.toString());
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(MapAddress)}. Executes {@code command}
     * instead.
     * @see ShowLocationCommandSystemTest#assertCommandSuccess(MapAddress)
     */
    private void assertCommandSuccess(String command) {
        Model expectedModel = getModel();
        String expectedResultMessage = ShowLocationCommand.MESSAGE_SUCCESS;

        assertCommandSuccess(command, expectedModel, expectedResultMessage);
    }

    /**
     * Performs the same verification as {@code assertCommandSuccess(String)} except asserts that
     * the,<br>
     * 1. Result display box displays {@code expectedResultMessage}.<br>
     * 2. {@code Model}, {@code Storage} and {@code PersonListPanel} equal to the corresponding components in
     * {@code expectedModel}.<br>
     * @see ShowLocationCommandSystemTest#assertCommandSuccess(String)
     */
    private void assertCommandSuccess(String command, Model expectedModel, String expectedResultMessage) {
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertResultDisplayAndCommandBoxShowsDefaultStyle();
    }

    /**
     * Executes {@code command} and asserts that the,<br>
     * 1. Command box displays {@code command}.<br>
     * 2. Command box has the error style class.<br>
     * 3. Result display box displays {@code expectedResultMessage}.<br>
     * 4. {@code Model}, {@code Storage} and {@code PersonListPanel} remain unchanged.<br>
     * 5. Browser url, selected card and status bar remain unchanged.<br>
     * Verifications 1, 3 and 4 are performed by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
```
