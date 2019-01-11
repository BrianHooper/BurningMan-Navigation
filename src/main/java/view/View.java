package view;

import javax.swing.*;
import java.awt.*;
import navigation.Navigator;

/**
 * Class View
 *
 * Creates JFrame and components
 */
public class View {
    private final JFrame mainFrame;
    private final MainInterfacePanel mainPanel;

    /**
     * Constructor
     *
     * instantiates main JFrame and components
     */
    public View() {
        setUIParameters();

        mainPanel = new MainInterfacePanel();

        mainFrame = new JFrame("MainInterfacePanel");
        mainFrame.setContentPane(mainPanel.getMainPanel());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(mainFrame.getWidth() + 50, mainFrame.getHeight() + 50);

        mainPanel.getMainPanel().setFocusable(true);
        mainPanel.getMainPanel().requestFocus();

        mainFrame.setVisible(true);
    }

    /**
     * Sets global view properties (font, etc)
     */
    public void setUIParameters() {
        Font standardFont = new Font("Monospaced", Font.PLAIN, 22);
        UIManager.put("Label.font", standardFont);
        UIManager.put("TextField.font", standardFont);
        UIManager.put("JList.font", standardFont);
        UIManager.put("JScrollPane.font", standardFont);
        UIManager.put("OptionPane.font", standardFont);
        UIManager.put("TextField.background", Color.WHITE);
    }

    /**
     * Binds KeyController to main JPanel
     * @param controller KeyController
     */
    public void setKeyListener(KeyController controller) {
        mainPanel.getMainPanel().addKeyListener(controller);
    }

    /**
     * Updates navigation fields with navigator data
     * @param navigator Navigator
     */
    public void setNavigation(Navigator navigator) {
        mainPanel.setNavigation(navigator.getPanelUpdate());
    }

    /**
     * Getter for mainFrame
     * @return mainFrame
     */
    public JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Getter for menu
     * @return menu
     */
    public Menu getMenu() {
        return mainPanel.getMenu();
    }

    /**
     * Repaints panel and requests keyboard focus
     */
    public void getFocus() {
        mainPanel.resetMenu();
        mainPanel.getMainPanel().requestFocus();
        mainFrame.repaint();
        mainFrame.revalidate();

    }
}
