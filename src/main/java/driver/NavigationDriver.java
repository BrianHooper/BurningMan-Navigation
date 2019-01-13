package driver;

import events.EventManager;
import navigation.Navigator;
import view.KeyController;
import view.MenuController;
import view.View;

public class NavigationDriver {

    public static void main(String[] args) {
        // Initialize navigator
        EventManager eventManager = new EventManager();
        eventManager.importEvents("eventslist.csv");

        Navigator navigator = new Navigator();
        navigator.initializeLandmarks("bathrooms.csv", "camps.csv", "favorites.csv");
        navigator.loadFromFile("config.cfg");
        // Initialize GUI
        View view = new View();
        MenuController menuController = new MenuController(view, navigator, eventManager);
        KeyController controller = new KeyController(menuController);
        view.setKeyListener(controller);
        view.setNavigation(navigator);

        // Start thread for reading coordinate data
        CoordinateListener coordinateListener = new CoordinateListener(navigator, view);
        coordinateListener.start();

        ClockDriver clockDriver = new ClockDriver(view);
        clockDriver.start();
    }
}
