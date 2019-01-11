/* Brian Hooper
 *
 * 2018
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class Landmarks
 *
 * Maintains lists of Location objects and methods for interacting with them
 */
public class Landmarks {
    private HashMap<String, Location> favorites;
    private ArrayList<Location> bathrooms;

    public HashMap<String, Location> getCamps() {
        return camps;
    }

    private HashMap<String, Location> camps;

    /**
     * Constructor
     *
     * Initializes empty sets
     */
    public Landmarks() {
        this.bathrooms = new ArrayList<>();
        this.camps = new HashMap<>();
        this.favorites = new HashMap<>();
    }

    /**
     * Reads a file into an ArrayList of lines
     * @param filename relative path of file
     * @return ArrayList of strings
     */
    private ArrayList<String> readFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner scan = new Scanner(new File(filename));
            while(scan.hasNextLine()) {
                lines.add(scan.nextLine());
            }
            scan.close();
        } catch(FileNotFoundException e) {
            System.err.println("Error reading " + filename);

        }
        return lines;
    }

    /**
     * Populates the list of bathroom locations from a file
     * @param filename relative path of file
     */
    public void readBathrooms(String filename) {
        if(filename == null) {
            return;
        }
        ArrayList<String> lines = readFile(filename);
        try {
            for(String line : lines) {
                String[] split = line.split(",");
                if(split.length == 2) {
                    double latitude = Double.parseDouble(split[0]);
                    double longitude = Double.parseDouble(split[1]);
                    bathrooms.add(new Location(latitude, longitude));
                } else if(split.length == 3) {
                    int hour = Integer.parseInt(split[0]);
                    int minute = Integer.parseInt(split[1]);
                    if(split[2].matches("0-9")) {
                        double distance = Double.parseDouble(split[2]);
                        bathrooms.add(new Location(hour, minute, distance));
                    } else {
                        bathrooms.add(new Location(hour, minute, split[2].charAt(0)));
                    }
                }
            }
        } catch(NumberFormatException e) {
            System.err.println("Error parsing bathroom coordinates");
        }
    }

    /**
     * Populates a list of named locations into a HashSet
     * @param map HashSet of String-Location
     * @param filename relative path of file
     */
    private void readNamedLocations(HashMap<String, Location> map, String filename) {
        ArrayList<String> lines = readFile(filename);
        try {
            for(String line : lines) {
                String[] split = line.split(",");
                if(split.length == 3) {
                    double latitude = Double.parseDouble(split[1]);
                    double longitude = Double.parseDouble(split[2]);
                    map.put(split[0], new Location(latitude, longitude));
                } else if(split.length == 4) {
                    int hour = Integer.parseInt(split[1]);
                    int minute = Integer.parseInt(split[2]);
                    if(split[3].matches("0-9")) {
                        double distance = Double.parseDouble(split[3]);
                        map.put(split[0], new Location(hour, minute, distance));
                    } else {
                        map.put(split[0], new Location(hour, minute, split[3].charAt(0)));
                    }
                }
            }
        } catch(NumberFormatException e) {
            System.err.println("Error parsing bathroom coordinates");
        }
    }

    /**
     * Populates the list of camp locations from a file
     * @param filename relative path of file
     */
    public void readCamps(String filename) {
        if(filename == null) {
            return;
        }
        readNamedLocations(camps, filename);
    }

    /**
     * Adds a bathroom to the list based on geographic coordinates
     * @param latitude latitude
     * @param longitude longitude
     */
    @SuppressWarnings("unused")
    public void addBathroom(double latitude, double longitude) {
        this.bathrooms.add(new Location(latitude, longitude));
    }

    /**
     * Adds a bathroom to the list based on time/distance
     * @param hour hour
     * @param minute minute
     * @param distance distance in feet
     */
    @SuppressWarnings("unused")
    public void addBathroom(int hour, int minute, double distance) {
        this.bathrooms.add(new Location(hour, minute, distance));
    }

    /**
     * Adds a bathroom to the list based on time/street
     * @param hour hour
     * @param minute minute
     * @param street street as char
     */
    @SuppressWarnings("unused")
    public void addBathroom(int hour, int minute, char street) {
        this.bathrooms.add(new Location(hour, minute, street));
    }

    /**
     * Adds a camp to the list based on geographic coordinates
     * @param latitude latitude
     * @param longitude longitude
     */
    @SuppressWarnings("unused")
    public void addCamp(String name, double latitude, double longitude) {
        this.camps.put(name, new Location(latitude, longitude));
    }

    /**
     * Adds a camp to the list based on time/distance
     * @param hour hour
     * @param minute minute
     * @param distance distance in feet
     */
    @SuppressWarnings("unused")
    public void addCamp(String name, int hour, int minute, double distance) {
        this.camps.put(name, new Location(hour, minute, distance));
    }

    /**
     * Adds a camp to the list based on time/street
     * @param hour hour
     * @param minute minute
     * @param street street as char
     */
    @SuppressWarnings("unused")
    public void addCamp(String name, int hour, int minute, char street) {
        this.camps.put(name, new Location(hour, minute, street));
    }

    /**
     * Finds the closest location to another location
     * @param locations list of Locations
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
     * @param currentLocation current Location
     * @return closest bathroom Location
     */
    public Location findBathroom(Location currentLocation) {
        return findClosest(bathrooms, currentLocation);
    }

    /**
     *  Attempts to find a Location matching a search term
     * @param locations Set of String to search
     * @param searchterm search term
     * @return String matching, or null if none
     */
    private static String findMatch(HashMap<String, Location> locations, String searchterm) {
        searchterm = searchterm.toLowerCase();

        ArrayList<String> matches = new ArrayList<>();
        for(String location : locations.keySet()) {
            String lc = location.toLowerCase();
            if(lc.contains(searchterm)) {
                matches.add(location);
            }
        }

        if(matches.isEmpty()) {
            return null;
        } if(matches.size() == 1) {
            return matches.get(0);
        } else {
            System.out.println("Multiple options found: ");
            for(int i = 1; i <= matches.size(); i++) {
                System.out.println(i + ": " + matches.get(i - 1) + " - " + locations.get(matches.get(i - 1)));
            }
            Scanner scan = new Scanner(System.in);
            try {
                System.out.print("Enter choice: ");
                int choice = scan.nextInt();
                scan.close();
                if(choice > 0 && choice <= matches.size()) {
                    choice -= 1;
                    return matches.get(choice);
                }
            } catch(NumberFormatException e) {
                System.err.println("Input error");
                scan.close();
                return null;
            }
            scan.close();
            return null;
        }
    }

    /**
     * Searches the list of camps for a camp matching the search term
     * @param name search term
     * @return Location of camp, or null if none
     */
    public String findCampName(String name) {
        String campName = findMatch(camps, name);
        if(campName == null) {
            return "";
        } else {
            return campName;
        }
    }

    public Location getCamp(String exactCampName) {
        return camps.get(exactCampName);
    }

    public HashMap<String, Location> getFavorites() {
        return favorites;
    }
}
