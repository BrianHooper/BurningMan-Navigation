package driver;

import events.Event;
import navigation.Navigator;
import view.KeyController;
import view.MenuController;
import view.View;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

        ArrayList<Integer> startOffsets = new ArrayList<>();
        startOffsets.add(3);
        startOffsets.add(16);
        startOffsets.add(0);
        startOffsets.add(4);
        startOffsets.add(5);
        startOffsets.add(40);
        startOffsets.add(4);
        startOffsets.add(8);
        startOffsets.add(0);
        LocalDateTime[] startDates = Event.multiDateBuilder(startOffsets);
        ArrayList<Integer> endOffsets = new ArrayList<>();
        endOffsets.add(3);
        endOffsets.add(17);
        endOffsets.add(0);
        endOffsets.add(4);
        endOffsets.add(7);
        endOffsets.add(0);
        LocalDateTime[] endDates = Event.multiDateBuilder(endOffsets);



        System.out.println(Event.asString(startDates, endDates));
    }
}
