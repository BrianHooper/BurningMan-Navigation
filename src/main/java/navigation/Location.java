package navigation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.AbstractMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class Location
 * <p>
 * Represents a single point within burning man, either a person, camp, bathroom, or art instillation
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class Location {

//**********************
// Class member fields
//**********************

    // Relative path for coordinate file
    private static final String coordinatePath = "config/coordinate";

    // Globals used for converting gps coordinates to addresses
    private static double man_latitude = 40.7864;
    private static double man_longitude = -119.2065;
    private static Object[][] blockDistances = new Object[][] {
            { 2300, "The Man"},
            { 2700, "Esplanade"},
            { 2950, "A"},
            { 3200, "B"},
            { 3450, "C"},
            { 3700, "D"},
            { 3900, "E"},
            { 4100, "F"},
            { 4300, "G"},
            { 4500, "H"},
            { 4650, "I"},
            { 4800, "J"},
            { 4950, "K"},
            { 5100, "L"},
            { 5250, "o" },
    };

    // Parameters for location
    private int hour;
    private int minute;
    private int distance;

    // Adjustment coefficients
    // Default 8.2 * Math.sin(0.035 * angle + 6.3) + 44.7;
    private static double[] adjustmentCoefficients = {8.2, 0.035, 6.3, 44.7};

//**********************
// Public static methods
//**********************

    /**
     * Getter for man latitude
     *
     * @return man_latitude
     */
    public static double getMan_latitude() {
        return man_latitude;

    }

    /**
     * Getter for man longitude
     *
     * @return man_longitude
     */
    public static double getMan_longitude() {
        return man_longitude;
    }

    /**
     * Updates coordinates of the man
     *
     * @param latitude  man latitude
     * @param longitude man longitude
     */
    public static void setManCoordinates(double latitude, double longitude) {
        man_latitude = latitude;
        man_longitude = longitude;
    }

    /**
     * Getter for adjustment coefficients
     *
     * @return double[]
     */
    public static double[] getAdjustmentCoefficients() {
        return adjustmentCoefficients;
    }

    /**
     * Setter for block distances
     * @param blockDistances object[][] of type Integer, String
     */
    public static void setBlockDistances(Object[][] blockDistances) {
        Location.blockDistances = blockDistances;
    }

    /**
     * Getter for block distances
     * @return  object[][] of type Integer, String
     */
    public static Object[][] getBlockDistances() {
        return blockDistances;
    }

    /**
     * Setter for adjustment parameters
     *
     * @param a double, default 8.2
     * @param b double, default 0.035
     * @param c double, default 6.3
     * @param d double, default 44.7
     */
    public static void setAdjustmentCoefficients(double a, double b, double c, double d) {
        adjustmentCoefficients = new double[]{a, b, c, d};
    }

    /**
     * Converts a char street to a distance
     *
     * @param streetChar char
     * @return double
     */
    static int toDistance(char streetChar) {
        streetChar = Character.toUpperCase(streetChar);
        if(streetChar < 65 || streetChar > 76) return 0;
        String street = String.valueOf(streetChar);
        int index = 1;
        while(index < Location.blockDistances.length - 1 && !street.equals(Location.blockDistances[index][1])) {
            index++;
        }

        int difference = (int) Location.blockDistances[index][0] - (int) Location.blockDistances[index + 1][0];
        return (int) Location.blockDistances[index][0] + (difference / 2);
    }

    /**
     * Converts a distance in feet to a street
     * <p>
     * returns '0' if invalid
     *
     * @param distance int
     * @return char
     */
    public static String toStreet(int distance) {
        String street = String.valueOf(distance);
        int index = 0;
        if(distance < ((int) blockDistances[blockDistances.length - 1][0])) {
            while (index < blockDistances.length  - 1 && distance > ((int) blockDistances[index][0])) {
                street = (String) blockDistances[index + 1][1];
                index++;
            }
        }
        return street;
    }

//**********************
// Constructors and initializers
//**********************

    /**
     * Constructor
     * <p>
     * creates a new Location based off an address as a CSV string
     * <p>
     * If the input is not in "hour,minute,distance" format, will default to 6:00 & B (center camp)
     *
     * @param csvAddress CSV address
     */
    Location(String csvAddress) {
        this(6, 0, 'B');
        String[] split = csvAddress.split(",");
        if(split.length != 3) {
            return;
        }
        try {
            hour = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);
            distance = Integer.parseInt(split[2]);
        } catch(NumberFormatException ignored) {
        }
    }

    /**
     * Constructor
     * <p>
     * Creates a location object from a set of coordinates
     *
     * @param latitude  latitude
     * @param longitude longitude
     */
    public Location(double latitude, double longitude) {
        updateLocation(latitude, longitude);
    }

    /**
     * Constructor
     * <p>
     * Creates a location object from time/distance
     *
     * @param hour     hour
     * @param minute   minute
     * @param distance distance in feet
     */
    public Location(int hour, int minute, int distance) {
        this.hour = hour;
        this.minute = minute;
        this.distance = distance;
    }

    /**
     * Constructor
     * <p>
     * Creates a location object from time/street
     *
     * @param hour   hour
     * @param minute minute
     * @param street street character
     */
    public Location(int hour, int minute, char street) {
        this.hour = hour;
        this.minute = minute;
        this.distance = toDistance(street);
    }

    /**
     * Reads the current coordinates from the coordinate file
     * and returns a latitude, longitude pair
     *
     * @return AbstractMap.SimpleEntry pair
     */
    public static AbstractMap.SimpleEntry<Double, Double> readCoordinates() {
        try {
            Scanner scan = new Scanner(new File(coordinatePath));
            double latitude = Double.parseDouble(scan.nextLine());
            double longitude = Double.parseDouble(scan.nextLine());
            scan.close();
            return new AbstractMap.SimpleEntry<>(latitude, longitude);
        } catch(FileNotFoundException | NumberFormatException | NoSuchElementException e) {
            return null;
        }
    }

