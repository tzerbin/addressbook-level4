package seedu.address.logic.commands.calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_GRAMMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_OSCAR;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPOINTMENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_APPOINTMENT;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalStorageCalendar.CONCERT;
import static seedu.address.testutil.TypicalStorageCalendar.EMPTY_CALENDAR;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.calendar.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.EditAppointmentDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditAppointmentCommand.
 */
public class EditAppointmentCommandTest {

    private Model model;

    @Test
    public void execute_allFieldsSpecifiedListingAppointments_success() {
        prepareModel(CONCERT);

        Appointment editedAppointment = new AppointmentBuilder().build();
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder(editedAppointment).build();
        EditAppointmentCommand editAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT, descriptor);
        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_SUCCESS, editedAppointment.getTitle());
        Model expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.addAppointmentToStorageCalendar(editedAppointment);
        expectedModel.setIsListingAppointments(false);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_someFieldsSpecifiedListingAppointments_success() {
        prepareModel(CONCERT);

        Appointment editedAppointment = new AppointmentBuilder(CONCERT)
                .withName("New Concert").withStartTime("15:00").withEndTime("16:00").build();
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder()
                .withName("New Concert").withStartTime("15:00").withEndTime("16:00").build();
        EditAppointmentCommand editAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT, descriptor);
        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_SUCCESS, editedAppointment.getTitle());
        Model expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.addAppointmentToStorageCalendar(editedAppointment);
        expectedModel.setIsListingAppointments(false);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldsSpecifiedListingAppointments_success() throws CommandException {
        prepareModel(CONCERT);

        EditAppointmentCommand editAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT,
                new EditAppointmentDescriptor());
        Appointment editedAppointment = model.getChosenAppointment(INDEX_FIRST_APPOINTMENT.getZeroBased());
        String expectedMessage = String.format(EditAppointmentCommand.MESSAGE_SUCCESS, editedAppointment.getTitle());
        Model expectedModel = new ModelManager(model.getAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        expectedModel.addAppointmentToStorageCalendar(editedAppointment);
        expectedModel.setIsListingAppointments(false);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validIndexNotListingAppointments_throwsCommandException() {
        prepareModel(CONCERT);
        model.setIsListingAppointments(false);

        Appointment editedAppointment = new AppointmentBuilder().build();
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder(editedAppointment).build();
        EditAppointmentCommand editAppointmentCommand = prepareCommand(INDEX_FIRST_APPOINTMENT, descriptor);

        assertCommandFailure(editAppointmentCommand,
                model, Messages.MESSAGE_MUST_SHOW_LIST_OF_APPOINTMENTS);
    }

    @Test
    public void execute_invalidIndexListingAppointments_throwsCommandException() {
        prepareModel(CONCERT);

        Index outOfBoundIndex = Index.fromOneBased(model.getAppointmentList().size() + 1);
        Appointment editedAppointment = new AppointmentBuilder().build();
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptorBuilder(editedAppointment).build();
        EditAppointmentCommand editAppointmentCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editAppointmentCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditAppointmentCommand standardCommand = prepareCommand(INDEX_FIRST_APPOINTMENT, DESC_OSCAR);

        // same values -> returns true
        EditAppointmentDescriptor copyDescriptor = new EditAppointmentDescriptor(DESC_OSCAR);
        EditAppointmentCommand commandWithSameValues = prepareCommand(INDEX_FIRST_APPOINTMENT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditAppointmentCommand(INDEX_SECOND_APPOINTMENT, DESC_OSCAR)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditAppointmentCommand(INDEX_FIRST_APPOINTMENT, DESC_GRAMMY)));
    }

    /**
     * Returns an {@code EditAppointmentCommand} with parameters {@code index} and {@code descriptor}
     */
    private EditAppointmentCommand prepareCommand(Index index, EditAppointmentDescriptor descriptor) {
        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(index, descriptor);
        editAppointmentCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editAppointmentCommand;
    }

    /**
     * Add appointment to storageCalendar Model and change isListingAppointment attribute of model
     */
    private void prepareModel(Appointment appt) {
        model = new ModelManager(getTypicalAddressBook(), EMPTY_CALENDAR, new UserPrefs());
        model.addAppointmentToStorageCalendar(appt);
        model.setIsListingAppointments(true);
        model.setCurrentlyDisplayedAppointments(model.getStoredAppointmentList());
    }
}
