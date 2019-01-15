package navigation;

import driver.FileManager;
import events.Event;
import events.EventManager;
import events.NoteManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeMap;

public class Navigator {
    private static final String configPath = "config/config.cfg";
    private static final String favoritesPath = "config/favorites.csv";

    private Location home;
    private Location currentDestination;
    private String currentDestinationName;
    private Properties properties;

    private final Location currentLocation;
    private final Landmarks landmarks;
    private final EventManager eventManager;
    private final NoteManager noteManager;

    /**
     * Constructor
     * <p>
     * initializes current location, landmarks, home, destination
     */
    public Navigator() {
        landmarks = new Landmarks();
        eventManager = new EventManager();
        noteManager = new NoteManager();

        currentLocation = new Location(6, 0, 'D');
        home = new Location(6, 0, 'D');

        currentDestination = home;
        currentDestinationName = "";

        readLandmarks();
        readConfig();

    }

    /**
     * Reads configuration file
     */
    private void readConfig() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(configPath)));
        } catch (IOException e) {
            System.err.println(configPath + " not found");
            return;
        }

        try {
            if (properties.containsKey("MAN-LATITUDE"))
                Location.man_latitude = Double.parseDouble((String) properties.get("MAN-LATITUDE"));
            if (properties.containsKey("MAN-LONGITUDE"))
                Location.man_longitude = Double.parseDouble((String) properties.get("MAN-LONGITUDE"));
            if (properties.containsKey("ESPLANADE-DISTANCE"))
                Location.esplanade_distance = Integer.parseInt((String) properties.get("ESPLANADE-DISTANCE"));
            if (properties.containsKey("BLOCK-WIDTH"))
                Location.block_width = Integer.parseInt((String) properties.get("BLOCK-WIDTH"));
            if (properties.containsKey("EVENT-START-TIME"))
                Event.setGlobalEventStartTime(properties.getProperty("EVENT-START-TIME"));
            if (properties.containsKey("CURRENT-DESTINATION-NAME")) {
                currentDestinationName = properties.getProperty("CURRENT-DESTINATION-NAME");
                currentDestination = landmarks.getCamp(currentDestinationName);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error reading configuration file");
        }
    }

    /**
     * Writes configuration to file
     */
    public void writeToConfigFile() {
        if (properties == null) {
            return;
        }

        properties.put("MAN-LATITUDE", String.valueOf(Location.man_latitude));
        properties.put("MAN-LONGITUDE", String.valueOf(Location.man_longitude));
        properties.put("ESPLANADE-DISTANCE", String.valueOf(Location.esplanade_distance));
        properties.put("BLOCK-WIDTH", String.valueOf(Location.block_width));
        properties.put("CURRENT-DESTINATION-NAME", currentDestinationName);
        properties.put("EVENT-START-TIME", Event.dfFull.format(Event.globalEventStartTime));

        try {
            properties.store(new FileOutputStream(configPath), "");
        } catch (IOException e) {
            System.err.println("Error writing config file");
        }
    }

    /**
     * Sets the current location based on gps coordinates
     *
     * @param latitude  double latitude
     * @param longitude double longitude
     */
    public void updateLocation(double latitude, double longitude) {
        currentLocation.updateLocation(latitude, longitude);
    }

    /**
     * Returns exact camp name string
     *
     * @param campName partial camp name
     * @return exact camp name
     */
    public String findCampName(String campName) {
        return landmarks.findCampName(campName);
    }

    /**
     * Getter for camps based on exact string name
     *
     * @param exactCampName exact camp name
     * @return camp Location
     */
    public Location getCamp(String exactCampName) {
        return landmarks.getCamp(exactCampName);
    }

    /**
     * Reads landmarks from csv files
     */
    private void readLandmarks() {
        landmarks.readBathrooms();
        landmarks.readCamps();
        landmarks.readFavorites();
    }

    /**
     * Returns a TreeMap
     *
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
        if (currentDestination != null) {
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
     *
     * @param hour   hour int
     * @param minute minute int
     * @param street street char
     */
    public void setHome(int hour, int minute, char street) {
        this.home = new Location(hour, minute, street);
    }

    /**
     * Getter for favorites
     *
     * @return TreeMap (String, Location)
     */
    public TreeMap<String, Location> getFavorites() {
        return landmarks.getFavorites();
    }

    /**
     * Getter for currentLocation
     *
     * @return current Location
     */
    public Location currentLocation() {
        return currentLocation;
    }

    /**
     * Outputs favorites to a CSV file
     */
    public void writeFavorites() {
        ArrayList<String> favorites = new ArrayList<>();
        for (String campName : landmarks.getFavorites().keySet()) {
            favorites.add(campName + ',' + landmarks.getFavorites().get(campName).getCSVAddress());
        }
        FileManager.writeLines(favoritesPath, favorites);
    }

    /**
     * Getter for camps
     *
     * @return TreeMap (String camp name, Location camp)
     */
    public TreeMap<String, Location> getCamps() {
        return landmarks.getCamps();
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public NoteManager getNoteManager() {
        return noteManager;
    }
}
