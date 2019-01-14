package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Class MenuLabel
 * <p>
 * extends JLabel
 */
class MenuLabel extends JLabel {

    /**
     * Constructor
     * <p>
     * sets JLabel text
     *
     * @param text label
     */
    MenuLabel(String text) {
        setText(text);
        setOpaque(true);
        setBorder(new EmptyBorder(5, 0, 5, 5));
    }

    /**
     * Applies background color to show label is selected
     */
    void select() {
        setBackground(Color.LIGHT_GRAY);
    }

    /**
     * Removes background color to show label is not selected
     */
    void deselect() {
        setBackground(null);
    }
}
