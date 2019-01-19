package driver;

import view.UserInterfaceController;

/**
 * Class NavigationDriver
 * <p>
 * Main entry point for the application
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class NavigationDriver {

    /**
     * Initializes the user interface and navigation control
     */
    private void run() {
        UserInterfaceController.start();
    }

    public static void main(String[] args) {
        new NavigationDriver().run();
    }
}
