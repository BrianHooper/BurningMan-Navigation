/* Brian Hooper
 *
 * 2018
 */

/**
 * Class Location
 *
 * Represents a single point within burning man, either a person, camp, bathroom, or art instillation
 */
public class Location {
    public static double man_latitude = 40.7864;
    public static double man_longitude = -119.2065;
    public static int esplanade_distance = 2600;
    public static int block_width = 240;

    private int hour;
    private int minute;
    private double distance;

    /**
     * Calculates the distance between a set of coordinates and the man
     * @param lat1 latitude
     * @param lon1 longitude
     * @return distance in feet
     */
    public static double distance(double lat1, double lon1) {
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
     * @param latitude latitude position
     * @param longitude longitude position
     * @return angle in degrees
     */
    public static double angle(double latitude, double longitude) {
        double angle = Math.toDegrees(Math.atan2(latitude - man_latitude, longitude - man_longitude));
        if(angle < 0) {
            angle += 360;
        }

        angle -= 90;
        if(angle < 0){
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
     * @param angle input angle
     * @return adjustment amount
     */
    private static double calculateOffset(double angle) {
        return 8.2 * Math.sin(0.035 * angle + 6.3) + 44.7;
    }

    /**
     * Constructor
     *
     * Creates a location object from a set of coordinates
     * @param latitude latitude
     * @param longitude longitude
     */
    public Location(double latitude, double longitude) {
        updateLocation(latitude, longitude);
    }

    /**
     * Constructor
     *
     * Creates a location object from time/distance
     * @param hour hour
     * @param minute minute
     * @param distance distance in feet
     */
    public Location(int hour, int minute, double distance) {
        this.hour = hour;
        this.minute = minute;
        this.distance = distance;
    }

    /**
     * Constructor
     *
     * Creates a location object from time/street
     * @param hour hour
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
        this.distance = 2700 + chVal * 240;
    }

    /**
     * Updates the current location based on a set of coordinates
     * @param latitude updated latitude
     * @param longitude updated longitude
     */
    public void updateLocation(double latitude, double longitude) {
        double time = (angle(latitude, longitude) / 360) * 12;
        this.hour = (int) time;
        this.minute = (int) ((time - hour) * 60);
        this.distance = distance(latitude, longitude);
    }

    /**
     * Calculates the current address based on angular position and distance from the man
     * @return Address as String
     */
    public String getAddress() {
        if(distance < esplanade_distance || distance > esplanade_distance + 12 * block_width) {
            return hour + ":" + minute + " & " + distance + "'";
        } else {
            String street;
            if(distance < esplanade_distance + block_width) {
                street = "Esplanade";
            } else {
                char chIndex = (char) (((distance - esplanade_distance) / block_width) + 64);
                street = Character.toString(chIndex);
            }
            if(minute < 10) {
                return hour + ":0" + minute + " & " + street;
            } else {
                return hour + ":" + minute + " & " + street;
            }
        }
    }

    /**
     * Calculates the angle from the man and north
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
     * @return x distance in feet
     */
    public double getY() {
        return -1 * distance * Math.cos(clockAngle());
    }

    /**
     * Calculates the Y position from the man
     * @return y distance in feet
     */
    public double getX() {
        return distance * Math.sin(clockAngle());
    }

    /**
     * Calculates the distance to a landmark
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
     * @param other Position of landmark
     * @return bearing in degrees
     */
    public int bearing(Location other) {
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
     * @param other Position of landmark
     * @return direction as String
     */
    public String cardinal(Location other) {
        double bearing = bearing(other);
        if(bearing < 23) return "north";
        else if(bearing < 68) return "northeast";
        else if(bearing < 113) return "east";
        else if(bearing < 158) return "southeast";
        else if(bearing < 203) return "south";
        else if(bearing < 248) return "southwest";
        else if(bearing < 293) return "west";
        else if(bearing < 338) return "northwest";
        else return "north";
    }

    /**
     * Prints address
     * @return hour:minute & street (or distance)
     */
    @Override
    public String toString() {
        return getAddress();
    }
}
