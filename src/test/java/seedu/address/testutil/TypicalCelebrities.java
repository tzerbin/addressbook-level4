package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.person.Celebrity;

//@@author WJY-norainu
/**
 * A utility class containing a list of {@code Celebrity} objects to be used in tests.
 */
public class TypicalCelebrities {
    public static final Celebrity JAY = new Celebrity(
            new PersonBuilder().withName("Jay Chou")
                    .withAddress("145, Taiwan")
                    .withEmail("jay@gmail.com")
                    .withPhone("134520789201")
                    .withTags("friends", "celebrity").build());

    public static final Celebrity AYANE = new Celebrity(
            new PersonBuilder().withName("Sakura Ayane")
                    .withAddress("Tokyo, Japan")
                    .withEmail("ayane@gmail.com")
                    .withPhone("5201314")
                    .withTags("celebrity", "colleagues").build());

    public static final Celebrity ROBERT = new Celebrity(
            new PersonBuilder().withName("Robert Downey")
                    .withAddress("USA")
                    .withEmail("ironman@firefox.com")
                    .withPhone("19650404")
                    .withTags("celebrity", "owesMoney").build());

    public static List<Celebrity> getTypicalCelebrities() {
        return new ArrayList<>(Arrays.asList(JAY, AYANE, ROBERT));
    }
}
