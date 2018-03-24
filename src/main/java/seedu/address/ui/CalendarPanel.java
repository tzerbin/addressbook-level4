package seedu.address.ui;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import javafx.application.Platform;

/**
 * The panel for the Calendar. Constructs a calendar view and attaches to it a CalendarSource.
 * The view is then returned by calling getCalendarView in MainWindow to attach it to the
 * browserPlaceholder.
 */
public class CalendarPanel {

    private CalendarView calendarView;

    public CalendarPanel(CalendarSource celebCalendarSource, CalendarSource storageCalendarSource) {
        CalendarView calendarView = new CalendarView();
        calendarView.getCalendarSources().clear(); // there is an existing default source when creating the view
        calendarView.getCalendarSources().add(celebCalendarSource);
        calendarView.getCalendarSources().add(storageCalendarSource);
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
}
