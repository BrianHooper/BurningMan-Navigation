package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

import events.Event;
import events.EventManager;
import events.NoteManager;
import navigation.*;

/**
 * Class MenuController
 *
 * Handles menu actions and updates view
 */
public class MenuController {
    private final View view;
    private final Navigator navigator;
    private final EventManager eventManager;
    private final NoteManager noteManager;

    /**
     * Constructor
     * @param view main View
     * @param navigator Navigator object
     * @param eventManager EventManager object
     * @param noteManager NoteManager object
     */
    public MenuController(View view, Navigator navigator, EventManager eventManager, NoteManager noteManager) {
        this.view = view;
        this.navigator = navigator;
        this.eventManager = eventManager;
        this.noteManager = noteManager;
    }

    /**
     * Performs an action based on an action command
     * @param actionCommand String action command
     */
    private void action(String actionCommand) {
        switch (actionCommand) {
            case "Find camp":
                findCamp();
                break;
            case "Set home address":
                setHome();
                break;
            case "Set man coordinates":
                setMan();
                break;
            case "Adjust Esplanade distance":
                setEsplanade();
                break;
            case "Adjust block width":
                setBlockWidth();
                break;
            case "Add new favorite":
                addFavorite();
                break;
            case "Navigate to favorite":
                navFavorite();
                break;
            case "Delete favorite":
                delFavorite();
                break;
            case "List all camps":
                listCamps();
                break;
            case "View Events by day":
                viewEventsByDay();
                break;
            case "Search events by name":
                searchEventsName();
                break;
            case "Search events by camp":
                searchEventsCamp();
                break;
            case "List events happening soon":
                listEventsHappeningSoon();
                break;
            case "Set event start time":
                setEventStartTime();
                break;
            case "Add new note":
                addNote();
                break;
            case "View and edit notes":
                editNotes();
                break;
            case "Delete notes":
                deleteNotes();
                break;
        }
        view.setNavigation(navigator);
    }

    /**
     * Lists all notes and returns the users selection
     * @return String note title
     */
    private String selectNote() {
        String[] menuItems = noteManager.getNoteTitles();
        if(menuItems.length == 0) {
            return null;
        }

        return (String) JOptionPane.showInputDialog(view.getMainFrame(), "",
                "Select note", JOptionPane.QUESTION_MESSAGE, null,
                menuItems, // Array of choices
                menuItems[0]); // Initial choice
    }

