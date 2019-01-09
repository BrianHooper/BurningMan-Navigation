

public class Controller {
    private Location currentLocation;
    private Landmarks landmarks;
    private View view;

    public Controller(Location location, Landmarks landmarks, View view) {
        this.currentLocation = location;
        this.landmarks = landmarks;
        this.view = view;
    }

    public void setLocation(double latitude, double longitude) {
        this.currentLocation.updateLocation(latitude, longitude);
    }

    public void updateView() {
        setLocation(40.77668, -119.210048);
        view.setAddress(currentLocation);
    }

    public void updateLocation() {
        String latitude = view.readLatitude();
        String longitude = view.readLongitude();

        try {
            double lat = Double.parseDouble(latitude);
            double lon = Double.parseDouble(longitude);
            setLocation(lat, lon);
            view.setAddress(currentLocation);
        } catch (NumberFormatException e) {

        }
    }
}
