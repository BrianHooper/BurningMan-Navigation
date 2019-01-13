package driver;

import events.EventManager;
import events.NoteManager;
import navigation.Navigator;
import view.*;


public class NavigationDriver {

    public static void main(String[] args) {
        // Initialize navigator
        EventManager eventManager = new EventManager();
        eventManager.importEvents("eventslist.csv");

        NoteManager noteManager = new NoteManager();
        noteManager.readNotes();

        Navigator navigator = new Navigator();
        navigator.initializeLandmarks("bathrooms.csv", "camps.csv", "favorites.csv");
        navigator.loadFromFile("config.cfg");
        // Initialize GUI
        View view = new View();
        MenuController menuController = new MenuController(view, navigator, eventManager, noteManager);
        KeyController controller = new KeyController(menuController);
        view.setKeyListener(controller);
        view.setNavigation(navigator);

        // Start thread for reading coordinate data
        CoordinateListener coordinateListener = new CoordinateListener(navigator, view);
        coordinateListener.start();

        ClockDriver clockDriver = new ClockDriver(view);
        clockDriver.start();


//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
//
//
//        JTextField field = new JTextField(10);
//        field.addAncestorListener(new RequestFocusListener());
//        field.setAlignmentX(0);
//
//        OptionPaneTextArea area = new OptionPaneTextArea(20, 10);
//        JScrollPane jScrollPane = new JScrollPane(area);
//        jScrollPane.setAlignmentX(0);
//
//        panel.add(new JLabel("Title:"));
//        panel.add(field);
//        panel.add(new JLabel("Body:"));
//        panel.add(jScrollPane);
//
//        JOptionPane.showConfirmDialog(null, panel, "Input", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
}
