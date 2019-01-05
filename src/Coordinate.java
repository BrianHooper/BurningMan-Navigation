/**
 * Class Coordinate
 * Represents the current GPS coordinate of the user,
 * their current location in the format of a BM address,
 * and calculates their position relative to landmarks.
 */
public class Coordinate {
    private Position currentPosition;
    private double latitude, longitude, bearing;

    /**
     * Constructor
     * @param latitude current latitude position
     * @param longitude current longitude position
     */
    public Coordinate(double latitude, double longitude) {
        updateCoordinates(latitude, longitude);
    }

    /**
     * Constructor
     * @param latitude current latitude position
     * @param longitude current longitude position
     * @param bearing current bearing in degrees
     */
    public Coordinate(double latitude, double longitude, double bearing) {
        this(latitude, longitude);
        this.bearing = bearing;
    }

    public void updateCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        double time = (angle() / 360) * 12;
        int hour = (int) time;
        int minute = (int) ((time - hour) * 60);
        currentPosition = new Position(hour, minute, distance());
    }

    /**
     * Calculates distance relative to the man
     * @return feet from the man
     */
    private double distance() {
        double latRad = Math.toRadians(latitude);
        double latMan = Math.toRadians(Main.MAN_LATITUDE);
        double lonRad = Math.toRadians(longitude);
        double lonMan = Math.toRadians(Main.MAN_LONGITUDE);
        double u = Math.sin((latMan - latRad) / 2);
        double v = Math.sin((lonMan - lonRad) / 2);
        double kilometers = 2.0 * 6371 * Math.asin(Math.sqrt(u * u + Math.cos(latRad) * Math.cos(latMan) * v * v));
        return kilometers / 1.6 * 5280;
    }

    /**
     * Calculates angle relative to the man and north
     * @return angle in degrees
     */
    private double angle() {
        double angle = Math.toDegrees(Math.atan2(latitude - Main.MAN_LATITUDE, longitude - Main.MAN_LONGITUDE));
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
     * Gets current position
     * @return currentPosition
     */
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
     * Calculates the relative bearing to another landmark, relative
     * to the current bearing
     * @param other Position of landmark
     * @return bearing in degrees
     */
    public double relativeBearing(Position other) {
        if(other == null) {
            return -1;
        }
        double otherBearing = bearing(other);
        return bearing - otherBearing;
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
