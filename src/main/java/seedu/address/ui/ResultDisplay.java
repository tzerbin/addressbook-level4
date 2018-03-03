package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    public static final String STYLE_ERROR_CLASS = "error";

    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "ResultDisplay.fxml";

    private final StringProperty displayed = new SimpleStringProperty("");

    @FXML
    private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
        resultDisplay.textProperty().bind(displayed);
        registerAsAnEventHandler(this);
    }
    /**
     * Sets the {@code ResultDisplay} style to the default style.
     */
    private void setStyleToShowCommandSuccess() {
        resultDisplay.getStyleClass().remove(STYLE_ERROR_CLASS);
    }

    /**
     * Sets the {@code ResultDisplay} style to show a failed command.
     */
    private void setStyleToShowCommandFailure() {
        ObservableList<String> styleClass = resultDisplay.getStyleClass();

        if (styleClass.contains(STYLE_ERROR_CLASS)) {
            return;
        }

        styleClass.add(STYLE_ERROR_CLASS);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent newEvent) {
        logger.info(LogsCenter.getEventHandlingLogMessage(newEvent));
        Platform.runLater(() -> {
            displayed.setValue(newEvent.message);

            if (newEvent.successful) {
                setStyleToShowCommandSuccess();
            } else {
                setStyleToShowCommandFailure();
            }
        });
    }

}
