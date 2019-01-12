package events;

import java.time.LocalDate;
import java.time.Month;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Event {

    public static final DateTimeFormatter dfDay = DateTimeFormatter.ofPattern("EEEE");
    public static final DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("h:mm a");

    public static LocalDateTime globalEventStartTime = LocalDateTime.of(
            LocalDate.of(2019, Month.AUGUST, 25), LocalTime.MIDNIGHT
    );

    private String name;
    private String location;
    private LocalDateTime[] startTimes;
    private LocalDateTime[] endTimes;
    private EventCategory category;

    /**
     * Constructor
     *
     * Initializes an Event object
     * @param name event name
     * @param location Location String
     * @param category EventCategory
     * @param startTimes Array of LocalDateTime object
     */
    public Event(String name, String location, EventCategory category, LocalDateTime[] startTimes) {
        this.name = name;
        this.location = location;
        this.startTimes = startTimes;
        this.category = category;
        this.endTimes = new LocalDateTime[0];
    }

    /**
     * Constructor
     *
     * Initializes an Event object
     * @param name event name
     * @param location Location String
     * @param category EventCategory
     * @param startTimes Array of LocalDateTime object
     * @param endTimes Array of LocalDateTime object
     */
    public Event(String name, String location, EventCategory category, LocalDateTime[] startTimes, LocalDateTime[] endTimes) {
        this(name, location, category, startTimes);
        this.endTimes = endTimes;
    }

    /**
     * Setter for globalEventStartTime
     * @param globalEventStartTime LocalDateTime object
     */
    public static void setGlobalEventStartTime(LocalDateTime globalEventStartTime) {
        Event.globalEventStartTime = globalEventStartTime;
    }

    /**
     * Builds a LocalDateTime object from the start time and an offset
     * @param day day offset
     * @param hour hour offset
     * @param minute minute offset
     * @return LocalDateTime object
     */
    public static LocalDateTime dateBuilder(int day, int hour, int minute) {
        Duration duration = Duration.ofDays(day);
        duration = duration.plusHours(hour);
        duration = duration.plusMinutes(minute);

        return globalEventStartTime.plus(duration);
    }

    /**
     * Converts a list of integers to a list of dates
     * @param dateStrIndexes String array of integers
     * @return LocalDateTime array
     */
    public static LocalDateTime[] multiDateBuilder(String[] dateStrIndexes) {
        ArrayList<Integer> dateIndexes = new ArrayList<>();
        for(String index : dateStrIndexes) {
            try {
                dateIndexes.add(Integer.parseInt(index));
            } catch (NumberFormatException e) {
                System.err.println("Error reading date indexes");
                return null;
            }
        }

        if(dateIndexes.size() % 3 != 0) {
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
     * @param startDate LocalDateTime object
     * @param endDate LocalDateTime object
     * @return String
     */
    public static String asString(LocalDateTime startDate, LocalDateTime endDate) {
        if(endDate == null) {
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
    public String timesToString() {
        int length = Math.min(startTimes.length, endTimes.length);
        int index = 0;
        StringBuilder sb = new StringBuilder();
        while(index < length) {
            sb.append("    ");
            sb.append(asString(startTimes[index], endTimes[index]));
            sb.append('\n');
            index++;
        }
        while(index < startTimes.length) {
            sb.append("    ");
            sb.append(asString(startTimes[index++], null));
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Getter for name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for location
     * @return String location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for start times
     * @return LocalDateTime[] array of start times
     */
    public LocalDateTime[] getStartTimes() {
        return startTimes;
    }

    /**
     * Getter for category
     * @return EventCategory
     */
    public EventCategory getCategory() {
        return category;
    }

    /**
     * Overrides toString() method
     * @return String representation of Event
     */
    @Override
    public String toString() {
        return "Name: " + name + " Location: " + location + " Category: " + category + '\n' + timesToString();
    }
}
