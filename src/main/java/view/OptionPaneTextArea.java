package view;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class OptionPaneTextArea extends JTextArea implements KeyListener {

    @SuppressWarnings("SameParameterValue")
    OptionPaneTextArea(int cols, int rows) {
        super();
        initialize();
        setColumns(cols);
        setRows(rows);
    }


    private void initialize() {
        addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                e.consume();
                transferFocus();
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_TAB:
                break;
        }

    }
}