public class NavigationDriver {
    public static void main(String[] args) {
        Location location = new Location(6,0,'K');

        Landmarks landmarks = new Landmarks();
        landmarks.readCamps("camps.csv");

        Location other = landmarks.findCamp("swab");
        System.out.println(location.distance(other));
        System.out.println(location.cardinal(other));
    }
}
