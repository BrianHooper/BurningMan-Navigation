public class Menu {
    private MenuLabel[] menuItems;
    private int selected = 0;

    public Menu() {
        menuItems = new MenuLabel[3];
        menuItems[0] = new MenuLabel("Find landmark (camp/art)");
        menuItems[1] = new MenuLabel("Favorites");
        menuItems[2] = new MenuLabel("Settings");

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

    public void action(String labelText) {
        switch (labelText) {
            case "Settings":
                settings();
        }
    }
}
