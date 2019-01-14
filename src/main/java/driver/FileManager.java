package driver;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileManager {
    private Writer writer;

    /**
     * Constructor
     * <p>
     * Opens a file for appending
     *
     * @param filename path to file
     * @throws IOException file not found
     */
    FileManager(@SuppressWarnings("SameParameterValue") String filename) throws IOException {
        writer = new BufferedWriter(new FileWriter(filename, true));
        appendLine("Program started," + ClockDriver.dfFull.format(LocalDateTime.now()));
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
            while ((currentLine = reader.readLine()) != null) {
                lines.add(currentLine);
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filename);
            return lines;
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
            for (String line : lines) {
                sb.append(line);
                sb.append('\n');
            }
            f.write(sb.toString());
            f.close();
        } catch (IOException e) {
            System.err.println("Error writing files");
        }
    }

    /**
     * Appends an single line to a file
     *
     * @param line String line
     */
    void appendLine(String line) {
        try {
            writer.write(line + '\n');
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error writing file");
        }

    }
}
