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
        home();
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
    void favorites() {
        menuItems = new MenuLabel[]{
                new MenuLabel("Add new favorite", OptionPaneCreator::goToAddress),
                new MenuLabel("Navigate to favorite", OptionPaneCreator::goToAddress),
                new MenuLabel("Delete favorite", OptionPaneCreator::goToAddress)
        };

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates home menu
     */
    void home() {
        menuItems = new MenuLabel[]{
                new MenuLabel("Go to address", OptionPaneCreator::goToAddress),
                new MenuLabel("Find camp", OptionPaneCreator::findCamp),
                new MenuLabel("Favorites", OptionPaneCreator::favorites),
                new MenuLabel("Find Events", OptionPaneCreator::findEvents),
                new MenuLabel("Happening soon", OptionPaneCreator::listEventsHappeningSoon),
                new MenuLabel("Notes", OptionPaneCreator::notes),
                new MenuLabel("Measure distance", OptionPaneCreator::measureDistance),
                new MenuLabel("Settings", OptionPaneCreator::settings)
        };

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates settings menu
     */
    void settings() {
        menuItems = new MenuLabel[]{
                new MenuLabel("Set home address", OptionPaneCreator::setHome),
                new MenuLabel("Set man coordinates", OptionPaneCreator::setMan),
                new MenuLabel("Adjust Esplanade distance", OptionPaneCreator::setEsplanade),
                new MenuLabel("Adjust block width", OptionPaneCreator::setBlockWidth),
                new MenuLabel("Set event start time", OptionPaneCreator::setEventStartTime),
                new MenuLabel("Adjust Correction Coefficients", OptionPaneCreator::adjustCoefficients),
                new MenuLabel("Calculate correction error", OptionPaneCreator::calculateError),
                new MenuLabel("Exit", null)
        };

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates notes menu
     */
    void notes() {
        menuItems = new MenuLabel[]{
                new MenuLabel("Add new note", OptionPaneCreator::addNote),
                new MenuLabel("View and edit notes", OptionPaneCreator::editNotes),
                new MenuLabel("Delete notes", OptionPaneCreator::deleteNotes)
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
