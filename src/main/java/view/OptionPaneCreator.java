package view;

import driver.ClockDriver;
import driver.ListManager;
import driver.LogDriver;
import events.Event;
import events.EventCategory;
import jdk.nashorn.internal.runtime.linker.NashornBeansLinker;
import navigation.Landmark;
import navigation.Location;
import navigation.Navigator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class OptionPaneCreator
 * <p>
 * Contains static methods for creating OptionPanes to get input from the user
 *
 * @author Brian Hooper
 * @since 0.9.5
 */
@SuppressWarnings("unused")
class OptionPaneCreator {
    /**
     * Creates OptionPane for measuring distance
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void measureDistance(View view, Navigator navigator) {
        JTextField distance = new JTextField();
        distance.setEditable(false);

        class MeasurePanel extends JPanel {
            private JTextField label;
            private Location location;
            private AddressPanel customAddress;

            private MeasurePanel(Location initialLocation) {
                setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
                this.location = initialLocation;
                customAddress = new AddressPanel(initialLocation);
                label = new JTextField(initialLocation.toString());
                label.setEditable(false);
                JButton useCurrentButton = new JButton("Use current address");
                useCurrentButton.setAlignmentX(0);
                useCurrentButton.addActionListener(actionEvent -> {
                    location = navigator.currentLocation();
                    label.setText(location.toString());
                });
                JPanel useCurrentButtonPanel = new JPanel(new GridLayout(0, 1));
                useCurrentButtonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
                useCurrentButtonPanel.add(useCurrentButton);

                JButton useCustomButton = new JButton("Use custom address");
                useCustomButton.setAlignmentX(0);
                useCustomButton.addActionListener(actionEvent -> {
                    location = customAddress.getAddress();
                    label.setText(location.toString());
                });
                JPanel useCustomButtonPanel = new JPanel(new GridLayout(0, 1));
                useCustomButtonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
                useCustomButtonPanel.add(useCustomButton);

                add(label);
                add(useCurrentButtonPanel);
                add(customAddress);
                add(useCustomButtonPanel);
            }
        }

        OptionPane pane = new OptionPane();

        pane.addLabel("Start location:");
        MeasurePanel startPanel = new MeasurePanel(navigator.getMeasureStart());
        pane.addComponent(startPanel);
        pane.addLabel("End location:");
        MeasurePanel stopPanel = new MeasurePanel(navigator.getMeasureStop());
        pane.addComponent(stopPanel);

        JTextField resultField = new JTextField(10);
        resultField.setEditable(false);
        pane.addComponent(resultField);
        JButton calculate = new JButton("Calculate distance");
        calculate.addActionListener(actionEvent -> {
            double distance1 = startPanel.location.distance(stopPanel.location);
            resultField.setText(String.valueOf(distance1));
        });
        JPanel calculateButtonPanel = new JPanel(new GridLayout(0, 1));
        calculateButtonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        calculateButtonPanel.add(calculate);
        pane.addComponent(calculateButtonPanel);

        pane.show(view.getMainFrame(), "Distance calculator");
        navigator.setMeasureStart(startPanel.location);
        navigator.setMeasureStop(stopPanel.location);
    }

    /**
     * Creates OptionPane for calculating adjustment error
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void calculateError(View view, Navigator navigator) {
        class ErrorPane extends JPanel {
            private JTextField latitude, longitude;
            private String name;

            private ErrorPane(String name) {
                this.name = name;
                setLayout(new FlowLayout());
                add(new JLabel(name));
                latitude = new JTextField(7);
                latitude.setText("40.768");
                longitude = new JTextField(7);
                longitude.setText("-119.214");
                add(latitude);
                add(longitude);
            }

            private String getLatitude() {
                return latitude.getText();
            }

            private String getLongitude() {
                return longitude.getText();
            }

        }

        OptionPane pane = new OptionPane();
        pane.addLabel("Enter coordinates:");

        ErrorPane[] errorPanes = new ErrorPane[]{
                new ErrorPane("02:00"),
                new ErrorPane("03:00"),
                new ErrorPane("04:00"),
                new ErrorPane("05:00"),
                new ErrorPane("06:00"),
                new ErrorPane("07:00"),
                new ErrorPane("08:00"),
                new ErrorPane("09:00"),
                new ErrorPane("10:00")
        };
        for(ErrorPane errorPane : errorPanes) {
            pane.addComponent(errorPane);
        }

        if(!pane.show(null, "Calculate errors")) {
            return;
        }

        OptionPane results = new OptionPane();
        for(ErrorPane errorPane : errorPanes) {
            try {
                double latitude = Double.parseDouble(errorPane.getLatitude());
                double longitude = Double.parseDouble(errorPane.getLongitude());
                Location location = new Location(latitude, longitude);
                results.addLabel(errorPane.name + ": " +
                        String.valueOf(location.getHour()) + ":" + String.format("%02d", location.getMinute()));
            } catch(NumberFormatException e) {
                results.addLabel("Cannot calculate " + errorPane.getName());
            }
        }
        results.show(view.getMainFrame(), "Results");
    }

    /**
     * Creates OptionPane for setting the adjustment coefficients
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void adjustCoefficients(View view, Navigator navigator) {
        double[] currentCoefficients = Location.getAdjustmentCoefficients();

        OptionPane pane = new OptionPane();
        pane.addLabel("Enter Adjustment Coefficients");
        pane.addLabel("Default: [8.2, 0.035, 6.3, 44.7]");
        JPanel coefficientsPanel = new JPanel(new FlowLayout());
        JTextField[] fields = new JTextField[]{
                new JTextField(5),
                new JTextField(5),
                new JTextField(5),
                new JTextField(5)
        };
        for(int i = 0; i < fields.length; i++) {
            fields[i].setText(String.valueOf(currentCoefficients[i]));
            coefficientsPanel.add(fields[i]);
        }
        pane.addComponent(coefficientsPanel);
        if(pane.show(view.getMainFrame(), "Adjust coefficients")) {
            try {
                double a = Double.parseDouble(fields[0].getText().replaceAll("[^a-zA-Z0-9.]", ""));
                double b = Double.parseDouble(fields[1].getText().replaceAll("[^a-zA-Z0-9.]", ""));
                double c = Double.parseDouble(fields[2].getText().replaceAll("[^a-zA-Z0-9.]", ""));
                double d = Double.parseDouble(fields[3].getText().replaceAll("[^a-zA-Z0-9.]", ""));
                Location.setAdjustmentCoefficients(a, b, c, d);
            } catch(NumberFormatException e) {
                OptionPane.showMessage(view.getMainFrame(), "Invalid coefficients");
            }
        }

    }

    /**
     * Creates an OptionPane for navigating to a specific address
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void goToAddress(View view, Navigator navigator) {
        OptionPane pane = new OptionPane();
        pane.addLabel("Name:");
        JTextField destinationName = new JTextField(10);
        pane.addTextInput(destinationName);

        pane.addLabel("Address");
        AddressPanel addressPanel = new AddressPanel(navigator.currentLocation());
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
     * @param view      main view panel
     * @param navigator main navigator object
     * @return String note title
     */
    private static String selectNote(View view, Navigator navigator) {
        if(navigator.getNoteManager().isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No notes found");
            return null;
        }

        OptionPane pane = new OptionPane();
        pane.addListInput(navigator.getNoteManager().getNoteTitles(), 10);


        if(pane.show(view.getMainFrame(), "Select Note")) {
            return pane.getInputs()[0];
        } else {
            return null;
        }
    }

