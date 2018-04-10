//@@author tzerbin
package seedu.address.storage;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.XmlUtil;
import seedu.address.testutil.TypicalStorageCalendar;


public class XmlSerializableStorageCalendarTest {

    private static final String TEST_DATA_FOLDER =
            FileUtil.getPath("src/test/data/XmlSerializableStorageCalendarTest/");
    private static final File TYPICAL_APPOINTMENTS_FILE =
            new File(TEST_DATA_FOLDER + "typicalAppointmentsStorageCalendar.xml");
    private static final File INVALID_APPOINTMENTS_FILE =
            new File(TEST_DATA_FOLDER + "invalidAppointmentsStorageCalendar.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Test
    public void toModelType_typicalAppointmentsFile_success() throws Exception {
        XmlSerializableStorageCalendar dataFromFile =
                XmlUtil.getDataFromFile(TYPICAL_APPOINTMENTS_FILE, XmlSerializableStorageCalendar.class);
        List storageCalendarFromFile = dataFromFile.toModelType().getAllAppointments();
        List typicalAppointmentList = TypicalStorageCalendar.getTypicalAppointmentList();
        assertEquals(storageCalendarFromFile, typicalAppointmentList);
    }

    @Test
    public void toModelType_invalidAppointmentFile_throwsDateTimeParseException() throws Exception {
        XmlSerializableStorageCalendar dataFromFile =
                XmlUtil.getDataFromFile(INVALID_APPOINTMENTS_FILE, XmlSerializableStorageCalendar.class);
        thrown.expect(IllegalValueException.class);
        dataFromFile.toModelType();
    }
    
}
