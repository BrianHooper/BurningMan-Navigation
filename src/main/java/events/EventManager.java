package events;

import driver.FileManager;
import driver.LogDriver;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Class EventManager
 * <p>
 * Creates, updates, and deletes events
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class EventManager {
    // relative path to file containing list of events
    private static final String eventsPath = "config/eventList.csv";

    // main list of events
    private final ArrayList<Event> events;

    // Logger
    private static final LogDriver logger = LogDriver.getInstance();

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
        if(lines == null) {
            return;
        }

        String[] split;
        for(String line : lines) {
            if(line.length() == 0 || line.charAt(0) == '#') {
                continue;
            }
            split = line.split(";");
            if(split.length > 4) {
                // mandatory elements
                String name = split[0].toLowerCase();
                String location = split[1].toLowerCase();
                String description = split[2].toLowerCase();
                EventCategory category = EventCategory.of(split[3]);
                String[] startDateStr = split[4].split(",");
                LocalDateTime[] startTimes = Event.multiDateBuilder(startDateStr);

                if(startTimes != null) {
                    // Optional elements
                    if(split.length == 6) {
                        String[] endDateStr = split[5].split(",");
                        LocalDateTime[] endTimes = Event.multiDateBuilder(endDateStr);
                        if(endTimes == null) {
                            logger.warning(EventManager.class,
                                    "MultiDateBuilder returned null while trying to parse event line " + line);
                        }
                        if(endTimes != null && startTimes.length <= endTimes.length) {
                            for(int i = 0; i < startTimes.length; i++) {
                                events.add(new Event(name, location, description, category, startTimes[i], endTimes[i]));
                            }
                        }
                    } else {
                        for(LocalDateTime startTime : startTimes) {
                            events.add(new Event(name, location, description, category, startTime));
                        }
                    }
                } else {
                    logger.warning(EventManager.class,
                            "MultiDateBuilder returned null while trying to parse event line " + line);
                }
            }
        }
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

        for(Event event : events) {
            if(event.getStartTime().isAfter(start) && event.getStartTime().isBefore(end)) {
                matchingEvents.add(event);
            }
        }
        return matchingEvents;
    }

    /**
     * Returns an ArrayList of events happening in the next 24 hours
     *
     * @param hours number of hours ahead to search
     * @return ArrayList of Event objects
     */
    public ArrayList<Event> listHappeningSoon(int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = LocalDateTime.now().plusHours(hours);
        return listBetween(now, tomorrow);
    }

    /**
     * Returns a list of events matching search terms
     *
     * @param name     String name of event to match
     * @param camp     String name of location
     * @param day      int day, if day is set to 0 this parameter is ignored
     * @param category int category, index of list of EventCategory enums in alphabetical order
     * @return ArrayList of Event objects matching search terms
     */
    public ArrayList<Event> listBySearch(String name, String camp, int day, int category) {
        EventCategory eventCategory;
        if(category > 0) {
            eventCategory = EventCategory.values()[--category];
        } else {
            eventCategory = null;
        }
        day--;

        ArrayList<Event> matchingEvents = new ArrayList<>();
        for(Event event : events) {
            if(matches(event, name, camp, day, eventCategory)) {
                matchingEvents.add(event);
            }
        }
        return matchingEvents;
    }

    /**
     * Checks that an individual event matches a search term
     *
     * @param event         Event object
     * @param name          String name
     * @param camp          String camp
     * @param day           int day
     * @param eventCategory EventCategory enum
     * @return true if event matches all search terms
     */
    private boolean matches(Event event, String name, String camp, int day, EventCategory eventCategory) {
        name = name.toLowerCase();
        camp = camp.toLowerCase();

        if(eventCategory != null && eventCategory != event.getCategory()) {
            return false;
        }

        if(day >= 0) {
            LocalDateTime start = Event.globalEventStartTime.plusDays(day);
            LocalDateTime end = start.plusDays(1);
            if(event.getStartTime().isBefore(start) || event.getStartTime().isAfter(end)) {
                return false;
            }
        }

        if(name.length() > 0 && !event.getName().contains(name)) {
            return false;
        }

        return camp.length() <= 0 || event.getLocation().contains(camp);
    }
}
