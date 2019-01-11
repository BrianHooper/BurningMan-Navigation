package view;

import javax.swing.*;
import java.awt.*;
import navigation.Navigator;

public class View {

    private JFrame mainFrame;
    private MainInterfacePanel mainPanel;


    public View() {
        Font standardFont = new Font("Monospaced", Font.PLAIN, 22);
        UIManager.put("Label.font", standardFont);
        UIManager.put("TextField.font", standardFont);
        UIManager.put("JList.font", standardFont);
        UIManager.put("JScrollPane.font", standardFont);
        UIManager.put("OptionPane.font", standardFont);
        UIManager.put("TextField.background", Color.WHITE);

        mainPanel = new MainInterfacePanel();

        mainFrame = new JFrame("MainInterfacePanel");
        mainFrame.setContentPane(mainPanel.getMainPanel());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(mainFrame.getWidth() + 50, mainFrame.getHeight() + 50);

        mainPanel.getMainPanel().setFocusable(true);
        mainPanel.getMainPanel().requestFocus();

        reset();
        mainFrame.setVisible(true);
    }

    public void setKeyListener(KeyController controller) {

        mainPanel.getMainPanel().addKeyListener(controller);
    }

    public void setNavigation(Navigator navigator) {
        mainPanel.setNavigation(navigator.getPanelUpdate());
    }

    public void reset() {
        mainPanel.resetMenu();
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    public JPanel getMainPanel() {
        return mainPanel.getMainPanel();
    }

    public Menu getMenu() {
        return mainPanel.getMenu();
    }
}
