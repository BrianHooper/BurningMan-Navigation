package navigation;

import java.util.*;

public class Navigator {
    private Location currentLocation;
    private Location home;
    private Location currentDestination;
    private String currentDestinationName;

    public HashMap<String, Location> getCamps() {
        return landmarks.getCamps();
    }

    private final Landmarks landmarks;

    public Navigator() {
        //todo read previous values from file

        landmarks = new Landmarks();
        landmarks.addBathroom(4,30,'D');
        landmarks.addBathroom(7,30,'D');

        currentLocation = new Location(6,0,'D');
        home = new Location(6,0,'D');

        currentDestination = null;
        currentDestinationName = "";

    }

    public void updateLocation(int hour, int minute, char street) {
        currentLocation = new Location(hour, minute, street);
    }

    public void updateLocation(double latitude, double longitude) {
        currentLocation.updateLocation(latitude, longitude);
    }

    public String findCampName(String campName) {
        return landmarks.findCampName(campName);
    }

    public Location getCamp(String exactCampName) {
        return landmarks.getCamp(exactCampName);
    }

    public void initializeLandmarks(String bathroomsPath, String campsPath) {
        landmarks.readBathrooms(bathroomsPath);
        landmarks.readCamps(campsPath);
    }

    public TreeMap<String, String> getPanelUpdate() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("currentAddress", currentLocation.getAddress());
        Location closestBathroom = landmarks.findBathroom(currentLocation);
        map.put("bathroomAddress", closestBathroom.getAddress());
        map.put("bathroomDirections", currentLocation.distance(closestBathroom) +
                ", " + currentLocation.cardinal(closestBathroom));
        map.put("homeAddress", home.getAddress());
        map.put("homeDirections", currentLocation.distance(home) + ", " + currentLocation.cardinal(home));
        if(currentDestination != null) {
            map.put("destinationName", currentDestinationName);
            map.put("destinationAddress", currentDestination.getAddress());
            map.put("destinationDirections", currentLocation.distance(currentDestination) +
                    ", " + currentLocation.cardinal(currentDestination));
        }
        return map;
    }

    public void setDestination(Location location, String name) {
        this.currentDestinationName = name;
        this.currentDestination = location;
    }

    public void setHome(int hour, int minute, char street) {
        this.home = new Location(hour, minute, street);
    }

    public void setManCoordinates() {
        AbstractMap.SimpleEntry<Double, Double> coordinates = Location.readCoordinates("coordinate");
        if(coordinates != null) {
            Location.man_latitude = coordinates.getValue();
            Location.man_latitude = coordinates.getKey();
        }
    }

    public HashMap<String, Location> getFavorites() {
        return landmarks.getFavorites();
    }

    public Location currentLocation() {
        return currentLocation;
    }
}
