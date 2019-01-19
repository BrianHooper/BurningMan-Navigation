package view;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Class OptionPaneTextArea
 * <p>
 * Extends JTextArea
 * <p>
 * Overrides tab key to force giving focus to the next component
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class OptionPaneTextArea extends JTextArea implements KeyListener {

    /**
     * Constructor
     * <p>
     * Creates a JTextArea of a given size
     *
     * @param cols number of columns
     * @param rows number of rows
     */
    @SuppressWarnings("SameParameterValue")
    OptionPaneTextArea(int cols, int rows) {
        super();
        addKeyListener(this);
        setColumns(cols);
        setRows(rows);
    }

    /**
     * Consumes tab key and transfers focus to the next focusable element
     *
     * @param e KeyEvent
     */
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                e.consume();
                transferFocus();
                break;
        }
    }

    /**
     * Consumes tab key
     *
     * @param e KeyEvent
     */
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                break;
        }
    }

    /**
     * Consumes tab key
     *
     * @param e KeyEvent
     */
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                break;
        }
    }
}