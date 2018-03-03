package guitests.guihandles;

import javafx.scene.control.TextArea;
import java.util.List;

/**
 * A handler for the {@code ResultDisplay} of the UI
 */
public class ResultDisplayHandle extends NodeHandle<TextArea> {

    public static final String RESULT_DISPLAY_ID = "#resultDisplay";

    public ResultDisplayHandle(TextArea resultDisplayNode) {
        super(resultDisplayNode);
    }
    /**
     *Returns the list of style classes present in the result display.
     */
    public List<String> getClassStyle() {
        return getRootNode().getStyleClass();
    }
    /**
     * Returns the text in the result display.
     */
    public String getText() {
        return getRootNode().getText();
    }
}