    /**
     * Deletes notes
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void deleteNotes(View view, Navigator navigator) {
        String noteTitle = selectNote(view, navigator);
        if(noteTitle == null) {
            return;
        }
        int confirmation = JOptionPane.showConfirmDialog(view.getMainFrame(), "Delete note " + noteTitle + "?");
        if(confirmation == JOptionPane.YES_OPTION) {
            navigator.getNoteManager().deleteNote(noteTitle);
            navigator.getNoteManager().saveNotes();
        }
    }

    /**
     * Displays all notes
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void editNotes(View view, Navigator navigator) {
        String noteTitle = selectNote(view, navigator);
        if(noteTitle == null) {
            return;
        }
        addNote(view, navigator, noteTitle);
    }

    /**
     * Adds or edits a note
     *
     * @param view      main view panel
     * @param navigator main navigator object
     * @param noteTitle title of note to edit
     */
    private static void addNote(View view, Navigator navigator, String noteTitle) {
        String noteBody;
        if(noteTitle != null) {
            noteBody = navigator.getNoteManager().getNote(noteTitle);
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

        navigator.getNoteManager().createNote(noteTitle, noteBody);
        navigator.getNoteManager().saveNotes();
    }

    /**
     * Adds a new note
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void addNote(View view, Navigator navigator) {
        addNote(view, navigator, null);
    }

    /**
     * Sets global event start time
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void setEventStartTime(View view, Navigator navigator) {
        String startDate = JOptionPane.showInputDialog(view.getMainFrame(), "" +
                "Current start date is " + ClockDriver.dfFull.format(events.Event.globalEventStartTime) +
                "\nEnter new start date in yyyy-MM-dd HH:mm format:");
        if(startDate == null || startDate.length() == 0) {
            return;
        }

        if(events.Event.setGlobalEventStartTime(startDate)) {
            String eventStartDate = ClockDriver.dfFull.format(events.Event.globalEventStartTime);
            JOptionPane.showMessageDialog(view.getMainFrame(), "Start date set to " + eventStartDate);
            navigator.writeToConfigFile();
        } else {
            JOptionPane.showMessageDialog(view.getMainFrame(), "Invalid date format");
        }
    }


    /**
     * Shows a list of all relevant events
     *
     * @param view      main view panel
     * @param eventList ArrayList of Event objects
     */
    private static void showEvents(View view, ArrayList<events.Event> eventList) {
        if(eventList == null || eventList.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No events found");
            return;
        }

        ArrayList<String[]> eventElements = new ArrayList<>();
        for(events.Event event : eventList) {
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
        eventPane.addLabel("Location: " + chosenEvent.getLocation());
        eventPane.addLabel("Category: " + chosenEvent.getCategory());
        eventPane.addLabel(chosenEvent.timeToString());

        OptionPaneTextArea descriptionArea = new OptionPaneTextArea(20, 10);
        descriptionArea.setLineWrap(true);
        descriptionArea.setText(chosenEvent.getDescription());
        descriptionArea.setEditable(false);
        eventPane.addTextInput(descriptionArea);
        eventPane.show(view.getMainFrame(), "Event");
    }

    /**
     * Lists events matching an event name
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void findEvents(View view, Navigator navigator) {
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

            showEvents(view, navigator.getEventManager().listBySearch(nameSearch, campSearch, day, category));
        }
    }

    /**
     * Lists events happening in the next 24 hours
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void listEventsHappeningSoon(View view, Navigator navigator) {
        OptionPane pane = new OptionPane();

        ArrayList<String> menuItems = new ArrayList<>();
        for(int i = 1; i <= 48; i++) {
            menuItems.add(String.valueOf(i));
        }

        pane.addLabel("Number of hours:");
        pane.addListInput(menuItems, 1, 23);


        if(pane.show(view.getMainFrame(), "See upcoming events")) {
            int hours = pane.getJListSelectedIndex(0) + 1;
            showEvents(view, navigator.getEventManager().listHappeningSoon(hours));
        }
    }

    /**
     * Creates JOptionPane popup for deleting a favorite location
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void delFavorite(View view, Navigator navigator) {
        Landmark landmark = chooseFavorite(view, navigator);
        if(landmark == null) {
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(view.getMainFrame(), "Delete favorite " + landmark.getName() + "?");
        if(confirmation == JOptionPane.YES_OPTION) {
            navigator.getFavorites().remove(landmark);
            navigator.writeFavorites();
            navigator.writeToConfigFile();
        }
    }

    /**
     * Creates JOptionPane popup for navigating to a favorite location
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void navFavorite(View view, Navigator navigator) {
        Landmark favName = chooseFavorite(view, navigator);
        if(favName != null) {
            navigator.setDestination(favName.getLocation(), favName.getName());
            navigator.writeToConfigFile();
        }
    }

    private static Landmark chooseFavorite(View view, Navigator navigator) {
        ArrayList<String[]> favoritePairs = navigator.getFavoritePairs();
        if(favoritePairs.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return null;
        }

        ArrayList<String[]> filteredFavorites = new ArrayList<>();
        for(String[] pair : favoritePairs) {
            filteredFavorites.add(new String[]{pair[0], pair[1]});
        }
        filteredFavorites.sort((o1, o2) -> o1[0].compareToIgnoreCase(o2[0]));
        ArrayList<String> menuItems = ListManager.splitEvenly(filteredFavorites, 2);

        OptionPane pane = new OptionPane();
        pane.addListInput(menuItems, 10);
        if(pane.show(view.getMainFrame(), "Navigate to favorite")) {
            int selectedIndex = pane.getJListSelectedIndex(0);
            return navigator.getFavorites().get(selectedIndex);
        } else {
            return null;
        }
    }

    /**
     * Creates JOptionPane popup for creating a favorite location
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void addFavorite(View view, Navigator navigator) {
        OptionPane pane = new OptionPane();
        pane.addLabel("Favorite name: ");

        JTextField nameField = new JTextField(20);
        pane.addTextInput(nameField);

        AddressPanel addressPanel = new AddressPanel(navigator.currentLocation());
        pane.addComponent(addressPanel);

        JTextArea descriptionField = new JTextArea(8, 20);
        pane.addTextInput(descriptionField);

        if(pane.show(view.getMainFrame(), "Add new favorite")) {
            String favName = nameField.getText();
            Location favLocation = addressPanel.getAddress();
            String description = descriptionField.getText();
            navigator.getFavorites().add(new Landmark(favName, description, favLocation));
            navigator.writeFavorites();
            navigator.writeToConfigFile();
        }
    }

    static void viewFavoriteDescription(View view, Navigator navigator) {
        Landmark landmark = chooseFavorite(view, navigator);
        if(landmark == null) {
            return;
        }

        OptionPane eventPane = new OptionPane();
        eventPane.addLabel("Name: " + landmark.getName());
        eventPane.addLabel("Location: " + landmark.getLocation());
        OptionPaneTextArea descriptionArea = new OptionPaneTextArea(20, 10);
        descriptionArea.setLineWrap(true);
        descriptionArea.setText(landmark.getDescription());
        descriptionArea.setEditable(false);
        eventPane.addTextInput(descriptionArea);
        eventPane.show(view.getMainFrame(), "Landmark");
    }

    /**
     * Creates JOptionPane popup for setting man coordinates
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void setMan(View view, Navigator navigator) {
        OptionPane pane = new OptionPane();
        pane.addLabel("Latitude:");
        JTextField latitudeField = new JTextField(10);
        latitudeField.setText(String.valueOf(Location.getMan_latitude()));
        pane.addTextInput(latitudeField);
        pane.addLabel("Longitude:");
        JTextField longitudeField = new JTextField(10);
        longitudeField.setText(String.valueOf(Location.getMan_longitude()));
        pane.addTextInput(longitudeField);

        if(pane.show(view.getMainFrame(), "Enter man coordinates")) {
            try {
                double latitude = Double.parseDouble(latitudeField.getText());
                double longitude = Double.parseDouble(longitudeField.getText());
                Location.setManCoordinates(latitude, longitude);
                navigator.writeToConfigFile();
            } catch(NumberFormatException e) {
                OptionPane.showMessage(view.getMainFrame(), "Invalid coordinates");
            }
        }


    }

    /**
     * Creates JOptionPane popup for setting home address
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void setHome(View view, Navigator navigator) {
        OptionPane pane = new OptionPane();
        pane.addLabel("Enter new address");
        AddressPanel addressPanel = new AddressPanel(navigator.currentLocation());
        pane.addComponent(addressPanel);

        if(pane.show(view.getMainFrame(), "Set home address")) {
            Location home = addressPanel.getAddress();
            navigator.setHome(home);
            navigator.writeToConfigFile();
        }
    }

    /**
     * Creates JOptionPane popup for searching for a camp
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void findCamp(View view, Navigator navigator) {
        OptionPane searchPane = new OptionPane();
        searchPane.addLabel("Camp name:");
        JTextField nameField = new JTextField(10);
        searchPane.addTextInput(nameField);
        if(!searchPane.show(view.getMainFrame(), "Search for camp")) {
            return;
        }


        ArrayList<String> camps = navigator.findCamps(nameField.getText());
        camps.sort(String::compareToIgnoreCase);
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

        ArrayList<String[]> campList = navigator.getCampPairs(camps);
        ArrayList<String> menuList = ListManager.splitEvenly(campList, 2);
        OptionPane resultsPane = new OptionPane();
        resultsPane.addListInput(menuList, 10);
        if(resultsPane.show(view.getMainFrame(), "Matching camps")) {
            int campNameIndex = resultsPane.getJListSelectedIndex(0);
            String campName = camps.get(campNameIndex);
            Location camp = navigator.getCamp(campName);
            navigator.setDestination(camp, campName);
            view.setNavigation(navigator);
            navigator.writeToConfigFile();
        }
    }

    /**
     * Sets menu to favorites submenu
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void favorites(View view, Navigator navigator) {
        view.getMenu().favorites(view, navigator);
    }

    /**
     * Sets menu to notes submenu
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void notes(View view, Navigator navigator) {
        view.getMenu().notes(view, navigator);
    }

    /**
     * Sets menu to events submenu
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void events(View view, Navigator navigator) {
        view.getMenu().events(view, navigator);
    }

    /**
     * Sets menu to settings submenu
     *
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void settings(View view, Navigator navigator) {
        view.getMenu().settings(view, navigator);
    }

    public static void adjustBlockWidths(View view, Navigator navigator) {
        class BlockPane extends JPanel {
            private JTextField distance, street;

            private BlockPane(String distanceStr, String streetStr) {
                setLayout(new FlowLayout());
                distance = new JTextField(6);
                distance.setText(distanceStr);
                street = new JTextField(11);
                street.setText(streetStr);
                add(distance);
                add(street);
            }

            private String getDistance() {
                return distance.getText();
            }

            private String getStreet() {
                return street.getText();
            }

        }

        OptionPane pane = new OptionPane();
        pane.addLabel("Enter block widths:");

        Object[][] blockDistances = Location.getBlockDistances();
        BlockPane[] blockPanes = new BlockPane[blockDistances.length];
        for(int i = 0; i < blockDistances.length; i++) {
            blockPanes[i] = new BlockPane(String.valueOf(blockDistances[i][0]), (String) blockDistances[i][1]);
        }

        for(BlockPane blockPane : blockPanes) {
            pane.addComponent(blockPane);
        }

        if(!pane.show(null, "Calculate errors")) {
            return;
        }

        Object[][] blockDistancesUpdated = new Object[blockDistances.length][2];
        for(int i = 0; i < blockPanes.length; i++) {
            try {
                blockDistancesUpdated[i][0] = Integer.parseInt(blockPanes[i].getDistance());
                blockDistancesUpdated[i][1] = blockPanes[i].getStreet();
            } catch(NumberFormatException e) {
                OptionPane.showMessage(view.getMainFrame(),
                        "Invalid distance for street " + blockPanes[i].getStreet());
                return;
            }
        }
        Location.setBlockDistances(blockDistancesUpdated);
    }

    public static void exitProgram(View view, Navigator navigator) {

    }

    /**
     * Shows the log file
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void viewLog(View view, Navigator navigator) {
        OptionPane pane = new OptionPane();
        ArrayList<String> logData = LogDriver.readLog();

        OptionPaneTextArea field = new OptionPaneTextArea(60, 30);
        field.setText(String.join("\n", logData));
        field.setEditable(false);
        pane.addComponent(field);

        JButton button = new JButton("Clear log file");
        button.addActionListener(actionEvent -> {
            int result = JOptionPane.showConfirmDialog(view.getMainFrame(), "Are you sure you want to delete the log file?", "Delete log file", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION) {
                LogDriver.clearLog();
            }
        });
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        buttonPanel.add(button);
        pane.addComponent(buttonPanel);
        pane.show(view.getMainFrame(), "Log data");

    }

    /**
     * Populates a list with all camps
     * @param view      main view panel
     * @param navigator main navigator object
     */
    static void viewAllCamps(View view, Navigator navigator) {
        ArrayList<String[]> allCamps = navigator.getCampPairs();
        if(allCamps.isEmpty()) {
            JOptionPane.showMessageDialog(view.getMainFrame(), "No camps found");
            return;
        }
        ArrayList<String> menuItems = ListManager.splitEvenly(allCamps, 2);

        OptionPane pane = new OptionPane();
        pane.addListInput(menuItems, 10);
        if(pane.show(view.getMainFrame(), "Navigate to camp")) {
            String campName = allCamps.get(pane.getJListSelectedIndex(0))[0];
            if(campName != null) {
                navigator.setDestination(navigator.getCamp(campName), campName);
                navigator.writeToConfigFile();
            }
        }

    }
}
