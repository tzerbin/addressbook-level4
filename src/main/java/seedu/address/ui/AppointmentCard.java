package seedu.address.ui;

import java.util.List;

import com.calendarfx.model.Entry;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.appointment.Appointment;

public class AppointmentCard extends UiPart<Region> {
    private static final String FXML = "AppointmentListCard.fxml";

    public final Appointment appt;

    @FXML
    private HBox appointmentCardPane;

    @FXML
    private Label name;

    @FXML
    private Label startTime;

    @FXML
    private Label startDate;

    @FXML
    private Label endTime;

    @FXML
    private Label endDate;

    @FXML
    private Label appointmentLocation;

    @FXML
    private Label celebrities;

    @FXML
    private Label id;

    public AppointmentCard(Appointment appt, int displayedIndex) {
        super(FXML);
        this.appt = appt;
        id.setText(displayedIndex + ". ");
        name.setText(appt.getTitle());
        startTime.setText("Start time: " + appt.getStartTime().format(Appointment.TIME_FORMAT));
        startDate.setText("Start date: " + appt.getStartDate().format(Appointment.DATE_FORMAT));
        endTime.setText("End time: " + appt.getEndTime().format(Appointment.TIME_FORMAT));
        endDate.setText("End date: " + appt.getEndDate().format(Appointment.DATE_FORMAT));
        appointmentLocation.setText(getLocation(appt));
        celebrities.setText(getCelebrities(appt));
    }

    private static String getLocation(Appointment appt) {
        if (appt.getLocation() == null) {
            return "No Location Data";
        } else {
            return "Location: " + appt.getLocation();
        }
    }

    private static String getCelebrities(Appointment appt) {
        List<Entry> childEntries = appt.getChildEntryList();
        if (childEntries.size() == 0) {
            return "No celebrities attending this appointment";
        } else {
            StringBuilder sb = new StringBuilder("Celebrities attending: ");
            for (Entry e : appt.getChildEntryList()) {
                sb.append(e.getCalendar().getName());
                sb.append(", ");
            }
            return sb.substring(0, sb.length() - 2);
        }

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (!(other instanceof AppointmentCard)) {
            return false;
        } else {
            AppointmentCard card = (AppointmentCard) other;
            return id.getText().equals(card.id.getText())
                    && appt.equals(card.appt);

        }
    }
}
