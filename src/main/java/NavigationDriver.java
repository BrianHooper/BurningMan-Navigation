public class NavigationDriver {

    public static void main(String[] args) {
        Navigator navigator = new Navigator();
        navigator.initializeLandmarks("bathrooms.csv", "camps.csv");

        navigator.updateLocation(4,30,'K');
        View view = new View(navigator);

        view.setNavigation(navigator);

        CoordinateListener coordinateListener = new CoordinateListener(navigator, view);
        coordinateListener.start();
    }
}
