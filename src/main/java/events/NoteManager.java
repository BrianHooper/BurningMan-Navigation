package events;

import driver.FileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

public class NoteManager {
    private final TreeMap<String, String> notes;
    private static final String delimiter = String.valueOf((char) 25);

    public NoteManager() {
        this.notes = new TreeMap<>();
    }

    public void readNotes() {
        ArrayList<String> lines = FileManager.readLines("notes.csv");
        if(lines == null)
            return;
        for(String line : lines) {
            String[] split = line.split(delimiter);
            if(split.length == 2) {
                notes.put(split[0], split[1]);
            }
        }
    }

    public void saveNotes() {
        ArrayList<String> lines = new ArrayList<>();
        for(String noteTitle : notes.keySet()) {
            lines.add(noteTitle + delimiter + notes.get(noteTitle));
        }
        FileManager.writeLines("notes.csv", lines);
    }

    public String[] getNoteTitles() {
        return Arrays.copyOf(notes.keySet().toArray(), notes.size(), String[].class);
    }

    public String getNote(String title) {
        return notes.get(title);
    }

    public void createNote(String title, String body) {
        notes.put(title, body);
    }

    public void deleteNote(String title) {
        notes.remove(title);
    }


}
