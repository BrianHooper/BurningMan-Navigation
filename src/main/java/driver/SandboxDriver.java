package driver;

import navigation.Location;

public class SandboxDriver {


    public static void main(String[] args) {
//        int distance = 5100;
//        String street = String.valueOf(distance);
//        int index = 0;
//        if(distance < ((int) Location.blockDistances[Location.blockDistances.length - 1][0])) {
//            while (index < Location.blockDistances.length  - 1 && distance > ((int) Location.blockDistances[index][0])) {
//                street = (String) Location.blockDistances[index + 1][1];
//                index++;
//            }
//        }
//        System.out.println(street);

//        System.out.println(toDistance('A'));
//        System.out.println(toDistance('A'));

//        while()


        Location location = new Location(6, 30, 6000);
        System.out.println(location.toString());
    }
}
