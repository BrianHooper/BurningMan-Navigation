package navigation;

import static org.junit.Assert.assertEquals;

public class LocationTest {
    @org.junit.Test
    public void streetDistanceConversionTest() {
        double distance1 = Location.toDistance('A');
        Location location = new Location(5, 30, 'A');
        assertEquals(distance1, location.getDistance(), 1.0);
    }
}
