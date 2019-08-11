package navigation;

import java.time.LocalDateTime;

public class Landmark {
    private String name, description;
    private Location location = null;

    public Landmark(String name, String description, String hour, String minute, String street) {
        this.name = name;
        this.description = description;
        try {
            if(street.length() == 1) {
                location = new Location(Integer.parseInt(hour), Integer.parseInt(minute), street.charAt(0));
            } else {
                location = new Location(Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(street));
            }
        } catch(NumberFormatException e) {
            System.out.println();
        }
    }

    public Landmark(String name, Location location) {
        this.name = name;
        this.location = location;
        this.description = "";
    }

    public Landmark(String name, String description, Location location) {
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public String toString() {
        return name + "\t" + location.toString() + "\t" + description;
    }

    public String toStringForOutput() {
        return name + "\t" + location.toStringForOutput() + "\t" + description;
    }
}
