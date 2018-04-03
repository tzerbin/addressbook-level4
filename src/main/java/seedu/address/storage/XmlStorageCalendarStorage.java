package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.calendar.StorageCalendar;

/**
 * A class to access StorageCalendar data stored as an xml file on the hard disk.
 */
public class XmlStorageCalendarStorage implements StorageCalendarStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlStorageCalendarStorage.class);

    private String filePath;

    public XmlStorageCalendarStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getStorageCalendarFilePath() {
        return filePath;
    }

    @Override
    public Optional<StorageCalendar> readStorageCalendar() throws FileNotFoundException, DataConversionException {
        return readStorageCalendar(filePath);
    }

    /**
     * Similar to {@link #readStorageCalendar()}
     * @param filePath location of the data. Cannot be null
     */
    public Optional<StorageCalendar> readStorageCalendar(String filePath)
            throws FileNotFoundException, DataConversionException {

        requireNonNull(filePath);
        File storageCalendarFile = new File(filePath);

        if (!storageCalendarFile.exists()) {
            logger.info("StorageCalendar file "  + storageCalendarFile + " not found");
            return Optional.empty();
        }


        XmlSerializableStorageCalendar xmlStorageCalendar = XmlFileStorage
                .loadStorageCalendarFromSaveFile(new File(filePath));

        try {
            return Optional.of(xmlStorageCalendar.toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + storageCalendarFile + ": " + ive.getMessage());
            throw new DataConversionException(ive);
        }
    }

    @Override
    public void saveStorageCalendar(StorageCalendar storageCalendar) throws IOException {
        saveStorageCalendar(storageCalendar, filePath);
    }

    /**
     * Similar to {@link #saveStorageCalendar(StorageCalendar)}
     * @param filePath location of the data. Cannot be null
     */
    @Override
    public void saveStorageCalendar(StorageCalendar storageCalendar, String filePath) throws IOException {
        requireNonNull(storageCalendar);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveStorageCalendarToFile(file, new XmlSerializableStorageCalendar(storageCalendar));
    }

}
