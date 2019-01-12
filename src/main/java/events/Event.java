package events;

import navigation.Location;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Event {

    public static final DateTimeFormatter dfDay = DateTimeFormatter.ofPattern("EEEE");
    public static final DateTimeFormatter dfTime = DateTimeFormatter.ofPattern("h:mm a");

    public static final LocalDateTime dt = LocalDateTime.of(
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

        return dt.plus(duration);
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
     * Formats an array of start times and an array of end times as a String
     *
     * Start dates without a corresponding end date will be treated as singular
     *
     * @param startDates LocalDateTime array
     * @param endDates LocalDateTime array
     * @return String
     */
    public static String asString(LocalDateTime[] startDates, LocalDateTime[] endDates) {
        int length = Math.min(startDates.length, endDates.length);
        int index = 0;
        StringBuilder sb = new StringBuilder();
        while(index < length) {
            sb.append(asString(startDates[index], endDates[index]));
            sb.append('\n');
            index++;
        }
        while(index < startDates.length) {
            sb.append(asString(startDates[index++], null));
            sb.append('\n');
        }
        return sb.toString();
    }
}
