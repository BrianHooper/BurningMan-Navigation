import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class CampList
 * Maintains a searchable list of unique camp names and coordinates
 */
public class CampList {
    private HashMap<String, Position> camps;

    public CampList() {
        camps = new HashMap<>();
    }

    /**
     * Adds a new camp
     * @param name name of the camp
     * @param camp Position of the camp
     * @return true if the list does not already contain the camp
     */
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

}
