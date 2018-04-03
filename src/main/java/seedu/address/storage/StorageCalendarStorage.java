//@@author tzerbin
package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.calendar.StorageCalendar;

/**
 * Represents a storage for {@link seedu.address.model.calendar.StorageCalendar}.
 */
public interface StorageCalendarStorage {

    /**
     *  Returns the file path of the data file.
     */
    String getStorageCalendarFilePath();

    /**
     * Returns StorageCalendar data as a {@link StorageCalendar}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<StorageCalendar> readStorageCalendar() throws DataConversionException, IOException;

    /**
     * @see #readStorageCalendar()
     */
    Optional<StorageCalendar> readStorageCalendar(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link StorageCalendar} to the storage.
     * @param storageCalendar cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveStorageCalendar(StorageCalendar storageCalendar) throws IOException;

    /**
     * @see #saveStorageCalendar(StorageCalendar)
     */
    void saveStorageCalendar(StorageCalendar storageCalendar, String filePath) throws IOException;
}
