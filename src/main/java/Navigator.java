public class Navigator {
    private Location currentLocation;
    private Location home;
    private Location currentDestination;
    private String currentDestinationName;
    private Landmarks landmarks;

    public Navigator() {
        landmarks = new Landmarks();
        landmarks.addBathroom(4,30,'D');
        landmarks.addBathroom(7,30,'D');

        currentLocation = new Location(6,0,'D');
        home = new Location(6,0,'D');

        currentDestination = null;
        currentDestinationName = "";

        findCamp("");

    }

    public void updateLocation(int hour, int minute, char street) {
        currentLocation = new Location(hour, minute, street);
    }

    public void findCamp(String campName) {
        this.currentDestination = new Location(2,30,'A');
        this.currentDestinationName = "Camp Question Mark";
    }

    public void initializeLandmarks(String bathroomsPath, String campsPath, String artPath) {
        landmarks.readBathrooms(bathroomsPath);
        landmarks.readCamps(campsPath);
        landmarks.readArt(artPath);
    }

    public void updatePanel(MainInterfacePanel menu) {
        menu.setCurrentAddress(currentLocation.getAddress());
        Location closestBathroom = landmarks.findBathroom(currentLocation);
        menu.setBathroomAddress(closestBathroom.getAddress());
        menu.setBathroomDirections(currentLocation.distance(closestBathroom) + ", " + currentLocation.cardinal(closestBathroom));

        menu.setHomeAddress(home.getAddress());
        menu.setHomeDirections(currentLocation.distance(home) + ", " + currentLocation.cardinal(home));

        menu.setDestinationName(currentDestinationName);
        menu.setDestinationAddress(currentDestination.getAddress());
        menu.setDestinationDirections(currentLocation.distance(currentDestination) + ", " + currentLocation.cardinal(currentDestination));
    }
}
