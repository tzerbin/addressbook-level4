package seedu.address.ui;

import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_CALENDARVIEW;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Logger;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.AgendaView;
import com.calendarfx.view.CalendarView;

import com.calendarfx.view.page.DayPage;
import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.AgendaViewPageRequestEvent;
import seedu.address.commons.events.ui.ChangeCalendarRequestEvent;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedToCelebrityEvent;
import seedu.address.commons.events.ui.ShowCombinedCalendarViewRequestEvent;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.calendar.CelebCalendar;
import seedu.address.model.person.Celebrity;

/**
 * The panel for the Calendar. Constructs a calendar view and attaches to it a CalendarSource.
 * The view is then returned by calling getCalendarView in MainWindow to attach it to the
 * calendarPlaceholder.
 */
public class CalendarPanel extends UiPart<Region> {

    private static final String FXML = "CalendarPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private CalendarView celebCalendarView;

    private final CalendarSource celebCalendarSource;
    private final CalendarSource storageCalendarSource;

    public CalendarPanel(CalendarSource celebCalendarSource, CalendarSource storageCalendarSource) {
        super(FXML);
        this.celebCalendarView = new CalendarView();
        this.celebCalendarSource = celebCalendarSource;
        this.storageCalendarSource = storageCalendarSource;

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);

        registerAsAnEventHandler(this);

        // To set up the calendar view.
        setUpCelebCalendarView();
    }

    private void setUpCelebCalendarView() {
        celebCalendarView.getCalendarSources().clear(); // there is an existing default source when creating the view
        celebCalendarView.getCalendarSources().add(celebCalendarSource);
        celebCalendarView.setRequestedTime(LocalTime.now());
        celebCalendarView.getDayPage().setDayPageLayout(DayPage.DayPageLayout.DAY_ONLY);
        celebCalendarView.showDayPage();

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        celebCalendarView.setToday(LocalDate.now());
                        celebCalendarView.setTime(LocalTime.now());
                    });
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        };
        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        hideButtons();
    }

    /** Hide all buttons in the calendar */
    private void hideButtons() {
        celebCalendarView.setShowSearchField(false);
        celebCalendarView.setShowSourceTrayButton(false);
        celebCalendarView.setShowAddCalendarButton(false);
        celebCalendarView.setShowPrintButton(false);
        celebCalendarView.setShowPageToolBarControls(false);
        celebCalendarView.setShowPageSwitcher(false);
        celebCalendarView.setShowToolBar(false);
    }

    public CalendarView getCalendarView() {
        return celebCalendarView;
    }

    @Subscribe
    private void handleChangeCalendarViewPageRequestEvent(ChangeCalendarViewPageRequestEvent event) {
        String calendarViewPage = event.calendarViewPage;

        Platform.runLater(() -> {
            celebCalendarView.showDate(LocalDate.now());
            celebCalendarView.getCalendarSources().clear();
            celebCalendarView.getCalendarSources().add(celebCalendarSource);
            switch (calendarViewPage) {

            case "day":
                celebCalendarView.getDayPage().setDayPageLayout(DayPage.DayPageLayout.DAY_ONLY);
                celebCalendarView.showDayPage();
                break;
            case "week":
                celebCalendarView.showWeekPage();
                break;
            case "month":
                celebCalendarView.showMonthPage();
                break;
            case "year":
                celebCalendarView.showYearPage();
                break;

            default:
                try {
                    throw new ParseException(MESSAGE_UNKNOWN_CALENDARVIEW);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe
    private void handleAgendaViewPageRequestEvent(AgendaViewPageRequestEvent event) {
        Platform.runLater(() -> {
            celebCalendarView.getCalendarSources().clear();
            celebCalendarView.getCalendarSources().add(storageCalendarSource);

            celebCalendarView.getDayPage().setDayPageLayout(DayPage.DayPageLayout.AGENDA_ONLY);
            AgendaView agendaView = celebCalendarView.getDayPage().getAgendaView();
            agendaView.setLookAheadPeriodInDays(event.getStartEndDateDifference());
            celebCalendarView.showDate(event.getStartDate());
        });
    }

    /** Shows the calendar of the specified {@code celebrity} */
    private void showCalendarOf(Celebrity celebrity) {
        CelebCalendar celebCalendarToShow = celebrity.getCelebCalendar();
        ObservableMap<Calendar, BooleanProperty> calendars =
                celebCalendarView.getSourceView().getCalendarVisibilityMap();
        Platform.runLater(() -> {
            for (Calendar calendar: calendars.keySet()) {
                if (!calendar.equals(celebCalendarToShow)) {
                    celebCalendarView.getSourceView().setCalendarVisibility(calendar, false);
                } else {
                    celebCalendarView.getSourceView().setCalendarVisibility(calendar, true);
                }
            }
        });
    }

    //keep this method to load calendar if the selected person is a celeb
    @Subscribe
    private void handlePersonPanelSelectionChangedToCelebrityEvent(PersonPanelSelectionChangedToCelebrityEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showCalendarOf((Celebrity) event.getNewSelection().person);
    }

    @Subscribe
    private void handleCalendarChangeRequestEvent(ChangeCalendarRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showCalendarOf(event.celebrity);
    }

    /** Shows a combined calendar that contains {@code appointment}s for all {@code celebrity}s */
    private void showAllCalendars() {
        ObservableMap<Calendar, BooleanProperty> calendars =
                celebCalendarView.getSourceView().getCalendarVisibilityMap();
        Platform.runLater(() -> {
            for (Calendar calendar: calendars.keySet()) {
                celebCalendarView.getSourceView().setCalendarVisibility(calendar, true);
            }
        });
    }

    @Subscribe
    private void handleShowCombinedCalendarViewRequestEvent(ShowCombinedCalendarViewRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        showAllCalendars();
    }
}
