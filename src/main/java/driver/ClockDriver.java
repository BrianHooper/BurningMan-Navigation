package driver;

import view.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClockDriver extends Thread {
    private static final DateTimeFormatter dfDay = DateTimeFormatter.ofPattern("EEEE");
    private static final DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("h:mm:ss a");
    private final View view;

    /**
     * Constructor
     *
     * Initializes with pointer to current view
     * @param view View object
     */
    public ClockDriver(View view) {
        this.view = view;
    }

    /**
     * Thread process
     *
     * Updates clock each second
     */
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while(true) {
                LocalDateTime time = LocalDateTime.now();
                String timeString = dfDay.format(time) + ", " + dfTime.format(time);
                view.setClock(timeString);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Clock process interrupted");
        }
    }

}
