package navigation;

import driver.FileManager;
import driver.LogDriver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;

/**
 * Class Landmarks
 * <p>
 * Maintains lists of Location objects and methods for interacting with them
 * <p>
 * CSV files for list data are stored in the config directory
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class Landmarks {
//**********************
// Class member fields
//**********************

    // Relative path to landmark files
    private static final String bathroomsPath = "config/bathrooms.csv";
    private static final String campsPath = "config/theme_camps.tsv";
    private static final String favoritesPath = "config/facilities.tsv";

    // Key-Value pairs representing the name and location of a favorite
    private final TreeMap<String, Location> favorites;
    private final ArrayList<Landmark> favoriteLandmarks;

    // Key-Value pairs representing the name and location of camps
    private final TreeMap<String, Location> camps;

    // List of bathroom locations
    private final ArrayList<Location> bathrooms;

    // Logger
    private static final LogDriver logger = LogDriver.getInstance();

//**********************
// Constructors and initializers
//**********************

    /**
     * Constructor
     * <p>
     * Initializes empty sets
     */
    Landmarks() {
        this.bathrooms = new ArrayList<>();
        this.camps = new TreeMap<>();
        this.favorites = new TreeMap<>();
        this.favoriteLandmarks = new ArrayList<>();
    }

    /**
     * Populates the list of bathroom locations from a file
     */
    void readBathrooms() {
        ArrayList<String> lines = FileManager.readLines(bathroomsPath);
        if(lines == null)
            return;
        for(String line : lines) {
            try {
                String[] split = line.split(",");
                if(split.length == 2) {
                    double latitude = Double.parseDouble(split[0]);
                    double longitude = Double.parseDouble(split[1]);
                    bathrooms.add(new Location(latitude, longitude));
                } else if(split.length == 3) {
                    int hour = Integer.parseInt(split[0]);
                    int minute = Integer.parseInt(split[1]);
                    if(split[2].matches("0-9")) {
                        int distance = Integer.parseInt(split[2]);
                        bathrooms.add(new Location(hour, minute, distance));
                    } else {
                        bathrooms.add(new Location(hour, minute, split[2].charAt(0)));
                    }
                }
            } catch(NumberFormatException e) {
                logger.warning(this.getClass(),
                        "NumberFormatException while reading bathroom coordinates, error parsing line \'" +
                                line + "\': " + e.getMessage());
            }
        }

    }

    /**
     * Populates a list of named locations into a HashSet
     *
     * @param map      HashSet of String-Location
     * @param filename relative path of file
     */
    private void readNamedLocations(TreeMap<String, Location> map, String filename) {
        ArrayList<String> lines = FileManager.readLines(filename);
        if(lines == null)
            return;
        for(String line : lines) {
            try {
                String[] split = line.split(",");
                if(split.length == 3) {
                    double latitude = Double.parseDouble(split[1]);
                    double longitude = Double.parseDouble(split[2]);
                    map.put(split[0], new Location(latitude, longitude));
                } else if(split.length == 4) {
                    int hour = Integer.parseInt(split[1]);
                    int minute = Integer.parseInt(split[2]);
                    if(split[3].matches("0-9")) {
                        int distance = Integer.parseInt(split[2]);
                        map.put(split[0], new Location(hour, minute, distance));
                    } else {
                        map.put(split[0], new Location(hour, minute, split[3].charAt(0)));
                    }
                }
            } catch(NumberFormatException e) {
                logger.warning(this.getClass(),
                        "NumberFormatException while reading named locations, error parsing line \'" +
                                line + "\': " + e.getMessage());
            }
        }
    }

    /**
     * Populates the list of camp locations from a file
     */
    void readCamps() {
        readNamedLocations(camps, campsPath);
    }

    void readCampsTSV() {
        ArrayList<String> lines = FileManager.readLines(campsPath);
        if(lines == null)
            return;
        String regex = "[0-9]+";
        for(String line : lines) {

            String[] split = line.split("\t");
            if (split.length >= 2) {
                String campName = split[0];
                String[] address = split[1].split(" ");
                if (address.length == 3) {
                    String[] time = address[0].split(":");
                    if (time.length == 2) {
                        try {
                            int hour = Integer.parseInt(time[0]);
                            int minute = Integer.parseInt(time[1]);
                            if(address[1].matches(regex)) {
                                camps.put(campName, new Location(hour, minute, Integer.parseInt(address[2])));
                            } else {
                                camps.put(campName, new Location(hour, minute, address[2].charAt(0)));
                            }
                        } catch (NumberFormatException e) {
                            logger.warning(this.getClass(),
                                    "NumberFormatException while reading named locations, error parsing line \'" +
                                            line + "\': " + e.getMessage());
                        }
                    } else {
                        logger.warning(this.getClass(),"Invalid time length: " + campName);
                    }
                } else {
                    logger.warning(this.getClass(),"No address break: " + campName);
                }
            } else {
                logger.warning(this.getClass(),"No breaks at all: " + line);
            }
        }
    }

    /**
     * Reads the favorites from a file
     */
    void readFavorites() {
        readNamedLocations(favorites, favoritesPath);
    }

    void readFavoritesTSV() {
        ArrayList<String> lines = FileManager.readLines(favoritesPath);
        if(lines == null)
            return;
        for(String line : lines) {
            String[] split = line.split("\t");

            String description;
            if(split.length == 3) {
                description = split[2];
            } else if(split.length == 2){
                description = "";
            } else {
                continue;
            }
            String name = split[0];
            String[] address;
            if(split[1].contains("&")) {
                address = split[1].split(" & ");
            } else if(split[1].contains("and")) {
                address = split[1].split(" and ");
            } else {
                continue;
            }
            String[] time = address[0].split(":");
            try {
                String hour = time[0];
                String minute = time[1];
                String street = address[1];

                Landmark landmark = new Landmark(name, description, hour, minute, street);
                if(landmark.getLocation() != null) {
                    favoriteLandmarks.add(landmark);
                }
            } catch(Exception e) {
                logger.warning(this.getClass(),"Error parsing favorite: " + line + "\n\t" + e.getStackTrace());
            }
        }
    }

