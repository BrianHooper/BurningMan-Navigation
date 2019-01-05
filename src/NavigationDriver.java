public class NavigationDriver {
    public static void main(String[] args) {
        Navigator navigator = new Navigator(40.7735, -119.2153);
        navigator.initializeCamplist("camplist.csv");
        navigator.initializeBathrooms("bathrooms.csv");

        System.out.println("Find the nearest bathroom:\n");
        navigator.findNearestBathroom();
        System.out.println("\nFind center camp:\n");
        navigator.findCamp("Center Camp");
    }
}
