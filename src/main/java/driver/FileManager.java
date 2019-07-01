package driver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Class FileManager
 * <p>
 * Central static methods for reading / writing files
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
// Keep suppress warnings until tracker is enabled
@SuppressWarnings("all")
public class FileManager {
    // File output writer
    private Writer writer;

    // Path to currently openend file
    private String filename;

    // Logger
    private static LogDriver logger = LogDriver.getInstance();

    /**
     * Constructor
     * <p>
     * Opens a file for appending
     *
     * @param filename path to file
     * @throws IOException file not found
     */
    FileManager(@SuppressWarnings("SameParameterValue") String filename) throws IOException {
        Path filePath = Paths.get(filename);
        File parent = new File(String.valueOf(filePath.getParent()));
        if(!parent.exists()) {
            if(!parent.mkdirs()) {
                throw new IOException("Could not create non-existant directory " + parent.getAbsolutePath());
            }
        }

        writer = new BufferedWriter(new FileWriter(filename, true));
        this.filename = filename;
    }

    /**
     * Reads a text file into an ArrayList line by line
     *
     * @param filename path to input file
     * @return ArrayList of Strings
     */
    public static ArrayList<String> readLines(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        File file = new File(filename);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String currentLine;
            while((currentLine = reader.readLine()) != null) {
                lines.add(currentLine);
            }
        } catch(IOException e) {
            logger.severe(FileManager.class,
                    "IOException while reading file " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    /**
     * Writes an ArrayList of strings to a file, line-by-line
     *
     * @param filename path to file
     * @param lines    ArrayList of Strings
     */
    public static void writeLines(String filename, ArrayList<String> lines) {
        try {
            FileWriter f = new FileWriter(filename);
            StringBuilder sb = new StringBuilder();
            for(String line : lines) {
                sb.append(line);
                sb.append('\n');
            }
            f.write(sb.toString());
            f.close();
        } catch(IOException e) {
            logger.severe(FileManager.class,
                    "IOException while writing file " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Appends an single line to a file
     *
     * @param line String line
     */
    public void appendLine(String line) {
        try {
            writer.write(line + '\n');
            writer.flush();
        } catch(IOException e) {
            logger.severe(this.getClass(),
                    "IOException while writing to file " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Closes the currently open file
     *
     * @throws IOException if the file cannot be closed
     */
    public void close() throws IOException {
        writer.close();
    }
}
