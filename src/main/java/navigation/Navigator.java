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
//**********************
// Class member fields
//**********************

    // Relative path of config file
    private static final String configPath = "config/config.cfg";

    // Relative path of favorites file
    private static final String favoritesPath = "config/favorites.tsv";

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

    // Start and stop locations for measuring distance
    private Location measureStart, measureStop;

//**********************
// Constructors and initializers
//**********************

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
            if(properties.containsKey("MAN-LATITUDE") && properties.containsKey("MAN-LONGITUDE"))
                Location.setManCoordinates(Double.parseDouble((String) properties.get("MAN-LATITUDE")),
                        Double.parseDouble((String) properties.get("MAN-LONGITUDE")));
            if(properties.containsKey("ADJUSTMENT-COEFFICIENTS")) {
                String[] split = ((String) properties.get("ADJUSTMENT-COEFFICIENTS")).split(",");
                Location.setAdjustmentCoefficients(Double.parseDouble(split[0]), Double.parseDouble(split[1]),
                        Double.parseDouble(split[2]), Double.parseDouble(split[3]));
            }
            if(properties.containsKey("EVENT-START-TIME"))
                Event.setGlobalEventStartTime(properties.getProperty("EVENT-START-TIME"));
            if(properties.containsKey("CURRENT-DESTINATION-NAME"))
                currentDestinationName = properties.getProperty("CURRENT-DESTINATION-NAME");
            if(properties.containsKey("CURRENT-DESTINATION-ADDRESS"))
                currentDestination = new Location(properties.getProperty("CURRENT-DESTINATION-ADDRESS"));
            if(properties.containsKey("BLOCK-DISTANCES")) {
                String blockDistanceStr = properties.getProperty("BLOCK-DISTANCES");
                String[] blockDistances = blockDistanceStr.split(";");
                Object[][] blockDistanceObjects = new Object[blockDistances.length][2];
                for(int i = 0; i < blockDistances.length; i++) {
                    String[] block = blockDistances[i].split(",");
                    if(block.length == 2) {
                        blockDistanceObjects[i][0] = Integer.parseInt(block[0]);
                        blockDistanceObjects[i][1] = block[1];
                    }
                }
                Location.setBlockDistances(blockDistanceObjects);

            }
        } catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
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

        properties.put("MAN-LATITUDE", String.valueOf(Location.getMan_latitude()));
        properties.put("MAN-LONGITUDE", String.valueOf(Location.getMan_longitude()));
        properties.put("CURRENT-DESTINATION-NAME", currentDestinationName);
        properties.put("CURRENT-DESTINATION-ADDRESS", currentDestination.getCSVAddress());
        properties.put("EVENT-START-TIME", ClockDriver.dfFull.format(Event.globalEventStartTime));
        double[] coefficients = Location.getAdjustmentCoefficients();
        String adjustmentCoefficients = String.valueOf(coefficients[0]) + ',' +
                String.valueOf(coefficients[1]) + ',' + String.valueOf(coefficients[2]) + ',' +
                String.valueOf(coefficients[3]);
        properties.put("ADJUSTMENT-COEFFICIENTS", adjustmentCoefficients);
        StringBuilder blockStringBuilder = new StringBuilder();
        for(Object[] block : Location.getBlockDistances()) {
            if(block.length == 2) {
                blockStringBuilder.append(block[0]);
                blockStringBuilder.append(',');
                blockStringBuilder.append(block[1]);
                blockStringBuilder.append(';');
            }
        }
        properties.put("BLOCK-DISTANCES", blockStringBuilder.toString());

        try {
            properties.store(new FileOutputStream(configPath), "");
        } catch(IOException e) {
            logger.warning(this.getClass(),
                    "IOException while writing to config file \'" + configPath + "\': " + e.getMessage());
        }
    }

    /**
     * Outputs favorites to a CSV file
     */
    public void writeFavorites() {
        ArrayList<String> favorites = new ArrayList<>();
        for(Landmark landmark : landmarks.getFavorites()) {
            favorites.add(landmark.toString());
        }
        FileManager.writeLines(favoritesPath, favorites);
    }

    /**
     * Reads landmarks from csv files
     */
    private void readLandmarks() {
        landmarks.readBathrooms();
        landmarks.readCampsTSV();
        landmarks.readFavoritesTSV();
    }

//**********************
// Getters and setters
//**********************

    /**
     * Getter for measure start
     * <p>
     * If measureStart is not set, returns current location
     *
     * @return Location
     */
    public Location getMeasureStart() {
        if(measureStart == null) {
            return currentLocation;
        } else {
            return measureStart;
        }
    }

    /**
     * Setter for measure start
     *
     * @param measureStart Location
     */
    public void setMeasureStart(Location measureStart) {
        this.measureStart = measureStart;
    }

    /**
     * Getter for measure stop
     * <p>
     * If measureStop is not set, returns current location
     *
     * @return Location
     */
    public Location getMeasureStop() {
        if(measureStart == null) {
            return currentLocation;
        } else {
            return measureStop;
        }
    }

    /**
     * Setter for measureStop
     *
     * @param measureStop Location
     */
    public void setMeasureStop(Location measureStop) {
        this.measureStop = measureStop;
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
     * Sets the home location
     *
     * @param home Location object
     */
    public void setHome(Location home) {
        this.home = home;
    }

    /**
     * Getter for favorites
     *
     * @return TreeMap (String, Location)
     */
    public ArrayList<Landmark> getFavorites() {
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
     * Sets the current location based on gps coordinates
     *
     * @param latitude  double latitude
     * @param longitude double longitude
     */
    public void updateLocation(double latitude, double longitude) {
        currentLocation.updateLocation(latitude, longitude);
    }

//**********************
// Class methods
//**********************

    /**
     * Returns a TreeMap
     *
     * @return TreeMap (String, String) with panel navigation updates
     */
    public TreeMap<String, String> getPanelUpdate() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("currentAddress", currentLocation.getAddress());

        Location closestBathroom = landmarks.findBathroom(currentLocation);
        if(closestBathroom != null) {
            map.put("bathroomAddress", closestBathroom.getAddress());
            map.put("bathroomDirections", currentLocation.distance(closestBathroom) +
                    ", " + currentLocation.cardinal(closestBathroom));
        }

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

    /**
     * Returns name/address pairs for each saved favorite
     *
     * @return ArrayList of length-2 String arrays
     */
    public ArrayList<String[]> getFavoritePairs() {
        ArrayList<String[]> favPairs = new ArrayList<>();
        for(Landmark landmark : landmarks.getFavorites()) {
            favPairs.add(new String[]{landmark.getName(), landmark.getLocation().toString(), landmark.getDescription()});
        }
        return favPairs;
    }

    /**
     * Returns name/address pairs for each camp in list
     *
     * @param camps ArrayList of String camp names
     * @return ArrayList of length-2 String arrays
     */
    public ArrayList<String[]> getCampPairs(ArrayList<String> camps) {
        ArrayList<String[]> pairs = new ArrayList<>();
        for(String camp : camps) {
            pairs.add(new String[]{camp, landmarks.getCamp(camp).toString()});
        }
        return pairs;
    }

    /**
     * Returns name/address pairs for all camps
     *
     * @return ArrayList of length-2 String arrays
     */
    public ArrayList<String[]> getCampPairs() {
        ArrayList<String[]> campPairs = new ArrayList<>();
        for(String camp : landmarks.getCamps().keySet()) {
            campPairs.add(new String[]{camp, landmarks.getCamps().get(camp).getAddress()});
        }
        return campPairs;
    }
}
