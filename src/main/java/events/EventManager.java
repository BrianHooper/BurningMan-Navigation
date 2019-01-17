package events;

import driver.FileManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventManager {
    private static final String eventsPath = "config/eventList.csv";

    private final ArrayList<Event> events;

//    todo implement music list
//    private final ArrayList<Event> music;

    /**
     * Constructor
     * <p>
     * Initializes event and music lists
     */
    public EventManager() {
        events = new ArrayList<>();
        readEvents();
    }

    /**
     * Read events from a csv file
     */
    private void readEvents() {
        ArrayList<String> lines = FileManager.readLines(eventsPath);
        if (lines == null) {
            return;
        }

        String[] split;
        for (String line : lines) {
            if (line.length() == 0 || line.charAt(0) == '#') {
                continue;
            }
            split = line.split(";");
            if (split.length > 3) {
                // mandatory elements
                String name = split[0].toLowerCase();
                String location = split[1].toLowerCase();
                EventCategory category = EventCategory.of(split[2]);
                String[] startDateStr = split[3].split(",");
                LocalDateTime[] startTimes = Event.multiDateBuilder(startDateStr);

                // Optional elements
                if (split.length == 5) {
                    String[] endDateStr = split[4].split(",");
                    LocalDateTime[] endTimes = Event.multiDateBuilder(endDateStr);
                    events.add(new Event(name, "", location, category, startTimes, endTimes));
                } else {
                    events.add(new Event(name, "", location, category, startTimes));
                }
            }
        }
    }

    /**
     * Returns an ArrayList of events matching a category
     *
     * @param category EventCategory
     * @return ArrayList of Event objects
     */
    @SuppressWarnings("unused")
    public ArrayList<Event> listByCategory(EventCategory category) {
        ArrayList<Event> matchingEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getCategory() == category) {
                matchingEvents.add(event);
            }
        }

        return matchingEvents;
    }

    /**
     * Returns a list of events matching an event name
     *
     * @param name partial match of event name
     * @return ArrayList of Event objects
     */
    public ArrayList<Event> listByName(String name) {
        name = name.toLowerCase();
        ArrayList<Event> matchingEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getName().contains(name)) {
                matchingEvents.add(event);
            }
        }

        return matchingEvents;
    }

    /**
     * Returns a list of events at a particular camp
     *
     * @param name partial match of camp name
     * @return ArrayList of Event objects
     */
    public ArrayList<Event> listByCamp(String name) {
        name = name.toLowerCase();
        ArrayList<Event> matchingEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getLocation().contains(name)) {
                matchingEvents.add(event);
            }
        }

        return matchingEvents;
    }

    /**
     * Returns all events happening between a start time and an end time
     *
     * @param start beginning LocalDateTime object
     * @param end   end LocalDateTime object
     * @return ArrayList of Event objects
     */
    private ArrayList<Event> listBetween(LocalDateTime start, LocalDateTime end) {
        ArrayList<Event> matchingEvents = new ArrayList<>();

        for (Event event : events) {
            for (LocalDateTime startTime : event.getStartTimes()) {
                if (startTime.isAfter(start) && startTime.isBefore(end)) {
                    matchingEvents.add(event);
                }
            }
        }
        return matchingEvents;
    }

    /**
     * Returns an ArrayList of events happening in the next 24 hours
     *
     * @return ArrayList of Event objects
     */
    public ArrayList<Event> listHappeningSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        return listBetween(now, tomorrow);
    }

    /**
     * Returns a list of events happening on a particular day
     *
     * @param day int day
     * @return ArrayList of Event objects
     */
    public ArrayList<Event> listByDay(int day) {
        LocalDateTime start = Event.globalEventStartTime.plusDays(day);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        return listBetween(start, end);
    }
}
