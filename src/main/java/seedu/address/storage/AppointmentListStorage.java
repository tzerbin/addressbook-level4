//@@author tzerbin
package seedu.address.storage;

import java.io.IOException;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.appointment.ReadOnlyAppointmentList;

/**
 * Represents a storage for {@link seedu.address.model.appointment.AppointmentList}.
 */
public interface AppointmentListStorage {

    /**
     *  Returns the file path of the data file.
     */
    String getAppointmentListFilePath();

    /**
     * Returns AppointmentList data as a {@link ReadOnlyAppointmentList}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyAppointmentList> readAppointmentList() throws DataConversionException, IOException;

    /**
     * @see #readAppointmentList()
     */
    Optional<ReadOnlyAppointmentList> readAppointmentList(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyAppointmentList} to the storage.
     * @param appointmentList cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAppointmentList(ReadOnlyAppointmentList appointmentList) throws IOException;

    /**
     * @see #saveAppointmentList(ReadOnlyAppointmentList)
     */
    void saveAppointmentList(ReadOnlyAppointmentList appointmentList, String filePath) throws IOException;
}
