package view;

import driver.ClockDriver;
import driver.CoordinateListener;
import driver.ListManager;
import driver.LogDriver;
import events.Event;
import events.EventCategory;
import events.EventManager;
import events.NoteManager;
import navigation.Location;
import navigation.Navigator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Class UserInterfaceController
 * <p>
 * Handles menu and updates view, creates all OptionPane objects
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class UserInterfaceController {
    // Main GUI view
    private final View view;

    // Managers for location and landmarks
    private final Navigator navigator;
    private final EventManager eventManager;
    private final NoteManager noteManager;

    // Threads for updating clock and coordinates
    private final ClockDriver clockDriver;
    private final CoordinateListener coordinateListener;

    // Logger
    private final LogDriver logger = LogDriver.getInstance();

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

        coordinateListener = new CoordinateListener(navigator, view);
        coordinateListener.start();

        clockDriver = new ClockDriver(view);
        clockDriver.start();
    }

    /**
     * Rejoins thread processes, closes open files, and terminates the program
     */
    private void exit() {
        navigator.writeToConfigFile();
        navigator.writeFavorites();
        noteManager.saveNotes();

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

    /**
     * Performs an action based on an action command
     *
     * @param actionCommand String action command
     */
    private void action(String actionCommand) {
        switch(actionCommand) {
            case "Go to address":
                goToAddress();
                break;
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
            case "Find Events":
                findEvents();
                break;
            case "Happening soon":
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
            case "Exit":
                exit();
        }
        view.setNavigation(navigator);
    }

    /**
     * Creates an OptionPane for navigating to a specific address
     */
    private void goToAddress() {
        OptionPane pane = new OptionPane();
        pane.addLabel("Name:");
        JTextField destinationName = new JTextField(10);
        pane.addTextInput(destinationName);

        pane.addLabel("Address");
        AddressPanel addressPanel = new AddressPanel();
        pane.addComponent(addressPanel);

        if(pane.show(view.getMainFrame(), "Go to address")) {
            String name = destinationName.getText();
            Location destination = addressPanel.getAddress();
            navigator.setDestination(destination, name);
            navigator.writeToConfigFile();
        }
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


        if(pane.show(view.getMainFrame(), "Select Note")) {
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

        pane.show(view.getMainFrame(), "Add / Edit note");

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
                "Current start date is " + ClockDriver.dfFull.format(Event.globalEventStartTime) +
                "\nEnter new start date in yyyy-MM-dd HH:mm format:");
        if(startDate == null || startDate.length() == 0) {
            return;
        }

        if(Event.setGlobalEventStartTime(startDate)) {
            String eventStartDate = ClockDriver.dfFull.format(Event.globalEventStartTime);
            JOptionPane.showMessageDialog(view.getMainFrame(), "Start date set to " + eventStartDate);
            navigator.writeToConfigFile();
        } else {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Invalid date format");
        }
    }


    /**
     * Shows a list of all relevant events
     *
     * @param eventList ArrayList of Event objects
     */
    private void showEvents(ArrayList<Event> eventList) {
        if(eventList == null || eventList.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No events found");
            return;
        }

        ArrayList<String[]> eventElements = new ArrayList<>();
        for(Event event : eventList) {
            eventElements.add(event.getElements());
        }

        ArrayList<String> eventStrList = ListManager.splitEvenly(eventElements, 4);

        OptionPane pane = new OptionPane();
        pane.addListInput(eventStrList, 10);

        if(!pane.show(view.getMainFrame(), "Events")) {
            return;
        }

        Event chosenEvent = eventList.get(pane.getJListSelectedIndex(0));

        OptionPane eventPane = new OptionPane();
        eventPane.addLabel("Name: " + chosenEvent.getName());
        eventPane.addLabel("Category: " + chosenEvent.getCategory());
        eventPane.addLabel(chosenEvent.timeToString());

        OptionPaneTextArea descriptionArea = new OptionPaneTextArea(20, 10);
        descriptionArea.setText(chosenEvent.getDescription());
        descriptionArea.setEditable(false);
        eventPane.addTextInput(descriptionArea);
        eventPane.show(view.getMainFrame(), "Event");
    }

    /**
     * Lists events matching an event name
     */
    private void findEvents() {
        OptionPane pane = new OptionPane();

        pane.addLabel("Event name:");
        JTextField name = new JTextField(10);
        pane.addTextInput(name);

        pane.addLabel("Camp name:");
        JTextField camp = new JTextField(10);
        pane.addTextInput(camp);

        pane.addLabel("Day:");
        ArrayList<String> days = new ArrayList<>(Arrays.asList("Any", "Sunday", "Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday", "Saturday", "Sunday"));
        pane.addListInput(days, 1);

        pane.addLabel("Category:");
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Any");
        categories.addAll(EventCategory.listAll());
        pane.addListInput(categories, 1);

        if(pane.show(view.getMainFrame(), "Search for event")) {
            String nameSearch = name.getText();
            String campSearch = camp.getText();
            int day = pane.getJListSelectedIndex(0);
            int category = pane.getJListSelectedIndex(1);

            showEvents(eventManager.listBySearch(nameSearch, campSearch, day, category));
        }
    }

    /**
     * Lists events happening in the next 24 hours
     */
    private void listEventsHappeningSoon() {
        OptionPane pane = new OptionPane();

        ArrayList<String> menuItems = new ArrayList<>();
        for(int i = 1; i <= 48; i++) {
            menuItems.add(String.valueOf(i));
        }

        pane.addLabel("Number of hours:");
        pane.addListInput(menuItems, 1, 23);


        if(pane.show(view.getMainFrame(), "See upcoming events")) {
            int hours = pane.getJListSelectedIndex(0) + 1;
            showEvents(eventManager.listHappeningSoon(hours));
        }
    }

    /**
     * Creates JOptionPane popup for deleting a favorite location
     */
    private void delFavorite() {
        ArrayList<String[]> favoritePairs = navigator.getFavoritePairs();
        if(favoritePairs.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }
        ArrayList<String> menuItems = ListManager.splitEvenly(favoritePairs, 2);


        OptionPane pane = new OptionPane();
        pane.addListInput(menuItems, 10);
        if(pane.show(view.getMainFrame(), "Delete favorite")) {
            String favName = favoritePairs.get(pane.getJListSelectedIndex(0))[0];
            if(favName != null) {
                int confirmation = JOptionPane.showConfirmDialog(view.getMainFrame(), "Delete favorite " + favName + "?");
                if(confirmation == JOptionPane.YES_OPTION) {
                    navigator.getFavorites().remove(favName);
                    navigator.writeFavorites();
                    navigator.writeToConfigFile();
                }
            }
        }
    }

    /**
     * Creates JOptionPane popup for navigating to a favorite location
     */
    private void navFavorite() {
        ArrayList<String[]> favoritePairs = navigator.getFavoritePairs();
        if(favoritePairs.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }
        ArrayList<String> menuItems = ListManager.splitEvenly(favoritePairs, 2);


        OptionPane pane = new OptionPane();
        pane.addListInput(menuItems, 10);
        if(pane.show(view.getMainFrame(), "Delete favorite")) {
            String favName = favoritePairs.get(pane.getJListSelectedIndex(0))[0];
            if(favName != null) {
                navigator.setDestination(navigator.getFavorites().get(favName), favName);
                navigator.writeToConfigFile();
            }
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

        AddressPanel addressPanel = new AddressPanel();
        pane.addComponent(addressPanel);


        if(pane.show(view.getMainFrame(), "Add new favorite")) {
            String favName = nameField.getText();
            Location favLocation;
            if(yes.isSelected()) {
                favLocation = navigator.currentLocation();
            } else {
                favLocation = addressPanel.getAddress();
            }
            navigator.getFavorites().put(favName, favLocation);
            navigator.writeFavorites();
            navigator.writeToConfigFile();
        }
    }

    /**
     * Creates JOptionPane popup for setting the width of a block
     */
    private void setBlockWidth() {
        //TODO change to OptionPane to fix cancel error
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
        //TODO change to OptionPane to fix cancel error
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
        //TODO add option for setting coordinates manually
        int result = JOptionPane.showConfirmDialog(view.getMainFrame(), "Use current location as man coordinates?");
        if(result == JOptionPane.YES_OPTION) {
            navigator.writeToConfigFile();
        }
    }

    /**
     * Creates JOptionPane popup for setting home address
     */
    private void setHome() {
        //TODO change to AddressPane
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
        OptionPane searchPane = new OptionPane();
        searchPane.addLabel("Camp name:");
        JTextField nameField = new JTextField(10);
        searchPane.addTextInput(nameField);
        if(!searchPane.show(view.getMainFrame(), "Search for camp")) {
            return;
        }


        ArrayList<String> camps = navigator.findCamps(nameField.getText());
        if(camps.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Camp not found");
            return;
        } else if(camps.size() == 1) {
            String campName = camps.get(0);
            Location camp = navigator.getCamp(campName);
            navigator.setDestination(camp, campName);
            view.setNavigation(navigator);
            navigator.writeToConfigFile();
            return;
        }

        OptionPane resultsPane = new OptionPane();
        resultsPane.addListInput(camps, 10);
        if(resultsPane.show(view.getMainFrame(), "Matching camps")) {
            String campName = resultsPane.getJListSelectedValue(0);
            Location camp = navigator.getCamp(campName);
            navigator.setDestination(camp, campName);
            view.setNavigation(navigator);
            navigator.writeToConfigFile();
        }
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
