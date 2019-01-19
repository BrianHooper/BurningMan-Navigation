package events;

import java.util.ArrayList;


/**
 * enum EventCategory
 * <p>
 * represents a type of event
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public enum EventCategory {
    ADULT, CARE, FIRE, FOOD, GAMES, KIDS, MUSIC, PARADE, PARTY, PERFORMANCE, RITUAL, WORKSHOP;

    /**
     * Converts a string to a category
     *
     * @param category String
     * @return EventCategory
     */
    public static EventCategory of(String category) {
        category = category.toLowerCase();
        switch(category) {
            case "adult":
                return ADULT;
            case "care":
                return CARE;
            case "fire":
                return FIRE;
            case "food":
                return FOOD;
            case "games":
                return GAMES;
            case "kids":
                return KIDS;
            case "music":
                return MUSIC;
            case "parade":
                return PARADE;
            case "party":
                return PARTY;
            case "performance":
                return PERFORMANCE;
            case "ritual":
                return RITUAL;
            case "workshop":
                return WORKSHOP;
            default:
                return null;
        }
    }

    /**
     * Returns a types of categories
     *
     * @return EventCategory array
     */
    public static ArrayList<String> listAll() {
        ArrayList<String> names = new ArrayList<>();
        for(EventCategory category : EventCategory.values()) {
            names.add(category.name());
        }
        return names;
    }
}
