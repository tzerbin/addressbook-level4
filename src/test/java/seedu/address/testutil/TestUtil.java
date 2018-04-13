package seedu.address.testutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.Model;
import seedu.address.model.person.Celebrity;
import seedu.address.model.person.Person;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final String SANDBOX_FOLDER = FileUtil.getPath("./src/test/data/sandbox/");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting string.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static String getFilePathInSandboxFolder(String fileName) {
        try {
            FileUtil.createDirs(new File(SANDBOX_FOLDER));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER + fileName;
    }

    /**
     * Returns the middle index of the person in the {@code model}'s person list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getAddressBook().getPersonList().size() / 2);
    }

    /**
     * Returns the last index of the person in the {@code model}'s person list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getAddressBook().getPersonList().size());
    }

    /**
     * Returns the person in the {@code model}'s person list at {@code index}.
     */
    public static Person getPerson(Model model, Index index) {
        return model.getAddressBook().getPersonList().get(index.getZeroBased());
    }

    /**
     * Returns the list of indices that correspond to the given celebrity list
     */
    public static List<Index> getCelebrityIndices(Model model, List<Celebrity> celebrityList) {
        List<Index> personIndices = new ArrayList<>();
        for (Celebrity c : celebrityList) {
            personIndices.add(getIndex(model, c));
        }
        return personIndices;
    }

    /**
     * Returns the list of indices that correspond to the given person list
     */
    public static List<Index> getPersonIndices(Model model, List<Person> personList) {
        List<Index> personIndices = new ArrayList<>();
        for (Person p : personList) {
            personIndices.add(getIndex(model, p));
        }
        return personIndices;
    }

    /**
     * Returns the index of the person
     */
    public static Index getIndex(Model model, Person person) {
        return Index.fromZeroBased(model.getAddressBook().getPersonList().indexOf(person));
    }
}
