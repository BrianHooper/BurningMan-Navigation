import javax.swing.*;

public class NavigationDriver {

    public static void main(String[] args) {

        MainInterfacePanel panel = new MainInterfacePanel();

        JFrame frame = new JFrame("MainInterfacePanel");
        frame.setContentPane(panel.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(frame.getWidth() + 10, frame.getHeight() + 10);
//        frame.setSize(400,300);

        frame.revalidate();
        frame.setVisible(true);

        Menu menu = panel.getMenu();
        try {
            Thread.sleep(500);
            menu.down();
            Thread.sleep(500);
            menu.down();
            Thread.sleep(500);
            menu.action("Settings");
            panel.resetMenu();
            frame.revalidate();
            frame.pack();
        } catch (InterruptedException e) {

        }
    }



}
