package events;

import driver.FileManager;
import driver.LogDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

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
    private static final String eventsTSVPath = "config/events.tsv";

    private static DateTimeFormatter dfWithoutMin = DateTimeFormatter.ofPattern("yyyy MMMM d ha");
    private static DateTimeFormatter dfWithMin = DateTimeFormatter.ofPattern("yyyy MMMM d h:mma");

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
        readEventsTSV();
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

    private void parseSingleDate(String name, String location, String eventType, String description, String dateString) {
        dateString = dateString.replaceAll("\\[", "");
        dateString = dateString.replaceAll("\\]", "");

        String[] splitDate = dateString.split(",");
        String[] daySplit;
        if(splitDate[1].charAt(0) == ' ') {
            daySplit = splitDate[1].substring(1).split(" ");
        } else {
            daySplit = splitDate[1].split(" ");
        }

        String[] timeSplit = splitDate[3].split(" – ");

        String month = daySplit[0].replaceAll(" ", "");
        String day = daySplit[1].substring(0, daySplit[1].length() - 2).replaceAll(" ", "");
        String year = splitDate[2].replaceAll(" ", "");
        String startTime = timeSplit[0].replaceAll(" ", "").replaceAll("\'", "");
        String endTime = timeSplit[1].replaceAll(" ", "").replaceAll("\'", "");

        String formattedStartDate = year + " " + month + " " + day + " " + startTime;
        String formattedEndDate = year + " " + month + " " + day + " " + endTime;

        LocalDateTime startDate = null, endDate = null;
        try {
            if(startTime.contains(":")) {
                startDate = LocalDateTime.parse(formattedStartDate, dfWithMin);
            } else {
                startDate = LocalDateTime.parse(formattedStartDate, dfWithoutMin);
            }

            if(endTime.contains(":")) {
                endDate = LocalDateTime.parse(formattedEndDate, dfWithMin);
            } else {
                endDate = LocalDateTime.parse(formattedEndDate, dfWithoutMin);
            }
        } catch(DateTimeParseException e) {
            System.err.println("Error on " + dateString);
            return;
        }

        if(EventCategory.of(eventType) == null) {
            System.out.println(eventType);
        }
        events.add(new Event(name, location, description, EventCategory.of(eventType), startDate, endDate));
    }

    private void parseEventDate(String name, String location, String eventType, String description, String dateString) {


        String[] allDates = dateString.split("<-->");
        for(String singleDate : allDates) {
            parseSingleDate(name, location, eventType, description, singleDate);
        }
//        if(dateString.charAt(0) == '[') {
//            dateString = dateString.substring(1, dateString.length() - 1);
//        }
//        String[] indivudialDates = dateString.split("', '");
//        for(String SingleDateString : indivudialDates) {
//            String[] dateParts = SingleDateString.split(",");
//            if(dateParts.length == 4) {
//                String[] day = dateParts[1].split(" ");
//                String[] times = dateParts[3].split(" – ");
//                System.out.println();
//            }
//            System.out.println();
//        }
    }

    private void readEventsTSV() {
        ArrayList<String> lines = FileManager.readLines(eventsTSVPath);
        if(lines == null) {
            return;
        }

        for(String line : lines) {
            String[] split = line.split("\t");
            if(split.length == 4 || split.length == 5) {
                String name = split[0];
                String location = split[1];
                String category = split[2];
                String dateListString = split[3];
                String description = split[4];
                parseEventDate(name, location, category, description, dateListString);
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
