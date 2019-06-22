package view;

import driver.ClockDriver;
import driver.CoordinateListener;
import driver.LogDriver;
import navigation.Navigator;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Class UserInterfaceController
 * <p>
 * Handles menu and updates view, creates all OptionPane objects
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class UserInterfaceController {
//**********************
// Class member fields
//**********************

    // Main GUI view
    private final View view;

    // Managers for location and landmarks
    private final Navigator navigator;

    // Threads for updating clock and coordinates
    private final ClockDriver clockDriver;
    private final CoordinateListener coordinateListener;

    // Logger
    private final LogDriver logger = LogDriver.getInstance();

//**********************
// Constructors and initializers
//**********************

    /**
     * Constructor
     *
     * @param view      main View
     * @param navigator Navigator object
     */
    private UserInterfaceController(View view, Navigator navigator) {
        LogDriver.activate();
        this.view = view;
        this.navigator = navigator;

        view.setNavigation(navigator);
        KeyController controller = new KeyController(this);
        view.setKeyListener(controller);

        coordinateListener = new CoordinateListener(navigator, view);
        coordinateListener.start();

        clockDriver = new ClockDriver(view);
        clockDriver.start();
        menuEscape();
    }

    /**
     * Initializes a new graphical interface and loads the navigator
     * <p>
     * Creates threads for reading coordinates and updating the clock / location monitoring
     */
    public static void start() {
        View view = new View();
        Navigator navigator = new Navigator();
        new UserInterfaceController(view, navigator);

    }

    /**
     * Rejoins thread processes, closes open files, and terminates the program
     */
    private void exit() {
        navigator.writeToConfigFile();
        navigator.writeFavorites();
        navigator.getNoteManager().saveNotes();
        logger.close();

        clockDriver.terminate();
        coordinateListener.terminate();

        try {
            clockDriver.join(2000);
            coordinateListener.join(2000);
        } catch(InterruptedException e) {
            logger.severe(this.getClass(), "InterruptedException while joining threads: " + e.getMessage());
            System.exit(1);
        }
        System.exit(0);
    }

//**********************
// Class methods
//**********************

    /**
     * Updates view / moves selection up
     */
    void menuUp() {
        view.getMenu().up();
    }

    /**
     * Updates view / moves selection down
     */
    void menuDown() {
        view.getMenu().down();
    }

    /**
     * Activates current selection
     */
    void menuSelect() {
        if(!view.getMenu().select(view, navigator)) {
            exit();
        }
        view.setNavigation(navigator);
        view.getFocus();
    }

    /**
     * Updates view with home selection
     */
    void menuEscape() {
        view.getMenu().home(view, navigator);
        view.getFocus();
    }


}
