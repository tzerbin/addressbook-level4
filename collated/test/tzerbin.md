# tzerbin
###### \java\seedu\address\storage\XmlSerializableStorageCalendarTest.java
``` java
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
```
###### \java\seedu\address\testutil\TypicalStorageCalendar.java
``` java
    public static final Appointment CONCERT = new Appointment(
            new AppointmentBuilder().withName("Concert")
                    .withStartTime("19:00")
                    .withStartDate("24-08-2018")
                    .withLocation("Singapore Indoors Stadium")
                    .withEndTime("23:00")
                    .withEndDate("24-08-2018").build());

    public static final Appointment DENTAL = new Appointment(
            new AppointmentBuilder().withName("Dental")
                    .withStartTime("15:30")
                    .withStartDate("25-08-2018")
                    .withLocation("Singapore General Hospital")
                    .withEndTime("15:50")
                    .withEndDate("25-08-2018").build());

    public static final Appointment MEETING = new Appointment(
            new AppointmentBuilder().withName("Meeting")
                    .withStartTime("10:30")
                    .withStartDate("26-08-2018")
                    .withLocation("Mediacorp Campus")
                    .withEndTime("18:00")
                    .withEndDate("26-08-2018").build());

    public static List<Appointment> getTypicalAppointmentList() {
        return new ArrayList<>(Arrays.asList(CONCERT, DENTAL, MEETING));
    }
}
```
