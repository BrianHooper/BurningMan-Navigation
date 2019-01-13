package view;

import navigation.Navigator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Class View
 *
 * Creates JFrame and components
 */
public class View {
    private final JFrame mainFrame;
    private final MainInterfacePanel mainPanel;

    static final Font standardFont = new Font("Monospaced", Font.PLAIN, 22);

    /**
     * Constructor
     *
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
     * @param frame JFrame
     */
    private static void fullScreen(final JFrame frame) {

        GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
        boolean result = device.isFullScreenSupported();

        if (result) {
            frame.setUndecorated(true);
            frame.setResizable(true);

            frame.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent arg0) {
                    frame.setAlwaysOnTop(true);
                }

                @Override
                public void focusLost(FocusEvent arg0) {
                    frame.setAlwaysOnTop(false);
                }
            });

            frame.pack();

            device.setFullScreenWindow(frame);
        }
        else {
            frame.setPreferredSize(frame.getGraphicsConfiguration().getBounds().getSize());

            frame.pack();

            frame.setResizable(true);

            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            boolean successful = frame.getExtendedState() == Frame.MAXIMIZED_BOTH;

            frame.setVisible(true);

            if (!successful)
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        }
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
     * @param controller KeyController
     */
    void setKeyListener(KeyController controller) {
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
    JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Getter for menu
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
     * @param clockValue time string
     */
    public void setClock(String clockValue) {
        mainPanel.setClock(clockValue);
    }
}
