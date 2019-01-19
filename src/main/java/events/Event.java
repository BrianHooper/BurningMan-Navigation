package events;

import driver.ClockDriver;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;


/**
 * Class Event
 * <p>
 * Represents a single event (Music, workshop, etc) with a name, location, category, and start/end time
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class Event {
    // Global start time, all relative events (+1 day, etc) are based on this
    public static LocalDateTime globalEventStartTime = LocalDateTime.of(
            LocalDate.of(2019, Month.AUGUST, 25), LocalTime.MIDNIGHT
    );

    // Main parameters for the event
    private final String name;
    private final String location;
    private final String description;
    private final EventCategory category;
    private final LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Constructor
     * <p>
     * Initializes an Event object
     *
     * @param name        event name
     * @param location    Location String
     * @param description String description
     * @param category    EventCategory
     * @param startTime   LocalDateTime object
     */
    Event(String name, String location, String description, EventCategory category, LocalDateTime startTime) {
        this.name = name;
        this.location = location;
        this.description = description;

        this.startTime = startTime;
        this.category = category;
    }

    /**
     * Constructor
     * <p>
     * Initializes an Event object
     *
     * @param name        event name
     * @param location    Location String
     * @param description String description
     * @param category    EventCategory
     * @param startTime   LocalDateTime object
     * @param endTime     LocalDateTime object
     */
    Event(String name, String location, String description, EventCategory category, LocalDateTime startTime, LocalDateTime endTime) {
        this(name, location, description, category, startTime);
        this.endTime = endTime;
    }

    /**
     * Setter for globalEventStartTime
     *
     * @param startTimeString string date
     */
    public static boolean setGlobalEventStartTime(String startTimeString) {
        try {
            globalEventStartTime = LocalDateTime.parse(startTimeString, ClockDriver.dfFull);
            return true;
        } catch(DateTimeParseException e) {
            System.err.println("Error parsing string as date");
        }
        return false;
    }

    /**
     * Builds a LocalDateTime object from the start time and an offset
     *
     * @param day    day offset
     * @param hour   hour offset
     * @param minute minute offset
     * @return LocalDateTime object
     */
    private static LocalDateTime dateBuilder(int day, int hour, int minute) {
        Duration duration = Duration.ofDays(day);
        duration = duration.plusHours(hour);
        duration = duration.plusMinutes(minute);

        return globalEventStartTime.plus(duration);
    }

    /**
     * Converts a list of integers to a list of dates
     *
     * @param dateStrIndexes String array of integers
     * @return LocalDateTime array
     */
    static LocalDateTime[] multiDateBuilder(String[] dateStrIndexes) {
        if(dateStrIndexes == null || dateStrIndexes.length % 3 != 0) {
            System.err.println("Data data has incorrect number of parameters");
            return null;
        }


        ArrayList<Integer> dateIndexes = new ArrayList<>();
        for(String index : dateStrIndexes) {
            try {
                dateIndexes.add(Integer.parseInt(index.replaceAll("[^\\d.]", "")));
            } catch(NumberFormatException e) {
                System.err.println("Error reading date indexes");
                return null;
            }
        }


        LocalDateTime[] dateArray = new LocalDateTime[dateIndexes.size() / 3];
        int arrayIndex = 0;
        try {
            for(int i = 0; i < dateIndexes.size(); i = i + 3) {
                int day = dateIndexes.get(i);
                int hour = dateIndexes.get(i + 1);
                int minute = dateIndexes.get(i + 2);
                dateArray[arrayIndex++] = dateBuilder(day, hour, minute);
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.err.println("Error reading date data");
            return null;
        }
        return dateArray;
    }

    /**
     * Formats an event start time and end time as a string
     *
     * @param startDate LocalDateTime object
     * @param endDate   LocalDateTime object
     * @return String
     */
    private static String asString(LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate == null) {
            return "";
        } else if(endDate == null) {
            return ClockDriver.dfTime.format(startDate) + " at " + ClockDriver.dfTime.format(startDate);
        } else {
            return ClockDriver.dfTime.format(startDate) + " " +
                    ClockDriver.dfTime.format(startDate) + "-" + ClockDriver.dfTime.format(endDate);
        }
    }

    /**
     * Getter for name
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for location
     *
     * @return String location
     */
    String getLocation() {
        return location;
    }

    /**
     * Getter for start time
     *
     * @return LocalDateTime start time
     */
    LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Getter for category
     *
     * @return EventCategory
     */
    public EventCategory getCategory() {
        return category;
    }

    /**
     * Overrides toString() method
     *
     * @return String representation of Event
     */
    @Override
    public String toString() {
        return "Name: " + name + " Location: " + location + " Category: " + category.toString().charAt(0) + '\n' + asString(startTime, endTime);
    }

    /**
     * Gets the name, location, category, and time as a String array
     *
     * @return String array
     */
    public String[] getElements() {
        return new String[]{name, location, String.valueOf(category.toString().charAt(0)), asString(startTime, endTime)};
    }

    /**
     * Formats the start and end time as a string
     *
     * @return String start and end time
     */
    public String timeToString() {
        return asString(startTime, endTime);
    }

    /**
     * Getter for event description
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }
}
