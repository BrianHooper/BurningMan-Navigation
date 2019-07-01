package driver;

import view.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClockDriver extends Thread {
    // Formatter for converting between Strings and LocalDateTime objects
    private static final DateTimeFormatter dfDay = DateTimeFormatter.ofPattern("EEEE");
    public static final DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("h:mma");
    public static final DateTimeFormatter dfFull = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter dfFilePath = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm");

    // Main GUI view
    private final View view;

    // Logger
    private final LogDriver logger = LogDriver.getInstance();

    // Keeps infinite loop running until instructed to stop
    private boolean keepRunning;

    /**
     * Constructor
     * <p>
     * Initializes with pointer to current view
     *
     * @param view View object
     */
    public ClockDriver(View view) {
        this.view = view;
        keepRunning = true;
    }

    /**
     * Thread process
     * <p>
     * Updates clock each second
     */
    public void run() {
        try {
            while(keepRunning) {
                LocalDateTime time = LocalDateTime.now();
                String timeString = dfDay.format(time) + ", " + dfTime.format(time);
                view.setClock(timeString);
                Thread.sleep(1000);
            }
        } catch(InterruptedException e) {
            logger.severe(this.getClass(), "Clock process interrupted: " + e.getMessage());
        }
    }

    /**
     * Function called when terminating application
     */
    public void terminate() {
        keepRunning = false;
    }

}
