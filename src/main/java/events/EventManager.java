package events;

import driver.FileManager;
import navigation.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.TreeMap;

public class EventManager {
    private ArrayList<Event> events;
    private ArrayList<Event> music;

    public EventManager() {
        events = new ArrayList<>();
        events = new ArrayList<>();
    }

    public void importEvents(String filename) {
        ArrayList<String> lines = FileManager.readLines(filename);
        if(lines == null) {
            return;
        }

        String[] split;
        for(String line : lines) {
            if(line.length() == 0 || line.charAt(0) == '#') {
                continue;
            }
            split = line.split(";");
            if(split.length > 3) {
                // mandatory elements
                String name = split[0];
                String location = split[1];
                EventCategory category = EventCategory.of(split[2]);
                String[] startDateStr = split[3].split(",");
                LocalDateTime[] startTimes = Event.multiDateBuilder(startDateStr);

                // Optional elements
                if(split.length == 5) {
                    String[] endDateStr = split[4].split(",");
                    LocalDateTime[] endTimes = Event.multiDateBuilder(endDateStr);
                    events.add(new Event(name, location, category, startTimes, endTimes));
                } else {
                    events.add(new Event(name, location, category, startTimes));
                }
            }
        }
    }

    public ArrayList<Event> listByCategory() {
        return null;
    }

    public Event searchByName() {
        return null;
    }

    public ArrayList<Event> listByCamp() {
        return null;
    }

    public ArrayList<Event> listHappeningSoon() {
        return null;
    }


    // list happening soon
    // Search by name
    // Search by camp
    // Search by category
}
