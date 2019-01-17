package events;

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
        switch (category) {
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
    @SuppressWarnings("unused")
    public EventCategory[] listAll() {
        return new EventCategory[]{ADULT, CARE, FIRE, FOOD, GAMES, KIDS, MUSIC, PARADE, PARTY, PERFORMANCE, RITUAL, WORKSHOP};
    }
}
