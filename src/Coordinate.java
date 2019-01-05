/**
 * Class Coordinate
 * Represents the current GPS coordinate of the user,
 * their current location in the format of a BM address,
 * and calculates their position relative to landmarks.
 */
public class Coordinate {
    private Position currentPosition;
    private double latitude, longitude;

    /**
     * Calculates the distance (in feet) between two sets of geographic coordinates
     * @param lat1 latitude of position 1
     * @param lon1 longitude of position 1
     * @param lat2 latitude of position 2
     * @param lon2 longitude of position 2
     * @return distance in feet
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        double u = Math.sin((lat2 - lat1) / 2);
        double v = Math.sin((lon2 - lon1) / 2);
        double kilometers = 2.0 * 6371 * Math.asin(Math.sqrt(u * u + Math.cos(lat1) * Math.cos(lat2) * v * v));
        return kilometers / 1.6 * 5280;
    }

    /**
     * Creates a Position object from a set of geographic coordinates
     * @param latitude latitude position
     * @param longitude longitude position
     * @return Position object with time/distance coordinates
     */
    public static Position toPosition(double latitude, double longitude) {
        double time = (angle(latitude, longitude) / 360) * 12;
        int hour = (int) time;
        int minute = (int) ((time - hour) * 60);
        return new Position(hour, minute, manDistance(latitude, longitude));
    }

    /**
     * Calculates the angle relative to the man from a set of geographic coordinates
     * @param latitude latitude position
     * @param longitude longitude position
     * @return angle in degrees
     */
    public static double angle(double latitude, double longitude) {
        double angle = Math.toDegrees(Math.atan2(latitude - Navigator.man_latitude, longitude - Navigator.man_longitude));
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
        return angle;
    }

    /**
     * Constructor
     * @param latitude current latitude position
     * @param longitude current longitude position
     */
    public Coordinate(double latitude, double longitude) {
        updateCoordinates(latitude, longitude);
    }

    /**
     * Updates the current geographic position
     * @param latitude latitude position
     * @param longitude longitude position
     */
    public void updateCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        currentPosition = toPosition(latitude, longitude);
    }

    /**
     * Calculates distance relative to the man
     * @return feet from the man
     */
    private static double manDistance(double latitude, double longitude) {
        return distance(latitude, longitude, Navigator.man_latitude, Navigator.man_longitude);
    }

    /**
     * Gets current position
     * @return currentPosition
     */
    @SuppressWarnings("unused")
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Gets address as a string
     * @return address
     */
    public String address() {
        return currentPosition.toString();
    }

    /**
     * Calculates the distance relative to a landmark
     * @param other Position of landmark
     * @return distance to landmark in feet
     */
    public int distance(Position other) {
        if(other == null) {
            return -1;
        }
        return currentPosition.distance(other);
    }

    /**
     * Calculates the bearing to another landmark, relative to north
     * @param other Position of landmark
     * @return bearing in degrees
     */
    public double bearing(Position other) {
        if(other == null) {
            return -1;
        }
        return currentPosition.bearing(other);
    }

    /**
     * Returns the bearing to another position as a cardinal (east-west) direction
     * @param other Position of landmark
     * @return direction as String
     */
    public String cardinal(Position other) {
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
     * Overridden toString
     * @return GPS coordinates as String
     */
    @Override
    public String toString() {
        return latitude + ", " + longitude;
    }
}
