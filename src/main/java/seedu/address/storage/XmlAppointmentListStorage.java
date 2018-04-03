//@@author tzerbin
package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.appointment.ReadOnlyAppointmentList;

/**
 * A class to access AppointmentList data stored as an xml file on the hard disk.
 */
public class XmlAppointmentListStorage implements AppointmentListStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlAppointmentListStorage.class);

    private String filePath;

    public XmlAppointmentListStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getAppointmentListFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAppointmentList> readAppointmentList() {
        return readAppointmentList(filePath);
    }

    /**
     * Similar to {@link #readAppointmentList()}
     * @param filePath location of the data. Cannot be null
     */
    public Optional<ReadOnlyAppointmentList> readAppointmentList(String filePath) {
        requireNonNull(filePath);

        File appointmentListFile = new File(filePath);

        if (!appointmentListFile.exists()) {
            logger.info("AppointmentList file "  + appointmentListFile + " not found");
            return Optional.empty();
        }

        /*
         * XmlSerializableAppointmentList xmlAppointmentList =
         *      XmlFileStorage.loadAppointmentListFromSaveFile(new File(filePath));
         */

        /*
         * TODO
         *try {
         *    return Optional.of(xmlAppointmentList.toModelType());
         *} catch (IllegalValueException ive) {
         *    logger.info("Illegal values found in " + appointmentListFile + ": " + ive.getMessage());
         *    throw new DataConversionException(ive);
         *}
         */
        return Optional.empty();
    }

    @Override
    public void saveAppointmentList(ReadOnlyAppointmentList appointmentList) throws IOException {
        saveAppointmentList(appointmentList, filePath);
    }

    /**
     * Similar to {@link #saveAppointmentList(ReadOnlyAppointmentList)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveAppointmentList(ReadOnlyAppointmentList appointmentList, String filePath) throws IOException {
        requireNonNull(appointmentList);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveAppointmentListToFile(file, new XmlSerializableAppointmentList(appointmentList));
    }

}
