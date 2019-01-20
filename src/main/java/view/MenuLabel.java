package view;

import navigation.Navigator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 * Class MenuLabel
 * <p>
 * Extends JLabel
 * <p>
 * Represents a single menu item, can be selected or deselected
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class MenuLabel extends JLabel {
    private final Border deselectedBorder = new EmptyBorder(5, 5, 5, 5);
    private final Border selectedBorder = new CompoundBorder(
            new BevelBorder(BevelBorder.RAISED), deselectedBorder);
    private final OptionPaneController optionPane;

    /**
     * Constructor
     * <p>
     * sets JLabel text
     *
     * @param text label
     */
    MenuLabel(String text, OptionPaneController optionPane) {
        this.optionPane = optionPane;
        setText(text);
        setOpaque(true);
        setBorder(deselectedBorder);
    }

    /**
     * Applies background color to show label is selected
     */
    void select() {
        setBorder(selectedBorder);
        setBackground(Color.LIGHT_GRAY);
    }

    /**
     * Removes background color to show label is not selected
     */
    void deselect() {
        setBorder(deselectedBorder);
        setBackground(null);
    }


    /**
     * Forces explicit size of menu labels
     *
     * @return Dimension
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 40);
    }

    /**
     * Perform action referenced by OptionPaneController instance
     *
     * @param view      view
     * @param navigator navigator
     */
    void performAction(View view, Navigator navigator) {
        optionPane.createPane(view, navigator);
    }
}
