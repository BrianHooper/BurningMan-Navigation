import javax.swing.*;
import java.util.TreeMap;

public class View {
    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 600;

    private JFrame mainFrame;
    private TreeMap<String, JComponent> components;
    private ActionHandler actionHandler;

    public View() {
        components = new TreeMap<>();

        mainFrame = new JFrame("Burning Man Navigator");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

    }

    public void setActionHandler(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
    }

    public void buildLayout() {
        JPanel mainPanel = new JPanel();
        JLabel address = new JLabel();

        JButton button = new JButton("Calculate address");
        button.addActionListener(actionHandler);
        button.setActionCommand("UpdateLocationButton");

        JTextField latitude = new JTextField();
        JTextField longitude = new JTextField();
        latitude.setColumns(16);
        longitude.setColumns(16);

        mainPanel.add(latitude);
        mainPanel.add(longitude);
        mainPanel.add(button);
        mainPanel.add(address);
        mainFrame.add(mainPanel);

        components.put("MainPanel", mainPanel);
        components.put("AddressField", address);
        components.put("Button", button);
        components.put("Latitude", latitude);
        components.put("Longitude", longitude);

        mainFrame.setVisible(true);
    }

    public void setAddress(Location location) {
        String address = location.getAddress();
        JLabel addressField = (JLabel) components.get("AddressField");
        addressField.setText(address);
        update();

    }

    public void update() {
        mainFrame.repaint();
    }

    public String readLatitude() {
        return ((JTextField) components.get("Latitude")).getText();
    }

    public String readLongitude() {
        return ((JTextField) components.get("Longitude")).getText();
    }
}
