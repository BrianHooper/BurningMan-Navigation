package events;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Event {

    private static final DateTimeFormatter dfDay = DateTimeFormatter.ofPattern("EEEE");
    private static final DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("h:mm a");
    public static final DateTimeFormatter dfFull = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static LocalDateTime globalEventStartTime = LocalDateTime.of(
            LocalDate.of(2019, Month.AUGUST, 25), LocalTime.MIDNIGHT
    );

    private final String name;
    private final String location;
    private final LocalDateTime[] startTimes;
    private LocalDateTime[] endTimes;
    private final EventCategory category;

    /**
     * Constructor
     * <p>
     * Initializes an Event object
     *
     * @param name       event name
     * @param location   Location String
     * @param category   EventCategory
     * @param startTimes Array of LocalDateTime object
     */
    Event(String name, String location, EventCategory category, LocalDateTime[] startTimes) {
        this.name = name;
        this.location = location;
        this.startTimes = startTimes;
        this.category = category;
        this.endTimes = new LocalDateTime[0];
    }

    /**
     * Constructor
     * <p>
     * Initializes an Event object
     *
     * @param name       event name
     * @param location   Location String
     * @param category   EventCategory
     * @param startTimes Array of LocalDateTime object
     * @param endTimes   Array of LocalDateTime object
     */
    Event(String name, String location, EventCategory category, LocalDateTime[] startTimes, LocalDateTime[] endTimes) {
        this(name, location, category, startTimes);
        this.endTimes = endTimes;
    }

    /**
     * Setter for globalEventStartTime
     *
     * @param startTimeString string date
     */
    public static boolean setGlobalEventStartTime(String startTimeString) {
        try {
            globalEventStartTime = LocalDateTime.parse(startTimeString, Event.dfFull);
            return true;
        } catch (DateTimeParseException e) {
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
        ArrayList<Integer> dateIndexes = new ArrayList<>();
        for (String index : dateStrIndexes) {
            try {
                dateIndexes.add(Integer.parseInt(index));
            } catch (NumberFormatException e) {
                System.err.println("Error reading date indexes");
                return null;
            }
        }

        if (dateIndexes.size() % 3 != 0) {
            System.err.println("Data data has incorrect number of parameters");
            return null;
        }

        LocalDateTime[] dateArray = new LocalDateTime[dateIndexes.size() / 3];
        int arrayIndex = 0;
        try {
            for (int i = 0; i < dateIndexes.size(); i = i + 3) {
                int day = dateIndexes.get(i);
                int hour = dateIndexes.get(i + 1);
                int minute = dateIndexes.get(i + 2);
                dateArray[arrayIndex++] = dateBuilder(day, hour, minute);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error reading date data");
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
        if (endDate == null) {
            return dfDay.format(startDate) + " at " + dfTime.format(startDate);
        } else {
            return dfDay.format(startDate) + " from " + dfTime.format(startDate) + " to " + dfTime.format(endDate);
        }
    }


    /**
     * Formats event times as a String
     *
     * @return String
     */
    private String timesToString() {
        int length = Math.min(startTimes.length, endTimes.length);
        int index = 0;
        StringBuilder sb = new StringBuilder();
        while (index < length) {
            sb.append("    ");
            sb.append(asString(startTimes[index], endTimes[index]));
            sb.append('\n');
            index++;
        }
        while (index < startTimes.length) {
            sb.append("    ");
            sb.append(asString(startTimes[index++], null));
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Getter for name
     *
     * @return String name
     */
    String getName() {
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
     * Getter for start times
     *
     * @return LocalDateTime[] array of start times
     */
    LocalDateTime[] getStartTimes() {
        return startTimes;
    }

    /**
     * Getter for category
     *
     * @return EventCategory
     */
    EventCategory getCategory() {
        return category;
    }

    /**
     * Overrides toString() method
     *
     * @return String representation of Event
     */
    @Override
    public String toString() {
        return "Name: " + name + " Location: " + location + " Category: " + category + '\n' + timesToString();
    }
}
