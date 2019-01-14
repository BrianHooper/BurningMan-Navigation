package navigation;

import driver.FileManager;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Class Landmarks
 * <p>
 * Maintains lists of Location objects and methods for interacting with them
 */
class Landmarks {
    private static final String bathroomsPath = "config/bathrooms.csv";
    private static final String campsPath = "config/camps.csv";
    private static final String favoritesPath = "config/favorites.csv";

    private final TreeMap<String, Location> favorites;
    private final ArrayList<Location> bathrooms;

    TreeMap<String, Location> getCamps() {
        return camps;
    }

    private final TreeMap<String, Location> camps;

    /**
     * Constructor
     * <p>
     * Initializes empty sets
     */
    Landmarks() {
        this.bathrooms = new ArrayList<>();
        this.camps = new TreeMap<>();
        this.favorites = new TreeMap<>();
    }

    /**
     * Populates the list of bathroom locations from a file
     */
    void readBathrooms() {
        ArrayList<String> lines = FileManager.readLines(bathroomsPath);
        if (lines == null)
            return;
        try {
            for (String line : lines) {
                String[] split = line.split(",");
                if (split.length == 2) {
                    double latitude = Double.parseDouble(split[0]);
                    double longitude = Double.parseDouble(split[1]);
                    bathrooms.add(new Location(latitude, longitude));
                } else if (split.length == 3) {
                    int hour = Integer.parseInt(split[0]);
                    int minute = Integer.parseInt(split[1]);
                    if (split[2].matches("0-9")) {
                        double distance = Double.parseDouble(split[2]);
                        bathrooms.add(new Location(hour, minute, distance));
                    } else {
                        bathrooms.add(new Location(hour, minute, split[2].charAt(0)));
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing bathroom coordinates");
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
        if (lines == null)
            return;
        try {
            for (String line : lines) {
                String[] split = line.split(",");
                if (split.length == 3) {
                    double latitude = Double.parseDouble(split[1]);
                    double longitude = Double.parseDouble(split[2]);
                    map.put(split[0], new Location(latitude, longitude));
                } else if (split.length == 4) {
                    int hour = Integer.parseInt(split[1]);
                    int minute = Integer.parseInt(split[2]);
                    if (split[3].matches("0-9")) {
                        double distance = Double.parseDouble(split[3]);
                        map.put(split[0], new Location(hour, minute, distance));
                    } else {
                        map.put(split[0], new Location(hour, minute, split[3].charAt(0)));
                    }
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing bathroom coordinates");
        }
    }

    /**
     * Populates the list of camp locations from a file
     */
    void readCamps() {
        readNamedLocations(camps, campsPath);
    }

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
    public void addBathroom(int hour, int minute, double distance) {
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
    public void addCamp(String name, int hour, int minute, double distance) {
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
     * Finds the closest location to another location
     *
     * @param locations       list of Locations
     * @param currentLocation Location to compare
     * @return closest Location
     */
    private Location findClosest(ArrayList<Location> locations, Location currentLocation) {
        if (locations.isEmpty()) {
            return null;
        }

        Location closestLocation = locations.get(0);
        double closestDistance = Double.MAX_VALUE;
        for (Location location : locations) {
            double distance = currentLocation.distance(location);
            if (distance < closestDistance) {
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

    /**
     * Attempts to find a Location matching a search term
     *
     * @param locations  Set of String to search
     * @param searchTerm search term
     * @return String matching, or null if none
     */
    private static String findMatch(TreeMap<String, Location> locations, String searchTerm) {
        searchTerm = searchTerm.toLowerCase();

        ArrayList<String> matches = new ArrayList<>();
        for (String location : locations.keySet()) {
            String lc = location.toLowerCase();
            if (lc.contains(searchTerm)) {
                matches.add(location);
            }
        }

        if (matches.isEmpty()) {
            return null;
        } else {
            return matches.get(0);
        }
    }

    /**
     * Searches the list of camps for a camp matching the search term
     *
     * @param name search term
     * @return Location of camp, or null if none
     */
    String findCampName(String name) {
        String campName = findMatch(camps, name);
        if (campName == null) {
            return "";
        } else {
            return campName;
        }
    }

    /**
     * Returns a camp or favorite based on an exact camp name
     *
     * @param exactCampName exact camp name
     * @return Location or null
     */
    Location getCamp(String exactCampName) {
        Location camp = camps.get(exactCampName);
        if (camp == null) {
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
    TreeMap<String, Location> getFavorites() {
        return favorites;
    }

    void readFavorites() {
        readNamedLocations(favorites, favoritesPath);
    }
}
