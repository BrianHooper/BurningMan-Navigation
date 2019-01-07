import java.util.Scanner;

public class NavigationDriver {
    public static void search() {
        Navigator navigator = new Navigator(40.7735, -119.2153);
        navigator.initializeCamplist("camplist.csv");
        navigator.initializeBathrooms("bathrooms.csv");

        System.out.println("Find the nearest bathroom:\n");
        navigator.findNearestBathroom();
        System.out.println("\nFind center camp:\n");
        navigator.findCamp("Center Camp");
    }

    public static void testAddresses() {
        Scanner scan = new Scanner(System.in);
        Navigator navigator = new Navigator(40.7735, -119.2153);
        while(true) {
            System.out.print("Latitude: ");
            double latitude = scan.nextDouble();
            System.out.print("Longitude: ");
            double longitude = scan.nextDouble();
            navigator.updateLocation(latitude, longitude);
            System.out.println(navigator.currentAddress());
        }
    }

    public static void main(String[] args) {
        testAddresses();
//        Navigator navigator = new Navigator(40.791233, -119.199964);
//        System.out.println(navigator.currentAddress());
    }
}