//**********************
// Private static methods
//**********************

    /**
     * Calculates the distance between a set of coordinates and the man
     *
     * @param lat1 latitude
     * @param lon1 longitude
     * @return distance in feet
     */
    private static int distance(double lat1, double lon1) {
        lat1 = Math.toRadians(lat1);
        double lat2 = Math.toRadians(man_latitude);
        lon1 = Math.toRadians(lon1);
        double lon2 = Math.toRadians(man_longitude);
        double u = Math.sin((lat2 - lat1) / 2);
        double v = Math.sin((lon2 - lon1) / 2);
        double kilometers = 2.0 * 6371 * Math.asin(Math.sqrt(u * u + Math.cos(lat1) * Math.cos(lat2) * v * v));
        return (int) (kilometers / 1.6 * 5280);
    }

    /**
     * Calculates the angle relative to the man from a set of geographic coordinates
     *
     * @param latitude  latitude position
     * @param longitude longitude position
     * @return angle in degrees
     */
    private static double angle(double latitude, double longitude) {
        double angle = Math.toDegrees(Math.atan2(latitude - man_latitude, longitude - man_longitude));
        if(angle < 0) {
            angle += 360;
        }

        angle -= 90;
        if(angle < 0) {
            angle += 360;
        }
        angle = 360 - angle;
        if(angle == 360) {
            angle = 0;
        }

        angle -= calculateOffset(angle);
        if(angle < 0) {
            angle = 360 + angle;
        } else if(angle > 360) {
            angle = angle - 360;
        }

        return angle;
    }

    /**
     * Regression equation that corrects the relative angle error when converting
     * from true north to the burning man offset
     *
     * @param angle input angle
     * @return adjustment amount
     */
    private static double calculateOffset(double angle) {
        // a * sin(b * x + c) + d
        return adjustmentCoefficients[0] *
                Math.sin(adjustmentCoefficients[1] * angle + adjustmentCoefficients[2]) +
                adjustmentCoefficients[3];
    }

