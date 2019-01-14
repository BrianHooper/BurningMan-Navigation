package view;

/**
 * Class Menu
 *
 * handles displaying of menu items
 */
class Menu {
    private MenuLabel[] menuItems;
    private int selected = 0;

    /**
     * Constructor
     *
     * creates Home menu
     */
    Menu() {
        home();
    }

    /**
     * Activates currently selected menu label
     * @return String actionCommand
     */
    String select() {
        String action = menuItems[selected].getText();

        switch (action) {
            case "Favorites":
                favorites();
                return "";
            case "Settings":
                settings();
                return "";
            case "Find Events":
                events();
                return "";
            case "Notes":
                notes();
                return "";
            case "Exit":
                System.exit(0);
            default: return action;
        }
    }

    /**
     * Creates favorites menu
     */
    private void favorites() {
        menuItems = new MenuLabel[3];
        menuItems[0] = new MenuLabel("Add new favorite");
        menuItems[1] = new MenuLabel("Navigate to favorite");
        menuItems[2] = new MenuLabel("Delete favorite");

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates home menu
     */
    void home() {
        menuItems = new MenuLabel[6];
        menuItems[0] = new MenuLabel("Find camp");
        menuItems[1] = new MenuLabel("Favorites");
        menuItems[2] = new MenuLabel("List all camps");
        menuItems[3] = new MenuLabel("Find Events");
        menuItems[4] = new MenuLabel("Notes");
        menuItems[5] = new MenuLabel("Settings");

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates settings menu
     */
    private void settings() {
        menuItems = new MenuLabel[6];

        menuItems[0] = new MenuLabel("Set home address");
        menuItems[1] = new MenuLabel("Set man coordinates");
        menuItems[2] = new MenuLabel("Adjust Esplanade distance");
        menuItems[3] = new MenuLabel("Adjust block width");
        menuItems[4] = new MenuLabel("Set event start time");
        menuItems[5] = new MenuLabel("Exit");

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates events menu
     */
    private void events() {
        menuItems = new MenuLabel[4];

        menuItems[0] = new MenuLabel("View Events by day");
        menuItems[1] = new MenuLabel("Search events by name");
        menuItems[2] = new MenuLabel("Search events by camp");
        menuItems[3] = new MenuLabel("List events happening soon");

        selected = 0;
        menuItems[0].select();
    }

    /**
     * Creates notes menu
     */
    private void notes() {
        menuItems = new MenuLabel[3];

        menuItems[0] = new MenuLabel("Add new note");
        menuItems[1] = new MenuLabel("View and edit notes");
        menuItems[2] = new MenuLabel("Delete notes");

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
     * @return menuItems
     */
    MenuLabel[] readMenu() {
        return menuItems;
    }

}
