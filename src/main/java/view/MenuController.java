package view;

import javax.swing.*;
import java.util.Set;
import navigation.*;

public class MenuController {
    private View view;
    private Navigator navigator;
    
    public MenuController(View view, Navigator navigator) {
        this.view = view;
        this.navigator = navigator;
    }
    
    private void action(String actionCommand) {
        switch (actionCommand) {
            case "findCamp":
                findCamp();
                break;
            case "setHome":
                setHome();
                break;
            case "setMan":
                setMan();
                break;
            case "setEsplanade":
                setEsplanade();
                break;
            case "setBlockWidth":
                setBlockWidth();
                break;
            case "addFavorite":
                addFavorite();
                break;
            case "navFavorite":
                navFavorite();
                break;
            case "delFavorite":
                delFavorite();
                break;
            case "listCamps":
                listCamps();
                break;
        }
        view.setNavigation(navigator);
    }

    private void listCamps() {
        Set<String> camps = navigator.getCamps().keySet();
        Object[] menuItems = new String[camps.size()];
        System.arraycopy(camps.toArray(), 0, menuItems, 0, camps.size());
        if(menuItems.length == 0) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }

        String input = (String) JOptionPane.showInputDialog(view.getMainFrame(), "",
                "Navigate to camp", JOptionPane.QUESTION_MESSAGE, null,
                menuItems, // Array of choices
                menuItems[0]); // Initial choice
        findCamp(input);
    }

    private void delFavorite() {
        //todo write to file
        Set<String> favorites = navigator.getFavorites().keySet();
        Object[] menuItems = new String[favorites.size()];
        System.arraycopy(favorites.toArray(), 0, menuItems, 0, favorites.size());
        if(menuItems.length == 0) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }

        String input = (String) JOptionPane.showInputDialog(view.getMainFrame(), "",
                "Navigate to camp", JOptionPane.QUESTION_MESSAGE, null,
                menuItems, // Array of choices
                menuItems[0]); // Initial choice

        if(input != null) {
            int confirmation = JOptionPane.showConfirmDialog(view.getMainFrame(), "Delete favorite " + input + "?");
            if (confirmation == JOptionPane.YES_OPTION) {
                //todo fix keyboard select error
                navigator.getFavorites().remove(input);
            }
        }
    }

    private void navFavorite() {
        Set<String> favorites = navigator.getFavorites().keySet();
        Object[] menuItems = new String[favorites.size()];
        System.arraycopy(favorites.toArray(), 0, menuItems, 0, favorites.size());
        if(menuItems.length == 0) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }

        String input = (String) JOptionPane.showInputDialog(view.getMainFrame(), "",
                "Navigate to camp", JOptionPane.QUESTION_MESSAGE, null,
                menuItems, // Array of choices
                menuItems[0]); // Initial choice

        if(input != null) {
            navigator.setDestination(navigator.getFavorites().get(input), input);
        }
    }

    private void addFavorite() {
        //todo write to file
        String favName = JOptionPane.showInputDialog(view.getMainFrame(), "Enter name:");
        if(favName == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view.getMainFrame(), "Use current address?");
        if(confirm == JOptionPane.YES_OPTION) {
            navigator.getFavorites().put(favName, new Location(navigator.currentLocation()));
            return;
        }

        String campAddress = JOptionPane.showInputDialog(view.getMainFrame(), "Enter address (Hour,Minute,Street): ");
        String[] split = campAddress.split(",");
        String err = "Invalid address";
        if(split.length != 3) {
            JOptionPane.showMessageDialog(view.getMainFrame(), err);
        }

        try {
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            char street = split[2].charAt(0);
            navigator.getFavorites().put(favName, new Location(hour, minute, street));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainFrame(), err);
        }
    }


    private void setBlockWidth() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter block width (default 240)");
        try {
            Location.block_width = Integer.parseInt(result);
            //TODO write to file
        } catch (NumberFormatException e) {
        }
    }

    private void setEsplanade() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter Esplanade distance (default 2600)");
        try {
            Location.esplanade_distance = Integer.parseInt(result);
            //TODO write to file
        } catch (NumberFormatException e) {
        }
    }

    private void setMan() {
        System.out.println(Location.man_latitude);
        int result = JOptionPane.showConfirmDialog(view.getMainFrame(), "Use current location as man coordinates?");
        if(result == JOptionPane.YES_OPTION) {
            //todo write to file
            navigator.setManCoordinates();
        }
        System.out.println(Location.man_latitude);
    }

    private void setHome() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter home address (Hour,Minute,Street): ");
        String[] split = result.split(",");
        String err = "Invalid address";
        if(split.length != 3) {
            JOptionPane.showMessageDialog(view.getMainFrame(), err);
        }
        try {
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            char street = split[2].charAt(0);
            navigator.setHome(hour, minute, street);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainFrame(), err);
        }
    }

    private void findCamp() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter search term: ");
        findCamp(result);
    }

    private void findCamp(String result) {
        if(result == null) {
            return;
        }
        String campName = navigator.findCampName(result);
        if(campName.length() > 0) {
            Location camp = navigator.getCamp(campName);
            if (camp != null) {
                navigator.setDestination(camp, campName);
                view.setNavigation(navigator);
            }
        }
    }

    public void menuUp() {
        view.getMenu().up();
    }

    public void menuDown() {
        view.getMenu().down();
    }

    public void menuSelect() {
        action(view.getMenu().select());
        view.reset();
    }

    public void menuEscape() {
        view.getMenu().home();
        view.reset();
    }
}