//**********************
// Getters and setters
//**********************

    /**
     * Prints address
     *
     * @return hour:minute & street (or distance)
     */
    @Override
    public String toString() {
        return getAddress();
    }

    /**
     * Gets the current address in "hour,minute,distance" format
     *
     * @return CSV String
     */
    String getCSVAddress() {
        return String.valueOf(hour) + "," + String.valueOf(minute) + "," + String.valueOf((int) distance);
    }

    /**
     * Getter for hour
     *
     * @return hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Getter for minute
     *
     * @return minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Getter for distance
     *
     * @return distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Calculates the current address based on angular position and distance from the man
     *
     * @return Address as String
     */
    String getAddress() {
        String minuteString = String.valueOf(minute);
        if(minuteString.length() < 2) {
            minuteString = "0" + minuteString;
        }
        return hour + ":" + minuteString + " & " + toStreet(distance);
    }

//**********************
// Class methods
//**********************

    /**
     * Updates the current location based on a set of coordinates
     *
     * @param latitude  updated latitude
     * @param longitude updated longitude
     */
    void updateLocation(double latitude, double longitude) {
        double time = (angle(latitude, longitude) / 360) * 12;
        this.hour = (int) time;
        this.minute = (int) ((time - hour) * 60);
        this.distance = distance(latitude, longitude);
    }

    /**
     * Calculates the angle from the man and north
     *
     * @return angle in radians
     */
    private double clockAngle() {
        double time = hour + ((double) minute / 60);
        if(time > 12) {
            time -= 12;
        }
        double percent = time / 12;
        return (2 * Math.PI * percent);
    }

    /**
     * Calculates the X position from the man
     *
     * @return x distance in feet
     */
    private double getY() {
        return -1 * distance * Math.cos(clockAngle());
    }

    /**
     * Calculates the Y position from the man
     *
     * @return y distance in feet
     */
    private double getX() {
        return distance * Math.sin(clockAngle());
    }

    /**
     * Calculates the distance to a landmark
     *
     * @param other Position of landmark
     * @return distance in feet
     */
    public int distance(Location other) {
        if(other == null) {
            return -1;
        }
        double deltaX = other.getX() - getX();
        double deltaY = other.getY() - getY();
        return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    /**
     * Calculates the bearing relative to another Position
     *
     * @param other Position of landmark
     * @return bearing in degrees
     */
    private int bearing(Location other) {
        double deltaX = other.getX() - getX();
        double deltaY = getY() - other.getY();
        double angle = 180 * Math.atan2(deltaX, deltaY) / Math.PI;
        angle += calculateOffset(angle);
        if(angle < 0) {
            angle += 360;
        }
        if(angle == 360) {
            angle = 0;
        }
        return (int) (angle);
    }

    /**
     * Returns the bearing to another position as a cardinal (east-west) direction
     *
     * @param other Position of landmark
     * @return direction as String
     */
    String cardinal(Location other) {
        double bearing = bearing(other);
        if(bearing < 11) return "north";
        else if(bearing < 34) return "north-northeast";
        else if(bearing < 56) return "northeast";
        else if(bearing < 79) return "northeast-east";
        else if(bearing < 101) return "east";
        else if(bearing < 124) return "southeast-east";
        else if(bearing < 146) return "southeast";
        else if(bearing < 169) return "south-southeast";
        else if(bearing < 191) return "south";
        else if(bearing < 214) return "south-southwest";
        else if(bearing < 236) return "southwest";
        else if(bearing < 259) return "southwest-west";
        else if(bearing < 281) return "west";
        else if(bearing < 304) return "northwest-west";
        else if(bearing < 326) return "northwest";
        else if(bearing < 348) return "north-northwest";
        else return "north";
    }

    String toStringForOutput() {
        String minuteString = String.valueOf(minute);
        if(minuteString.length() < 2) {
            minuteString = "0" + minuteString;
        }
        String street = toStreet(distance);
        if(street.length() > 1) {
            street = String.valueOf(distance);
        }
        return hour + ":" + minuteString + " & " + street;
    }
}