//**********************
// Getters and setters
//**********************

    /**
     * Adds a bathroom to the list based on geographic coordinates
     *
     * @param latitude  latitude
     * @param longitude longitude
     */
    @SuppressWarnings("unused")
    public void addBathroom(double latitude, double longitude) {
        this.bathrooms.add(new Location(latitude, longitude));
    }

    /**
     * Adds a bathroom to the list based on time/distance
     *
     * @param hour     hour
     * @param minute   minute
     * @param distance distance in feet
     */
    @SuppressWarnings("unused")
    public void addBathroom(int hour, int minute, int distance) {
        this.bathrooms.add(new Location(hour, minute, distance));
    }

    /**
     * Adds a bathroom to the list based on time/street
     *
     * @param hour   hour
     * @param minute minute
     * @param street street as char
     */
    @SuppressWarnings("unused")
    public void addBathroom(int hour, int minute, char street) {
        this.bathrooms.add(new Location(hour, minute, street));
    }

    /**
     * Adds a camp to the list based on geographic coordinates
     *
     * @param latitude  latitude
     * @param longitude longitude
     */
    @SuppressWarnings("unused")
    public void addCamp(String name, double latitude, double longitude) {
        this.camps.put(name, new Location(latitude, longitude));
    }

    /**
     * Adds a camp to the list based on time/distance
     *
     * @param hour     hour
     * @param minute   minute
     * @param distance distance in feet
     */
    @SuppressWarnings("unused")
    public void addCamp(String name, int hour, int minute, int distance) {
        this.camps.put(name, new Location(hour, minute, distance));
    }

    /**
     * Adds a camp to the list based on time/street
     *
     * @param hour   hour
     * @param minute minute
     * @param street street as char
     */
    @SuppressWarnings("unused")
    public void addCamp(String name, int hour, int minute, char street) {
        this.camps.put(name, new Location(hour, minute, street));
    }

    /**
     * Returns a camp or favorite based on an exact camp name
     *
     * @param exactCampName exact camp name
     * @return Location or null
     */
    Location getCamp(String exactCampName) {
        Location camp = camps.get(exactCampName);
        if(camp == null) {
            return favorites.get(exactCampName);
        } else {
            return camp;
        }
    }

    /**
     * Getter for favorites HashMap
     *
     * @return favorites
     */
    ArrayList<Landmark> getFavorites() {
        return favoriteLandmarks;
    }

    /**
     * Getter for camps
     *
     * @return TreeMap of String-Locations
     */
    TreeMap<String, Location> getCamps() {
        return camps;
    }

//**********************
// Class methods
//**********************

    /**
     * Finds the closest location to another location
     *
     * @param locations       list of Locations
     * @param currentLocation Location to compare
     * @return closest Location
     */
    private Location findClosest(ArrayList<Location> locations, Location currentLocation) {
        if(locations.isEmpty()) {
            return null;
        }

        Location closestLocation = locations.get(0);
        double closestDistance = Double.MAX_VALUE;
        for(Location location : locations) {
            double distance = currentLocation.distance(location);
            if(distance < closestDistance) {
                closestLocation = location;
                closestDistance = distance;
            }
        }
        return closestLocation;
    }

    /**
     * Finds the closest bathroom relative to the current location
     *
     * @param currentLocation current Location
     * @return closest bathroom Location
     */
    Location findBathroom(Location currentLocation) {
        return findClosest(bathrooms, currentLocation);
    }
}
