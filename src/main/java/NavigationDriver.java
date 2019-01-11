import navigation.Navigator;
import view.KeyController;
import view.MenuController;
import view.View;

public class NavigationDriver {

    public static void main(String[] args) {
        Navigator navigator = new Navigator();
        navigator.initializeLandmarks("bathrooms.csv", "camps.csv");

        navigator.updateLocation(4,30,'K');
        View view = new View();
        MenuController menuController = new MenuController(view, navigator);
        KeyController controller = new KeyController(menuController);
        view.setKeyListener(controller);
        view.setNavigation(navigator);

        CoordinateListener coordinateListener = new CoordinateListener(navigator, view);
        coordinateListener.start();
    }
}
