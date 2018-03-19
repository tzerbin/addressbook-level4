package seedu.address.model.map;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.logic.maps.Geocoding;

/**
 * Represents a map address in the CelebManager.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class MapAddress {
    public static final String MESSAGE_ADDRESS_CONSTRAINTS_MAPS =
            "Address should be in location name, road name, block and road name or postal code format.\n"
                    + "Note:(Person address may not be valid as it consist of too many details like unit number)";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String ADDRESS_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid MapAddress.
     */
    public MapAddress(String address) {
        requireNonNull(address);
        checkArgument(isValidAddress(address), MESSAGE_ADDRESS_CONSTRAINTS_MAPS);
        this.value = address;
    }

    /**
     * Returns true if a given string is a valid map address.
     */
    public static boolean isValidAddress(String test) {
        boolean isValid;
        Geocoding testAddress = new Geocoding();
        isValid = testAddress.checkIfAddressCanBeFound(test);
        return test.matches(ADDRESS_VALIDATION_REGEX) && isValid;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MapAddress // instanceof handles nulls
                && this.value.equals(((MapAddress) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
