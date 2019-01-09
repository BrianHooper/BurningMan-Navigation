import javax.swing.*;

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

    private Menu menu;

    public Menu getMenu() {
        return menu;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setCurrentAddress(String fieldValue) {
        this.currentAddress.setText(fieldValue);
    }

    public void setBathroomAddress(String fieldValue) {
        this.bathroomAddress.setText(fieldValue);
    }

    public void setBathroomDirections(String fieldValue) {
        this.bathroomDirections.setText(fieldValue);
    }

    public void setHomeAddress(String fieldValue) {
        this.homeAddress.setText(fieldValue);
    }

    public void setHomeDirections(String fieldValue) {
        this.homeDirections.setText(fieldValue);
    }

    public void setDestinationName(String fieldValue) {
        this.destinationName.setText(fieldValue);
    }

    public void setDestinationAddress(String fieldValue) {
        this.destinationAddress.setText(fieldValue);
    }

    public void setDestinationDirections(String fieldValue) {
        this.destinationDirections.setText(fieldValue);
    }

    private void createUIComponents() {
        leftMenu = new JPanel();
        leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.PAGE_AXIS));

        menu = new Menu();

        MenuLabel[] menuLabels = menu.readMenu();
        if(menuLabels != null) {
            for (MenuLabel menuLabel : menuLabels) {
                leftMenu.add(menuLabel);
            }
        }
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
