package view;

import navigation.Location;

import javax.swing.*;
import java.awt.*;


/**
 * Class AddressPanel
 * <p>
 * Extends JPanel
 * <p>
 * Creates an address input with drop-down fields for hour, minute, and street
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
class AddressPanel extends JPanel {
    // Panel components
    private final JComboBox<String> hourBox;
    private final JComboBox<String> minuteBox;
    private final JComboBox<String> streetBox;

    /**
     * Constructor
     * <p>
     * Adds components to the panel
     */
    AddressPanel() {
        setLayout(new FlowLayout());

        hourBox = new JComboBox<>(
                new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10"});
        minuteBox = new JComboBox<>(
                new String[]{"00", "15", "30", "45"});
        streetBox = new JComboBox<>(
                new String[]{"Esp.", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"});
        add(hourBox);
        add(minuteBox);
        add(streetBox);
    }

    /**
     * Creates a Location object based off the address of the panel components
     *
     * @return Location object
     */
    Location getAddress() {
        int hour = 2 + hourBox.getSelectedIndex();
        int minute = 15 * minuteBox.getSelectedIndex();
        if(streetBox.getSelectedIndex() == 0) {
            return new Location(hour, minute, (double) Location.esplanade_distance);
        } else {
            char street = (char) (64 + streetBox.getSelectedIndex());
            return new Location(hour, minute, street);
        }
    }
}
