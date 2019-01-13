package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.TreeMap;

class MainInterfacePanel {
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
    private JPanel clockPanel;
    private JLabel clockLabel;
    private JLabel addressLabel;
    private JPanel rightPanel;

    private view.Menu menu;

    /**
     * getter for Menu object
     * @return menu
     */
    view.Menu getMenu() {
        return menu;
    }

    /**
     * Getter for mainPanel
     * @return mainPanel
     */
    JPanel getMainPanel() {
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
     * Sets the clock value
     * @param clockValue time string
     */
    void setClock(String clockValue) {
        this.clockLabel.setText(clockValue);
    }

    /**
     * Updates all navigation fields
     * @param map TreeMap containing field name and field value
     */
    void setNavigation(TreeMap<String, String> map) {
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

        addressLabel = new JLabel("Current Address:");
        addressLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        nearestBathroomLabel = new JLabel("Nearest Bathroom: ");
        nearestBathroomLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        homeCampLabel = new JLabel("Home camp: ");
        homeCampLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        destinationLabel = new JLabel("Destination: ");
        destinationLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        clockPanel = new JPanel();
        clockLabel = new JLabel("Clock");
        clockLabel.setFont(new Font(View.standardFont.getFontName(), Font.PLAIN, 32));
        clockPanel.add(clockLabel);

        resetMenu();
    }

    /**
     * Reads and resets menu items
     */
    void resetMenu() {
        leftMenu.removeAll();
        MenuLabel[] menuLabels = menu.readMenu();
        if(menuLabels != null) {
            for (MenuLabel menuLabel : menuLabels) {
                leftMenu.add(menuLabel);
            }
        }
    }
}
