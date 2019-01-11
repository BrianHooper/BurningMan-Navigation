package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.TreeMap;

public class MainInterfacePanel {
    private JPanel mainPanel;

    private JTextField currentAddress;
    private JTextField bathroomAddress;
    private JTextField bathroomDirections;
    private JTextField homeAddress;
    private JTextField homeDirections;
    private JTextField destinationName;
    private JTextField destinationAddress;
    private JTextField destinationDirections;
    private JPanel leftMenu;
    private JPanel leftMenuPanel;
    private JLabel nearestBathroomLabel;
    private JLabel homeCampLabel;
    private JLabel destinationLabel;

    private view.Menu menu;

    /**
     * getter for Menu object
     * @return menu
     */
    public view.Menu getMenu() {
        return menu;
    }

    /**
     * Getter for mainPanel
     * @return mainPanel
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }

    /**
     * Sets current address field
     * @param fieldValue address
     */
    private void setCurrentAddress(String fieldValue) {
        this.currentAddress.setText(fieldValue);
    }

    /**
     * sets bathroom address field
     * @param fieldValue address
     */
    private void setBathroomAddress(String fieldValue) {
        this.bathroomAddress.setText(fieldValue);
    }

    /**
     * sets bathroom directions field
     * @param fieldValue directions
     */
    private void setBathroomDirections(String fieldValue) {
        this.bathroomDirections.setText(fieldValue);
    }

    /**
     * sets home address field
     * @param fieldValue address
     */
    private void setHomeAddress(String fieldValue) {
        this.homeAddress.setText(fieldValue);
    }

    /**
     * sets home directions field
     * @param fieldValue directions
     */
    private void setHomeDirections(String fieldValue) {
        this.homeDirections.setText(fieldValue);
    }


    /**
     * sets destination name field
     * @param fieldValue destination
     */
    private void setDestinationName(String fieldValue) {
        this.destinationName.setText(fieldValue);
    }

    /**
     * sets destination address field
     * @param fieldValue address
     */
    private void setDestinationAddress(String fieldValue) {
        this.destinationAddress.setText(fieldValue);
    }

    /**
     * sets destination directions field
     * @param fieldValue directions
     */
    private void setDestinationDirections(String fieldValue) {
        this.destinationDirections.setText(fieldValue);
    }

    /**
     * Updates all navigation fields
     * @param map TreeMap containing field name and field value
     */
    public void setNavigation(TreeMap<String, String> map) {
        setCurrentAddress(map.get("currentAddress"));
        setBathroomAddress(map.get("bathroomAddress"));
        setBathroomDirections(map.get("bathroomDirections"));
        setHomeAddress(map.get("homeAddress"));
        setHomeDirections(map.get("homeDirections"));
        setDestinationName(map.get("destinationName"));
        setDestinationAddress(map.get("destinationAddress"));
        setDestinationDirections(map.get("destinationDirections"));
    }

    /**
     * Builds custom UI components
     */
    private void createUIComponents() {
        leftMenuPanel = new JPanel();
        leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.PAGE_AXIS));
        leftMenu.setPreferredSize(new Dimension(400,368));
        leftMenuPanel.add(leftMenu);

        menu = new Menu();

        nearestBathroomLabel = new JLabel("Nearest Bathroom: ");
        nearestBathroomLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        homeCampLabel = new JLabel("Home camp: ");
        homeCampLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        destinationLabel = new JLabel("Destination: ");
        destinationLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        resetMenu();
    }

    /**
     * Reads and resets menu items
     */
    public void resetMenu() {
        leftMenu.removeAll();
        MenuLabel[] menuLabels = menu.readMenu();
        if(menuLabels != null) {
            for (MenuLabel menuLabel : menuLabels) {
                leftMenu.add(menuLabel);
            }
        }
    }
}
