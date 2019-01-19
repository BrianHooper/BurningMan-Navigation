package driver;

import navigation.Location;
import navigation.Navigator;
import view.View;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap;

/**
 * Class CoordinateListener
 * <p>
 * Reads GPS coordinates from file & updates view
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class CoordinateListener extends Thread {
    // Relative path to tracker file
    private static final String trackerPath = "config/tracker.csv";

    // Main navigator object
    private final Navigator navigator;

    // Main GUI view
    private final View view;

    /**
     * Constructor
     *
     * @param navigator Navigator object
     * @param view      main View
     */
    public CoordinateListener(Navigator navigator, View view) {
        this.navigator = navigator;
        this.view = view;
    }

    /**
     * Thread process
     * <p>
     * Listens for changes to GPS coordinate file
     * Writes coordinates and time to tracker file
     */
    public void run() {
        FileManager fileManager;
        try {
            fileManager = new FileManager(trackerPath);
            fileManager.appendLine("Program started," + ClockDriver.dfFull.format(LocalDateTime.now()));
        } catch(IOException e) {
            fileManager = null;
            System.err.println("Error opening tracker file");
        }

        int count = 0;
        int every = 20; //
        //noinspection InfiniteLoopStatement
        while(true) {
            try {
                AbstractMap.SimpleEntry<Double, Double> coordinates = Location.readCoordinates();
                if(coordinates != null) {
                    double latitude = coordinates.getKey();
                    double longitude = coordinates.getValue();
                    navigator.updateLocation(latitude, longitude);
                    view.setNavigation(navigator);
                    if(count++ >= every && fileManager != null) {
                        count = 0;
                        String line = String.valueOf(latitude) + ',' + String.valueOf(longitude) + ','
                                + ClockDriver.dfFull.format(LocalDateTime.now());
                        fileManager.appendLine(line);
                    }
                    Thread.sleep(500);
                }
            } catch(InterruptedException e) {
                System.err.println("Coordinate thread interrupted");
            }
        }
    }

}
