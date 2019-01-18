package driver;

import java.util.ArrayList;
import java.util.Arrays;

public class ListManager {

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

    private static String addSpacers(String str, int requiredLength) {
        int spacerLength = requiredLength - str.length();
        char[] repeat = new char[spacerLength];
        Arrays.fill(repeat, ' ');
        return str + new String(repeat);
    }

    public static ArrayList<String> splitEvenly(ArrayList<String[]> data, int columns) {
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

        ArrayList<String> lines = new ArrayList<>();
        for(String[] row : data) {
            lines.add(createRow(row, maxLengths));
        }
        return lines;

    }
}
