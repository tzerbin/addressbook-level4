# tzerbin
###### \java\seedu\address\storage\Storage.java
``` java
    @Override
    String getStorageCalendarFilePath();

    @Override
    Optional<StorageCalendar> readStorageCalendar() throws DataConversionException, IOException;

    @Override
    void saveStorageCalendar(StorageCalendar storageCalendar) throws IOException;

    /**
     * Saves the current version of the StorageCalendar to the hard disk.
     * Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleStorageCalendarChangedEvent(StorageCalendarChangedEvent alce);
}
```
###### \java\seedu\address\storage\StorageCalendarStorage.java
``` java
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
```
###### \java\seedu\address\storage\StorageManager.java
``` java
    // ================ StorageCalendar methods ==============================

    @Override
    public String getStorageCalendarFilePath() {
        return storageCalendarStorage.getStorageCalendarFilePath();
    }

    @Override
    public Optional<StorageCalendar> readStorageCalendar() throws DataConversionException, IOException {
        return readStorageCalendar(storageCalendarStorage.getStorageCalendarFilePath());
    }

    @Override
    public Optional<StorageCalendar> readStorageCalendar(String filePath)
            throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return storageCalendarStorage.readStorageCalendar(filePath);
    }

    @Override
    public void saveStorageCalendar(StorageCalendar storageCalendar) throws IOException {
        saveStorageCalendar(storageCalendar, storageCalendarStorage.getStorageCalendarFilePath());
    }

    @Override
    public void saveStorageCalendar(StorageCalendar storageCalendar, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        storageCalendarStorage.saveStorageCalendar(storageCalendar, filePath);
    }

    @Override
    @Subscribe
    public void handleStorageCalendarChangedEvent(StorageCalendarChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveStorageCalendar(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedAppointment.java
``` java
package seedu.address.storage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.map.MapAddress;

/**
 * JAXB-friendly version of an Appointment.
 */
public class XmlAdaptedAppointment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Appointment's %s field is missing!";

    @XmlElement(required = true)
    private String title;
    @XmlElement
    private String location;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String startTime;
    @XmlElement(required = true)
    private String endDate;
    @XmlElement(required = true)
    private String endTime;

    /**
     * Constructs an XmlAdaptedAppointment.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAppointment() {}

    /**
     * Constructs an {@code XmlAdaptedAppointment} with the given appointment details.
     */
    public XmlAdaptedAppointment(String title, LocalTime startTime, LocalDate startDate, MapAddress mapAddress,
                                 LocalTime endTime, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate.toString();
        this.startTime = startTime.toString();
        this.endDate = endDate.toString();
        this.endTime = endTime.toString();

        if (mapAddress != null) {
            location = mapAddress.toString();
        }
    }

    /**
     * Converts a given Appointment into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedAppointment
     */
    public XmlAdaptedAppointment(Appointment source) {
        title = source.getTitle();
        startDate = source.getStartDate().toString();
        startTime = source.getStartTime().toString();
        endDate = source.getEndDate().toString();
        endTime = source.getEndTime().toString();

        if (source.getMapAddress() != null) {
            location = source.getMapAddress().toString();
        }
    }

    /**
     * Converts this jaxb-friendly adapted appointment object into the model's Appointment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Appointment toModelType() throws IllegalValueException {

        LocalDate startDateCreated;
        LocalTime startTimeCreated;
        LocalDate endDateCreated;
        LocalTime endTimeCreated;

        if (startTime == null) {
            throw new IllegalValueException(Appointment.MESSAGE_TIME_CONSTRAINTS);
        }
        try {
            startTimeCreated = LocalTime.parse(startTime);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_TIME_CONSTRAINTS);
        }

        if (startDate == null) {
            throw new IllegalValueException(Appointment.MESSAGE_DATE_CONSTRAINTS);
        }
        try {
            startDateCreated = LocalDate.parse(startDate);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_DATE_CONSTRAINTS);
        }

        if (endTime == null) {
            throw new IllegalValueException(String.format(Appointment.MESSAGE_TIME_CONSTRAINTS,
                    LocalTime.class.getSimpleName()));
        }
        try {
            endTimeCreated = LocalTime.parse(endTime);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_TIME_CONSTRAINTS);
        }

        if (endDate == null) {
            throw new IllegalValueException(String.format(Appointment.MESSAGE_DATE_CONSTRAINTS,
                    LocalTime.class.getSimpleName()));
        }
        try {
            endDateCreated = LocalDate.parse(endDate);
        } catch (DateTimeParseException e) {
            throw new IllegalValueException(Appointment.MESSAGE_DATE_CONSTRAINTS);
        }

        if (title == null) {
            throw new IllegalValueException(Appointment.MESSAGE_NAME_CONSTRAINTS);
        }
        if (!Appointment.isValidName(title)) {
            throw new IllegalValueException(Appointment.MESSAGE_NAME_CONSTRAINTS);
        }
        final String appointmentName = title;

        MapAddress mapAddressCreated = null;
        if (location != null) {
            if (!MapAddress.isValidAddress(location)) {
                throw new IllegalValueException(String.format(MapAddress.MESSAGE_ADDRESS_MAP_CONSTRAINTS));
            }

            mapAddressCreated = new MapAddress(location);
        }

        return new Appointment(appointmentName,
                               startTimeCreated,
                               startDateCreated,
                               mapAddressCreated,
                               endTimeCreated,
                               endDateCreated);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAppointment)) {
            return false;
        }

        XmlAdaptedAppointment otherAppointment = (XmlAdaptedAppointment) other;
        return Objects.equals(title, otherAppointment.title)
                && Objects.equals(startDate, otherAppointment.startDate)
                && Objects.equals(startTime, otherAppointment.startTime)
                && Objects.equals(endDate, otherAppointment.endDate)
                && Objects.equals(endTime, otherAppointment.endTime)
                && Objects.equals(location, otherAppointment.location);
    }
}
```
###### \java\seedu\address\storage\XmlSerializableStorageCalendar.java
``` java
package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.calendar.StorageCalendar;

/**
 * An Immutable StorageCalendar that is serializable to XML format
 */
@XmlRootElement(name = "storagecalendar")
public class XmlSerializableStorageCalendar {

    @XmlElement
    private List<XmlAdaptedAppointment> appointments;

    /**
     * Creates an empty XMLSerializableStorageCalendar.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableStorageCalendar() {
        appointments = new ArrayList<>();
    }

    public XmlSerializableStorageCalendar(StorageCalendar storageCalendar) {
        this();
        List<Appointment> appointmentList = getStoredAppointmentList();
        for (Appointment appt : appointmentList) {
            appointments.add(new XmlAdaptedAppointment(appt));
        }
    }

    /**
     * Converts XMLAdapterAppointments into a {@code StorageCalendar} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedAppointments}.
     */
    public StorageCalendar toModelType() throws IllegalValueException {
        StorageCalendar calendar = new StorageCalendar("Storage Calendar");
        for (XmlAdaptedAppointment a : appointments) {
            calendar.addEntry(a.toModelType());
        }
        return calendar;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableStorageCalendar)) {
            return false;
        }

        XmlSerializableStorageCalendar otherAl = (XmlSerializableStorageCalendar) other;
        return appointments.equals(otherAl.appointments);
    }
}
```
###### \java\seedu\address\storage\XmlStorageCalendarStorage.java
``` java
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
```
