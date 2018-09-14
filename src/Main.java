public class Main {
    public static final double MAN_LATITUDE = 40.7864;
    public static final double MAN_LONGITUDE = -119.2065;
    public static final int ESPLANADE_DISTANCE = 2700;
    public static final int BLOCK_WIDTH = 240;

    public static void main(String[] args) {
        CampList campList = new CampList();
        campList.importCamps("camplist.csv");
        Coordinate location = new Coordinate(40.7785, -119.2153, 60);
        System.out.println("Current coordinates: " + location);
        System.out.println("Current address: " + location.address());
        System.out.println("Distance to the man: " + location.distance(campList.findCamp("Man")) + "'");
        System.out.println("Bearing to center camp: " + location.bearing(campList.findCamp("Center")) + " degrees");
    }
}
