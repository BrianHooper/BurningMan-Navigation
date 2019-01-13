package driver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileManager {
    /**
     * Reads a text file into an ArrayList line by line
     * @param filename path to input file
     * @return ArrayList of Strings
     */
    public static ArrayList<String> readLines(String filename) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            Scanner inputStream = new Scanner(new File(filename));
            while(inputStream.hasNextLine()) {
                lines.add(inputStream.nextLine());
            }
            inputStream.close();
        } catch (IOException e) {
            System.err.println("Error reading file " + filename);
            return null;
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
            System.err.println("Error writing files");
        }
    }
}
