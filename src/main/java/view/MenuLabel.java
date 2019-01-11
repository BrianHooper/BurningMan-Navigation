package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Class MenuLabel
 *
 * extends JLabel
 */
public class MenuLabel extends JLabel {

    /**
     * Constructor
     *
     * sets JLabel text
     * @param text label
     */
    public MenuLabel(String text) {
        setText(text);
        setOpaque(true);
        setBorder(new EmptyBorder(5, 0, 5, 5));
    }

    /**
     * Applies background color to show label is selected
     */
    public void select() {
        setBackground(Color.LIGHT_GRAY);
    }

    /**
     * Removes background color to show label is not selected
     */
    public void deselect() {
        setBackground(null);
    }
}
