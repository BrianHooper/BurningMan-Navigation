package driver;

import java.io.File;
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
}
