package seedu.address.model;

import java.util.Objects;

import seedu.address.commons.core.GuiSettings;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    private GuiSettings guiSettings;
    private String addressBookFilePath = "data/addressbook.xml";
    private String addressBookName = "MyAddressBook";
    private String storageCalendarFilePath = "data/storagecalendar.xml";
    private String storageCalendarName = "MyCalendar";

    public UserPrefs() {
        this.setGuiSettings(500, 500, 0, 0);
    }

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }

    public String getAddressBookFilePath() {
        return addressBookFilePath;
    }

    public void setAddressBookFilePath(String addressBookFilePath) {
        this.addressBookFilePath = addressBookFilePath;
    }

    public String getAddressBookName() {
        return addressBookName;
    }

    public void setAddressBookName(String addressBookName) {
        this.addressBookName = addressBookName;
    }

    public String getStorageCalendarFilePath() {
        return storageCalendarFilePath;
    }

    public void setStorageCalendarFilePath(String storageCalendarFilePath) {
        this.storageCalendarFilePath = storageCalendarFilePath;
    }

    public String getStorageCalendarName() {
        return storageCalendarName;
    }

    public void setStorageCalendarName(String storageCalendarName) {
        this.storageCalendarName = storageCalendarName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UserPrefs)) { //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs) other;

        return Objects.equals(guiSettings, o.guiSettings)
                && Objects.equals(addressBookFilePath, o.addressBookFilePath)
                && Objects.equals(addressBookName, o.addressBookName)
                && Objects.equals(storageCalendarFilePath, o.storageCalendarFilePath)
                && Objects.equals(storageCalendarName, o.storageCalendarName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings, addressBookFilePath, addressBookName,
                storageCalendarFilePath, storageCalendarName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gui Settings : " + guiSettings.toString());
        sb.append("\nLocal contacts data file location : " + addressBookFilePath);
        sb.append("\nAddressBook name : " + addressBookName);
        sb.append("\nLocal calendar data file location : " + storageCalendarFilePath);
        sb.append("\nCalendar name : " + storageCalendarName);
        return sb.toString();
    }

}
