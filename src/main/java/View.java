import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;

public class View {

    private JFrame mainFrame;
    private MainInterfacePanel mainPanel;
    private Navigator navigator;


    public View(Navigator navigator) {
        Font standardFont = new Font("Monospaced", Font.PLAIN, 22);
        UIManager.put("Label.font", standardFont);
        UIManager.put("TextField.font", standardFont);
        UIManager.put("JList.font", standardFont);
        UIManager.put("JScrollPane.font", standardFont);
        UIManager.put("OptionPane.font", standardFont);
        UIManager.put("TextField.background", Color.WHITE);

        this.navigator = navigator;
        mainPanel = new MainInterfacePanel();

        mainFrame = new JFrame("MainInterfacePanel");
        mainFrame.setContentPane(mainPanel.getMainPanel());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(mainFrame.getWidth() + 50, mainFrame.getHeight() + 50);

        mainPanel.getMainPanel().setFocusable(true);
        mainPanel.getMainPanel().requestFocus();
        mainPanel.getMainPanel().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch( keyCode ) {
                    case KeyEvent.VK_UP:
                        mainPanel.getMenu().up();
                        break;
                    case KeyEvent.VK_DOWN:
                        mainPanel.getMenu().down();
                        break;
                    case KeyEvent.VK_ENTER:
                        action(mainPanel.getMenu().select());
                        reset();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        mainPanel.getMenu().home();
                        reset();
                        break;
                }
            }
        });

        reset();
        mainFrame.setVisible(true);
    }

    public void setNavigation(Navigator navigator) {
        mainPanel.setNavigation(navigator.getPanelUpdate());
    }

    public void reset() {
        mainPanel.resetMenu();
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void action(String actionCommand) {
        switch (actionCommand) {
            case "findCamp":
                findCamp();
                break;
            case "setHome":
                setHome();
                break;
            case "setMan":
                setMan();
                break;
            case "setEsplanade":
                setEsplanade();
                break;
            case "setBlockWidth":
                setBlockWidth();
                break;
            case "addFavorite":
                addFavorite();
                break;
            case "navFavorite":
                navFavorite();
                break;
            case "delFavorite":
                delFavorite();
                break;
            case "listCamps":
                listCamps();
                break;
        }
        mainPanel.setNavigation(navigator.getPanelUpdate());
    }

    private void listCamps() {
        Set<String> camps = navigator.getCamps().keySet();
        Object[] menuItems = new String[camps.size()];
        System.arraycopy(camps.toArray(), 0, menuItems, 0, camps.size());
        if(menuItems.length == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No camps found");
            return;
        }

        String input = (String) JOptionPane.showInputDialog(mainFrame, "Choose now...",
                "Navigate to camp", JOptionPane.QUESTION_MESSAGE, null,
                menuItems, // Array of choices
                menuItems[0]); // Initial choice
        findCamp(input);
        mainPanel.getMainPanel().requestFocus();
    }

    private void delFavorite() {
        //todo write to file
        Set<String> favorites = navigator.getFavorites().keySet();
        Object[] menuItems = new String[favorites.size()];
        System.arraycopy(favorites.toArray(), 0, menuItems, 0, favorites.size());
        if(menuItems.length == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No camps found");
            return;
        }

        String input = (String) JOptionPane.showInputDialog(mainFrame, "Choose now...",
                "Navigate to camp", JOptionPane.QUESTION_MESSAGE, null,
                menuItems, // Array of choices
                menuItems[0]); // Initial choice

        if(input != null) {
            int confirmation = JOptionPane.showConfirmDialog(mainFrame, "Delete favorite " + input + "?");
            if (confirmation == JOptionPane.YES_OPTION) {
                //todo fix keyboard select error
                navigator.getFavorites().remove(input);
            }
        }
        mainPanel.getMainPanel().requestFocus();
    }

    private void navFavorite() {
        Set<String> favorites = navigator.getFavorites().keySet();
        Object[] menuItems = new String[favorites.size()];
        System.arraycopy(favorites.toArray(), 0, menuItems, 0, favorites.size());
        if(menuItems.length == 0) {
            JOptionPane.showMessageDialog(mainFrame, "No camps found");
            return;
        }

        String input = (String) JOptionPane.showInputDialog(mainFrame, "Choose now...",
                "Navigate to camp", JOptionPane.QUESTION_MESSAGE, null,
                menuItems, // Array of choices
                menuItems[0]); // Initial choice

        if(input != null) {
            navigator.setDestination(navigator.getFavorites().get(input), input);
        }
        mainPanel.getMainPanel().requestFocus();
    }

    private void addFavorite() {
        //todo write to file
        String favName = JOptionPane.showInputDialog(mainFrame, "Enter name:");
        if(favName == null) {
            mainPanel.getMainPanel().requestFocus();
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(mainFrame, "Use current address?");
        if(confirm == JOptionPane.YES_OPTION) {
            navigator.getFavorites().put(favName, new Location(navigator.currentLocation()));
            mainPanel.getMainPanel().requestFocus();
            return;
        }

        String campAddress = JOptionPane.showInputDialog(mainFrame, "Enter address (Hour,Minute,Street): ");
        String[] split = campAddress.split(",");
        String err = "Invalid address";
        if(split.length != 3) {
            JOptionPane.showMessageDialog(mainFrame, err);
            mainPanel.getMainPanel().requestFocus();
        }

        try {
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            char street = split[2].charAt(0);
            navigator.getFavorites().put(favName, new Location(hour, minute, street));
            mainPanel.getMainPanel().requestFocus();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, err);
            mainPanel.getMainPanel().requestFocus();
        }
    }


    private void setBlockWidth() {
        String result = JOptionPane.showInputDialog(mainFrame, "Enter block width (default 240)");
        try {
            Location.block_width = Integer.parseInt(result);
            //TODO write to file
            mainPanel.getMainPanel().requestFocus();
        } catch (NumberFormatException e) {
            mainPanel.getMainPanel().requestFocus();
        }
    }

    private void setEsplanade() {
        String result = JOptionPane.showInputDialog(mainFrame, "Enter Esplanade distance (default 2600)");
        try {
            Location.esplanade_distance = Integer.parseInt(result);
            //TODO write to file
            mainPanel.getMainPanel().requestFocus();
        } catch (NumberFormatException e) {
            mainPanel.getMainPanel().requestFocus();
        }
    }

    private void setMan() {
        System.out.println(Location.man_latitude);
        int result = JOptionPane.showConfirmDialog(mainFrame, "Use current location as man coordinates?");
        if(result == JOptionPane.YES_OPTION) {
            //todo write to file
            navigator.setManCoordinates();
            mainPanel.getMainPanel().requestFocus();
        }
        System.out.println(Location.man_latitude);
    }

    private void setHome() {
        String result = JOptionPane.showInputDialog(mainFrame, "Enter home address (Hour,Minute,Street): ");
        String[] split = result.split(",");
        String err = "Invalid address";
        if(split.length != 3) {
            JOptionPane.showMessageDialog(mainFrame, err);
            mainPanel.getMainPanel().requestFocus();
        }
        try {
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            char street = split[2].charAt(0);
            navigator.setHome(hour, minute, street);
            mainPanel.getMainPanel().requestFocus();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainFrame, err);
            mainPanel.getMainPanel().requestFocus();
        }
    }

    private void findCamp() {
        String result = JOptionPane.showInputDialog(mainFrame, "Enter search term: ");
        findCamp(result);
    }

    private void findCamp(String result) {
        if(result == null) {
            return;
        }
        String campName = navigator.findCampName(result);
        if(campName.length() > 0) {
            Location camp = navigator.getCamp(campName);
            if (camp != null) {
                navigator.setDestination(camp, campName);
                mainPanel.setNavigation(navigator.getPanelUpdate());
            }
        }
    }
}
