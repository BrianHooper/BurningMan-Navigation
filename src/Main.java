import java.util.Scanner;

public class Main {
    public static final double MAN_LATITUDE = 40.7864;
    public static final double MAN_LONGITUDE = -119.2065;
    public static final int ESPLANADE_DISTANCE = 2700;
    public static final int BLOCK_WIDTH = 240;

    public static CampList campList = new CampList();

    public static void printInfo(Position currentPosition, Position destination) {
        if(currentPosition == null) {
            return;
        }

        System.out.println("Current address: " + currentPosition.toString());

        if(destination != null) {
            System.out.println("Distance to destination: " + currentPosition.distance(destination));
            System.out.println("Bearing to destination: " + currentPosition.bearing(destination));
        }
    }

    public static Position findCamp() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter camp search term: ");
        String campName = scan.nextLine();
        return campList.findCamp(campName);
    }

    public static void updatePosition(Position destination) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter latitude: ");
        double latitude = scan.nextDouble();
        System.out.print("Enter longitude: ");
        double longitude = scan.nextDouble();
        System.out.print("Enter bearing: ");
        int bearing = scan.nextInt();

        Coordinate coordinate = new Coordinate(latitude, longitude, bearing);
        Position currentPosition = coordinate.getCurrentPosition();

        printInfo(currentPosition, destination);
    }

    public static void menu() {
        String menuChoices = "1] update location\n2] find camp";


        String input = "1";
        Scanner scan = null;
        int choice = 0;

        Position destination = null;

        scan = new Scanner(System.in);
        while(input.length() > 0) {
            System.out.println(menuChoices);
            if(scan.hasNextLine()) {
                input = scan.nextLine();
                if(input.length() == 0) {
                    break;
                }

                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    break;
                }

                switch (choice) {
                    case 1: updatePosition(destination); break;
                    case 2: destination = findCamp(); break;
                    default: break;
                }
            }
        }
        scan.close();
    }

    public static void main(String[] args) {
        campList.importCamps("camplist.csv");
        menu();
        /*
        Coordinate location = new Coordinate(40.7785, -119.2153, 60);
        System.out.println("Current coordinates: " + location);
        System.out.println("Current address: " + location.address());
        System.out.println("Distance to the man: " + location.distance(campList.findCamp("Man")) + "'");
        System.out.println("Bearing to center camp: " + location.bearing(campList.findCamp("Center")) + " degrees");
        */
    }
}
