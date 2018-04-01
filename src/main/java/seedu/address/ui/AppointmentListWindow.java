package seedu.address.ui;

import java.util.List;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.model.appointment.Appointment;

/**
 * Window to display the list of appointments using appointment cards
 */
public class AppointmentListWindow extends UiPart<Region> {

    private static final String FXML = "AppointmentListWindow.fxml";


    @FXML
    private ListView<AppointmentCard> appointmentListView;

    public AppointmentListWindow(Stage root, List<Appointment> appointments) {
        super(FXML);
        ObservableList<Appointment> observableAppts = FXCollections.observableArrayList(appointments);
        setConnections(observableAppts);
    }

    public AppointmentListWindow(List<Appointment> appointments) {
        this(new Stage(), appointments);
    }

    private void setConnections(ObservableList<Appointment> appointmentList) {
        ObservableList<AppointmentCard> mappedList = EasyBind.map(
                appointmentList, (appointment) -> new AppointmentCard(appointment,
                        appointmentList.indexOf(appointment) + 1));
        appointmentListView.setItems(mappedList);
        appointmentListView.setCellFactory(listView -> new AppointmentListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code AppointmentCard}.
     */
    class AppointmentListViewCell extends ListCell<AppointmentCard> {

        @Override
        protected void updateItem(AppointmentCard appointment, boolean empty) {
            super.updateItem(appointment, empty);

            if (empty || appointment == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(appointment.getRoot());
            }
        }
    }

}
