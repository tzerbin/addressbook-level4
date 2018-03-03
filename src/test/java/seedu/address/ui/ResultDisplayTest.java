package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static seedu.address.testutil.EventsUtil.postNow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.ResultDisplayHandle;
import seedu.address.commons.events.ui.NewResultAvailableEvent;

public class ResultDisplayTest extends GuiUnitTest {

    private static final NewResultAvailableEvent NEW_RESULT_EVENT_STUB_FAILURE = new NewResultAvailableEvent("failure", false);
    private static final NewResultAvailableEvent NEW_RESULT_EVENT_STUB_SUCCESS = new NewResultAvailableEvent("success", true);
    private List<String> styleOfResultDisplayDefault;
    private List<String> styleOfResultDisplayError;

    private ResultDisplayHandle resultDisplayHandle;

    @Before
    public void setUp() {
        ResultDisplay resultDisplay = new ResultDisplay();
        uiPartRule.setUiPart(resultDisplay);

        resultDisplayHandle = new ResultDisplayHandle(getChildNode(resultDisplay.getRoot(), ResultDisplayHandle.RESULT_DISPLAY_ID));
        styleOfResultDisplayDefault = new ArrayList<>(resultDisplayHandle.getClassStyle());
        styleOfResultDisplayError = new ArrayList<>(styleOfResultDisplayDefault);
        styleOfResultDisplayError.add(ResultDisplay.STYLE_ERROR_CLASS);
    }

    /**
     * Posts the {@code event} to the {@code EventsCenter}, then verifies that <br>
     *      - the text on the result display matches the {@code event}'s message <br>
     *      - the result display's style is the same as {@code styleOfResultDisplayDefault} if event is successful,
     *        {@code styleOfResultDisplayError} otherwise.
     */
    private void assertResultDisplay(NewResultAvailableEvent event) {
        postNow(event);
        guiRobot.pauseForHuman();
        List<String> expectedStyleClass = event.successful ? styleOfResultDisplayDefault : styleOfResultDisplayError;
        assertEquals(event.message, resultDisplayHandle.getText());
        assertEquals(expectedStyleClass, resultDisplayHandle.getClassStyle());
    }

    @Test
    public void display() {
        // default result text
        guiRobot.pauseForHuman();
        assertEquals("", resultDisplayHandle.getText());
        assertEquals(styleOfResultDisplayDefault, resultDisplayHandle.getClassStyle());

        // receiving new results
        assertResultDisplay(NEW_RESULT_EVENT_STUB_SUCCESS);
        assertResultDisplay(NEW_RESULT_EVENT_STUB_FAILURE);
    }

}
