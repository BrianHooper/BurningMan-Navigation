import navigation.Location;
import navigation.Navigator;
import view.View;

import java.util.AbstractMap;

/**
 * class CoordinateListener
 *
 * Reads GPS coordinates from file & updates view
 */
public class CoordinateListener extends Thread {
    private Navigator navigator;
    private View view;

    /**
     * Constructor
     * @param navigator Navigator object
     * @param view main View
     */
    public CoordinateListener(Navigator navigator, View view) {
        this.navigator = navigator;
        this.view = view;
    }

    /**
     * Thread process
     *
     * Listens for changes to GPS coordinate file
     */
    public void run() {
        while(true) {
            try {
                AbstractMap.SimpleEntry<Double, Double> coordinates = Location.readCoordinates("coordinate");
                if (coordinates != null) {
                    double latitude = coordinates.getKey();
                    double longitude = coordinates.getValue();
                    navigator.updateLocation(latitude, longitude);
                    view.setNavigation(navigator);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                System.err.println("Coordinate thread interrupted");
            }
        }
    }

}
