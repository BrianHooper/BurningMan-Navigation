package navigation;

import java.io.*;
import java.util.*;

public class Navigator {
    private Location currentLocation;
    private Location home;
    private Location currentDestination;
    private String currentDestinationName;
    private Properties properties;
    private final Landmarks landmarks;

    /**
     * Constructor
     *
     * initializes current location, landmarks, home, destination
     */
    public Navigator() {
        landmarks = new Landmarks();

        currentLocation = new Location(6,0,'D');
        home = null;

        currentDestination = null;
        currentDestinationName = "";
    }

    /**
     * Reads configuration file
     * @param filename path to configuration file
     */
    public void loadFromFile(String filename) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(filename)));
        } catch (IOException e) {
            System.err.println(filename + " not found");
            return;
        }

        try {
            if(properties.containsKey("MAN-LATITUDE"))
                Location.man_latitude = Double.parseDouble((String) properties.get("MAN-LATITUDE"));
            if(properties.containsKey("MAN-LONGITUDE"))
                Location.man_longitude = Double.parseDouble((String) properties.get("MAN-LONGITUDE"));
            if(properties.containsKey("ESPLANADE-DISTANCE"))
                Location.esplanade_distance = Integer.parseInt((String) properties.get("ESPLANADE-DISTANCE"));
            if(properties.containsKey("BLOCK-WIDTH"))
                Location.block_width = Integer.parseInt((String) properties.get("BLOCK-WIDTH"));
            if(properties.containsKey("CURRENT-DESTINATION-NAME")) {
                currentDestinationName = properties.getProperty("CURRENT-DESTINATION-NAME");
                currentDestination = landmarks.getCamp(currentDestinationName);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error reading configuration file");
        }
    }

    /**
     * Writes configuration to file
     * @param filename path to config file
     */
    public void writeToConfigFile(String filename) {
        if(properties == null) {
            return;
        }

        properties.put("MAN-LATITUDE", String.valueOf(Location.man_latitude));
        properties.put("MAN-LONGITUDE", String.valueOf(Location.man_longitude));
        properties.put("ESPLANADE-DISTANCE", String.valueOf(Location.esplanade_distance));
        properties.put("BLOCK-WIDTH", String.valueOf(Location.block_width));
        properties.put("CURRENT-DESTINATION-NAME", currentDestinationName);

        try {
            properties.store(new FileOutputStream(filename), "");
        } catch (IOException e) {
            System.err.println("Error writing config file");
        }
    }

    /**
     * Updates the current location based on address
     * @param hour int hour
     * @param minute int minute
     * @param street char street
     */
    public void updateLocation(int hour, int minute, char street) {
        currentLocation = new Location(hour, minute, street);
    }

    /**
     * Sets the current location based on gps coordinates
     * @param latitude double latitude
     * @param longitude double longitude
     */
    public void updateLocation(double latitude, double longitude) {
        currentLocation.updateLocation(latitude, longitude);
    }

    /**
     * Returns exact camp name string
     * @param campName partial camp name
     * @return exact camp name
     */
    public String findCampName(String campName) {
        return landmarks.findCampName(campName);
    }

    /**
     * Getter for camps based on exact string name
     * @param exactCampName exact camp name
     * @return camp Location
     */
    public Location getCamp(String exactCampName) {
        return landmarks.getCamp(exactCampName);
    }

    /**
     * Reads csv files
     * @param bathroomsPath path to bathroom locations
     * @param campsPath path to camp locations
     * @param favoritesPath path to favorite locations
     */
    public void initializeLandmarks(String bathroomsPath, String campsPath, String favoritesPath) {
        landmarks.readBathrooms(bathroomsPath);
        landmarks.readCamps(campsPath);
        landmarks.readFavorites(favoritesPath);
    }

    /**
     * Returns a TreeMap
     * @return TreeMap (String, String) with panel navigation updates
     */
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

    /**
     * Sets home to a new location
     * @param hour hour int
     * @param minute minute int
     * @param street street char
     */
    public void setHome(int hour, int minute, char street) {
        this.home = new Location(hour, minute, street);
    }

    /**
     * Getter for favorites
     * @return TreeMap (String, Location)
     */
    public TreeMap<String, Location> getFavorites() {
        return landmarks.getFavorites();
    }

    /**
     * Getter for currentLocation
     * @return current Location
     */
    public Location currentLocation() {
        return currentLocation;
    }

    /**
     * Outputs favorites to a CSV file
     */
    public void writeFavorites() {
        try {
            FileWriter f = new FileWriter("favorites.csv");
            StringBuilder sb = new StringBuilder();
            for(String campName : landmarks.getFavorites().keySet()) {
                sb.append(campName);
                sb.append(',');
                sb.append(landmarks.getFavorites().get(campName).getCSVAddress());
                sb.append('\n');
            }
            f.write(sb.toString());
            f.close();
        } catch(IOException e) {
            System.err.println("Error writing files");
        }
    }

    /**
     * Getter for camps
     * @return TreeMap (String camp name, Location camp)
     */
    public TreeMap<String, Location> getCamps() {
        return landmarks.getCamps();
    }
}
