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
    PARTY, OTHER, PERFORMANCE, ADULT, CARE, RITUAL, FOOD, WORKSHOP, FIRE, GAME, KIDS, PARADE;

    /**
     * Converts a string to a category
     *
     * @param category String
     * @return EventCategory
     */
    public static EventCategory of(String category) {
        category = category.toLowerCase();
        switch(category) {
            case "gathering/party":
                return PARTY;
            case "other":
                return OTHER;
            case "performance":
                return PERFORMANCE;
            case "adult-oriented":
                return ADULT;
            case "care/support":
                return CARE;
            case "ritual/ceremony":
                return RITUAL;
            case "food":
                return FOOD;
            case "class/workshop":
                return WORKSHOP;
            case "fire":
                return FIRE;
            case "game":
                return GAME;
            case "kid-friendly":
                return KIDS;
            case "parade":
                return PARADE;
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
