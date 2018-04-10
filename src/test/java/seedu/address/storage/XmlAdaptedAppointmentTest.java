package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.TypicalStorageCalendar.DENTAL;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.map.MapAddress;
import seedu.address.testutil.Assert;


public class XmlAdaptedAppointmentTest {
    private static final String INVALID_TITLE = "Event N@me";

    private static final String VALID_STARTTIME = DENTAL.getStartTime().toString();
    private static final String VALID_STARTDATE = DENTAL.getStartDate().toString();
    private static final String VALID_ENDTIME = DENTAL.getEndTime().toString();
    private static final String VALID_ENDDATE = DENTAL.getEndDate().toString();
    private static final String VALID_LOCATION = DENTAL.getMapAddress().toString();

    @Test
    public void toModelType_validAppointmentDetails_returnsAppointment() throws Exception {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(DENTAL);
        assertEquals(DENTAL, appointment.toModelType());
    }

    @Test
    public void toModelType_nullTitle_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(null,
                LocalTime.parse(VALID_STARTTIME),
                LocalDate.parse(VALID_STARTDATE),
                new MapAddress(VALID_LOCATION),
                LocalTime.parse(VALID_ENDTIME),
                LocalDate.parse(VALID_ENDDATE));
        String expectedMessage = Appointment.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }

    @Test
    public void toModelType_invalidTitle_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(INVALID_TITLE,
                LocalTime.parse(VALID_STARTTIME),
                LocalDate.parse(VALID_STARTDATE),
                new MapAddress(VALID_LOCATION),
                LocalTime.parse(VALID_ENDTIME),
                LocalDate.parse(VALID_ENDDATE));
        String expectedMessage = Appointment.MESSAGE_NAME_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
    }
}
