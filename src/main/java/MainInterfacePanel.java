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

    private Menu menu;

    public Menu getMenu() {
        return menu;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private void setCurrentAddress(String fieldValue) {
        this.currentAddress.setText(fieldValue);
    }

    private void setBathroomAddress(String fieldValue) {
        this.bathroomAddress.setText(fieldValue);
    }

    private void setBathroomDirections(String fieldValue) {
        this.bathroomDirections.setText(fieldValue);
    }

    private void setHomeAddress(String fieldValue) {
        this.homeAddress.setText(fieldValue);
    }

    private void setHomeDirections(String fieldValue) {
        this.homeDirections.setText(fieldValue);
    }

    private void setDestinationName(String fieldValue) {
        this.destinationName.setText(fieldValue);
    }

    private void setDestinationAddress(String fieldValue) {
        this.destinationAddress.setText(fieldValue);
    }

    private void setDestinationDirections(String fieldValue) {
        this.destinationDirections.setText(fieldValue);
    }

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

    private void createUIComponents() {
        leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.PAGE_AXIS));
        leftMenu.setPreferredSize(new Dimension(400,368));

        menu = new Menu();

        nearestBathroomLabel = new JLabel("Nearest Bathroom: ");
        nearestBathroomLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        homeCampLabel = new JLabel("Home camp: ");
        homeCampLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        destinationLabel = new JLabel("Destination: ");
        destinationLabel.setBorder(new EmptyBorder(20, 0, 0, 0));

        resetMenu();
    }

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
