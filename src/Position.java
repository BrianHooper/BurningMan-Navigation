/**
 * Class Position
 * Represents the BM address of a camp or person
 */
public class Position {

    private int hour;
    private int minute;
    private double distance;
    private double angle;

    /**
     * Constructor
     * @param hour angular hourly position
     * @param minute angular minute position
     * @param street street as Character, A-L for street or other for Esplanade
     */
    public Position(int hour, int minute, char street) {
        this.hour = hour;
        this.minute = minute;
        int chVal = ((int) Character.toUpperCase(street));
        if(chVal >= 65 && chVal <= 76) {
            chVal -= 64;
        } else {
            chVal = 0;
        }
        this.distance = 2700 + chVal * 240;
        this.angle = angle();
    }

    /**
     * Consturctor
     * @param hour angular hourly position
     * @param minute angular minute position
     * @param distance distance from the man in feet
     */
    public Position(int hour, int minute, double distance) {
        this.hour = hour;
        this.minute = minute;
        this.distance = distance;
        this.angle = angle();
    }

    /**
     * Calculates the current address based on angular position and distance from the man
     * @return Address as String
     */
    private String getAddress() {
        if(distance < Main.ESPLANADE_DISTANCE || distance > Main.ESPLANADE_DISTANCE + 12 * Main.BLOCK_WIDTH) {
            return hour + ":" + minute + " & " + distance + "'";
        } else {
            String street;
            if(distance < Main.ESPLANADE_DISTANCE + Main.BLOCK_WIDTH) {
                street = "Esplanade";
            } else {
                char chIndex = (char) (((distance - Main.ESPLANADE_DISTANCE) / Main.BLOCK_WIDTH) + 64);
                street = Character.toString(chIndex);
            }
            return hour + ":" + minute + " & " + street;
        }
    }

    /**
     * Calculates the angle from the man and north
     * @return angle in degrees
     */
    private double angle() {
        double time = hour + ((double) minute / 60) + 3;
        if(time > 12) {
            time -= 12;
        }
        double percent = time / 12;
        return 2 * Math.PI * percent;
    }

    /**
     * Calculates the X position from the man
     * @return x distance in feet
     */
    private double getX() {
        return -1 * distance * Math.cos(angle);
    }

    /**
     * Calculates the Y position from the man
     * @return y distance in feet
     */
    private double getY() {
        return distance * Math.sin(angle);
    }

    /**
     * Calculates the bearing relative to another Position
     * @param other Position of landmark
     * @return bearing in degrees
     */
    public int bearing(Position other) {
        double deltaX = other.getX() - getX();
        double deltaY = other.getY() - getY();
        double angle = 180 * Math.atan2(deltaX, deltaY) / Math.PI;
        if(angle < 0) {
            angle += 360;
        }
        if(angle == 360) {
            angle = 0;
        }
        return (int) angle;
    }

    /**
     * Calculates the distance to a landmark
     * @param other Position of landmark
     * @return distance in feet
     */
    public int distance(Position other) {
        if(other == null) {
            return -1;
        }
        double deltaX = other.getX() - getX();
        double deltaY = other.getY() - getY();
        return (int) Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    /**
     * Overridden toString
     * @return address
     */
    @Override
    public String toString() {
        return getAddress();
    }
}
