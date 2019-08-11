package driver;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class ListManager
 * <p>
 * Static helper methods for working with lists
 *
 * @author Brian Hooper
 * @since 0.9.0
 */
public class ListManager {

    /**
     * Creates an evenly spaced row from a list of strings and a list of lengths
     *
     * @param elements   String array
     * @param maxLengths int array of lengths
     * @return String row
     */
    private static String createRow(String[] elements, int[] maxLengths) {
        if(elements.length != maxLengths.length) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < elements.length; i++) {
            sb.append(addSpacers(elements[i], maxLengths[i] + 2));
        }
        return sb.toString();
    }

    /**
     * Pads the end of a string with spaces to make it a specific length
     *
     * @param str            input String
     * @param requiredLength specified length
     * @return String
     */
    private static String addSpacers(String str, int requiredLength) {
        if(str.length() < requiredLength) {
            int spacerLength = requiredLength - str.length();
            char[] repeat = new char[spacerLength];
            Arrays.fill(repeat, ' ');
            return str + new String(repeat);
        } else if(str.length() > requiredLength) {
            return str.substring(0, requiredLength);
        } else {
            return str;
        }
    }

    /**
     * Spaces a list of data evenly
     * <p>
     * turns
     * <p>
     * "One","Two"
     * "Three and four","and five"
     * "Six","as well as seven"
     * <p>
     * into
     * <p>
     * "One            Two"
     * "Three and four and five"
     * "Six            as well as seven"
     *
     * @param data    ArrayList of String arrays
     * @param columns number of columns in data
     * @return ArrayList of strings
     */
    public static ArrayList<String> splitEvenly(ArrayList<String[]> data, int columns) {
        int globalMax = 20;
        int[] maxLengths = new int[columns];
        for(String[] row : data) {
            if(row.length == columns) {
                for(int i = 0; i < columns; i++) {
                    if(row[i].length() > maxLengths[i]) {
                        maxLengths[i] = row[i].length();
                    }
                }
            }
        }

        for(int i = 0; i < maxLengths.length; i++) {
            if(maxLengths[i] > globalMax) {
                maxLengths[i] = globalMax;
            }
        }

        ArrayList<String> lines = new ArrayList<>();
        for(String[] row : data) {
            lines.add(createRow(row, maxLengths));
        }
        return lines;

    }
}
