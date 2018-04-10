//@author: tzerbin
package seedu.address.storage;

import static seedu.address.testutil.TypicalStorageCalendar.DENTAL;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

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

    /*
    @Test
    public void toModelType_validAppointmentDetails_returnsAppointment() throws Exception {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(DENTAL);
        assertEquals(DENTAL, appointment.toModelType());
    }
    */

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

    //TODO: Problem - parsing valid fields will lead to null captured in XmlAdaptedAppointment
    @Test
    public void toModelType_invalidStartTime_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment();
        try {
            appointment = new XmlAdaptedAppointment(VALID_TITLE,
                            LocalTime.parse(INVALID_STARTTIME),
                            LocalDate.parse(VALID_STARTDATE),
                            new MapAddress(VALID_LOCATION),
                            LocalTime.parse(VALID_ENDTIME),
                            LocalDate.parse(VALID_ENDDATE));
        } catch (DateTimeParseException e) {
            String expectedMessage = Appointment.MESSAGE_TIME_CONSTRAINTS;
            Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
        }
    }

    /*
    @Test
    public void toModelType_invalidStartDate_throwsIllegalValueException() {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment();
        try {
            appointment = new XmlAdaptedAppointment(VALID_TITLE,
                    LocalTime.parse(VALID_STARTTIME),
                    LocalDate.parse(INVALID_STARTDATE),
                    new MapAddress(VALID_LOCATION),
                    LocalTime.parse(VALID_ENDTIME),
                    LocalDate.parse(VALID_ENDDATE));
        } catch (DateTimeParseException e) {
            String expectedMessage = Appointment.MESSAGE_DATE_CONSTRAINTS;
            Assert.assertThrows(IllegalValueException.class, expectedMessage, appointment::toModelType);
        }
    }
    */
    /*
    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_PHONE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_EMAIL_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_ADDRESS_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        XmlAdaptedPerson person = new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<XmlAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new XmlAdaptedTag(INVALID_TAG));
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags);
        Assert.assertThrows(IllegalValueException.class, person::toModelType);
    }
    */
}
