package driver;

import navigation.Location;

public class sandbox {
    public static void main(String[] args) {
        Location location1 = new Location(40.794279476868816, -119.20677209303403);
        Location location2 = new Location(40.79442766707914, -119.20412613066321);
        Location location3 = new Location(40.79228657928632,-119.20005310721638);
        Location location4 = new Location(40.79123387964529,-119.19838334304688);
        System.out.println(location1.toString());
        System.out.println(location2.toString());
        System.out.println(location3.toString());
        System.out.println(location4.toString());

    }
}