    /**
     * Deletes notes
     */
    private void deleteNotes() {
        String noteTitle = selectNote();
        if(noteTitle == null) {
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(view.getMainFrame(), "Delete note " + noteTitle + "?");
        if (confirmation == JOptionPane.YES_OPTION) {
            noteManager.deleteNote(noteTitle);
            noteManager.saveNotes();
        }
    }

    /**
     * Displays all notes
     */
    private void editNotes() {
        String noteTitle = selectNote();
        if(noteTitle == null) {
            return;
        }
        addNote(noteTitle);
    }

    /**
     * Adds or edits a note
     * @param noteTitle title of note to edit
     */
    private void addNote(String noteTitle) {
        String noteBody;
        if(noteTitle != null) {
            noteBody = noteManager.getNote(noteTitle);
        } else {
            noteTitle = "";
            noteBody = "";
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(new JLabel("Note title:"));

        JTextField noteTitleField = new JTextField(noteTitle);
        noteTitleField.setColumns(40);
        panel.add(noteTitleField);

        panel.add(new JLabel("Note body:"));

        JTextArea textArea = new JTextArea(noteBody);
        textArea.setColumns(40);
        textArea.setRows(15);
        JScrollPane jScrollPane = new JScrollPane(textArea);
        panel.add(jScrollPane);
        noteTitleField.requestFocus();

        JOptionPane.showMessageDialog(null,panel,"Add / Edit note", JOptionPane.INFORMATION_MESSAGE);
        noteTitle = noteTitleField.getText();
        noteBody = textArea.getText();
        if(noteTitle.length() == 0 || noteBody.length() == 0) {
            return;
        }

        noteManager.createNote(noteTitle, noteBody);
        noteManager.saveNotes();
    }

    /**
     * Adds a new note
     */
    private void addNote() {
        addNote(null);
    }

    /**
     * Sets global event start time
     */
    private void setEventStartTime() {
        String startDate = JOptionPane.showInputDialog(view.getMainFrame(), "" +
                "Current start date is " + Event.dfFull.format(Event.globalEventStartTime) +
                "\nEnter new start date in yyyy-MM-dd HH:mm format:");
        if(startDate == null || startDate.length() == 0) {
            return;
        }

        if(Event.setGlobalEventStartTime(startDate)) {
            String eventStartDate = Event.dfFull.format(Event.globalEventStartTime);
            JOptionPane.showMessageDialog(view.getMainFrame(), "Start date set to " + eventStartDate);
            navigator.writeToConfigFile("config.cfg");
        } else {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Invalid date format");
        }
    }


    /**
     * Lists all events on a specific day
     */
    private void viewEventsByDay() {
        String[] choices = {"1: Sunday", "2: Monday", "3: Tuesday", "4: Wednesday", "5: Thursday",
                "6: Friday", "7: Saturday", "8: Sunday"};
        String input = (String) JOptionPane.showInputDialog(view.getMainFrame(), "",
                "Select day", JOptionPane.QUESTION_MESSAGE, null,
                choices, // Array of choices
                choices[0]); // Initial choice
        if(input == null)
            return;

        int day;
        try {
            day = Integer.parseInt(input.substring(0, 1));
        } catch (NumberFormatException e) {
            return;
        }

        ArrayList<Event> eventList = eventManager.listByDay(day);
        String[] eventStringArray = new String[eventList.size()];
        for(int i = 0; i < eventList.size(); i++) {
            eventStringArray[i] = eventList.get(i).toString();
        }

        if(eventStringArray.length == 0) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No events found");
        } else {
            //TODO change to scroll pane
            JOptionPane.showInputDialog(view.getMainFrame(), "",
                    "Events", JOptionPane.QUESTION_MESSAGE, null,
                    eventStringArray, // Array of choices
                    eventStringArray[0]); // Initial choice
        }
    }

    /**
     * Lists events matching an event name
     */
    private void searchEventsName() {
        String eventName = JOptionPane.showInputDialog(view.getMainFrame(), "Enter event name:");
        if(eventName == null) {
            return;
        }
        ArrayList<Event> eventList = eventManager.listByName(eventName);
        String[] eventStringArray = new String[eventList.size()];
        for(int i = 0; i < eventList.size(); i++) {
            eventStringArray[i] = eventList.get(i).toString();
        }

        if(eventStringArray.length == 0) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No events found");
        } else {
            //TODO change to scroll pane
            JOptionPane.showInputDialog(view.getMainFrame(), "",
                    "Events", JOptionPane.QUESTION_MESSAGE, null,
                    eventStringArray, // Array of choices
                    eventStringArray[0]); // Initial choice
        }
    }

