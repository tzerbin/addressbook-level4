package seedu.address.ui;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import com.google.common.eventbus.Subscribe;
import javafx.application.Platform;
import seedu.address.commons.events.ui.ChangeCalendarViewPageRequestEvent;

/**
 * The panel for the Calendar. Constructs a calendar view and attaches to it a CalendarSource.
 * The view is then returned by calling getCalendarView in MainWindow to attach it to the
 * browserPlaceholder.
 */
public class CalendarPanel {

    private CalendarView calendarView;

    public CalendarPanel(CalendarSource calendarSource) {
        CalendarView calendarView = new CalendarView();
        calendarView.getCalendarSources().addAll(calendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
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
        this.calendarView = calendarView;
    }

    public CalendarView getCalendarView() {
        return calendarView;
    }

    @Subscribe
    private void handleChangeCalendarViewPageRequestEvent(ChangeCalendarViewPageRequestEvent event) {
        switch(event.calendarViewPage) {
            case "day":
                calendarView.showDayPage();
                break;
            case "week":
                calendarView.showWeekPage();
                break;
            case "month":
                calendarView.showMonthPage();
                break;
            case "year":
                calendarView.showYearPage();
                break;
        }
    }
}
