import java.util.ArrayList;

public class Bathrooms {
    private ArrayList<Position> bathrooms = new ArrayList<>();


    /**
     * Adds a new bathroom to the list
     * @param latitude latitude of bathroom
     * @param longitude longitude of bathroom
     */
    public void add(double latitude, double longitude) {
        bathrooms.add(Coordinate.toPosition(latitude, longitude));
    }

    /**
     * Finds the closest bathroom to the current position
     * @param currentPosition current Position
     * @return closest bathroom Position, or null if none exist
     */
    public Position findClosest(Coordinate currentPosition) {
        if(bathrooms.isEmpty()) {
            return null;
        }

        Position closestBathroom = bathrooms.get(0);
        double closestDistance = Double.MAX_VALUE;
        for(Position bathroom : bathrooms) {
            double distance = currentPosition.distance(bathroom);
            if(distance < closestDistance) {
                closestBathroom = bathroom;
                closestDistance = distance;
            }
        }
        return closestBathroom;
    }
}
