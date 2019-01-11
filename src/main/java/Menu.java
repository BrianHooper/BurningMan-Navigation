public class Menu {
    private MenuLabel[] menuItems;
    private int selected = 0;

    public Menu() {
        home();
    }

    public String select() {
        String action = menuItems[selected].getText();

        switch (action) {
            case "Find camp":
                home();
                return "findCamp";
            case "Favorites":
                favorites();
                return "";
            case "Settings":
                settings();
                return "";
            case "Set home address":
                home();
                return "setHome";
            case "Set man coordinates":
                home();
                return "setMan";
            case "Adjust Esplanade distance":
                home();
                return "setEsplanade";
            case "Adjust block width":
                home();
                return "setBlockWidth";
            case "Add new favorite":
                home();
                return "addFavorite";
            case "Navigate to favorite":
                home();
                return "navFavorite";
            case "Delete favorite":
                home();
                return "delFavorite";
            case "List all camps":
                home();
                return "listCamps";
            default: return "";
        }
    }

    private void favorites() {
        menuItems = new MenuLabel[3];
        menuItems[0] = new MenuLabel("Add new favorite");
        menuItems[1] = new MenuLabel("Navigate to favorite");
        menuItems[2] = new MenuLabel("Delete favorite");

        selected = 0;
        menuItems[0].select();
    }

    public void home() {
        menuItems = new MenuLabel[4];
        menuItems[0] = new MenuLabel("Find camp");
        menuItems[1] = new MenuLabel("Favorites");
        menuItems[2] = new MenuLabel("List all camps");
        menuItems[3] = new MenuLabel("Settings");

        selected = 0;
        menuItems[0].select();
    }

    public void settings() {
        menuItems = new MenuLabel[4];

        menuItems[0] = new MenuLabel("Set home address");
        menuItems[1] = new MenuLabel("Set man coordinates");
        menuItems[2] = new MenuLabel("Adjust Esplanade distance");
        menuItems[3] = new MenuLabel("Adjust block width");

        selected = 0;
        menuItems[0].select();
    }

    public void down() {
        menuItems[selected].deselect();
        selected++;
        if(selected >= menuItems.length) {
            selected = 0;
        }

        menuItems[selected].select();
    }

    public void up() {
        menuItems[selected].deselect();
        selected--;
        if(selected < 0) {
            selected = menuItems.length - 1;
        }
        menuItems[selected].select();
    }

    public MenuLabel[] readMenu() {
        return menuItems;
    }

}
