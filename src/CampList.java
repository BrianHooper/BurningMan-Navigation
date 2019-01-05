import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class CampList
 * Maintains a searchable list of unique camp names and coordinates
 */
public class CampList {
    private HashMap<String, Position> camps = new HashMap<>();

    /**
     * Adds a new camp
     * @param name name of the camp
     * @param camp Position of the camp
     * @return true if the list does not already contain the camp
     */
    @SuppressWarnings("unused")
    public boolean add(String name, Position camp) {
        if(camps.containsKey(name)) {
            return false;
        }
        camps.put(name, camp);
        return true;
    }

    /**
     * Searches for a camp
     * @param searchTerm partial or complete camp name
     * @return Position of the camp, or null if no match
     */
    public Position findCamp(String searchTerm) {
        Pattern pattern = Pattern.compile(searchTerm);
        for(String str : camps.keySet()) {
            Matcher matcher = pattern.matcher(str);
            if(matcher.find()) {
                return camps.get(str);
            }
        }
        return null;
    }

    /**
     * Checks that a string is numeric
     * @param str string to check
     * @return true if numeric
     */
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Reads a CSV file containing camp names and addresses
     * @param filename location of csv file
     */
    public void importCamps(String filename) {
        BufferedReader reader = null;
        String line;
        String delimiter = ",";

        try {
            reader = new BufferedReader(new FileReader(filename));
            while((line = reader.readLine()) != null) {
                try {
                    String[] split = line.split(delimiter);
                    String[] address = split[1].split(" & ");
                    String[] angle = address[0].split(":");
                    try {
                        int hour = Integer.parseInt(angle[0]);
                        int minute = Integer.parseInt(angle[1]);
                        if (isNumeric(address[1])) {
                            int distance = Integer.parseInt(address[1]);
                            camps.put(split[0], new Position(hour, minute, distance));
                        } else {
                            camps.put(split[0], new Position(hour, minute, address[1].charAt(0)));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error: camp " + line + " contains invalid address");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error: camp " + line + " contains invalid address");
                }
            }
        } catch(FileNotFoundException e) {
            System.err.println("Error: file " + filename + " not found.");
        } catch(IOException e) {
            System.err.println("Error: IOException when reading file.");
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch(IOException e) {
                    System.err.println("Error: IOException when closing file.");
                }
            }
        }
    }

}
