package view;

import navigation.Navigator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Class View
 * <p>
 * Creates JFrame and components
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class View {
//**********************
// Class member fields
//**********************

    // Main frame and panel
    private final JFrame mainFrame;
    private final MainInterfacePanel mainPanel;

    // Global standard font
    static final Font standardFont = new Font("Monospaced", Font.PLAIN, 22);

//**********************
// Constructors and initializers
//**********************

    /**
     * Constructor
     * <p>
     * instantiates main JFrame and components
     */
    View() {
        setUIParameters();

        mainPanel = new MainInterfacePanel();
        mainPanel.getMainPanel().setBackground(Color.LIGHT_GRAY);

        mainFrame = new JFrame("MainInterfacePanel");

        // F1 key requests focus for the main panel
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if(e.getKeyCode() == 112) {
                        mainPanel.getMainPanel().requestFocus();
                    }
                    return false;
                });

        fullScreen(mainFrame);
        mainFrame.setContentPane(mainPanel.getMainPanel());

        mainPanel.getMainPanel().setFocusable(true);
        mainPanel.getMainPanel().requestFocus();

        mainFrame.setResizable(true);
    }

    /**
     * Sets global view properties (font, etc)
     */
    private void setUIParameters() {
        UIManager.put("Label.font", standardFont);
        UIManager.put("TextField.font", standardFont);
        UIManager.put("JList.font", standardFont);
        UIManager.put("JScrollPane.font", standardFont);
        UIManager.put("OptionPane.font", standardFont);
        UIManager.put("TextField.background", Color.WHITE);
    }

//**********************
// Private static methods
//**********************

    /**
     * Makes the frame fullscreen
     *
     * @param frame JFrame
     */
    private static void fullScreen(final JFrame frame) {
        frame.setUndecorated(true);
        frame.setResizable(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setTitle("Burning Man Navigation");
        ImageIcon img = new ImageIcon("src/main/resources/icon.png");
        frame.setIconImage(img.getImage());
        frame.setVisible(true);
    }

//**********************
// Getters and setters
//**********************

    /**
     * Binds KeyController to main JPanel
     *
     * @param controller KeyController
     */
    void setKeyListener(KeyController controller) {
        mainPanel.getMainPanel().addKeyListener(controller);
    }

    /**
     * Updates navigation fields with navigator data
     *
     * @param navigator Navigator
     */
    public void setNavigation(Navigator navigator) {
        mainPanel.setNavigation(navigator.getPanelUpdate());
    }

    /**
     * Getter for mainFrame
     *
     * @return mainFrame
     */
    JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Getter for menu
     *
     * @return menu
     */
    Menu getMenu() {
        return mainPanel.getMenu();
    }

    /**
     * Repaints panel and requests keyboard focus
     */
    void getFocus() {
        mainPanel.resetMenu();
        mainPanel.getMainPanel().requestFocus();
        mainFrame.repaint();
        mainFrame.revalidate();
    }

    /**
     * Sets the clock value
     *
     * @param clockValue time string
     */
    public void setClock(String clockValue) {
        mainPanel.setClock(clockValue);
    }

    /**
     * Updates main panel with latitude and longitude
     *
     * @param latitude  latitude
     * @param longitude longitude
     */
    public void setLocation(double latitude, double longitude) {
        mainPanel.setLocation(latitude, longitude);
    }

//**********************
// Class methods
//**********************

}
