package events;

import driver.FileManager;

import java.util.ArrayList;
import java.util.TreeMap;


/**
 * Class NoteManager
 * <p>
 * Creates, updates, and deletes notes
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class NoteManager {
    // Relative path to notes file
    private static final String notesPath = "config/notes.csv";

    // Main list of note names and notes
    private final TreeMap<String, String> notes;

    // Static delimiter to separate note names and notes in file
    private static final String delimiter = String.valueOf((char) 25);

    // Number of notes in the NoteManager
    private int size;

    /**
     * Constructor
     * <p>
     * Initializes empty list of notes
     */
    public NoteManager() {
        this.notes = new TreeMap<>();
        readNotes();
        size = notes.size();
    }

    /**
     * Reads notes from a file
     */
    private void readNotes() {
        ArrayList<String> lines = FileManager.readLines(notesPath);
        if(lines == null)
            return;
        for(String line : lines) {
            String[] split = line.split(delimiter);
            if(split.length == 2) {
                notes.put(split[0], split[1]);
            }
        }
    }

    /**
     * Saves notes to a file
     */
    public void saveNotes() {
        ArrayList<String> lines = new ArrayList<>();
        for(String noteTitle : notes.keySet()) {
            lines.add(noteTitle + delimiter + notes.get(noteTitle));
        }
        FileManager.writeLines(notesPath, lines);
    }

    /**
     * Gets a String array containing all note titles
     *
     * @return String[]
     */
    public ArrayList<String> getNoteTitles() {
        return new ArrayList<>(notes.keySet());
    }

    /**
     * Gets the body of a note based on the title
     *
     * @param title String title
     * @return String body
     */
    public String getNote(String title) {
        return notes.get(title);
    }

    /**
     * Creates or updates a note
     *
     * @param title String title
     * @param body  String body
     */
    public void createNote(String title, String body) {
        notes.put(title, body);
        size = notes.size();
    }

    /**
     * Deletes a note, if it exists
     *
     * @param title String title
     */
    public void deleteNote(String title) {
        notes.remove(title);
        size = notes.size();
    }

    /**
     * Checks for 0 elements
     *
     * @return true if the NoteManager is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }


}
