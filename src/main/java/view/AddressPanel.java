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
    private final JTextField streetBox;

    /**
     * Constructor
     * <p>
     * Adds components to the panel
     */
    private AddressPanel() {
        setLayout(new GridLayout(0, 3));

        hourBox = new JComboBox<>(
                new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "10"});
        minuteBox = new JComboBox<>(
                new String[]{"00", "15", "30", "45"});
        streetBox = new JTextField(5);
        add(hourBox);
        add(minuteBox);
        add(streetBox);
    }

    /**
     * Creates an AddressPanel with selected indices as the current location
     *
     * @param location Current Location object
     */
    AddressPanel(Location location) {
        this();
        int hour = location.getHour();
        int minute = location.getMinute();
        double distance = location.getDistance();

        if(hour >= 2 && hour <= 10) {
            hourBox.setSelectedIndex(hour - 2);
        }

        minuteBox.setSelectedIndex(minute / 15);

        if(distance < Location.getEsplanade_distance() + Location.getBlock_width()) {
            streetBox.setText(String.valueOf(distance));
        } else {
            streetBox.setText(String.valueOf(Location.toStreet(distance)));
        }

    }

    /**
     * Creates a Location object based off the address of the panel components
     *
     * @return Location object
     */
    Location getAddress() {
        int hour = 2 + hourBox.getSelectedIndex();
        int minute = 15 * minuteBox.getSelectedIndex();

        // Remove everything except numbers and letters
        String text = streetBox.getText().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");

        if(text.charAt(0) > 64) {
            return new Location(hour, minute, text.charAt(0));
        } else {
            try {
                double distance = Double.parseDouble(text);
                return new Location(hour, minute, distance);
            } catch(NumberFormatException e) {
                return null;
            }
        }

    }
}
