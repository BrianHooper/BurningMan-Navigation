import java.util.AbstractMap;

public class CoordinateListener extends Thread {
    private Navigator navigator;
    private View view;

    public CoordinateListener(Navigator navigator, View view) {
        this.navigator = navigator;
        this.view = view;
    }

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
