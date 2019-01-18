package view;

import driver.ClockDriver;
import driver.CoordinateListener;
import events.Event;
import events.EventManager;
import events.NoteManager;
import navigation.Location;
import navigation.Navigator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Class UserInterfaceController
 * <p>
 * Handles menu actions and updates view
 */
public class UserInterfaceController {
    private final View view;
    private final Navigator navigator;
    private final EventManager eventManager;
    private final NoteManager noteManager;

    public static void start() {
        View view = new View();
        Navigator navigator = new Navigator();
        new UserInterfaceController(view, navigator);
        CoordinateListener coordinateListener = new CoordinateListener(navigator, view);
        coordinateListener.start();

        ClockDriver clockDriver = new ClockDriver(view);
        clockDriver.start();
    }

    /**
     * Constructor
     *
     * @param view      main View
     * @param navigator Navigator object
     */
    private UserInterfaceController(View view, Navigator navigator) {
        this.view = view;
        this.navigator = navigator;
        this.eventManager = navigator.getEventManager();
        this.noteManager = navigator.getNoteManager();

        view.setNavigation(navigator);
        KeyController controller = new KeyController(this);
        view.setKeyListener(controller);
    }

    /**
     * Performs an action based on an action command
     *
     * @param actionCommand String action command
     */
    private void action(String actionCommand) {
        switch(actionCommand) {
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
     *
     * @return String note title
     */
    private String selectNote() {
        if(noteManager.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No notes found");
            return null;
        }

        OptionPane pane = new OptionPane();
        pane.addListInput(noteManager.getNoteTitles(), 10);
        pane.show(view.getMainFrame(), "Select Note", JOptionPane.OK_CANCEL_OPTION);

        if(pane.okPressed()) {
            return pane.getInputs()[0];
        } else {
            return null;
        }
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
        if(confirmation == JOptionPane.YES_OPTION) {
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
     *
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

        OptionPane pane = new OptionPane();

        pane.addLabel("Title:");
        JTextField field = new JTextField(20);
        field.setText(noteTitle);
        pane.addTextInput(field);

        pane.addLabel("Body:");
        OptionPaneTextArea area = new OptionPaneTextArea(20, 10);
        area.setText(noteBody);
        pane.addTextInput(area);

        pane.show(view.getMainFrame(), "Add / Edit note", JOptionPane.OK_CANCEL_OPTION);

        noteTitle = field.getText();
        noteBody = area.getText();
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
            navigator.writeToConfigFile();
        } else {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Invalid date format");
        }
    }


    /**
     * Lists all events on a specific day
     */
    private void viewEventsByDay() {
        ArrayList<String> choices = new ArrayList<>(Arrays.asList("1: Sunday", "2: Monday",
                "3: Tuesday", "4: Wednesday", "5: Thursday", "6: Friday", "7: Saturday", "8: Sunday"));

        OptionPane pane = new OptionPane();
        pane.addListInput(choices, 10);
        pane.show(view.getMainFrame(), "Choose day", JOptionPane.OK_CANCEL_OPTION);

        if(!pane.okPressed()) {
            return;
        }
        JList<String> jList = (JList<String>) pane.getJComponents().get(0);
        int day = jList.getSelectedIndex();

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
        ArrayList<String> menuItems = new ArrayList<>(navigator.getCamps().keySet());
        if(menuItems.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }

        int longest = 0;
        for(String campName : menuItems) {
            if(campName.length() > longest) {
                longest = campName.length();
            }
        }
        longest += 8;

        ArrayList<String> fullMenuItems = new ArrayList<>();
        for(String campName : menuItems) {
            Location campLocation = navigator.getCamp(campName);
            int spacerLength = longest - campName.length();
            if(campLocation.getHour() >= 10) {
                spacerLength--;
            }
            char[] spacer = new char[spacerLength];
            Arrays.fill(spacer, ' ');
            String fullItem = campName + new String(spacer) + campLocation.toString();
            fullMenuItems.add(fullItem);
        }

        OptionPane pane = new OptionPane();
        pane.addListInput(fullMenuItems, 10);
        pane.show(view.getMainFrame(), "All camps", JOptionPane.OK_CANCEL_OPTION);

        JList<String> jList = (JList<String>) pane.getJComponents().get(0);
        String input = menuItems.get(jList.getSelectedIndex());
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
            if(confirmation == JOptionPane.YES_OPTION) {
                navigator.getFavorites().remove(input);
                navigator.writeFavorites();
                navigator.writeToConfigFile();
            }
        }
    }

    /**
     * Creates JOptionPane popup for navigating to a favorite location
     */
    private void navFavorite() {
        ArrayList<String> menuItems = new ArrayList<>(navigator.getFavorites().keySet());
        if(menuItems.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }

        OptionPane pane = new OptionPane();
        pane.addListInput(menuItems, 10);
        pane.show(view.getMainFrame(), "All camps", JOptionPane.OK_CANCEL_OPTION);

        String input = pane.getInputs()[0];
        if(input != null) {
            navigator.setDestination(navigator.getFavorites().get(input), input);
            navigator.writeToConfigFile();
        }
    }

    /**
     * Creates JOptionPane popup for creating a favorite location
     */
    private void addFavorite() {
        OptionPane pane = new OptionPane();
        pane.addLabel("Favorite name: ");

        JTextField nameField = new JTextField(20);
        pane.addTextInput(nameField);

        pane.addLabel("Use current address?");
        JPanel currentAddressPanel = new JPanel(new FlowLayout());
        JRadioButton yes = new JRadioButton("Yes");
        yes.setSelected(true);
        JRadioButton no = new JRadioButton("No");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(yes);
        buttonGroup.add(no);
        currentAddressPanel.add(yes);
        currentAddressPanel.add(no);
        pane.addComponent(currentAddressPanel);

        JPanel customAddressPanel = new JPanel(new FlowLayout());
        JComboBox<String> hourBox = new JComboBox<>(
                new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10"});
        JComboBox<String> minuteBox = new JComboBox<>(
                new String[]{"00", "15", "30", "45"});
        JComboBox<String> streetBox = new JComboBox<>(
                new String[]{"Esp.", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"});
        customAddressPanel.add(hourBox);
        customAddressPanel.add(minuteBox);
        customAddressPanel.add(streetBox);
        pane.addComponent(customAddressPanel);


        pane.show(null, "", JOptionPane.OK_CANCEL_OPTION);

        String favName = nameField.getText();
        Location favLocation;
        if(yes.isSelected()) {
            favLocation = navigator.currentLocation();
        } else {
            int hour = 2 + hourBox.getSelectedIndex();
            int minute = 15 * minuteBox.getSelectedIndex();
            if(streetBox.getSelectedIndex() == 0) {
                favLocation = new Location(hour, minute, (double) Location.esplanade_distance);
            } else {
                favLocation = new Location(hour, minute, (char) 64 + streetBox.getSelectedIndex());
            }
        }
        navigator.getFavorites().put(favName, favLocation);
        navigator.writeFavorites();
        navigator.writeToConfigFile();
    }

    /**
     * Creates JOptionPane popup for setting the width of a block
     */
    private void setBlockWidth() {
        String result = JOptionPane.showInputDialog(view.getMainFrame(), "Enter block width (default 240)");
        try {
            Location.block_width = Integer.parseInt(result);
            navigator.writeToConfigFile();
        } catch(NumberFormatException e) {
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
            navigator.writeToConfigFile();
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Error, invalid esplanade distance");
        }
    }

    /**
     * Creates JOptionPane popup for setting man coordinates
     */
    private void setMan() {
        int result = JOptionPane.showConfirmDialog(view.getMainFrame(), "Use current location as man coordinates?");
        if(result == JOptionPane.YES_OPTION) {
            navigator.writeToConfigFile();
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
            navigator.writeToConfigFile();
        } catch(NumberFormatException e) {
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
            if(camp != null) {
                navigator.setDestination(camp, campName);
                view.setNavigation(navigator);
                navigator.writeToConfigFile();
                return true;
            }
        }
        return false;
    }

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
        action(view.getMenu().select());
        view.getFocus();
    }

    /**
     * Updates view with home selection
     */
    void menuEscape() {
        view.getMenu().home();
        view.getFocus();
    }
}
