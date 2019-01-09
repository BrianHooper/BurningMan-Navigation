import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuLabel extends JLabel {

    public MenuLabel(String text) {
        setText(text);
        setOpaque(true);
        setBorder(new EmptyBorder(5, 0, 5, 5));
    }

    public void select() {
        setBackground(Color.LIGHT_GRAY);
    }

    public void deselect() {
        setBackground(null);
    }
}
