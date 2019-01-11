package events;

import navigation.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Event {
    public static Calendar getInitialCalendar() {
        Calendar c1 = GregorianCalendar.getInstance();
        c1.set(2019, Calendar.AUGUST, 25, 0, 0);  //January 30th 2000
        return c1;
    }

    String name;
    Location location;
    Date[] startTimes;
    Date[] endTimes;
    EventCategory category;

    SimpleDateFormat dateBuilderFormat = new SimpleDateFormat("");

    public Event(Location location, Date[] startTimes, Date[] endTimes, EventCategory category) {
        this.location = location;
        this.startTimes = startTimes;
        this.endTimes = endTimes;
        this.category = category;
    }

    public static Date dateBuilder(int day, int hour, int minute) {
            Calendar date = Event.getInitialCalendar();
            date.add(Calendar.DATE, day);
            date.add(Calendar.HOUR, hour);
            date.add(Calendar.MINUTE, minute);
            return date.getTime();
    }
}
