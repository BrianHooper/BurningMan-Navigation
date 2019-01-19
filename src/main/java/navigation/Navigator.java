package navigation;

import driver.ClockDriver;
import driver.FileManager;
import driver.LogDriver;
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


/**
 * Class Navigator
 * <p>
 * Maintains the current location, destination, home, favorites
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class Navigator {
    // Relative path of config file
    private static final String configPath = "config/config.cfg";

    // Relative path of favorites file
    private static final String favoritesPath = "config/favorites.csv";

    // Current home location
    private Location home;

    // Current navigation destination
    private Location currentDestination;
    private String currentDestinationName;

    // File manager for reading/writing properties
    private Properties properties;

    // Current location
    private final Location currentLocation;

    // Managers for camps, favorites, events, notes
    private final Landmarks landmarks;
    private final EventManager eventManager;
    private final NoteManager noteManager;

    // Logger
    private static final LogDriver logger = LogDriver.getInstance();

    /**
     * Constructor
     * <p>
     * initializes current location, landmarks, home, destination
     */
    public Navigator() {
        landmarks = new Landmarks();

        currentLocation = new Location(6, 0, 'D');
        home = new Location(6, 0, 'D');

        currentDestination = home;
        currentDestinationName = "";

        readLandmarks();
        readConfig();
        noteManager = new NoteManager();
        eventManager = new EventManager();

    }

    /**
     * Reads configuration file
     */
    private void readConfig() {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(configPath)));
        } catch(IOException e) {
            logger.severe(this.getClass(),
                    "Error reading configuration file \'" + configPath + "\': " + e.getMessage());
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
            if(properties.containsKey("EVENT-START-TIME"))
                Event.setGlobalEventStartTime(properties.getProperty("EVENT-START-TIME"));
            if(properties.containsKey("CURRENT-DESTINATION-NAME"))
                currentDestinationName = properties.getProperty("CURRENT-DESTINATION-NAME");
            if(properties.containsKey("CURRENT-DESTINATION-ADDRESS"))
                currentDestination = new Location(properties.getProperty("CURRENT-DESTINATION-ADDRESS"));
        } catch(NumberFormatException e) {
            logger.warning(this.getClass(),
                    "NumberFormatException while reading config file: " + e.getMessage());
        }
    }

    /**
     * Writes configuration to file
     */
    public void writeToConfigFile() {
        if(properties == null) {
            return;
        }

        properties.put("MAN-LATITUDE", String.valueOf(Location.man_latitude));
        properties.put("MAN-LONGITUDE", String.valueOf(Location.man_longitude));
        properties.put("ESPLANADE-DISTANCE", String.valueOf(Location.esplanade_distance));
        properties.put("BLOCK-WIDTH", String.valueOf(Location.block_width));
        properties.put("CURRENT-DESTINATION-NAME", currentDestinationName);
        properties.put("CURRENT-DESTINATION-ADDRESS", currentDestination.getCSVAddress());
        properties.put("EVENT-START-TIME", ClockDriver.dfFull.format(Event.globalEventStartTime));

        try {
            properties.store(new FileOutputStream(configPath), "");
        } catch(IOException e) {
            logger.warning(this.getClass(),
                    "IOException while writing to config file \'" + configPath + "\': " + e.getMessage());
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
        if(currentDestination != null) {
            map.put("destinationName", currentDestinationName);
            map.put("destinationAddress", currentDestination.getAddress());
            map.put("destinationDirections", currentLocation.distance(currentDestination) +
                    ", " + currentLocation.cardinal(currentDestination));
        }
        return map;
    }

    /**
     * Setter for the current destination
     *
     * @param location Location object
     * @param name     name of destination
     */
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
        for(String campName : landmarks.getFavorites().keySet()) {
            favorites.add(campName + ',' + landmarks.getFavorites().get(campName).getCSVAddress());
        }
        FileManager.writeLines(favoritesPath, favorites);
    }

    /**
     * Getter for event manager
     *
     * @return EventManager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Getter for note manager
     *
     * @return NoteManager
     */
    public NoteManager getNoteManager() {
        return noteManager;
    }

    /**
     * Returns name/address pairs for each saved favorite
     *
     * @return ArrayList of length-2 String arrays
     */
    public ArrayList<String[]> getFavoritePairs() {
        ArrayList<String[]> favPairs = new ArrayList<>();
        for(String favName : landmarks.getFavorites().keySet()) {
            favPairs.add(new String[]{favName, landmarks.getFavorites().get(favName).getAddress()});
        }
        return favPairs;
    }

    /**
     * Returns all camps containing a partial match to a search term
     * <p>
     * can use * as a wildcard to return all camps
     *
     * @param searchTerm search term
     * @return ArrayList of String camp names
     */
    public ArrayList<String> findCamps(String searchTerm) {
        if(searchTerm.equals("*")) {
            return new ArrayList<>(landmarks.getCamps().keySet());
        }

        searchTerm = searchTerm.toLowerCase();
        ArrayList<String> results = new ArrayList<>();
        for(String campName : landmarks.getCamps().keySet()) {
            if(campName.toLowerCase().contains(searchTerm)) {
                results.add(campName);
            }
        }
        return results;
    }
}
