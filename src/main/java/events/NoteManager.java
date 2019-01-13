package events;

import driver.FileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;

public class NoteManager {
    private TreeMap<String, String> notes;

    public NoteManager() {
        this.notes = new TreeMap<>();
    }

    public void readNotes() {
        ArrayList<String> lines = FileManager.readLines("notes.csv");
        //todo load notes from file
    }

    public void saveNotes() {
        //todo write notes to file
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
