/**
 * Class Navigator
 * Provides directions to Burning Man landmarks
 */
public class Navigator {
    public static double man_latitude = 40.7864;
    public static double man_longitude = -119.2065;
    public static int esplanade_distance = 2700;
    public static int block_width = 240;

    private Coordinate currentCoordinate;
    private CampList campList;
    private Bathrooms bathrooms;

    /**
     * Constructor
     *
     * Initializes a navigator object with an initial position
     * @param latitude initial latitude
     * @param longitude initial longitude
     */
    public Navigator(double latitude, double longitude) {
        currentCoordinate = new Coordinate(latitude, longitude);
    }

    /**
     * Imports a csv file containing a list of bathroom coordinates
     * @param bathroomListLocation path to csv file
     */
    public void initializeBathrooms(String bathroomListLocation) {
        //TODO read bathroom locations from csv file
        bathrooms = new Bathrooms();
        bathrooms.add(40.7933,-119.2118);
    }

    /**
     * Imports a csv file containing a list of camp locations
     * @param campListLocation path to csv file
     */
    public void initializeCamplist(String campListLocation) {
        //TODO read camp locations from csv file
        campList = new CampList();
        campList.importCamps(campListLocation);
    }

    /**
     * Prints out directions to the nearest bathroom
     */
    public void findNearestBathroom() {
        Position closestBathroom = bathrooms.findClosest(currentCoordinate);
        System.out.println("You are currently located at " + currentCoordinate);
        System.out.println("Nearest to address " + currentCoordinate.address());
        System.out.println("The closest bathroom is located at " + closestBathroom);
        System.out.println(directions(closestBathroom));
    }

    /**
     * Prints out directions to another camp
     * @param campName name of other camp
     */
    public void findCamp(String campName) {
        System.out.println("You are currently located at " + currentCoordinate.toString());
        System.out.println("Nearest to address " + currentCoordinate.address());
        Position camp = campList.findCamp(campName);
        if(camp == null) {
            System.out.println(campName + " cannot be found");
            return;
        }
        System.out.println(campName + " is located at " + camp);
        System.out.println(directions(camp));
    }

    /**
     * Prints out directions to a position
     * @param other Position object
     * @return directions as String
     */
    public String directions(Position other) {
        double distance = currentCoordinate.distance(other);
        String cardinal = currentCoordinate.cardinal(other);
        double bearing = currentCoordinate.bearing(other);

        return "Move " + distance + " feet to the " + cardinal + ", bearing " + bearing + ".";
    }
}
