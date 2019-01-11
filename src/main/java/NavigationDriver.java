import events.Event;
import navigation.Navigator;
import view.KeyController;
import view.MenuController;
import view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NavigationDriver {

    public static void main(String[] args) {
//        // Initialize navigator
//        Navigator navigator = new Navigator();
//        navigator.initializeLandmarks("bathrooms.csv", "camps.csv", "favorites.csv");
//        navigator.loadFromFile("config.cfg");
//
//        // Initialize GUI
//        View view = new View();
//        MenuController menuController = new MenuController(view, navigator);
//        KeyController controller = new KeyController(menuController);
//        view.setKeyListener(controller);
//        view.setNavigation(navigator);
//
//        // Start thread for reading coordinate data
//        CoordinateListener coordinateListener = new CoordinateListener(navigator, view);
//        coordinateListener.start();

        Date date = Event.dateBuilder(1, 16, 0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        System.out.println(df.format(date));
    }
}
