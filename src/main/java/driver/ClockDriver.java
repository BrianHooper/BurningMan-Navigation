package driver;

import view.View;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClockDriver extends Thread {
    public static final DateTimeFormatter dfDay = DateTimeFormatter.ofPattern("EEEE");
    public static final DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("h:mm:ss a");
    public static final DateTimeFormatter dfFull = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final View view;

    /**
     * Constructor
     * <p>
     * Initializes with pointer to current view
     *
     * @param view View object
     */
    public ClockDriver(View view) {
        this.view = view;
    }

    /**
     * Thread process
     * <p>
     * Updates clock each second
     */
    public void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
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
