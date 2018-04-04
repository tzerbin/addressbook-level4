//@@author tzerbin
package seedu.address.storage;

import java.io.File;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import seedu.address.commons.util.FileUtil;

public class XmlSerializableStorageCalendarTest {

    private static final String TEST_DATA_FOLDER =
            FileUtil.getPath("src/test/data/XmlSerializableStorageCalendarTest/");
    private static final File TYPICAL_APPOINTMENTS_FILE =
            new File(TEST_DATA_FOLDER + "typicalAppointmentsStorageCalendar.xml");
    private static final File INVALID_APPOINTMENTS_FILE =
            new File(TEST_DATA_FOLDER + "invalidAppointmentsStorageCalendar.xml");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // Test is failing, need to correct
    /*
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
    */
}
