package view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Class KeyController
 * <p>
 * listens for key presses
 */
class KeyController implements KeyListener {
    private final UserInterfaceController menu;

    /**
     * Constructor
     *
     * @param menu UserInterfaceController object
     */
    KeyController(UserInterfaceController menu) {
        this.menu = menu;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Activates menu based on KeyEvents
     *
     * @param e KeyEvent object
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch(keyCode) {
            case KeyEvent.VK_UP:
                menu.menuUp();
                break;
            case KeyEvent.VK_DOWN:
                menu.menuDown();
                break;
            case KeyEvent.VK_ENTER:
                menu.menuSelect();
                break;
            case KeyEvent.VK_ESCAPE:
                menu.menuEscape();
                break;
        }
    }
}
