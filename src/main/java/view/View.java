package view;

import navigation.Navigator;

import javax.swing.*;
import java.awt.*;


/**
 * Class View
 * <p>
 * Creates JFrame and components
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class View {
    // Main frame and panel
    private final JFrame mainFrame;
    private final MainInterfacePanel mainPanel;

    // Global standard font
    static final Font standardFont = new Font("Monospaced", Font.PLAIN, 22);

    /**
     * Constructor
     * <p>
     * instantiates main JFrame and components
     */
    View() {
        setUIParameters();

        mainPanel = new MainInterfacePanel();

        mainFrame = new JFrame("MainInterfacePanel");
        fullScreen(mainFrame);
        mainFrame.setContentPane(mainPanel.getMainPanel());

        mainPanel.getMainPanel().setFocusable(true);
        mainPanel.getMainPanel().requestFocus();

        mainFrame.setResizable(true);
    }

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
}
