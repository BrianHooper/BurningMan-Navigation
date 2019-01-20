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
    private static int esplanade_distance = 2600;
    private static int block_width = 240;

    // Parameters for location
    private int hour;
    private int minute;
    private double distance;

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
     * Getter for esplanade distance
     *
     * @return esplanade distance
     */
    public static int getEsplanade_distance() {
        return esplanade_distance;
    }

    /**
     * Setter for esplanade distance
     *
     * @param esplanade_distance esplanade distance
     */
    public static void setEsplanade_distance(int esplanade_distance) {
        Location.esplanade_distance = esplanade_distance;
    }

    /**
     * Getter for block width
     *
     * @return block width
     */
    public static int getBlock_width() {
        return block_width;
    }

    /**
     * Setter for block width
     *
     * @param block_width block width
     */
    public static void setBlock_width(int block_width) {
        Location.block_width = block_width;
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
     * @param street char
     * @return double
     */
    static double toDistance(char street) {
        return (street - 64) * block_width + esplanade_distance;
    }

    /**
     * Converts a distance in feet to a street
     * <p>
     * returns '0' if invalid
     *
     * @param distance double
     * @return char
     */
    public static char toStreet(double distance) {
        if(distance < esplanade_distance) {
            return '0';
        }
        return (char) (((distance - esplanade_distance) / block_width) + 64);
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
            distance = Double.parseDouble(split[2]);
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
    public Location(int hour, int minute, double distance) {
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
        int chVal = ((int) Character.toUpperCase(street));
        if(chVal >= 65 && chVal <= 76) {
            chVal -= 64;
        } else {
            chVal = 0;
        }
        this.distance = esplanade_distance + chVal * block_width;
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
    private static double distance(double lat1, double lon1) {
        lat1 = Math.toRadians(lat1);
        double lat2 = Math.toRadians(man_latitude);
        lon1 = Math.toRadians(lon1);
        double lon2 = Math.toRadians(man_longitude);
        double u = Math.sin((lat2 - lat1) / 2);
        double v = Math.sin((lon2 - lon1) / 2);
        double kilometers = 2.0 * 6371 * Math.asin(Math.sqrt(u * u + Math.cos(lat1) * Math.cos(lat2) * v * v));
        return kilometers / 1.6 * 5280;
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
    public double getDistance() {
        return distance;
    }

    /**
     * Calculates the current address based on angular position and distance from the man
     *
     * @return Address as String
     */
    String getAddress() {
        if(distance < esplanade_distance || distance > esplanade_distance + 12 * block_width) {
            return hour + ":" + minute + " & " + ((int) distance) + "'";
        } else {
            String street;
            if(distance < esplanade_distance + block_width) {
                street = "Esplanade";
            } else {
                street = Character.toString(toStreet(distance));
            }
            if(minute < 10) {
                return hour + ":0" + minute + " & " + street;
            } else {
                return hour + ":" + minute + " & " + street;
            }
        }
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
}
