package seedu.address.logic.map;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
//@@author Damienskt
public class GoogleWebServicesTest {

    private GoogleWebServices test;

    @Test
    public void isValidConnection() {

        test = new GoogleWebServices();

        //Check valid connection
        assertTrue(test.checkInitialisedConnection());
    }
}