    /**
     * Lists events at camps matching a search term
     */
    private void searchEventsCamp() {
        String campName = JOptionPane.showInputDialog(view.getMainFrame(), "Enter event name:");
        if(campName == null) {
            return;
        }
        ArrayList<Event> eventList = eventManager.listByCamp(campName);
        String[] eventStringArray = new String[eventList.size()];
        for(int i = 0; i < eventList.size(); i++) {
            eventStringArray[i] = eventList.get(i).toString();
        }

        if(eventStringArray.length == 0) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No events found");
        } else {
            //TODO change to scroll pane
            JOptionPane.showInputDialog(view.getMainFrame(), "",
                    "Events", JOptionPane.QUESTION_MESSAGE, null,
                    eventStringArray, // Array of choices
                    eventStringArray[0]); // Initial choice
        }
    }

    /**
     * Lists events happening in the next 24 hours
     */
    private void listEventsHappeningSoon() {
        ArrayList<Event> eventList = eventManager.listHappeningSoon();
        String[] eventStringArray = new String[eventList.size()];
        for(int i = 0; i < eventList.size(); i++) {
            eventStringArray[i] = eventList.get(i).toString();
        }

        if(eventStringArray.length == 0) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No upcoming events");
        } else {
            //TODO change to scroll pane
            JOptionPane.showInputDialog(view.getMainFrame(), "",
                    "Events", JOptionPane.QUESTION_MESSAGE, null,
                    eventStringArray, // Array of choices
                    eventStringArray[0]); // Initial choice
        }
    }

    /**
     * Creates JOptionPane popup for listing camps
     */
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

    /**
     * Creates JOptionPane popup for deleting a favorite location
     */
    private void delFavorite() {
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
                navigator.getFavorites().remove(input);
                navigator.writeFavorites();
                navigator.writeToConfigFile("config.cfg");
            }
        }
    }

    /**
     * Creates JOptionPane popup for navigating to a favorite location
     */
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
            navigator.writeToConfigFile("config.cfg");
        }
    }

    /**
     * Creates JOptionPane popup for creating a favorite location
     */
    private void addFavorite() {
        String favName = JOptionPane.showInputDialog(view.getMainFrame(), "Enter name:");
        if(favName == null || favName.length() == 0) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view.getMainFrame(), "Use current address?");
        if(confirm == JOptionPane.YES_OPTION) {
            navigator.getFavorites().put(favName, new Location(navigator.currentLocation()));
            navigator.writeFavorites();
            navigator.writeToConfigFile("config.cfg");
            return;
        }

        String campAddress = JOptionPane.showInputDialog(view.getMainFrame(), "Enter address (Hour,Minute,Street): ");
        String[] split = campAddress.split(",");
        String err = "Invalid address";
        if(campAddress.length() == 0 || split.length != 3) {
            JOptionPane.showMessageDialog(view.getMainFrame(), err);
        }

        try {
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            char street = split[2].charAt(0);
            navigator.getFavorites().put(favName, new Location(hour, minute, street));
            navigator.writeToConfigFile("config.cfg");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainFrame(), err);
        }
    }

    /**
     * Creates JOptionPane popup for setting the width of a block
     */
    private void setBlockWidth() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter block width (default 240)");
        try {
            Location.block_width = Integer.parseInt(result);
            navigator.writeToConfigFile("config.cfg");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Error, invalid block width");
        }
    }

    /**
     * Creates JOptionPane popup for setting esplanade distance
     */
    private void setEsplanade() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter Esplanade distance (default 2600)");
        try {
            Location.esplanade_distance = Integer.parseInt(result);
            navigator.writeToConfigFile("config.cfg");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Error, invalid esplanade distance");
        }
    }

    /**
     * Creates JOptionPane popup for setting man coordinates
     */
    private void setMan() {
        int result = JOptionPane.showConfirmDialog(view.getMainFrame(), "Use current location as man coordinates?");
        if(result == JOptionPane.YES_OPTION) {
            navigator.writeToConfigFile("config.cfg");
        }
    }

    /**
     * Creates JOptionPane popup for setting home address
     */
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
            navigator.writeToConfigFile("config.cfg");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainFrame(), err);
        }
    }

    /**
     * Creates JOptionPane popup for searching for a camp
     */
    private void findCamp() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter search term: ");
        if(result != null && !findCamp(result)) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Camp not found");
        }
    }

    /**
     * Searches for a camp and sets navigation
     */
    private boolean findCamp(String result) {
        if(result == null || result.length() == 0) {
            return false;
        }
        String campName = navigator.findCampName(result);
        if(campName.length() > 0) {
            Location camp = navigator.getCamp(campName);
            if (camp != null) {
                navigator.setDestination(camp, campName);
                view.setNavigation(navigator);
                navigator.writeToConfigFile("config.cfg");
                return true;
            }
        }
        return false;
    }

    /**
     * Updates view / moves selection up
     */
    public void menuUp() {
        view.getMenu().up();
    }

    /**
     * Updates view / moves selection down
     */
    public void menuDown() {
        view.getMenu().down();
    }

    /**
     * Activates current selection
     */
    public void menuSelect() {
        action(view.getMenu().select());
        view.getFocus();
    }

    /**
     * Updates view with home selection
     */
    public void menuEscape() {
        view.getMenu().home();
        view.getFocus();
    }
}
