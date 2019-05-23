package view;


import navigation.Navigator;

/**
 * Class Menu
 * <p>
 * Handles displaying menu items
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class Menu {
    // Current list of menu items
    private MenuLabel[] menuItems;

    // Currently selected menu item
    private int selected = 0;

    /**
     * Constructor
     * <p>
     * creates Home menu
     */
    Menu() {

    }

    /**
     * Activates currently selected menu label
     *
     * @return String actionCommand
     */
    boolean select(View view, Navigator navigator) {
        if(menuItems[selected].getText().equals("Exit")) {
            return false;
        } else {
            menuItems[selected].performAction(view, navigator);
            return true;
        }
    }

    /**
     * Creates favorites menu
     */
    void favorites(View view, Navigator navigator) {
        menuItems = new MenuLabel[]{
                new MenuLabel("Add new favorite", OptionPaneCreator::goToAddress, view, navigator),
                new MenuLabel("Navigate to favorite", OptionPaneCreator::goToAddress, view, navigator),
                new MenuLabel("Delete favorite", OptionPaneCreator::goToAddress, view, navigator)
        };

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates home menu
     */
    void home(View view, Navigator navigator) {
        menuItems = new MenuLabel[]{
                new MenuLabel("Go to address", OptionPaneCreator::goToAddress, view, navigator),
                new MenuLabel("Find camp", OptionPaneCreator::findCamp, view, navigator),
                new MenuLabel("Favorites", OptionPaneCreator::favorites, view, navigator),
                new MenuLabel("Find Events", OptionPaneCreator::findEvents, view, navigator),
                new MenuLabel("Happening soon", OptionPaneCreator::listEventsHappeningSoon, view, navigator),
                new MenuLabel("Notes", OptionPaneCreator::notes, view, navigator),
                new MenuLabel("Measure distance", OptionPaneCreator::measureDistance, view, navigator),
                new MenuLabel("Settings", OptionPaneCreator::settings, view, navigator)
        };

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates settings menu
     */
    void settings(View view, Navigator navigator) {
        menuItems = new MenuLabel[]{
                new MenuLabel("Set Home Address", OptionPaneCreator::setHome, view, navigator),
                new MenuLabel("Set Man Coordinates", OptionPaneCreator::setMan, view, navigator),
                new MenuLabel("Set Event Start Time", OptionPaneCreator::setEventStartTime, view, navigator),
                new MenuLabel("Adjust Block Widths", OptionPaneCreator::adjustBlockWidths, view, navigator),
                new MenuLabel("Adjust Correction Coefficients", OptionPaneCreator::adjustCoefficients, view, navigator),
                new MenuLabel("Calculate Correction Error", OptionPaneCreator::calculateError, view, navigator),
                new MenuLabel("Exit", OptionPaneCreator::exitProgram, view, navigator)
        };

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates notes menu
     */
    void notes(View view, Navigator navigator) {
        menuItems = new MenuLabel[]{
                new MenuLabel("Add new note", OptionPaneCreator::addNote, view, navigator),
                new MenuLabel("View and edit notes", OptionPaneCreator::editNotes, view, navigator),
                new MenuLabel("Delete notes", OptionPaneCreator::deleteNotes, view, navigator)
        };
        selected = 0;
        menuItems[0].select();
    }

    /**
     * Moves menu selection down
     */
    void down() {
        menuItems[selected].deselect();
        selected++;
        if(selected >= menuItems.length) {
            selected = 0;
        }

        menuItems[selected].select();
    }

    /**
     * Moves menu selection up
     */
    void up() {
        menuItems[selected].deselect();
        selected--;
        if(selected < 0) {
            selected = menuItems.length - 1;
        }
        menuItems[selected].select();
    }

    /**
     * Getter for menuItems
     *
     * @return menuItems
     */
    MenuLabel[] readMenu() {
        return menuItems;
    }

}
